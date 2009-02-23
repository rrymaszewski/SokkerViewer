package pl.pronux.sokker.ui.widgets.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.ui.handlers.DisplayHandler;

public class MessageDialog {

	private static int result = 0;
	
	public static int openErrorMessage(final Shell parent, final String message) {
		int result = 0;
		Display display = DisplayHandler.getDisplay();
		if (display != null) {
			display.syncExec(new Runnable() {
				public void run() {
					MessageBox msg = new MessageBox(parent, SWT.OK | SWT.ICON_ERROR);
					msg.setMessage(message);
					msg.open();
				}
			});
		}
		return result;
	}
	
	public static int openInformationMessage(final Shell parent, final String message) {
		int result = 0;
		Display display = DisplayHandler.getDisplay();
		if (display != null) {
			display.syncExec(new Runnable() {
				public void run() {
					MessageBox msg = new MessageBox(parent, SWT.OK | SWT.ICON_INFORMATION);
					msg.setMessage(message);
					msg.open();
				}
			});
		}
		return result;
	}
	
	public static int openConfirmationMessage(final Shell parent, final String message) {
		
		Display display = DisplayHandler.getDisplay();
		if (display != null) {
			display.syncExec(new Runnable() {
				public void run() {
					MessageBox msg = new MessageBox(parent, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
					msg.setMessage(message);
					result = msg.open();
				}
			});
		}
		return result;
	}
}
