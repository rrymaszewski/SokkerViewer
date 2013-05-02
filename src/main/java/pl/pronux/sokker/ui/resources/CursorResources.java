package pl.pronux.sokker.ui.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import pl.pronux.sokker.ui.handlers.DisplayHandler;

public final class CursorResources {
	private static final Map<Integer, Cursor> cache = new HashMap<Integer, Cursor>();

	static {
		DisplayHandler.getDisplay().disposeExec(new Runnable() {
			public void run() {
				if (!cache.isEmpty()) {
					for (Cursor cursor : cache.values()) {
						if (!cursor.isDisposed()) {
							cursor.dispose();
						}
					}
				}
			}
		});
	}

	private CursorResources() {
	}

	public static Cursor getCursor(int style) {
		return getCursor(DisplayHandler.getDisplay(), style);
	}
	
	public static Cursor getCursor(Display display, int style) {
		Cursor cursor = cache.get(style);
		if (cursor == null || cursor.isDisposed()) {
			cursor = new Cursor(display, style);
			cache.put(style, cursor);
		}
		return cursor;
		
	}

}
