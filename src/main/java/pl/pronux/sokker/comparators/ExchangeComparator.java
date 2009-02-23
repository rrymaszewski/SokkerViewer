package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Exchange;

public class ExchangeComparator implements SVComparator<Exchange>, ISort {

//	public static final int FLAG = 0;

	public static final int ID = 1;

	public static final int NAME = 2;

	public static final int ORIGINALNAME = 3;

	public static final int CURRENCY = 4;

	public static final int VALUE = 5;

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
	public int compare(Exchange ex1, Exchange ex2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);
		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case ID:
				rc = (ex1.getId() < ex2.getId()) ? -1 : 1;
				break;
			case NAME:
				rc = coll.compare(ex1.getName(), ex2.getName());
//				rc = ex1.getName().compareTo(ex2.getName());
				break;
			case ORIGINALNAME:
				rc = coll.compare(ex1.getOriginalName(), ex2.getOriginalName());
//				rc = ex1.getOriginalName().compareTo(ex2.getOriginalName());
				break;
			case CURRENCY:
				rc = coll.compare(ex1.getCurrency(), ex2.getCurrency());
//				rc = ex1.getCurrency().compareTo(ex2.getCurrency());
				break;
			case VALUE:
				rc = (ex1.getValue() < ex2.getValue()) ? -1 : 1;
				break;
		}

		// Check the direction for sort and flip the sign
		// if appropriate
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.ExtendedComparator#setColumn(int)
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.ExtendedComparator#getColumn()
	 */
	public int getColumn() {
		return column;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.ExtendedComparator#setDirection(int)
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.ExtendedComparator#getDirection()
	 */
	public int getDirection() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see pl.pronux.sokker.ExtendedComparator#reverseDirection()
	 */
	public void reverseDirection() {
		direction = 1 - direction;
	}
}