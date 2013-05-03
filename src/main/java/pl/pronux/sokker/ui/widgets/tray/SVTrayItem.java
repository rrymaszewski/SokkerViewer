package pl.pronux.sokker.ui.widgets.tray;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.wizards.updater.UpdaterWizard;

public class SVTrayItem extends TrayItem{

	public SVTrayItem(Tray tray, int style) {
		super(tray, style);
		this.setToolTipText("SokkerViewer " + SV.SK_VERSION); 

		this.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (ViewerHandler.getViewer().getVisible()) {
					ViewerHandler.getViewer().setVisible(false);
				} else {
					ViewerHandler.getViewer().setVisible(true);
				}
			}
		});

		final Menu menu = new Menu(ViewerHandler.getViewer(), SWT.POP_UP);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("viewer.menu.tools.preferences")); 
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().getConfigurator().setVisible(true);
			}
		});
		item = new MenuItem(menu, SWT.SEPARATOR);

		item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("button.update")); 
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new UpdaterWizard(ViewerHandler.getViewer()).open();
			}
		});
		item = new MenuItem(menu, SWT.SEPARATOR);

		item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("button.close")); 
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().close();
			}
		});

		this.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				menu.setVisible(true);
			}
		});
		this.setImage(ImageResources.getImageResources("sokkerViewer_ico[16x16].png")); 
	}
	
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}

}
