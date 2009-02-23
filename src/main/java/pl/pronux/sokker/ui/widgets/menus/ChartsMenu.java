package pl.pronux.sokker.ui.widgets.menus;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;

public class ChartsMenu extends Menu {

	private Player player;

	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	public ChartsMenu(MenuItem parentItem, Composite parent) {
		super(parentItem);
		MenuItem item;
		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.value")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.salary")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.age")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.stamina")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.pace")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.technique")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.passing")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.keeper")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.defender")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.playmaker")); //$NON-NLS-1$

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.scorer")); //$NON-NLS-1$
}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
