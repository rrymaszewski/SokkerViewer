package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.widgets.tables.SVTable;

public class TableLabelsListener<T> implements Listener {
	private Label label = null;
	private Shell shell = ViewerHandler.getViewer();
	private Shell tip = null;
	private SVTable<T> table;
	private Listener labelListener;
	
	public TableLabelsListener(final SVTable<T> table) {
		this.table = table;
		
		// Disable native tooltip
		table.setToolTipText(""); 

		// Implement a "fake" tooltip
		labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM"); 
					// Assuming table is single select, set the selection as
					// if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					break;
				// fall through
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};
	}

	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
		case SWT.KeyDown:
		case SWT.MouseMove: {
			if (tip == null) {
				break;
			}
			tip.dispose();
			tip = null;
			label = null;
			break;
		}
		case SWT.MouseHover: {
			Rectangle clientArea = table.getClientArea();
			Point pt = new Point(event.x, event.y);
			TableItem item = table.getItem(pt);
			if (item != null) {
				boolean visible = false;
				for (int i = 0; i < table.getColumnCount(); i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						if (!visible) {
							return;
						}
						if (tip != null && !tip.isDisposed()) {
							tip.dispose();
						}
						tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
						tip.setLayout(new FillLayout());
						label = new Label(tip, SWT.NONE);
						label.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item); 
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						label.setSize(0,0);
						
						table.setLabel(label, i, item);

						Point size = label.getSize();
						if(size.x != 0 && size.y != 0) {
							Point ptDisplay = table.toDisplay(event.x - size.x, event.y - size.y);
							tip.setBounds(ptDisplay.x, ptDisplay.y, size.x + 3, size.y + 3);
							tip.setVisible(true);
						} else {
							tip.dispose();
						}
					}
					if (!visible && rect.intersects(clientArea)) {
						visible = true;
					}
				}

			}
		}
			break;
		}
		
	}

}
