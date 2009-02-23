package pl.pronux.sokker.ui.events;

import org.eclipse.swt.widgets.Event;

import pl.pronux.sokker.model.Club;

public class TeamEvent extends Event {

	private Club team;

	public TeamEvent(Club team) { 
		this.team = team;
	}

	public Club getTeam() {
		return team;
	}

	public void setTeam(Club team) {
		this.team = team;
	}
}
