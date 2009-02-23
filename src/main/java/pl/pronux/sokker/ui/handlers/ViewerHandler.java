package pl.pronux.sokker.ui.handlers;

import org.eclipse.swt.dnd.Clipboard;

import pl.pronux.sokker.ui.Viewer;

public class ViewerHandler {

	private static Viewer viewer;
	private static Clipboard clipboard;

	public static Viewer getViewer() {
		return ViewerHandler.viewer;
	}

	public static void setViewer(Viewer viewer) {
		ViewerHandler.viewer = viewer;
	}

	public static Clipboard getClipboard() {
		return ViewerHandler.clipboard;
	}

	public static void setClipboard(Clipboard clipboard) {
		ViewerHandler.clipboard = clipboard;
	}

}
