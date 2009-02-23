package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.LeagueTeam;

public class LeagueComparator implements SVComparator<LeagueTeam>, ISort {
	public static final int PLACE = 0;

	public static final int NAME = 1;

	public static final int POINTS = 2;

	public static final int WINS = 3;

	public static final int DRAWS = 4;

	 public static final int LOSSES = 5;

	public static final int GOALS_SCORED = 6;

	public static final int GOALS_LOST = 7;

	public static final int RANK_TOTAL = 8;

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
	public int compare(LeagueTeam l1, LeagueTeam l2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case PLACE:
//				rc = (l1.getId() < l2.getId()) ? -1 : 1;
//				break;
			case NAME:
//				rc = coll.compare(l1.getName(), l2.getName());
//				rc = c1.getName().compareTo(c2.getName());
				break;
			case POINTS:
				rc = (l1.getPoints() < l2.getPoints()) ? -1 : 1;
				break;
			case WINS:
				rc = (l1.getWins() < l2.getWins()) ? -1 : 1;
				break;
			case DRAWS:
				rc = (l1.getDraws() < l2.getDraws()) ? -1 : 1;
				break;
			case LOSSES:
				rc = (l1.getLosses() < l2.getLosses()) ? -1 : 1;
				break;
			case GOALS_SCORED:
				rc = (l1.getGoalsScored() < l2.getGoalsScored()) ? -1 : 1;
				break;
			case GOALS_LOST:
				rc = (l1.getGoalsLost() < l2.getGoalsLost()) ? -1 : 1;
				break;
			case RANK_TOTAL:
				rc = coll.compare(l1.getRankTotal(), l2.getRankTotal());
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