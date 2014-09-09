package slingshot.experiment;

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


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import jprime.State;
import jprime.util.DynamicClassManager;
import jprime.util.ModelInterface;
import jprime.util.ModelInterface.ModelParam;
import jprime.util.ModelInterface.ModelParamValue;
import jprime.util.XMLModelInterface;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.framework.internal.core.Constants;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import slingshot.ModelViewPerspective;
import slingshot.Perspective;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.commands.ExperimentCommandHandler;
import slingshot.experiment.create.CreateExperimentModel;
import slingshot.experiment.structure.SlingshotExperimentParent;
import slingshot.model.ModelView;
import slingshot.visualization.ResetRowVisitor;

/**
 * 
 * This class is responsible for managing, loading, and closing projects. 
 *  
 * @author Nathanael Van Vorst
 * @author Edurado Pena
 *
 */
@SuppressWarnings("restriction")
public class ProjectLoader {
	private static PyExperiment ACTIVE_EXPERIMENT = null;
	public static final String GENERATED_MODELS = "Models";
	private static ArrayList<ExperimentCommandHandler> buttons = new ArrayList<ExperimentCommandHandler>();
	private static HashMap<String, ProjectState> activeProjects = new HashMap<String, ProjectState>();
	
	/**
	 * @return the experiment which currently in view
	 */
	public static PyExperiment getActiveExperiment() {
		return ACTIVE_EXPERIMENT;
	}
	
	/**
	 * @param ae set the experiment that is currently in view
	 */
	public static void setActiveExperiment(PyExperiment ae) {
		ACTIVE_EXPERIMENT = ae;
	}
	
	/**
	 * An internal class which wraps all the state associated with an active project view.
	 * @author Nathanael Van Vorst
	 */
	public static class ProjectState {
		
		/**
		 * The gui components, including the console and prefuse visualization
		 */
		public ModelView view;
		
		/**
		 * the jython/jPRIME python interpreter and a wrapper of the jPRIME experiment construct
		 */
		public PyExperiment exp;
		
		/**
		 * If this is not null, then state updates that are received while an experiment is running
		 * will be sent to the listener.
		 */
		public IExpStateListener l;
		
		/**
		 * the constructor
		 */
		public ProjectState() {
			super();
			this.view = null;
			this.exp = null;
			this.l=null;
		}
		
		/** 
		 * @return the state of the experiment
		 */
		public State getState() {
			if(exp==null)
				return State.CLOSED;
			return exp.getState();
		}
		
		/**
		 * @param l set the state update listener
		 */
		public void setExpStateListener(IExpStateListener l) {
			this.l=l;
		}
	}

	/**
	 * Get the state of a project from its name
	 * @param expName the name of the experiment/library
	 * @return the associated state (if found), otherwise null.
	 */
	public static ProjectState getProjectState(String expName) {
		return activeProjects.get(expName);
	}

	/**
	 * @return try to figure out what project/experiment is currently being viewed.
	 */
	public static String getCurrentExpName() {
		String expName = null;
		try {
			IPerspectiveDescriptor descriptor = null;
			if(null != PlatformUI.getWorkbench() &&
					null != PlatformUI.getWorkbench().getActiveWorkbenchWindow() && 
					null != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()) {
				descriptor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective();				
			}
			//get the name of the selected experiment and attempt to load it from the database
			if(descriptor != null && descriptor.getId().equalsIgnoreCase(ModelViewPerspective.ID)){
				if(null != PlatformUI.getWorkbench() &&
						null != PlatformUI.getWorkbench().getActiveWorkbenchWindow() && 
						null != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() &&
						null != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()) {
					expName = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getTitle();
				}
			}
			else if(descriptor != null && descriptor.getId().equalsIgnoreCase(Perspective.ID)){
				if(null != PlatformUI.getWorkbench() &&
						null != PlatformUI.getWorkbench().getActiveWorkbenchWindow() && 
						null != PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()) {
					IStructuredSelection selection = (IStructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
					
					if(selection != null && selection.getFirstElement() != null){
						Object o = selection.getFirstElement();
						if(o instanceof SlingshotExperimentParent){
							SlingshotExperimentParent e = (SlingshotExperimentParent) o;
							expName = e.getText();
						}
					}
				}
			}
		}catch(Exception e) {
			expName=null;
		}
		if(expName!=null &&expName.length()==0) expName=null;
		//System.out.println("found expname="+expName);
		return expName;
	}

	/**
	 * Load an experiment from the database.
	 * 
	 * @param expName the name of the experiment to load.
	 */
	public static void LoadExperiment(final String expName) {
		//create a new PyExperiment and attempt to load the experiment with that name existing in the database
		if(activeProjects.containsKey(expName)) {
			ProjectState p=activeProjects.get(expName);
			p.view.setFocus();
		}
		else {
			try{

				ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()); 
				try {
					dialog.run(true, false, new IRunnableWithProgress(){ 
						public void run(final IProgressMonitor monitor) { 
							monitor.beginTask("Loading experiment '" + expName+"'", 100);
							PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
								@Override
								public void run() {
									monitor.worked(5);
									IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
									monitor.worked(5);
									PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(reg.findPerspectiveWithId(ModelViewPerspective.ID));
									monitor.worked(5);
									ProjectState p = new ProjectState();
									monitor.worked(5);
									p.exp = new PyExperiment(expName);
									monitor.worked(5);
									monitor.worked(5);
									activeProjects.put(expName, p);
									monitor.worked(5);
									p.view=ProjectLoader.createView(expName);
									monitor.worked(5);
									p.exp.startVisualization();
									monitor.worked(5);
								}
							});
							monitor.done(); 
						} 
					});
				} catch (InvocationTargetException e) {
				} catch (InterruptedException e) {
				}

			}catch(Exception e){
				System.err.println("Couldn't load the project from the database");
				e.printStackTrace();
			}
		}
	}

	
	/** 
	 * Create a new experiment
	 * @param model the information required to create a new experiment.
	 */
	public static void createExperiment(final CreateExperimentModel model) {
		try{

			ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()); 
			try {
				dialog.run(true, false, new IRunnableWithProgress(){ 
					public void run(final IProgressMonitor monitor) { 
						monitor.beginTask("Creating experiment '" + model.getExpName()+"'", 100);
						PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
							@Override
							public void run() {
								monitor.worked(5);
								// First open the new perspective and load the View
								IPerspectiveRegistry reg = PlatformUI.getWorkbench().getPerspectiveRegistry();
								monitor.worked(5);
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(reg.findPerspectiveWithId(ModelViewPerspective.ID));
								monitor.worked(5);
								ProjectState p = new ProjectState();
								monitor.worked(5);
								activeProjects.put(model.getExpName(), p);
								monitor.worked(5);
								p.exp=new PyExperiment(model.getExpName());
								monitor.worked(5);
								//create the new experiment view
								p.view=createView(model.getExpName());
								monitor.worked(5);
								if(model.getSourceFile().exists()){
									getExperimentFromSourceFile(model,p);
								}
								monitor.worked(5);
							}
						});
						monitor.done(); 
					} 
				});
			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) {
			}

		}catch(Exception e){
			System.err.println("Couldn't load the project from the database");
			e.printStackTrace();
		}

	}

	/**
	 * Create the GUI components of a project
	 * 
	 * @param expName
	 * @return
	 */
	private static ModelView createView(String expName) {
		String secondaryID = expName;
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		/* open view */
		ModelView view = null;
		try {
			view = (ModelView) (page.showView(ModelView.ID, secondaryID, IWorkbenchPage.VIEW_CREATE));
		} catch (PartInitException e) {
			System.out.println("Something went wrong while creating the new view");
			e.printStackTrace();
		}

		page.activate(view);

		return view;
	}

	
	/**
	 * 
	 * Cleanup resources associted with an experiment and flush all changes to the database.
	 * 
	 * @param expName
	 * @return
	 */
	public static ProjectState closeExp(String expName, boolean cleanup) {
		ProjectState p = activeProjects.remove(expName);
		if(p != null) {
			try {
				if(p.exp.getState().gt(State.PARTITIONED)) {
					p.exp.terminateExperiment();
				}
			}
			catch(Exception e) {}
			if(!p.exp.beenSaved)
				p.exp.save(false);
			if(cleanup)p.exp.cleanup(p);
			else new ResetRowVisitor(p.exp.getRootNode());

			p.view.closeView();
			if(null != p.l){
				p.l.updateButtons(State.CLOSED);
			}
			p.l=null;
			p.view=null;
			p.exp=null;
		}
		return p;
	}


	/**
	 * Create a new experiment from a file
	 * 
	 * @param model
	 * @param p
	 */
	private static void getExperimentFromSourceFile(CreateExperimentModel model, ProjectState p) {

		File modelFile = model.getSourceFile();
		String modelFileName = modelFile.getName();
		String modelFileExtension = modelFileName.substring(modelFileName.lastIndexOf(".")+1);

		// check the type of the source file
		if (modelFileExtension.equalsIgnoreCase("xml")) {
			try {
				XMLModelInterface xml = new XMLModelInterface(p.exp.getExperiment().getMetadata().getDatabase(), p.exp.getExperiment(), modelFile);
				xml.buildModel(new HashMap<String, ModelInterface.ModelParamValue>());
			} catch (Exception e) {
				Util.dialog(Type.ERROR, "Error Compiling '"+modelFile.getAbsolutePath()+"'", Util.getStackTraceAsString(e));
			}

			p.exp.startVisualization();
		}
		else if (modelFileExtension.equalsIgnoreCase("class") || modelFileExtension.equalsIgnoreCase("java")) {
			try {
				Collection<String> cp = getBundleClassPath("slingshot");
				ClassLoader cl = ProjectLoader.class.getClassLoader();
				DynamicClassManager dm =new DynamicClassManager(cl,cp,modelFile.getAbsolutePath().replace(modelFileName, ""));
				ModelInterface m = dm.loadModel(modelFileName, p.exp.getExperiment().getMetadata().getDatabase(), p.exp.getExperiment());
				Map<String, ModelInterface.ModelParamValue> params = m.loadParametersFromSystemProperties();
				for(ModelParam pd : m.getParameterDefinitions()) {
					if(pd.name.compareTo("CWD")==0) {
						params.put(pd.name, new ModelParamValue(pd,modelFile.getParent()));
					}
				}
				m.buildModel(params);
				p.exp.startVisualization();
			} catch (Exception e) {
				Util.dialog(Type.ERROR, "Error Compiling '"+modelFile.getAbsolutePath()+"'", Util.getStackTraceAsString(e));
			}
		}
		else if (modelFileExtension.equalsIgnoreCase("py")) {
			try {
				p.exp.getConsole().execfile(modelFile.getAbsolutePath());
				p.exp.startVisualization();
			} catch (Exception e) {
				Util.dialog(Type.ERROR, "Error Compiling '"+modelFile.getAbsolutePath()+"'", Util.getStackTraceAsString(e));
			}
		} else {
			throw new RuntimeException("Invalid model type!");
		}
	}

	/**
	 * @param b
	 */
	public static void addExpCommand(ExperimentCommandHandler b) {
		buttons.add(b);	
	}

	/**
	 * As the state of an experiment changes certain commands are either activated for de-activated.
	 * This function takes care of that.
	 * 
	 * @param expname the name of the project
	 */
	public static void updateCommandAvailablility(final String expname) {
		if(expname != null) {
			updateCommandAvailablility(ProjectLoader.getProjectState(expname));
		}
	}
	
	/**
	 * As the state of an experiment changes certain commands are either activated for de-activated.
	 * This function takes care of that.
	 * 
	 * @param ps the project state
	 */
	public static void updateCommandAvailablility(final ProjectState ps) {
		int tries=1000;
		while(tries>0) {
			try {
				//System.out.println("updateCommandAvailablility");
				if(ps != null && ps.view!=null && null != ps.view.getLogTab()) {
					ps.view.getLogTab().getDisplay().asyncExec(new Runnable() {
						public void run() {
							if(ps.l != null) {
								ps.l.updateButtons(ps.getState());
							}
							for(ExperimentCommandHandler b: buttons) {
								b.updateButton(ps.getState());
							}
						}
					});
					tries=0;
				}
			}
			catch(Exception e) {
				//e.printStackTrace();
			}
			tries--;
		}
	}


	/**
	 * @param pluginID
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	public static Collection<String> getBundleClassPath(String pluginID) throws IOException, BundleException {
		Collection<String> result = new LinkedHashSet<String>(); // no duplicates, preserve order
		getBundleClassPath(pluginID, result, 1);
		return result;
	}

	/**
	 * @param pluginID
	 * @param result
	 * @param nestinglevel
	 * @throws IOException
	 * @throws BundleException
	 */
	@SuppressWarnings("null")
	private static void getBundleClassPath(String pluginID, Collection<String> result, int nestinglevel) throws IOException, BundleException {
		Bundle bundle = Platform.getBundle(pluginID);
		if (bundle == null) throw new BundleException(pluginID + " cannot be retrieved from the Platform");
		// first the entries from this plugin itself
		for(String s: getClassPath(bundle)) {
			File f = new File(s.replace("file:", "").replace("!/",""));
			if(f.exists() & !f.isDirectory())
				result.add(f.getAbsolutePath());
		}
		// next the entries from dependent plugins
		String requires = (String) bundle.getHeaders().get(Constants.REQUIRE_BUNDLE);
		ManifestElement[] elements = ManifestElement.parseHeader(Constants.REQUIRE_BUNDLE, requires);
		if (elements != null) {
			// ignore elements that are not reexported?
			for (int i = 0; i < elements.length; ++i) {
				ManifestElement element = elements[i];
				Bundle requiredBundle = Platform.getBundle(element.getValue());
				if (requiredBundle == null) throw new BundleException(pluginID + " requires bundle "+requiredBundle.getSymbolicName()+" which cannot be retrieved from the Platform");
				if (nestinglevel == 1) {
					getBundleClassPath(requiredBundle.getSymbolicName(),result, nestinglevel + 1);
				} else {
					String[] visibility = element.getDirectives(Constants.VISIBILITY_DIRECTIVE);
					if (visibility != null && visibility[0].equalsIgnoreCase("reexport"))
						getBundleClassPath(requiredBundle.getSymbolicName(),result, nestinglevel + 1);
				}
			}
		} }

	/**
	 * @param bundle
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	private static Collection<String> getClassPath(Bundle bundle) throws IOException, BundleException {
		Collection<String> result = new ArrayList<String>();
		String requires = (String) bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
		if (requires == null) requires = ".";
		ManifestElement[] elements = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, requires);
		if (elements != null) {
			for (int i = 0; i < elements.length; ++i) {
				ManifestElement element = elements[i];
				String value = element.getValue();
				if (".".equals(value)) value = "/";
				URL url = bundle.getEntry(value);
				if (url != null) {
					URL resolvedURL = FileLocator.resolve(url);
					String filestring = FileLocator.toFileURL(resolvedURL).getFile();
					File f = new File(filestring);
					if(f.exists() & !f.isDirectory())
						result.add(f.getAbsolutePath());
				}
			}
		}
		return result;
	}

}
