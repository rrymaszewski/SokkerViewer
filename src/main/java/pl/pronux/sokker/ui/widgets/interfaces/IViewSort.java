package pl.pronux.sokker.ui.widgets.interfaces;

import pl.pronux.sokker.interfaces.SVComparator;

public interface IViewSort<T> {
	public void sort(SVComparator<T> comparator);
	
	public SVComparator<T> getComparator();
}
