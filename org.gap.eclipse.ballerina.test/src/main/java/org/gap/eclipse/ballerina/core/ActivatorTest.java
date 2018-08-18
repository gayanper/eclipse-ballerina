package org.gap.eclipse.ballerina.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
* Sample integration test. In Eclipse, right-click > Run As > JUnit-Plugin. <br/>
* In Maven CLI, run "mvn integration-test".
*/
public class ActivatorTest {

	@Test
	public void veryStupidTest() {
		assertEquals("org.gap.eclipse.ballerina.core",BallerinaPlugin.PLUGIN_ID);
		assertTrue("Plugin should be started", BallerinaPlugin.getDefault().started);
	}
}