package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.PlayerArchive;

public class PlayerArchiveComparator implements SVComparator<PlayerArchive>, ISort {

	public static final int COUNTRY = 0;

	public static final int ID = 1;

	public static final int NAME = 2;

	public static final int SURNAME = 3;

	public static final int YOUTH_TEAM_ID = 4;

	public static final int NOTE = 5;

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
	public int compare(PlayerArchive p1, PlayerArchive p2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case COUNTRY:
				rc = (p1.getCountryId() < p2.getCountryId()) ? -1 : 1;
				break;
			case ID:
				rc = (p1.getId() < p2.getId()) ? -1 : 1;
				break;
			case NAME:
				rc = coll.compare(p1.getName(), p2.getName());
				break;
			case SURNAME:
				rc = coll.compare(p1.getSurname(), p2.getSurname());
				break;
			case YOUTH_TEAM_ID:
				rc = (p1.getYouthTeamId() < p2.getYouthTeamId()) ? -1 : 1;
				break;
			case NOTE:
				rc = coll.compare(p1.getNote(), p2.getNote());
				break;
			default:
				// TODO: Implement 'default' statement
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