package org.gap.eclipse.ballerina.core;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gap.eclipse.ballerina.core.preference.BallerinaPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BallerinaPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.gap.eclipse.ballerina.core";

	// The shared instance
	private static BallerinaPlugin plugin;
	
	boolean started;

	private BallerinaPreferenceStore ballerinaPreferenceStore;
	/**
	 * The constructor
	 */
	public BallerinaPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin  = this;
		started = true;
		ballerinaPreferenceStore = new BallerinaPreferenceStore(plugin);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		started = false;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BallerinaPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public void logError(String message) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, message));
	}

	public void logError(Throwable t) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, t.getMessage(), t));
	}
	
	public BallerinaPreferenceStore getBallerinaPreferenceStore() {
		return ballerinaPreferenceStore;
	}
}
