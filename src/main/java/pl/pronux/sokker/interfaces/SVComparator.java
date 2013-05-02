package pl.pronux.sokker.interfaces;

import java.util.Comparator;

public interface SVComparator<T> extends Comparator<T> {

	/**
	 * Sets the column for sorting
	 * 
	 * @param column
	 *            the column
	 */
	void setColumn(int column);

	int getColumn();

	/**
	 * Sets the direction for sorting
	 * 
	 * @param direction
	 *            the direction
	 */
	void setDirection(int direction);

	int getDirection();

	/**
	 * Reverses the direction
	 */
	void reverseDirection();

}