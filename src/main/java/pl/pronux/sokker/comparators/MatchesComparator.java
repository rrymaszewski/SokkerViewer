package pl.pronux.sokker.comparators;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Match;

public class MatchesComparator implements SVComparator<Match>, ISort {

	public static final int WEEK_DAY = -1;

	private int direction;

	private int column;

	public MatchesComparator() {
	}
	
	public MatchesComparator(int column, int direction) {
		this.column = column;
		this.direction = direction;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.ExtendedComparator#setColumn(int)
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.ExtendedComparator#getColumn()
	 */
	public int getColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.ExtendedComparator#setDirection(int)
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.ExtendedComparator#getDirection()
	 */
	public int getDirection() {
		return direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.ExtendedComparator#reverseDirection()
	 */
	public void reverseDirection() {
		direction = 1 - direction;
	}

	public int compare(Match m1, Match m2) {
		int rc = 0;
		switch (column) {
		case WEEK_DAY:
			if(m1.getWeek() < m2.getWeek()) {
				rc = -1; 
			} else if(m1.getWeek() > m2.getWeek()) {
				rc = 1;
			} else {
				rc = (m1.getDay() < m2.getDay()) ? -1 : 1;
			}
			break;

		}

		// Check the direction for sort and flip the sign
		// if appropriate
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
