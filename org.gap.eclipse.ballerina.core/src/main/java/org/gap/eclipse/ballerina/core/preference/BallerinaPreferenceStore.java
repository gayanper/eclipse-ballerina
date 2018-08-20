package org.gap.eclipse.ballerina.core.preference;

import java.io.File;

import org.gap.eclipse.ballerina.core.BallerinaPlugin;

public class BallerinaPreferenceStore {
	private BallerinaPlugin ballerinaPlugin;
	private String sdkHome;
	private String ballerinaJavaHome;

	public BallerinaPreferenceStore(BallerinaPlugin ballerinaPlugin) {
		this.ballerinaPlugin = ballerinaPlugin;
		ballerinaPlugin.getPreferenceStore().addPropertyChangeListener(e -> {
			init();
		});

		init();
	}

	private void init() {
		final String sdkHomeValue = ballerinaPlugin.getPreferenceStore().getString(Preferences.SDK_HOME);
		if (sdkHomeValue == null || sdkHomeValue.isEmpty()) {
			this.sdkHome = null;
			return;
		}

		final File sdkPath = new File(sdkHomeValue);
		if (sdkPath.exists()) {
			this.sdkHome = sdkPath.getAbsolutePath();
		} else {
			this.sdkHome = null;
		}

		ballerinaJavaHome = ballerinaPlugin.getPreferenceStore()
				.getString(Preferences.BALLERINA_JAVE_HOME);
	}

	public String getSdkHome() {
		return sdkHome;
	}

	public boolean isSdkHomeValid() {
		return sdkHome != null;
	}

	public boolean isBallerinaJavaHomeValid() {
		return ballerinaJavaHome != null && ballerinaJavaHome.isEmpty();
	}

	public String getBallerinaJavaHome() {
		return ballerinaJavaHome;
	}
}
