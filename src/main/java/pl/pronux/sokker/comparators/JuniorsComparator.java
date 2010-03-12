package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Junior;

public class JuniorsComparator implements SVComparator<Junior>, ISort {
	// public static final int ID = 0;

	public static final int NAME = 0;

	public static final int SURNAME = 1;
	
	public static final int FORMATION = 2;

	public static final int SKILL = 3;

	public static final int WEEKS = 4;

	public static final int WITHOUT_JUMP = 5;

	public static final int AVERAGE_JUMP = 6;

	public static final int BEGIN_LEVEL = 7;

	public static final int ESTIMATED_LEVEL = 8;

	public static final int JUMPS = 9;
	
	public static final int AGE = 10;

	public static final int ESTIMATED_AGE = 11;
	
	public static final int EXIT_WEEK = 12;

	public static final int EXIT_DATE = 13;

	public static final int MONEY_SPENT = 14;

	public static final int MONEY_LEFT = 15;

	public static final int MONEY_ALL = 16;

	public static final int NOTE = 17;

	private int column;

	private int direction;

	/**
	 * Compares two Player objects
	 * 
	 * @param obj1
	 *          the first Player
	 * @param obj2
	 *          the second Player
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
		// case ID:
		// rc = (c1.getId() < c2.getId()) ? -1 : 1;
		// break;
		case NAME:
			rc = coll.compare(j1.getName(), j2.getName());
			// rc = j1.getName().compareTo(j2.getName());
			break;
		case SURNAME:
			rc = coll.compare(j1.getSurname(), j2.getSurname());
			// rc = j1.getSurname().compareTo(j2.getSurname());
			break;
		case FORMATION:
			rc = j1.getFormation() < j2.getFormation() ? -1 : 1;
			break;
		case SKILL:
			rc = (j1.getSkills()[j1.getSkills().length - 1].getSkill() < j2.getSkills()[j2.getSkills().length - 1].getSkill()) ? -1 : 1;
			break;
		case WEEKS:
			rc = (j1.getSkills()[j1.getSkills().length - 1].getWeeks() < j2.getSkills()[j2.getSkills().length - 1].getWeeks()) ? -1 : 1;
			break;
		case WITHOUT_JUMP:
			rc = (j1.getWeeksWithoutJump() < j2.getWeeksWithoutJump()) ? -1 : 1;
			break;
		case BEGIN_LEVEL:
			rc = (j1.getSkills()[0].getSkill() < j2.getSkills()[0].getSkill()) ? -1 : 1;
			break;
		case ESTIMATED_LEVEL:
			if (j1.getPops() < 2 && j2.getPops() < 2) {
				rc = (j1.getEstimatedSkill() < j2.getEstimatedSkill()) ? -1 : 1;
			} else if (j1.getPops() < 2 && j2.getPops() >= 2) {
				rc = 1;
			} else if (j1.getPops() >= 2 && j2.getPops() < 2) {
				rc = -1;
			} else {
				rc = (j1.getEstimatedSkill() < j2.getEstimatedSkill()) ? -1 : 1;
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
		case JUMPS:
			rc = (j1.getPops() < j2.getPops()) ? -1 : 1;
			break;
		case EXIT_WEEK:
			rc = (j1.getEndDate().getSeason().getSeasonWeek() < j2.getEndDate().getSeason().getSeasonWeek()) ? -1 : 1;
			break;
		case EXIT_DATE:
			rc = j1.getEndDate().compareTo(j2.getEndDate());
			break;
		case MONEY_SPENT:
			rc = (j1.getSkills()[0].getWeeks() - j1.getSkills()[j1.getSkills().length - 1].getWeeks() < j2.getSkills()[0].getWeeks() - j2.getSkills()[j2.getSkills().length - 1].getWeeks()) ? -1 : 1;
			break;
		case MONEY_LEFT:
			rc = (j1.getSkills()[j1.getSkills().length - 1].getWeeks() < j2.getSkills()[j2.getSkills().length - 1].getWeeks()) ? -1 : 1;
			break;
		case MONEY_ALL:
			rc = (j1.getSkills()[0].getWeeks() < j2.getSkills()[0].getWeeks()) ? -1 : 1;
			break;
		case NOTE:
			rc = j1.getNote().compareTo(j2.getNote());
			break;
		case ESTIMATED_AGE:
			rc = (j1.getEstimatedAge() < j2.getEstimatedAge()) ? -1 : 1;
			break;
		case AGE:
			rc = (j1.getSkills()[j1.getSkills().length - 1].getAge() < j2.getSkills()[j2.getSkills().length - 1].getAge()) ? -1 : 1;
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
	 *          the column
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
	 *          the direction
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