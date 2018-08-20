package org.gap.eclipse.ballerina.core.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.lsp4e.server.ProcessOverSocketStreamConnectionProvider;
import org.gap.eclipse.ballerina.core.BallerinaPlugin;

public class BallerinaStreamConnectionProvider extends ProcessOverSocketStreamConnectionProvider {

	private boolean validSDK = false;

	public BallerinaStreamConnectionProvider() throws IOException {
		this(BallerinaPlugin.getDefault(), findTwoFreePorts());
	}

	public BallerinaStreamConnectionProvider(BallerinaPlugin ballerinaPlugin, int[] ports) throws IOException {
		super(Collections.emptyList(), "", ports[0]);

		if (ballerinaPlugin.getBallerinaPreferenceStore().isSdkHomeValid()) {
			this.validSDK = true;
		} else {
			return;
		}
		
		final String sdkPath = ballerinaPlugin.getBallerinaPreferenceStore().getSdkHome();
		setCommands(Arrays.asList(pathWithJava(System.getProperty("java.home")), "-cp", 
				classPath(System.getProperty("java.home"), sdkPath), "-Dballerina.home=" + sdkPath,
				"org.ballerinalang.vscode.server.Main", String.valueOf(ports[0]), String.valueOf(ports[1])));
		setWorkingDirectory(sdkPath);
	}

	private static int[] findTwoFreePorts() {
		final int[] ports = new int[2];
		try (ServerSocket socket = new ServerSocket(0)) {
			ports[0] = socket.getLocalPort();
		} catch (IOException e) {
		}

		try (ServerSocket socket = new ServerSocket(0)) {
			ports[1] = socket.getLocalPort();
		} catch (IOException e) {
		}
		return ports;
	}

	@Override
	public void start() throws IOException {
		if (validSDK) {
			super.start();
		} else {
			throw new IOException("Ballerina SDK Home is not set");
		}
	}

	private static String pathWithJava(final String javaHome) {
		final StringBuilder pathBuilder = new StringBuilder(javaHome);
		pathBuilder.append(File.separator).append("bin").append(File.separator).append("java");
		return pathBuilder.toString();
	}

	private static String classPath(final String javaHome, final String sdkHome) throws IOException {
		final StringBuilder pathBuilder = new StringBuilder("");
		String version = System.getProperty("java.version");
		final URL url = BallerinaStreamConnectionProvider.class.getResource("/lib/langserver.jar");
		final File jarFile = new File(FileLocator.toFileURL(url).getPath());

		pathBuilder.append(jarFile.getAbsolutePath());
		pathBuilder.append(File.pathSeparator).append(sdkHome);
		addToPath(pathBuilder, "bre", "lib", "*");
		pathBuilder.append(File.pathSeparator).append(sdkHome);
		addToPath(pathBuilder, "lib", "resources", "composer", "services", "*");

		if (version.startsWith("1.8")) {
			pathBuilder.append(File.pathSeparator);
			pathBuilder.append("\"").append(javaHome).append(File.separator).append("lib").append(File.separator)
					.append("tools.jar");
		}
		return pathBuilder.toString();
	}

	private static void addToPath(StringBuilder builder, String... paths) {
		for (String path : paths) {
			builder.append(File.separator).append(path);
		}
	}
}
