package pl.pronux.sokker.ui.widgets.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.shells.AddMatchShell;

public class MatchesMenu extends Menu {
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public MatchesMenu(final Decorations parent, int style) {
		super(parent, style);
		MenuItem item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("matchesmenu.item.match.add")); 
		item.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				new AddMatchShell(parent.getShell(), SWT.CLOSE | SWT.PRIMARY_MODAL).open();
			}

		});
	}
}
