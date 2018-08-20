package org.gap.eclipse.ballerina.core.launching;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.gap.eclipse.ballerina.core.BallerinaPlugin;

public class BallerinaLaunchTab extends AbstractLaunchConfigurationTab {
	private Text projectPath;

	private Text ballerinaFile;

	private IProject selectedProject;

	private Button fileButton;

	private Button projectButton;

	@Override
	public void createControl(Composite parent) {
		Group group = new Group(parent, SWT.BORDER);
		GridLayoutFactory.swtDefaults().numColumns(3).applyTo(group);
		setControl(group);

		addProjectFieldsToGroup(group);
		addFileFieldsToGroup(group);
	}

	private void addFileFieldsToGroup(Group group) {
		Label fileLabel = new Label(group, SWT.NONE);
		fileLabel.setText("File");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).applyTo(fileLabel);

		ballerinaFile = new Text(group, SWT.BORDER);
		ballerinaFile.setEditable(false);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(ballerinaFile);

		ballerinaFile.addModifyListener(e -> {
			updateDirtyFlag();
		});

		fileButton = new Button(group, SWT.NONE);
		fileButton.setText("Browse");
		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new LabelProvider());
				dialog.setMessage("Select file to run");
				dialog.setTitle("Ballerina File");
				dialog.setElements(collectAllBallerinaFiles());

				int result = dialog.open();

				if (result == ResourceSelectionDialog.OK) {
					IFile selectedFile = (IFile) dialog.getResult()[0];
					ballerinaFile.setText(selectedFile.getProjectRelativePath().toOSString());
					updateDirtyFlag();
				}
			}
		});
	}

	private IFile[] collectAllBallerinaFiles() {
		if(selectedProject == null) {
			return new IFile[0];
		}
		
		try {
			List<IFile> ballerinaFiles = collectFiles(selectedProject, f -> "bal".equals(f.getFileExtension()));
			return ballerinaFiles.toArray(new IFile[ballerinaFiles.size()]);
		} catch (CoreException e) {
			BallerinaPlugin.getDefault().logError(e);
			return new IFile[0];
		}
	}

	private List<IFile> collectFiles(IContainer container, Predicate<IFile> predicate) throws CoreException {
		final List<IFile> fileList = new ArrayList<>();
		for (IResource resource : container.members()) {
			if (resource instanceof IFile) {
				final IFile file = (IFile) resource;
				if (predicate.test(file)) {
					fileList.add(file);
				}
			} else if (resource instanceof IContainer) {
				fileList.addAll(collectFiles((IContainer) resource, predicate));
			}
		}
		return fileList;
	}

	private void addProjectFieldsToGroup(Group group) {
		Label projectLabel = new Label(group, SWT.NONE);
		projectLabel.setText("Project");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).applyTo(projectLabel);
		
		projectPath = new Text(group, SWT.BORDER);
		projectPath.setEditable(false);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(projectPath);

		projectPath.addModifyListener(e -> {
			updateDirtyFlag();

			fileButton.setEnabled(!projectPath.getText().isEmpty());
		});

		projectButton = new Button(group, SWT.NONE);
		projectButton.setText("Browse");
		projectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(projectButton.getShell(),
						new LabelProvider());
				dialog.setElements(ResourcesPlugin.getWorkspace().getRoot().getProjects());
				dialog.setMessage("Select a project");
				dialog.setTitle("Project");
				int result = dialog.open();

				if (result == ContainerSelectionDialog.OK) {
					IProject selection = (IProject) dialog.getResult()[0];
					selectedProject = selection;
					projectPath.setText(selectedProject.getName());
					updateDirtyFlag();
				}
			}
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BallerinaLauchConfiguration.PROEJCT, "");
		configuration.setAttribute(BallerinaLauchConfiguration.BAL_FILE, "");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			projectPath.setText(configuration.getAttribute(BallerinaLauchConfiguration.PROEJCT, ""));
			selectedProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectPath.getText());
			ballerinaFile.setText(configuration.getAttribute(BallerinaLauchConfiguration.BAL_FILE, ""));
		} catch (CoreException e) {
			BallerinaPlugin.getDefault().logError(e);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BallerinaLauchConfiguration.PROEJCT, projectPath.getText());
		configuration.setAttribute(BallerinaLauchConfiguration.BAL_FILE, ballerinaFile.getText());
		setDirty(false);
	}

	@Override
	public String getName() {
		return "Ballerina";
	}

	private void updateDirtyFlag() {
		setDirty(true);
		updateLaunchConfigurationDialog();
	}

}
