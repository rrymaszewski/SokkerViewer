package pl.pronux.sokker.comparators;

import java.io.File;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;

public class BackupDBComparator implements SVComparator<File>, ISort {
	
	public static final int NAME = 0;
	
	public static final int DATE = 1;
	
	public static final int LONG = 2;
	
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
	public int compare(File file1, File file2) {
		int rc = 0;

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case DATE:
				rc = (file1.lastModified() < file2.lastModified()) ? -1 : 1;
				break;
			case NAME:
				rc = file1.getName().compareTo(file2.getName());
				break;
			case LONG:
				rc = file1.getName().split("\\.")[0].compareTo(file2.getName().split("\\.")[0]); //$NON-NLS-1$ //$NON-NLS-2$
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