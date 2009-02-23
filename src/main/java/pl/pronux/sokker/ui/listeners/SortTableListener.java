package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableColumn;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.ui.widgets.tables.SVTable;


public class SortTableListener<T> implements SelectionListener, ISort {
	private SVTable<T> table;
	private SVComparator<T> comparator;

	public SortTableListener(SVTable<T> table, SVComparator<T> comparator) {
		this.table = table;
		this.comparator = comparator;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	public void widgetSelected(SelectionEvent e) {
		int column = table.indexOf((TableColumn) e.widget);

		if (column != comparator.getColumn()) {
			comparator.setColumn(column);
			comparator.setDirection(DESCENDING);
			table.setSortColumn(table.getColumn(column));
			table.setSortDirection(SWT.DOWN);
		} else {
			if (comparator.getDirection() == ASCENDING) {
				table.setSortDirection(SWT.DOWN);
				comparator.reverseDirection();
			} else {
				table.setSortDirection(SWT.UP);
				comparator.reverseDirection();
			}
		}

		table.sort(comparator);
	}

}
