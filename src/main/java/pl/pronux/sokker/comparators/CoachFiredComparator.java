package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Coach;

public class CoachFiredComparator implements SVComparator<Coach>, ISort {
//	public static final int ID = 0;

	public static final int NAME = 1;

	public static final int SURNAME = 2;

	// public static final int COUNTRYFROM = 5;

	public static final int SALARY = 3;
	
	public static final int AGE = 4;

	public static final int GENERAL_SKILL = 5;

	public static final int STAMINA = 6;

	public static final int PACE = 7;

	public static final int TECHNIQUE = 8;

	public static final int PASSING = 9;

	public static final int KEEPERS = 10;

	public static final int DEFENDERS = 11;

	public static final int PLAYMAKERS = 12;

	public static final int SCORERS = 13;

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
	public int compare(Coach c1, Coach c2) {
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
				rc = coll.compare(c1.getName(), c2.getName());
//				rc = c1.getName().compareTo(c2.getName());
				break;
			case SURNAME:
				rc = coll.compare(c1.getSurname(), c2.getSurname());
//				rc = c1.getSurname().compareTo(c2.getSurname());
				break;
			case AGE:
				rc = (c1.getAge() < c2.getAge()) ? -1 : 1;
				break;
			case SALARY:
				rc = c1.getSalary().compareTo(c2.getSalary());
				break;
			case GENERAL_SKILL:
				rc = (c1.getGeneralskill() < c2.getGeneralskill()) ? -1 : 1;
				break;
			case STAMINA:
				rc = (c1.getStamina() < c2.getStamina()) ? -1 : 1;
				break;
			case PACE:
				rc = (c1.getPace() < c2.getPace()) ? -1 : 1;
				break;
			case TECHNIQUE:
				rc = (c1.getTechnique() < c2.getTechnique()) ? -1 : 1;
				break;
			case PASSING:
				rc = (c1.getPassing() < c2.getPassing()) ? -1 : 1;
				break;
			case KEEPERS:
				rc = (c1.getKeepers() < c2.getKeepers()) ? -1 : 1;
				break;
			case DEFENDERS:
				rc = (c1.getDefenders() < c2.getDefenders()) ? -1 : 1;
				break;
			case PLAYMAKERS:
				rc = (c1.getPlaymakers() < c2.getPlaymakers()) ? -1 : 1;
				break;
			case SCORERS:
				rc = (c1.getScorers() < c2.getScorers()) ? -1 : 1;
				break;
			default: 
				//TODO: Implement 'default' statement
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