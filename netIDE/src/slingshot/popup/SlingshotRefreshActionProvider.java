package slingshot.popup;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

import slingshot.Activator;

/**
 * This class handles the Refresh option in the pop-up menu of the project explorer.
 *
 * @author Eduardo Pena
 *
 */
public class SlingshotRefreshActionProvider extends CommonActionProvider {

	private RefreshAction refreshAction;

	private Shell shell;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator
	 * .ICommonActionExtensionSite)
	 */
	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		shell = aSite.getViewSite().getShell();
		makeActions();
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
				refreshAction);
		updateActionBars();
	}

	/**
	 * Adds the refresh resource actions to the context menu.
	 *
	 * @param menu
	 *            context menu to add actions to
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void fillContextMenu(IMenuManager menu) {
		IStructuredSelection selection = (IStructuredSelection) getContext()
				.getSelection();
		boolean hasClosedProjects = false;
		Iterator resources = selection.iterator();

		while (resources.hasNext() && (!hasClosedProjects)) {
			Object next = resources.next();
			IProject project = null;

			if (next instanceof IProject) {
				project = (IProject) next;
			} else if (next instanceof IAdaptable) {
				project = (IProject) ((IAdaptable) next).getAdapter(IProject.class);
			}

			if (project == null) {
				continue;
			}

			if (!project.isOpen()) {
				hasClosedProjects = true;
			}
		}

		if (!hasClosedProjects) {
			refreshAction.selectionChanged(selection);
			menu.appendToGroup(ICommonMenuConstants.GROUP_BUILD, refreshAction);
		}

	}

	/**
	 * This method handles the Refresh Action.
	 */
	protected void makeActions() {


		IShellProvider sp = new IShellProvider() {
			@SuppressWarnings("synthetic-access")
			@Override
			public Shell getShell() {
				return shell;
			}
		};

		refreshAction = new RefreshAction(sp) {
			//By default, you cannot refresh if you have something selected
			//I do not know why?
			protected boolean updateSelection(IStructuredSelection s) {
				return true;
			}
		};

		/*
		refreshAction = new RefreshAction(sp) {
			@Override
			public void run() {
				final IStatus[] errorStatus = new IStatus[1];
				errorStatus[0] = Status.OK_STATUS;
				final WorkspaceModifyOperation op = (WorkspaceModifyOperation) createOperation(errorStatus);
				WorkspaceJob job = new WorkspaceJob("refresh") { //$NON-NLS-1$

					@SuppressWarnings("synthetic-access")
					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor)
							throws CoreException {
						try {
							op.run(monitor);
							if (shell != null && !shell.isDisposed()) {
								shell.getDisplay().asyncExec(new Runnable() {
									@Override
									public void run() {
										StructuredViewer viewer = getActionSite().getStructuredViewer();
										if (viewer != null
												&& viewer.getControl() != null
												&& !viewer.getControl()
														.isDisposed()) {
											viewer.refresh();
										}
									}
								});
							}
						} catch (InvocationTargetException e) {
							String msg = NLS.bind("Exception in {0}. run: {1}",
									getClass().getName(), e.getTargetException());
							throw new CoreException(new Status(IStatus.ERROR,
									Activator.PLUGIN_ID, IStatus.ERROR, msg, e.getTargetException()));
						} catch (InterruptedException e) {
							return Status.CANCEL_STATUS;
						}
						return errorStatus[0];
					}

				};
				ISchedulingRule rule = op.getRule();
				if (rule != null) {
					job.setRule(rule);
				}
				job.setUser(true);
				job.schedule();
			}
		};
	*/
		refreshAction.setImageDescriptor(getImageDescriptor("icons/refresh.png"));//$NON-NLS-1$
		refreshAction.setActionDefinitionId(IWorkbenchCommandConstants.FILE_REFRESH);
	}

	/**
	 * Returns the image descriptor with the given relative path.
	 */
	protected ImageDescriptor getImageDescriptor(String relativePath) {
		return Activator.getIDEImageDescriptor(relativePath);

	}

	@Override
	public void updateActionBars() {
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		refreshAction.selectionChanged(selection);
	}

}
