/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Fedorov <Alexander.Fedorov@borland.com>
 *     		- Bug 172000 [Wizards] WizardNewFileCreationPage should support overwriting existing resources
 *******************************************************************************/
package slingshot.wizards.widgets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
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
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;

import slingshot.experiment.structure.ISlingshotExperimentElement;
import slingshot.wizards.creation.WizardNewFileCreationPage;

/**
 * Standard main page for a wizard that creates a file resource.
 * <p>
 * This page may be used by clients as-is; it may be also be subclassed to suit.
 * </p>
 * <p>
 * Subclasses may override
 * <ul>
 * <li><code>getInitialContents</code></li>
 * <li><code>getNewFileLabel</code></li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may extend
 * <ul>
 * <li><code>handleEvent</code></li>
 * </ul>
 * </p>
 */
@SuppressWarnings({ "restriction" })
public class MyWizardNewFileCreationPage extends WizardPage implements Listener {
	private static final int SIZING_CONTAINER_GROUP_HEIGHT = 250;

	// the current resource selection
	private IStructuredSelection currentSelection;

	// cache of newly-created file
	private IFile newFile;

	private URI linkTargetPath;

	// widgets
	private MyResourceAndContainerGroup resourceGroup;

	//private CreateLinkedResourceGroup linkedResourceGroup;

	// initial value stores
	private String initialFileName;

	/**
	 * The file extension to use for this page's file name field when it does
	 * not exist yet.
	 *
	 * @see WizardNewFileCreationPage#setFileExtension(String)
	 * @since 3.3
	 */
	private String initialFileExtension;

	private IPath initialContainerFullPath;

	private boolean initialAllowExistingResources = false;

	/**
	 * First time the advanced group is validated.
	 */
	//private boolean firstLinkCheck = true;

	/**
	 * Creates a new file creation wizard page. If the initial resource
	 * selection contains exactly one container resource then it will be used as
	 * the default container resource.
	 *
	 * @param pageName
	 *            the name of the page
	 * @param selection
	 *            the current resource selection
	 */
	public MyWizardNewFileCreationPage(String pageName,
			IStructuredSelection selection) {
		super(pageName);
		setPageComplete(false);
		this.currentSelection = selection;
	}

	/**
	 * (non-Javadoc) Method declared on IDialogPage.
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		// top level group
		Composite topLevel = new Composite(parent, SWT.NONE);
		topLevel.setLayout(new GridLayout());
		topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		topLevel.setFont(parent.getFont());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(topLevel, IIDEHelpContextIds.NEW_FILE_WIZARD_PAGE);

		// resource and container group
		resourceGroup = new MyResourceAndContainerGroup(topLevel, this,
				getNewFileLabel(),
				IDEWorkbenchMessages.WizardNewFileCreationPage_file, false,
				SIZING_CONTAINER_GROUP_HEIGHT);
		resourceGroup.setAllowExistingResources(initialAllowExistingResources);
		initialPopulateContainerNameField();
		if (initialFileName != null) {
			resourceGroup.setResource(initialFileName);
		}
		if (initialFileExtension != null) {
			resourceGroup.setResourceExtension(initialFileExtension);
		}
		validatePage();
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(topLevel);
	}

	/**
	 * Creates a file resource given the file handle and contents.
	 *
	 * @param fileHandle
	 *            the file handle to create a file resource with
	 * @param contents
	 *            the initial contents of the new file resource, or
	 *            <code>null</code> if none (equivalent to an empty stream)
	 * @param monitor
	 *            the progress monitor to show visual progress with
	 * @exception CoreException
	 *                if the operation fails
	 * @exception OperationCanceledException
	 *                if the operation is canceled
	 *
	 * @deprecated As of 3.3, use or override {@link #createNewFile()} which
	 *             uses the undoable operation support. To supply customized
	 *             file content for a subclass, use
	 *             {@link #getInitialContents()}.
	 */
	protected void createFile(IFile fileHandle, InputStream contents,
			IProgressMonitor monitor) throws CoreException {
		if (contents == null) {
			contents = new ByteArrayInputStream(new byte[0]);
		}

		try {
			// Create a new file resource in the workspace
			if (linkTargetPath != null) {
				fileHandle.createLink(linkTargetPath,
						IResource.ALLOW_MISSING_LOCAL, monitor);
			} else {
				IPath path = fileHandle.getFullPath();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				int numSegments = path.segmentCount();
				if (numSegments > 2
						&& !root.getFolder(path.removeLastSegments(1)).exists()) {
					// If the direct parent of the path doesn't exist, try to
					// create the
					// necessary directories.
					for (int i = numSegments - 2; i > 0; i--) {
						IFolder folder = root.getFolder(path
								.removeLastSegments(i));
						if (!folder.exists()) {
							folder.create(false, true, monitor);
						}
					}
				}
				fileHandle.create(contents, false, monitor);
			}
		} catch (CoreException e) {
			// If the file already existed locally, just refresh to get contents
			if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED) {
				fileHandle.refreshLocal(IResource.DEPTH_ZERO, null);
			} else {
				throw e;
			}
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	/**
	 * Creates a file resource handle for the file with the given workspace
	 * path. This method does not create the file resource; this is the
	 * responsibility of <code>createFile</code>.
	 *
	 * @param filePath
	 *            the path of the file resource to create a handle for
	 * @return the new file resource handle
	 * @see #createFile
	 */
	protected IFile createFileHandle(IPath filePath) {
		return IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(
				filePath);
	}

	/**
	 * Creates a new file resource in the selected container and with the
	 * selected name. Creates any missing resource containers along the path;
	 * does nothing if the container resources already exist.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on on this page currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this page caches the new file once it has been successfully
	 * created; subsequent invocations of this method will answer the same file
	 * resource without attempting to create it again.
	 * </p>
	 * <p>
	 * This method should be called within a workspace modify operation since it
	 * creates resources.
	 * </p>
	 *
	 * @return the created file resource, or <code>null</code> if the file was
	 *         not created
	 */
	public IFile createNewFile() {
		if (newFile != null) {
			return newFile;
		}

		// create the new file and cache it if successful

		IPath containerPath = resourceGroup.getContainerFullPath();
		IPath newFilePath = containerPath.append(resourceGroup.getResource());

		final IFile newFileHandle = createFileHandle(newFilePath);
		final InputStream initialContents = getInitialContents();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				CreateFileOperation op = new CreateFileOperation(newFileHandle,
						linkTargetPath, initialContents,
						IDEWorkbenchMessages.WizardNewFileCreationPage_title);
				try {
					// see bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=219901
					// directly execute the operation so that the undo state is
					// not preserved.  Making this undoable resulted in too many
					// accidental file deletions.
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
																.getShell(), // Was
														// Utilities.getFocusShell()
														IDEWorkbenchMessages.WizardNewFileCreationPage_errorTitle,
														null, // no special
														// message
														((CoreException) e
																.getCause())
																.getStatus());
									} else {
										IDEWorkbenchPlugin
												.log(
														getClass(),
														"createNewFile()", e.getCause()); //$NON-NLS-1$
										MessageDialog
												.openError(
														getContainer()
																.getShell(),
														IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorTitle,
														NLS
																.bind(
																		IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorMessage,
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
			// Execution Exceptions are handled above but we may still get
			// unexpected runtime errors.
			IDEWorkbenchPlugin.log(getClass(),
					"createNewFile()", e.getTargetException()); //$NON-NLS-1$
			MessageDialog
					.open(MessageDialog.ERROR,
							getContainer().getShell(),
							IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorTitle,
							NLS
									.bind(
											IDEWorkbenchMessages.WizardNewFileCreationPage_internalErrorMessage,
											e.getTargetException().getMessage()), SWT.SHEET);

			return null;
		}

		newFile = newFileHandle;

		return newFile;
	}

	/**
	 * Returns the scheduling rule to use when creating the resource at the
	 * given container path. The rule should be the creation rule for the
	 * top-most non-existing parent.
	 *
	 * @param resource
	 *            The resource being created
	 * @return The scheduling rule for creating the given resource
	 * @since 3.1
	 * @deprecated As of 3.3, scheduling rules are provided by the undoable
	 *             operation that this page creates and executes.
	 */
	protected ISchedulingRule createRule(IResource resource) {
		IResource parent = resource.getParent();
		while (parent != null) {
			if (parent.exists()) {
				return resource.getWorkspace().getRuleFactory().createRule(
						resource);
			}
			resource = parent;
			parent = parent.getParent();
		}
		return resource.getWorkspace().getRoot();
	}

	/**
	 * Returns the current full path of the containing resource as entered or
	 * selected by the user, or its anticipated initial value.
	 *
	 * @return the container's full path, anticipated initial value, or
	 *         <code>null</code> if no path is known
	 */
	public IPath getContainerFullPath() {
		return resourceGroup.getContainerFullPath();
	}

	/**
	 * Returns the current file name as entered by the user, or its anticipated
	 * initial value. <br>
	 * <br>
	 * The current file name will include the file extension if the
	 * preconditions are met.
	 *
	 * @see WizardNewFileCreationPage#setFileExtension(String)
	 *
	 * @return the file name, its anticipated initial value, or
	 *         <code>null</code> if no file name is known
	 */
	public String getFileName() {
		if (resourceGroup == null) {
			return initialFileName;
		}

		return resourceGroup.getResource();
	}

	/**
	 * Returns the file extension to use when creating the new file.
	 *
	 * @return the file extension or <code>null</code>.
	 * @see WizardNewFileCreationPage#setFileExtension(String)
	 * @since 3.3
	 */
	public String getFileExtension() {
		if (resourceGroup == null) {
			return initialFileExtension;
		}
		return resourceGroup.getResourceExtension();
	}

	/**
	 * Returns a stream containing the initial contents to be given to new file
	 * resource instances. <b>Subclasses</b> may wish to override. This default
	 * implementation provides no initial contents.
	 *
	 * @return initial contents to be given to new file resource instances
	 */
	protected InputStream getInitialContents() {
		return null;
	}

	/**
	 * Returns the label to display in the file name specification visual
	 * component group.
	 * <p>
	 * Subclasses may reimplement.
	 * </p>
	 *
	 * @return the label to display in the file name specification visual
	 *         component group
	 */
	protected String getNewFileLabel() {
		return IDEWorkbenchMessages.WizardNewFileCreationPage_fileLabel;
	}

	/**
	 * The <code>WizardNewFileCreationPage</code> implementation of this
	 * <code>Listener</code> method handles all events and enablements for
	 * controls on this page. Subclasses may extend.
	 */
	public void handleEvent(Event event) {
		setPageComplete(validatePage());
	}

	/**
	 * Sets the initial contents of the container name entry field, based upon
	 * either a previously-specified initial value or the ability to determine
	 * such a value.
	 */
	@SuppressWarnings("rawtypes")
	protected void initialPopulateContainerNameField() {
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
	}

	/**
	 * Sets the flag indicating whether existing resources are permitted to be
	 * specified on this page.
	 *
	 * @param value
	 *            <code>true</code> if existing resources are permitted, and
	 *            <code>false</code> otherwise
	 * @since 3.4
	 */
	public void setAllowExistingResources(boolean value) {
		if (resourceGroup == null) {
			initialAllowExistingResources = value;
		} else {
			resourceGroup.setAllowExistingResources(value);
		}
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

	/**
	 * Sets the value of this page's file name field, or stores it for future
	 * use if this page's controls do not exist yet.
	 *
	 * @param value
	 *            new file name
	 */
	public void setFileName(String value) {
		if (resourceGroup == null) {
			initialFileName = value;
		} else {
			resourceGroup.setResource(value);
		}
	}

	/**
	 * Set the only file extension allowed for this page's file name field. If
	 * this page's controls do not exist yet, store it for future use. <br>
	 * <br>
	 * If a file extension is specified, then it will always be appended with a
	 * '.' to the text from the file name field for validation when the
	 * following conditions are met: <br>
	 * <br>
	 * (1) File extension length is greater than 0 <br>
	 * (2) File name field text length is greater than 0 <br>
	 * (3) File name field text does not already end with a '.' and the file
	 * extension specified (case sensitive) <br>
	 * <br>
	 * The file extension will not be reflected in the actual file name field
	 * until the file name field loses focus.
	 *
	 * @param value
	 *            The file extension without the '.' prefix (e.g. 'java', 'xml')
	 * @since 3.3
	 */
	public void setFileExtension(String value) {
		if (resourceGroup == null) {
			initialFileExtension = value;
		} else {
			resourceGroup.setResourceExtension(value);
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

		String resourceName = resourceGroup.getResource();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus result = workspace.validateName(resourceName, IResource.FILE);
		if (!result.isOK()) {
			setErrorMessage(result.getMessage());
			return false;
		}

		if(valid)
			setErrorMessage(null);

		return valid;
	}

	public boolean isPageComplete(){
		return validatePage();
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			resourceGroup.setFocus();
		}
	}
}
