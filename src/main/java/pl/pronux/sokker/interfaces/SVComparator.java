package pl.pronux.sokker.interfaces;

import java.util.Comparator;

public interface SVComparator<T> extends Comparator<T> {

	/**
	 * Sets the column for sorting
	 * 
	 * @param column
	 *            the column
	 */
	public abstract void setColumn(int column);

	public abstract int getColumn();

	/**
	 * Sets the direction for sorting
	 * 
	 * @param direction
	 *            the direction
	 */
	public abstract void setDirection(int direction);

	public abstract int getDirection();

	/**
	 * Reverses the direction
	 */
	public abstract void reverseDirection();

}