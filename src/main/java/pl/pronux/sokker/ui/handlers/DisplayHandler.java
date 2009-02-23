package pl.pronux.sokker.ui.handlers;

import org.eclipse.swt.widgets.Display;

public class DisplayHandler {
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if(display == null) {
			display = Display.getDefault();
		}
		return display;
	}
}
