package pl.pronux.sokker.ui.widgets.interfaces;

import pl.pronux.sokker.interfaces.SVComparator;

public interface IViewSort<T> {
	void sort(SVComparator<T> comparator);
	
	SVComparator<T> getComparator();
}
