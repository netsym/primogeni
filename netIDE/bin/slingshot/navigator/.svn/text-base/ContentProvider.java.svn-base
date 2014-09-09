package slingshot.navigator;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonViewer;

import slingshot.experiment.structure.ISlingshotExperimentElement;
import slingshot.experiment.structure.SlingshotExperimentParent;
import slingshot.natures.ExperimentNature;

/**
 * This class manages the content that is displayed on the navigator.
 * This class checks when resources in the workspace change and takes action accordingly.
 *
 * @author Eduardo Pena
 *
 */
public class ContentProvider implements ITreeContentProvider, IResourceChangeListener {

	/** An empty object array to state that an element has no children */
    private static final Object[] NO_CHILDREN = {};
    /** A HashMap that stores all the Slingshot projects currently in the workspace */
    private Map<String, Object> _wrapperCache = new HashMap<String, Object>();
    private CommonViewer viewer;

    /**
     * a Runnable object that refreshes the navigator
     */
    public  final Runnable refreshViewer = new Runnable() {
		public void run() {
			if (isDisposed(viewer.getControl())) {
				return;
			}
			TreePath[] treePaths = viewer.getExpandedTreePaths();
			try {
				viewer.refresh();
				viewer.setExpandedTreePaths(treePaths);
			} catch(Exception e) {}
		}
	};

	/**
	 * The empty constructor for this content provider
	 */
    public ContentProvider(){
    	//System.out.println("called content provider constructor");
    	ResourcesPlugin.getWorkspace().addResourceChangeListener(this,IResourceChangeEvent.POST_CHANGE);
    }


    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
     * Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
    	//System.out.println("ContentProvider.getChildren: " + parentElement.getClass().getName());

    	Object[] children = null;
        if (parentElement instanceof IWorkspaceRoot) {
            IProject[] projects = ((IWorkspaceRoot)parentElement).getProjects();
            children = createPrimeProjectParents(projects);
        } else if (parentElement instanceof ISlingshotExperimentElement) {
            children = ((ISlingshotExperimentElement) parentElement).getChildren();
        }

        if(children == null)
        	children = NO_CHILDREN;

        return children;

    }

    /**
     * This method takes the parentElement of each project and checks whether it is of Slingshot nature.
     * Only projects of Slingshot nature will be shown in the project explorer.
     *
     * @param parentElement			The project to be checked
     * @return						The Slingshot project, or NULL if the project wasn't of Slingshot nature
     */
    private Object createPrimeProjectParent(IProject parentElement) {

        Object result = null;
        //Project must be open first
        try {
            if(!parentElement.isAccessible()){
            	try {
            		parentElement.open(null);
            	} catch(Exception e) { }
            	
            	/* Debug
            		if (!parentElement.exists())
            			System.out.println("Project "+ parentElement.getName() + " does not exist!");
            	
            		//IFolder peFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(parentElement.getFullPath().append(parentElement.getName()));
            		//peFolder.create(false, true, null);
            		if(!parentElement.isAccessible())
            			System.out.println("Project " + parentElement.getName() + " is inaccessible.");
            	*/
                	try {
            			//System.out.println("deleting project " + parentElement.getName());
        				parentElement.delete(true, null);
            			//System.out.println("done");
        			} catch (CoreException e1) {
        				//e1.printStackTrace();
        			}

            }
        } catch (Exception e) {
        	//System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        
        
        try {
            if (parentElement.getNature(ExperimentNature.NATURE_ID) != null) {
                result = new SlingshotExperimentParent(parentElement);
            }
            /* Debug
            else
            	System.out.println("Element " + parentElement.getName() + " element nature found null.");
       		*/
        } catch (CoreException e) {
        	//System.out.println("CoreException: " + e.getMessage());
        	//System.out.println(e);
        	//e.printStackTrace();
        }

        return result;
    }

    /**
     * This method checks whether several projects are of Slingshot nature and adds them to our wrapperCache variable.
     *
     * @param projects			The list of projects to be checked
     * @return					The list of all Slingshot projects
     */
    private Object[] createPrimeProjectParents(IProject[] projects) {
        Object[] result = null;

        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < projects.length; i++) {
        	Object primeProjectParent = _wrapperCache.get(projects[i].getName());
            if (primeProjectParent == null) {
                primeProjectParent = createPrimeProjectParent(projects[i]);
                if(primeProjectParent != null)
                	_wrapperCache.put(projects[i].getName(), primeProjectParent);
            }
            if(primeProjectParent != null){
            	list.add(primeProjectParent);
            } // else ignore the project
            /* Debug
            else
            	System.out.println("project " + projects[i].getName() + " project found null.");
            */
        }

        result = new Object[list.size()];
        list.toArray(result);

        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
     * )
     */
    @Override
    public Object getParent(Object element) {
    	//System.out.println("ContentProvider.getParent: " + element.getClass().getName());

    	Object parent = null;

        if (element instanceof IProject) {
            parent = ((IProject)element).getWorkspace().getRoot();
        } else if (element instanceof ISlingshotExperimentElement) {
            parent = ((ISlingshotExperimentElement) element).getParent();
        }else if (element instanceof IFile){
        	parent = ((IFile) element).getParent();
        }
        // else parent = null if IWorkspaceRoot or anything else

        return parent;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
     * Object)
     */
    @Override
    public boolean hasChildren(Object element) {
    	//System.out.println("ContentProvider.hasChildren: " + element.getClass().getName());

    	boolean hasChildren = false;

        if (IWorkspaceRoot.class.isInstance(element)) {
            hasChildren = ((IWorkspaceRoot)element).getProjects().length > 0;
        } else if (ISlingshotExperimentElement.class.isInstance(element)) {
            hasChildren = ((ISlingshotExperimentElement)element).hasChildren();
        }

        // else it is not one of these so return false

        return hasChildren;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
     * .lang.Object)
     */
    @Override
    public Object[] getElements(Object inputElement) {
        // This is the same as getChildren() so we will call that instead
        return getChildren(inputElement);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {
    	ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
     * .viewers.Viewer, java.lang.Object, java.lang.Object)
     */

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    	//System.out.println("Input Changed");
    	this.viewer = (CommonViewer)viewer;
    }


	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		//System.out.println("Resources Changed");
		if (isDisposed(viewer.getControl())) {
			return;
		}
		runWithCareAboutThreads(viewer.getControl().getDisplay(), refreshViewer);
	}

	private static void runWithCareAboutThreads(Display display, Runnable runnable) {
		if (display.getThread() == Thread.currentThread()) {
			runnable.run();
		} else {
			display.asyncExec(runnable);
		}
	}

	private static boolean isDisposed(Control control) {
		return control == null || control.isDisposed();
	}



}