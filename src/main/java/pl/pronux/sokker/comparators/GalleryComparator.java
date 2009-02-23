package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.GalleryImage;

public class GalleryComparator implements SVComparator<GalleryImage>, ISort {

	public static final int TYPE = 0;

	public static final int PUBLICATION_DATE = 1;
	
	public static final int NAME = 2;

	public static final int AUTHOR = 3;


	private int column;

	private int direction;

	/**
	 * Compares two Player objects
	 *
	 * @param obj1
	 *            the first Player
	 * @param obj2
	 *            the second Player
	 * @return int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(GalleryImage g1, GalleryImage g2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case TYPE:
				rc = (g1.getType() < g2.getType()) ? -1 : 1;
				break;
			case NAME:
				rc = coll.compare(g1.getName(), g2.getName());
				break;
			case AUTHOR:
				rc = coll.compare(g1.getUserID(), g2.getUserID());
				break;
			case PUBLICATION_DATE:
				rc = g1.getPublicationDate().compareTo(g2.getPublicationDate());
				break;
//			case FORMAT:
//				rc = coll.compare(g1.getFormat(), g2.getFormat());
//				break;
//			case SIZE:
//				rc = ((g1.getImage().height * g1.getImage().width) < (g2.getImage().height * g2.getImage().width)) ? -1 : 1;
//				break;
		}

		// Check the direction for sort and flip the sign
		// if appropriate
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

	/**
	 * Sets the column for sorting
	 *
	 * @param column
	 *            the column
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	/**
	 * Sets the direction for sorting
	 *
	 * @param direction
	 *            the direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	/**
	 * Reverses the direction
	 */
	public void reverseDirection() {
		direction = 1 - direction;
	}

}