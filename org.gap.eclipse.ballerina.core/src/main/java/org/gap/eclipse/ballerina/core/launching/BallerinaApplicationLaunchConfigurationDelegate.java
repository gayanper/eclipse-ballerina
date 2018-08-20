package org.gap.eclipse.ballerina.core.launching;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.gap.eclipse.ballerina.core.BallerinaPlugin;
import org.gap.eclipse.ballerina.core.Messages;
import org.gap.eclipse.ballerina.core.preference.Preferences;

public class BallerinaApplicationLaunchConfigurationDelegate extends LaunchConfigurationDelegate {
	private BallerinaPlugin ballerinaPlugin = BallerinaPlugin.getDefault();


	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		if (!ballerinaPlugin.getBallerinaPreferenceStore().isSdkHomeValid()) {
			Messages.showMissingSDKError();
		}

		if (ballerinaPlugin.getBallerinaPreferenceStore().isBallerinaJavaHomeValid()) {
			Messages.showMissingJavaHomeError();
		}

		final String sdkHome = ballerinaPlugin.getPreferenceStore().getString(Preferences.SDK_HOME);
		final IPath projectLocation = projectLocation(configuration);
		final String fileToRun = fileLocation(configuration);

		final StringBuilder ballerinaCmd = new StringBuilder();
		ballerinaCmd.append(sdkHome);
		ballerinaCmd.append(File.separator);
		ballerinaCmd.append("bin");
		ballerinaCmd.append(File.separator);
		ballerinaCmd.append("ballerina");

		String[] commands = new String[] { ballerinaCmd.toString(), "run", "--sourceroot", projectLocation.toOSString(),
				fileToRun };
		Process process = DebugPlugin.exec(commands, projectLocation.toFile(),
				new String[] { String.format("JAVA_HOME=%s",
						ballerinaPlugin.getBallerinaPreferenceStore().getBallerinaJavaHome()) });
		IProcess debugProcess = DebugPlugin.newProcess(launch, process, commands[0]);
		debugProcess.setAttribute(IProcess.ATTR_CMDLINE, String.join(" ", commands));
	}

	private String fileLocation(ILaunchConfiguration configuration) throws CoreException {
		String filePath = configuration.getAttribute(BallerinaLauchConfiguration.BAL_FILE, "");
		if (filePath.isEmpty()) {
			throw new CoreException(new Status(IStatus.ERROR, BallerinaPlugin.PLUGIN_ID,
					String.format("Configuration <%s> doesn''t point to a valid file.", configuration.getName())));
		}
		return filePath;
	}

	private IPath projectLocation(ILaunchConfiguration configuration) throws CoreException {
		final String projectName = configuration.getAttribute(BallerinaLauchConfiguration.PROEJCT, "");
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if(project == null) {
			throw new CoreException(new Status(IStatus.ERROR, BallerinaPlugin.PLUGIN_ID,
					String.format("Configuration <%s> doesn''t point to a valid project.", configuration.getName())));
		}

		return project.getLocation();
	}

}
