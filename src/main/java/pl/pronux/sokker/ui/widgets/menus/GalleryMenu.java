package pl.pronux.sokker.ui.widgets.menus;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pl.pronux.sokker.data.xml.dao.GalleryImagesDownloader;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.dialogs.ProgressBarDialog;

public class GalleryMenu extends Menu {
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public GalleryMenu(final Decorations parent, int style) {
		super(parent, style);
		MenuItem item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("GalleryMenu.item.getimages")); //$NON-NLS-1$
		item.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ProgressBarDialog dialog = new ProgressBarDialog(parent.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
				try {
					dialog.run(false, true, false, new GalleryImagesDownloader(new ArrayList<GalleryImage>()));
				} catch (InterruptedException e1) {
					// FIXME
				} catch (InvocationTargetException e1) {
					// FIXME
				}
			}

		});
	}
}
