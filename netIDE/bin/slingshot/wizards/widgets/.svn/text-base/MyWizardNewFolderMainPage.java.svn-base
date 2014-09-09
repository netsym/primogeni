package slingshot.wizards.widgets;

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Leon J. Breedt - Added multiple folder creation support (in WizardNewFolderMainPage)
 *
 *******************************************************************************/

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.dialogs.UIResourceFilterDescription;
import org.eclipse.ui.ide.undo.CreateFolderOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;

import slingshot.experiment.structure.ISlingshotExperimentElement;

/**
 * Standard main page for a wizard that creates a folder resource.
 * <p>
 * This page may be used by clients as-is; it may be also be subclassed to suit.
 * </p>
 * <p>
 * Subclasses may extend
 * <ul>
 * <li><code>handleEvent</code></li>
 * </ul>
 * </p>
 */
@SuppressWarnings("restriction")
public class MyWizardNewFolderMainPage extends WizardPage implements Listener {
	private static final int SIZING_CONTAINER_GROUP_HEIGHT = 250;

	private IStructuredSelection currentSelection;

	private IFolder newFolder;

	private IPath initialContainerFullPath;

	// widgets
	private MyResourceAndContainerGroup resourceGroup;

	private UIResourceFilterDescription[] filterList = null;

	/**
	 * Creates a new folder creation wizard page. If the initial resource
	 * selection contains exactly one container resource then it will be used as
	 * the default container resource.
	 *
	 * @param pageName
	 *            the name of the page
	 * @param selection
	 *            the current resource selection
	 */
	public MyWizardNewFolderMainPage(String pageName,
			IStructuredSelection selection) {
		super("newFolderPage1");//$NON-NLS-1$
		setTitle(pageName);
		setDescription(IDEWorkbenchMessages.WizardNewFolderMainPage_description);
		this.currentSelection = selection;
	}

	/**
	 * (non-Javadoc) Method declared on IDialogPage.
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		// top level group
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));

		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
				IIDEHelpContextIds.NEW_FOLDER_WIZARD_PAGE);

		resourceGroup = new MyResourceAndContainerGroup(composite, this,
				IDEWorkbenchMessages.WizardNewFolderMainPage_folderName,
				IDEWorkbenchMessages.WizardNewFolderMainPage_folderLabel,
				false, SIZING_CONTAINER_GROUP_HEIGHT);
		resourceGroup.setAllowExistingResources(false);
		initializePage();
		validatePage();
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
	}

	/**
	 * Creates a folder resource given the folder handle.
	 *
	 * @param folderHandle
	 *            the folder handle to create a folder resource for
	 * @param monitor
	 *            the progress monitor to show visual progress with
	 * @exception CoreException
	 *                if the operation fails
	 * @exception OperationCanceledException
	 *                if the operation is canceled
	 *
	 * @deprecated As of 3.3, use {@link #createNewFolder()} which uses the
	 *   undoable operation support.
	 */
	protected void createFolder(IFolder folderHandle, IProgressMonitor monitor)
			throws CoreException {
		try {
			// Create the folder resource in the workspace
			// Update: Recursive to create any folders which do not exist
			// already
			if (!folderHandle.exists()) {
					IPath path = folderHandle.getFullPath();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
							.getRoot();
					int numSegments = path.segmentCount();
					if (numSegments > 2
							&& !root.getFolder(path.removeLastSegments(1))
									.exists()) {
						// If the direct parent of the path doesn't exist, try
						// to create the
						// necessary directories.
						for (int i = numSegments - 2; i > 0; i--) {
							IFolder folder = root.getFolder(path
									.removeLastSegments(i));
							if (!folder.exists()) {
								folder.create(false, true, monitor);
							}
						}
					}
					folderHandle.create(false, true, monitor);
			}
		} catch (CoreException e) {
			// If the folder already existed locally, just refresh to get
			// contents
			if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED) {
				folderHandle.refreshLocal(IResource.DEPTH_INFINITE,
						new SubProgressMonitor(monitor, 500));
			} else {
				throw e;
			}
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	/**
	 * Creates a folder resource handle for the folder with the given workspace
	 * path. This method does not create the folder resource; this is the
	 * responsibility of <code>createFolder</code>.
	 *
	 * @param folderPath
	 *            the path of the folder resource to create a handle for
	 * @return the new folder resource handle
	 * @see #createFolder
	 */
	protected IFolder createFolderHandle(IPath folderPath) {
		return IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFolder(
				folderPath);
	}

	/**
	 * Creates a container resource handle for the container with the given workspace path. This
	 * method does not create the resource.
	 *
	 * @param containerPath the path of the container resource to create a handle for
	 * @return the new container resource handle
	 * @see #createFolder
	 * @since 3.6
	 */
	protected IContainer createContainerHandle(IPath containerPath) {
		if (containerPath.segmentCount() == 1)
			return IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getProject(
					containerPath.segment(0));
		return IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFolder(
				containerPath);
	}

	/**
	 * Creates a new folder resource in the selected container and with the
	 * selected name. Creates any missing resource containers along the path;
	 * does nothing if the container resources already exist.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on this page currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this page caches the new folder once it has been successfully
	 * created; subsequent invocations of this method will answer the same
	 * folder resource without attempting to create it again.
	 * </p>
	 * <p>
	 * This method should be called within a workspace modify operation since it
	 * creates resources.
	 * </p>
	 *
	 * @return the created folder resource, or <code>null</code> if the folder
	 *         was not created
	 */
	public IFolder createNewFolder() {
		if (newFolder != null) {
			return newFolder;
		}

		// create the new folder and cache it if successful
		final IPath containerPath = resourceGroup.getContainerFullPath();
		IPath newFolderPath = containerPath.append(resourceGroup.getResource());
		final IFolder newFolderHandle = createFolderHandle(newFolderPath);

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				AbstractOperation op;
				op = new CreateFolderOperation(
					newFolderHandle, null, false, filterList,
					IDEWorkbenchMessages.WizardNewFolderCreationPage_title);
				try {
					// see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
					// directly execute the operation so that the undo state is
					// not preserved.  Making this undoable can result in accidental
					// folder (and file) deletions.
					op.execute(monitor, WorkspaceUndoUtil
						.getUIInfoAdapter(getShell()));
				} catch (final ExecutionException e) {
					getContainer().getShell().getDisplay().syncExec(
							new Runnable() {
								public void run() {
									if (e.getCause() instanceof CoreException) {
										ErrorDialog
												.openError(
														getContainer()
																.getShell(), // Was Utilities.getFocusShell()
														IDEWorkbenchMessages.WizardNewFolderCreationPage_errorTitle,
														null, // no special message
														((CoreException) e
																.getCause())
																.getStatus());
									} else {
										IDEWorkbenchPlugin
												.log(
														getClass(),
														"createNewFolder()", e.getCause()); //$NON-NLS-1$
										MessageDialog
												.openError(
														getContainer()
																.getShell(),
														IDEWorkbenchMessages.WizardNewFolderCreationPage_internalErrorTitle,
														NLS
																.bind(
																		IDEWorkbenchMessages.WizardNewFolder_internalError,
																		e
																				.getCause()
																				.getMessage()));
									}
								}
							});
				}
			}
		};

		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			// ExecutionExceptions are handled above, but unexpected runtime
			// exceptions and errors may still occur.
			IDEWorkbenchPlugin.log(getClass(),
					"createNewFolder()", e.getTargetException()); //$NON-NLS-1$
			MessageDialog
					.open(MessageDialog.ERROR,
							getContainer().getShell(),
							IDEWorkbenchMessages.WizardNewFolderCreationPage_internalErrorTitle,
							NLS
									.bind(
											IDEWorkbenchMessages.WizardNewFolder_internalError,
											e.getTargetException().getMessage()), SWT.SHEET);
			return null;
		}

		newFolder = newFolderHandle;

		return newFolder;
	}
	/**
	 * The <code>WizardNewFolderCreationPage</code> implementation of this
	 * <code>Listener</code> method handles all events and enablements for
	 * controls on this page. Subclasses may extend.
	 */
	public void handleEvent(Event ev) {
		setPageComplete(validatePage());
	}

	/**
	 * Initializes this page's controls.
	 */
	@SuppressWarnings("rawtypes")
	protected void initializePage() {
		if (initialContainerFullPath != null) {
			resourceGroup.setContainerFullPath(initialContainerFullPath);
		} else {
			Iterator it = currentSelection.iterator();
			if (it.hasNext()) {
				Object object = it.next();
				IResource selectedResource = null;
				if (object instanceof IProject) {
					selectedResource = (IProject) object;
				} else if (object instanceof ISlingshotExperimentElement) {
					selectedResource = ((ISlingshotExperimentElement) object).getProject();
				}
				if (selectedResource != null) {
					if (selectedResource.getType() == IResource.FILE) {
						selectedResource = selectedResource.getProject();
					}
					if (selectedResource.isAccessible()) {
						resourceGroup.setContainerFullPath(selectedResource.getFullPath());
					}
				}
			}
		}

		setPageComplete(false);
	}

	/*
	 * @see DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			resourceGroup.setFocus();
		}
	}

	/**
	 * Returns whether this page's controls currently all contain valid values.
	 *
	 * @return <code>true</code> if all controls are valid, and
	 *         <code>false</code> if at least one is invalid
	 */
	protected boolean validatePage() {
		boolean valid = true;

		if (!resourceGroup.areAllValuesValid()) {
			// if blank name then fail silently
			if (resourceGroup.getProblemType() == MyResourceAndContainerGroup.PROBLEM_RESOURCE_EMPTY
					|| resourceGroup.getProblemType() == MyResourceAndContainerGroup.PROBLEM_CONTAINER_EMPTY) {
				setMessage(resourceGroup.getProblemMessage());
				setErrorMessage(null);
			} else {
				setErrorMessage(resourceGroup.getProblemMessage());
			}
			valid = false;
		}
		return valid;
	}

	/**
	 * Sets the value of this page's container name field, or stores it for
	 * future use if this page's controls do not exist yet.
	 *
	 * @param path
	 *            the full path to the container
	 */
	public void setContainerFullPath(IPath path) {
		if (resourceGroup == null) {
			initialContainerFullPath = path;
		} else {
			resourceGroup.setContainerFullPath(path);
		}
	}
}
