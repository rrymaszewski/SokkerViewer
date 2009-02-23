package pl.pronux.sokker.ui.events;

import org.eclipse.swt.widgets.Event;

import pl.pronux.sokker.model.Player;

public class TranslateEvent extends Event {
	private Player player;

	public TranslateEvent() {
	}

	public TranslateEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
