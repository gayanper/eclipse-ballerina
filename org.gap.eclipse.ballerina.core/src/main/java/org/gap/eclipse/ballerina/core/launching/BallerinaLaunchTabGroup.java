package org.gap.eclipse.ballerina.core.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class BallerinaLaunchTabGroup extends AbstractLaunchConfigurationTabGroup {

	public BallerinaLaunchTabGroup() {
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] { new BallerinaLaunchTab(), new CommonTab() });
	}

}
