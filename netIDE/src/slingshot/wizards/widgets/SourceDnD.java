package slingshot.wizards.widgets;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * This class creates a Composite element that holds the drag and drop component for the Load Experiment Wizard.
 *
 * @author Eduardo Pena
 *
 */
public class SourceDnD extends Composite {

	/** The field for the available source files */
	public Table availableSrcs;
	/** The field for the selected source files */
	public Table selectedSrcs;

	/** The column that contains the elements for the available source files */
	TableColumn columnAv;
	/** The column that contains the elements for the selected source files */
	TableColumn columnSel;

	/**
	 * The constructor for this class.
	 * It creates the layout and sets all visual elements for this component.
	 *
	 * @param parent				The composite that will hold this drag and drop component
	 */
	public SourceDnD(Composite parent) {
		super(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		setFont(parent.getFont());

		// Selected source files label
		Label selSrcLabel = new Label(this, SWT.NONE);
		selSrcLabel.setText("Selected Source Files:");
		selSrcLabel.setFont(parent.getFont());

		// Available source files label
		Label avSrcLabel = new Label(this, SWT.NONE);
		avSrcLabel.setText("Available Source Files:");
		avSrcLabel.setFont(parent.getFont());

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL, 80);

		//The field that holds the selected sources
		selectedSrcs = new Table(this, SWT.BORDER | SWT.V_SCROLL);
		selectedSrcs.setLayoutData(data);
		selectedSrcs.setFont(parent.getFont());

		//The field that holds the available sources
		availableSrcs = new Table(this, SWT.BORDER | SWT.V_SCROLL);
		availableSrcs.setLayoutData(data);
		availableSrcs.setFont(parent.getFont());

		//creates drag behavior for the fields
		createDragSource(availableSrcs);
		createDragSource(selectedSrcs);
		//creates drop behavior for the fields
		createDropTarget(selectedSrcs);
		createDropTarget(availableSrcs);

		//sets the column that holds the data for both fields
		columnAv = new TableColumn(availableSrcs, SWT.NONE);
		columnSel = new TableColumn(selectedSrcs, SWT.NONE);

	}

	/**
	 * Adds a new source file element to the available sources field.
	 *
	 * @param src				The name of the source file to be added
	 */
	public void addSource(String src) {
		//create the item to add to the available sources field
		TableItem item = new TableItem(availableSrcs, SWT.NONE);
		item.setText(new String[] { src });
		availableSrcs.redraw();
	}

	/**
	 * Packs the table data for both the available and selected sources.
	 * Necessary for the data inside the tables to display.
	 */
	public void packTables() {
		columnAv.pack();
		columnSel.pack();
	}

	/**
	 * Removes all the elements inside both fields.
	 */
	public void removeAll() {
		availableSrcs.removeAll();
		selectedSrcs.removeAll();
	}

	/**
	 * obtains the sources selected by the user.
	 * (Elemens inside the selected sources field)
	 *
	 * @return ArrayList<String>		A list of source file names selected by the user
	 */
	public ArrayList<String> getSources() {
		ArrayList<String> sources = new ArrayList<String>();
		TableItem[] items = selectedSrcs.getItems();
		for (int i = 0; i < items.length; i++) {
			sources.add(items[i].getText());
		}
		return sources;
	}

	/**
	 * Adds drop behavior to a Table field.
	 * Sets how the table field should behave when an item is dropped inside it.
	 *
	 * @param table					The table field
	 */
	private void createDropTarget(final Table table) {
		// Create the drop target
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		DropTarget target = new DropTarget(table, DND.DROP_MOVE);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY
							: DND.DROP_NONE;
				}

				// Allow dropping text only
				for (int i = 0, n = event.dataTypes.length; i < n; i++) {
					if (TextTransfer.getInstance().isSupportedType(
							event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
					}
				}

			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
			}

			public void drop(DropTargetEvent event) {
				if (TextTransfer.getInstance().isSupportedType(
						event.currentDataType)) {
					// Get the dropped data
					DropTarget target = (DropTarget) event.widget;
					Table table = (Table) target.getControl();
					String data = (String) event.data;

					// Create a new item in the table to hold the dropped data
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(new String[] { data });
					table.redraw();
				}
			}
		});
	}

	/**
	 * Adds drag behavior to a Table field.
	 * Sets how the table field should behave when an item is dragged inside it.
	 *
	 * @param table					The table field
	 */
	private void createDragSource(final Table table) {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		DragSource source = new DragSource(table, DND.DROP_MOVE);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				if (table.getItems().length == 0) {
					event.doit = false;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				// A drop has been performed, so provide the data of the
				// requested type.
				// (Checking the type of the requested data is only
				// necessary if the drag source supports more than
				// one data type but is shown here as an example).
				DragSource ds = (DragSource) event.widget;
				Table table = (Table) ds.getControl();
				TableItem[] selection = table.getSelection();

				StringBuffer buff = new StringBuffer();
				for (int i = 0, n = selection.length; i < n; i++) {
					buff.append(selection[i].getText());
				}

				event.data = buff.toString();
			}

			public void dragFinished(DragSourceEvent event) {
				// A Move operation has been performed so remove the data
				// from the source
				if (event.detail == DND.DROP_MOVE) {
					DragSource ds = (DragSource) event.widget;
					Table table = (Table) ds.getControl();
					int index = table.getSelectionIndex();

					table.remove(index);
				}
			}
		});
	}
}
