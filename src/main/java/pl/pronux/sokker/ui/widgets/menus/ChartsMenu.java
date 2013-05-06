package pl.pronux.sokker.ui.widgets.menus;

import org.eclipse.swt.SWT;
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
	public ChartsMenu(MenuItem parentItem) {
		super(parentItem);
		MenuItem item;
		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.value")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.salary")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.age")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.stamina")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.pace")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.technique")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.passing")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.keeper")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.defender")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.playmaker")); 

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("player.scorer")); 
}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
