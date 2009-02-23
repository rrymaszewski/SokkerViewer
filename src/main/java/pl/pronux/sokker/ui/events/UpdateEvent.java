package pl.pronux.sokker.ui.events;

import org.eclipse.swt.widgets.Event;

public class UpdateEvent extends Event {
	private boolean update;

	public UpdateEvent(boolean update) {
		this.update = update;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

}
