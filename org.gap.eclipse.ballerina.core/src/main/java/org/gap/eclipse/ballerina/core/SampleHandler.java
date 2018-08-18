package org.gap.eclipse.ballerina.core;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Executed by click menu.<br/>
 */
public class SampleHandler extends AbstractHandler {

    private final IWorkbenchWindow window;

    /**
     * constructor.
     */
    public SampleHandler() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        this.window = workbench.getActiveWorkbenchWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        MessageDialog.openInformation(window.getShell(), "Eclipse Plugin Archetype", "Hello, Maven+Eclipse world,\n org.gap.eclipse.ballerina is built with Tycho");
        return null;
    }

}
