package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.Sort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Note;

public class NoteComparator implements SVComparator<Note>, Sort {
//	public static final int ID = 0;

	public static final int DATE = 0;

	public static final int TITLE = 1;

	public static final int TEXT = 2;
	
	public static final int ALERT_DATE = 3;
	
	public static final int MODIFICATION_DATE = 4;

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
	public int compare(Note n1, Note n2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);
		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
//			case ID:
//				rc = (c1.getId() < c2.getId()) ? -1 : 1;
//				break;
			case DATE:
				rc = n1.getDate().getMillis() < n2.getDate().getMillis() ? -1 : 1;
				break;
			case TITLE:
				rc = coll.compare(n1.getTitle(), n2.getTitle());
//				rc = n1.getTitle().compareTo(n2.getTitle());
				break;
			case TEXT:
				rc = coll.compare(n1.getText(), n2.getText());
//				rc = n1.getText().compareTo(n2.getText());
				break;
			case ALERT_DATE:
				if(n1.getAlertDate() != null && n2.getAlertDate() != null) {
					rc = n1.getAlertDate().getMillis() < n2.getAlertDate().getMillis() ? -1 : 1;
					
				} else if(n1.getAlertDate() == null && n2.getAlertDate() == null) {
					rc = 0;
					 
				} else if(n1.getAlertDate() != null&& n2.getAlertDate() == null) {
					rc = 1;
				} else if(n1.getAlertDate() == null&& n2.getAlertDate() != null) {
					rc = -1;
				}
				break;
			case MODIFICATION_DATE:
				rc = n1.getModificationDate().getMillis() < n2.getModificationDate().getMillis() ? -1 : 1;
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