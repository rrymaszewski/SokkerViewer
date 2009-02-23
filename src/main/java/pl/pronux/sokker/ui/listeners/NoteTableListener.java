package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.ui.widgets.tables.SVTable;

public class NoteTableListener<T> implements Listener {

	private SVTable<T> table;
	private int column;
	private String identifier;

	public NoteTableListener(SVTable<T> table, String identifier, int column) {
		this.table = table;
		this.column = column;
		this.identifier = identifier;
	}

	public void handleEvent(Event event) {
		Rectangle clientArea = table.getClientArea();
		Point pt = new Point(event.x, event.y);
		int index = table.getTopIndex();

		while (index < table.getItemCount()) {
			boolean visible = false;
			TableItem item = table.getItem(index);
			for (int i = 0; i < table.getColumnCount(); i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt) && i == column) {
					table.openNote(item, identifier, column);
				}
				if (!visible && rect.intersects(clientArea)) {
					visible = true;
				}
			}
			if (!visible) {
				return;
			}
			index++;
		}
	}

}
