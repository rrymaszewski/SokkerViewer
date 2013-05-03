package pl.pronux.sokker.ui.widgets.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;

public class SVTree extends Tree {

	public SVTree(Composite parent, int style) {
		super(parent, style);
		
		final Menu menu = new Menu(this.getShell(), SWT.POP_UP);

		this.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event arg0) {
				SVTree.this.setMenu(menu);
				// _mainShellTree.getMenu()._setVisible(false);
				SVTree.this.getMenu().setVisible(false);
			}
		});

		ViewerHandler.getViewer().addListener(IEvents.SHOW, new Listener() {
			public void handleEvent(Event event) {
				TreeItem item = SVTree.this.getSelection()[0];

				if (item != null) {
					while (item.getParentItem() != null) {
						item = item.getParentItem();
					}
					ViewerHandler.getViewer().showView(((Composite) item.getData(IPlugin.IDENTIFIER))); 
				}
			}
		});

		this.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				TreeItem item = SVTree.this.getItem(point);

				if (item != null) {
					while (item.getParentItem() != null) {
						item = item.getParentItem();
					}
					ViewerHandler.getViewer().showView(((Composite) item.getData(IPlugin.IDENTIFIER))); 
				}
			}
		});

		this.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < SVTree.this.getSelection().length; i++) {
					item = SVTree.this.getSelection()[i];
				}

				if (item != null) {

					if (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_UP) {
						if (item != null) {
							while (item.getParentItem() != null) {
								item = item.getParentItem();
							}
							ViewerHandler.getViewer().showView(((Composite) item.getData(IPlugin.IDENTIFIER))); 
						}
					} else if (event.keyCode == SWT.ARROW_RIGHT) {
						item.setExpanded(true);
					} else if (event.keyCode == SWT.ARROW_LEFT) {
						item.setExpanded(false);
					}
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public void collapseAll() {
		collapse(this.getItems());
	}

	private void collapse(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			items[i].setExpanded(false);
			collapse(items[i].getItems());
		}
	}
	
	public void selectTopItem() {
		TreeItem[] selectTreeItem = { this.getTopItem() };
		this.setSelection(selectTreeItem);
	}
}
