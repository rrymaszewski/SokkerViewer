package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class VerifyDigitsListener implements Listener, VerifyListener {
	public void handleEvent(Event e) {
		String string = e.text;
		char[] chars = new char[string.length()];
		string.getChars(0, chars.length, chars, 0);
		for (int j = 0; j < chars.length; j++) {
			if ((!('0' <= chars[j] && chars[j] <= '9')) && !(chars[j] == '.')) {
				e.doit = false;
				return;
			}
		}
	}

	public void verifyText(VerifyEvent event) {
		String string = event.text;
		char[] chars = new char[string.length()];
		string.getChars(0, chars.length, chars, 0);
		for (int j = 0; j < chars.length; j++) {
			if ((!('0' <= chars[j] && chars[j] <= '9')) && !(chars[j] == '.')) {
				event.doit = false;
				return;
			}
		}
	}
}
