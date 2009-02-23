package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;

public class PlayerComparator implements SVComparator<Player>, ISort {
	// public static final int ID = 0;

	public static final int NAME = 1;

	public static final int SURNAME = 2;

	public static final int VALUE = 3;

	public static final int SALARY = 4;

	public static final int AGE = 5;

	public static final int FORM = 6;

	public static final int STAMINA = 7;

	public static final int PACE = 8;

	public static final int TECHNIQUE = 9;

	public static final int PASSING = 10;

	public static final int KEEPER = 11;

	public static final int DEFENDER = 12;

	public static final int PLAYMAKER = 13;

	public static final int SCORER = 14;

	public static final int DISCIPLINE = 15;

	public static final int EXPERIENCE = 16;

	public static final int TEAMWORK = 17;

	public static final int CARDS = 18;

	public static final int INJURY = 19;
	
	public static final int NOTE = 20;

	public static final int MATCH_SUNDAY = 21;

	public static final int MATCH_WEDNESDAY = 22;

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
	public int compare(Player p1, Player p2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
		case NAME:
			rc = coll.compare(p1.getName(), p2.getName());
			// rc = p1.getName().compareTo(p2.getName());
			break;
		case SURNAME:
			rc = coll.compare(p1.getSurname(), p2.getSurname());
			// rc = p1.getSurname().compareTo(p2.getSurname());
			break;
		case VALUE:
			rc = p1.getSkills()[p1.getSkills().length - 1].getValue().compareTo(p2.getSkills()[p2.getSkills().length - 1].getValue());
			break;
		case SALARY:
			rc = p1.getSkills()[p1.getSkills().length - 1].getSalary().compareTo(p2.getSkills()[p2.getSkills().length - 1].getSalary());
			break;
		case AGE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getAge() < p2.getSkills()[p2.getSkills().length - 1].getAge()) ? -1 : 1;
			break;
		case FORM:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getForm() < p2.getSkills()[p2.getSkills().length - 1].getForm()) ? -1 : 1;
			break;
		case STAMINA:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getStamina() < p2.getSkills()[p2.getSkills().length - 1].getStamina()) ? -1 : 1;
			break;
		case PACE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getPace() < p2.getSkills()[p2.getSkills().length - 1].getPace()) ? -1 : 1;
			break;
		case TECHNIQUE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getTechnique() < p2.getSkills()[p2.getSkills().length - 1].getTechnique()) ? -1 : 1;
			break;
		case PASSING:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getPassing() < p2.getSkills()[p2.getSkills().length - 1].getPassing()) ? -1 : 1;
			break;
		case KEEPER:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getKeeper() < p2.getSkills()[p2.getSkills().length - 1].getKeeper()) ? -1 : 1;
			break;
		case DEFENDER:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getDefender() < p2.getSkills()[p2.getSkills().length - 1].getDefender()) ? -1 : 1;
			break;
		case PLAYMAKER:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getPlaymaker() < p2.getSkills()[p2.getSkills().length - 1].getPlaymaker()) ? -1 : 1;
			break;
		case SCORER:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getScorer() < p2.getSkills()[p2.getSkills().length - 1].getScorer()) ? -1 : 1;
			break;
		case DISCIPLINE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getDiscipline() < p2.getSkills()[p2.getSkills().length - 1].getDiscipline()) ? -1 : 1;
			break;
		case EXPERIENCE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getExperience() < p2.getSkills()[p2.getSkills().length - 1].getExperience()) ? -1 : 1;
			break;
		case TEAMWORK:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getTeamwork() < p2.getSkills()[p2.getSkills().length - 1].getTeamwork()) ? -1 : 1;
			break;

		case MATCH_SUNDAY:
			// rc = coll.compare(p1.getSurname(), p2.getSurname());
		case MATCH_WEDNESDAY:
			// rc = coll.compare(p1.getSurname(), p2.getSurname());

		case NOTE:
			if (p1.getNote() == null && p2.getNote() == null) {
				rc = 0;
			} else if (p1.getNote() != null && p2.getNote() == null) {
				rc = 1;
			} else if (p1.getNote() == null && p2.getNote() != null) {
				rc = -1;
			} else {
				rc = (p1.getNote().compareTo(p2.getNote()));
			}
			break;
		case CARDS:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getCards() < p2.getSkills()[p2.getSkills().length - 1].getCards()) ? -1 : 1;
			break;
		case INJURY:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getInjurydays() < p2.getSkills()[p2.getSkills().length - 1].getInjurydays()) ? -1 : 1;
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