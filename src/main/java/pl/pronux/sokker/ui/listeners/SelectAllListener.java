package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class SelectAllListener implements Listener {
	public void handleEvent(Event event) {
		((Text) event.widget).selectAll();
	}
}
