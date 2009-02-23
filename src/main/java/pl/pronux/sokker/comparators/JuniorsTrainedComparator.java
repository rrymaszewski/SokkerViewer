package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Player;

public class JuniorsTrainedComparator implements SVComparator<Junior>, ISort {
//	public static final int ID = 0;

	public static final int NAME = 0;

	public static final int SURNAME = 1;

	public static final int SKILL = 2;

	public static final int SUM = 3;
	
	public static final int STAMINA = 4;

	public static final int SUM_WITHOUT_STAMINA = 5;
	
	public static final int PACE = 6;
	
	public static final int TECHNIQUE = 7;
	
	public static final int PASSING = 8;
	
	public static final int KEEPER = 9;
	
	public static final int DEFENDER = 10;
	
	public static final int PLAYMAKER = 11;
	
	public static final int SCORER = 12;
	
	public static final int AGE = 13;
	
	public static final int WEEKS = 14;
	
	public static final int AVERAGE_JUMP = 15;
	
	public static final int COSTS = 16;
	
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
		Player p1 = j1.getPlayer();
		Player p2 = j2.getPlayer();
		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
//			case ID:
//				rc = (c1.getId() < c2.getId()) ? -1 : 1;
//				break;
			case NAME:
				rc = coll.compare(j1.getName(),j2.getName());
				break;
			case SURNAME:
				rc = coll.compare(j1.getSurname(),j2.getSurname());
				break;
			case SKILL:
				rc = (j1.getSkills()[j1.getSkills().length-1].getSkill() < j2.getSkills()[j2.getSkills().length-1].getSkill()) ? -1 : 1;
				break;
			case WEEKS:
				rc = (j1.getSkills()[0].getWeeks() < j2.getSkills()[0].getWeeks()) ? -1 : 1;
				break;
			case COSTS:
				rc = (j1.getAllMoneyToSpend().toInt() < j2.getAllMoneyToSpend().toInt()) ? -1 : 1;
				break;
			case AGE:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getAge() < p2.getSkills()[0].getAge()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case STAMINA:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getStamina() < p2.getSkills()[0].getStamina()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case PACE:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getPace() < p2.getSkills()[0].getPace()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case TECHNIQUE:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getTechnique() < p2.getSkills()[0].getTechnique()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case PASSING:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getPassing() < p2.getSkills()[0].getPassing()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case KEEPER:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getKeeper() < p2.getSkills()[0].getKeeper()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case DEFENDER:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getDefender() < p2.getSkills()[0].getDefender()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case PLAYMAKER:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getPlaymaker() < p2.getSkills()[0].getPlaymaker()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case SCORER:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getScorer() < p2.getSkills()[0].getScorer()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case SUM:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getSummarySkill() + p1.getSkills()[0].getStamina() < p2.getSkills()[0].getSummarySkill() + p2.getSkills()[0].getStamina()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case SUM_WITHOUT_STAMINA:
				if(p1 != null && p2 != null) {
					rc = (p1.getSkills()[0].getSummarySkill() < p2.getSkills()[0].getSummarySkill()) ? -1 : 1;
				} else {
					rc = comparePlayers(p1, p2);
				}
				break;
			case AVERAGE_JUMP:
				if (j1.getPops() < 2 && j2.getPops() < 2) {
					rc = (j1.getAveragePops() < j2.getAveragePops()) ? -1 : 1;
				} else if (j1.getPops() < 2 && j2.getPops() >= 2) {
					rc = 1;
				} else if (j1.getPops() >= 2 && j2.getPops() < 2) {
					rc = -1;
				} else {
					rc = (j1.getAveragePops() < j2.getAveragePops()) ? -1 : 1;
				}
				break;
			default:
				break;

		}

		// Check the direction for sort and flip the sign
		// if appropriate
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

	private int comparePlayers(Player p1, Player p2) {
		if(p1 != null && p2 == null) {
			return 1;
		} else if(p1 == null && p2 != null) {
			return -1;
		} else {
			return 0;
		}
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