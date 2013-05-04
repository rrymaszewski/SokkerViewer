package pl.pronux.sokker.comparators;

import pl.pronux.sokker.interfaces.Sort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Report;

public class ReportComparator implements SVComparator<Report>, Sort {

	public static final int IMAGE = 0;
	public static final int DATE = 1;

	public static final int TYPE = 2;
	
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
	public int compare(Report r1, Report r2) {
		int rc = 0;

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case IMAGE:
			if(r1.getType() < r2.getType()) {
				rc = -1;
			} else if(r1.getType() > r2.getType()) {
				rc = 1;
			} else {
				rc = (r1.getReportId() < r2.getReportId()) ? -1 : 1;
			}
			break;
			case TYPE:
				if(r1.getType() < r2.getType()) {
					rc = -1;
				} else if(r1.getType() > r2.getType()) {
					rc = 1;
				} else {
					rc = (r1.getReportId() < r2.getReportId()) ? -1 : 1;
				}
				break;
			case DATE:
				rc = r1.getDate().compareTo(r2.getDate());
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