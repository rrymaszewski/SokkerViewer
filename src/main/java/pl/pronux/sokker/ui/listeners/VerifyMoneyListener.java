package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class VerifyMoneyListener implements Listener {
	public void handleEvent(Event e) {
		String text = e.text;
		char[] chars = new char[text.length()];
		text.getChars(0, chars.length, chars, 0);
		for (int j = 0; j < chars.length; j++) {
			if ((!('0' <= chars[j] && chars[j] <= '9')) && !(chars[j] == '.')) {
				e.doit = false;
				return;
			}
		}
	}
}
