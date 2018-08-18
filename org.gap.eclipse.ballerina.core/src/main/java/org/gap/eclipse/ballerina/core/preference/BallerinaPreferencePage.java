package org.gap.eclipse.ballerina.core.preference;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.gap.eclipse.ballerina.core.BallerinaPlugin;

public class BallerinaPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(BallerinaPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor(Preferences.SDK_HOME, "Ballerina SDK Home", getFieldEditorParent()));
	}
}
