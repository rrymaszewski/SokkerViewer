package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.Sort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Junior;

public class JuniorTrashComparator implements SVComparator<Junior>, Sort {
//	public static final int ID = 0;

	public static final int NAME = 0;

	public static final int SURNAME = 1;
	
	public static final int FORMATION = 2;

	public static final int SKILL = 3;
	
	public static final int STATUS = 4;

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
	public int compare(Junior j1, Junior j2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
//			case ID:
//				rc = (c1.getId() < c2.getId()) ? -1 : 1;
//				break;
			case NAME:
				rc = coll.compare(j1.getName(), j2.getName());
//				rc = j1.getName().compareTo(j2.getName());
				break;
			case SURNAME:
				rc = coll.compare(j1.getSurname(), j2.getSurname());
//				rc = j1.getSurname().compareTo(j2.getSurname());
				break;
			case FORMATION:
				rc = j1.getFormation() < j2.getFormation() ? -1 : 1;
				break;
			case SKILL:
				rc = (j1.getSkills()[j1.getSkills().length-1].getSkill() < j2.getSkills()[j2.getSkills().length-1].getSkill()) ? -1 : 1;
				break;
			case STATUS:
				rc = (j1.getStatus() < j2.getStatus()) ? -1 : 1;
				break;
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