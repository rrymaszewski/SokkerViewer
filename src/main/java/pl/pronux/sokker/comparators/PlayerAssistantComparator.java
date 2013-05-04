package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.Sort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;

public class PlayerAssistantComparator implements SVComparator<Player>, Sort {
//	public static final int ID = 0;

	public static final int NAME = 0;

	public static final int SURNAME = 1;
	
//	public static final int FORM = 2;
//	
//	public static final int STAMINA = 3;
//	
//	public static final int PACE = 4;
//	
//	public static final int TECHNIQUE = 5;
//	
//	public static final int PASSING = 6;
//	
//	public static final int KEEPER = 7;
//	
//	public static final int DEFENDER = 8;
//	
//	public static final int PLAYMAKER = 9;
//	
//	public static final int SCORER = 10;
	
	public static final int POSITION1 = 2;
	
	public static final int POSITION2 = 3;
	
	public static final int POSITION3 = 4;
	
	public static final int POSITION4 = 5;
	
	public static final int POSITION5 = 6;
	
	public static final int POSITION6 = 7;
	
	public static final int POSITION7 = 8;
	
	public static final int POSITION8 = 9;
	
	public static final int POSITION9 = 10;
	
	public static final int POSITION10 = 11;
	
	public static final int POSITION11 = 12;
	
	public static final int POSITION = 13;
	
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
	public int compare(Player p1, Player p2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case NAME:
				rc = coll.compare(p1.getName(), p2.getName());
//				rc = p1.getName().compareTo(p2.getName());
				break;
			case SURNAME:
				rc = coll.compare(p1.getSurname(), p2.getSurname());
//				rc = p1.getSurname().compareTo(p2.getSurname());
				break;
//			case FORM:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getForm() < p2.getSkills()[p2.getSkills().length-1].getForm()) ? -1 : 1;
//				break;
//			case STAMINA:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getStamina() < p2.getSkills()[p2.getSkills().length-1].getStamina()) ? -1 : 1;
//				break;
//			case PACE:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getPace() < p2.getSkills()[p2.getSkills().length-1].getPace()) ? -1 : 1;
//				break;
//			case TECHNIQUE:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getTechnique() < p2.getSkills()[p2.getSkills().length-1].getTechnique()) ? -1 : 1;
//				break;
//			case PASSING:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getPassing() < p2.getSkills()[p2.getSkills().length-1].getPassing()) ? -1 : 1;
//				break;
//			case KEEPER:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getKeeper() < p2.getSkills()[p2.getSkills().length-1].getKeeper()) ? -1 : 1;
//				break;
//			case DEFENDER:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getDefender() < p2.getSkills()[p2.getSkills().length-1].getDefender()) ? -1 : 1;
//				break;
//			case PLAYMAKER:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getPlaymaker() < p2.getSkills()[p2.getSkills().length-1].getPlaymaker()) ? -1 : 1;
//				break;
//			case SCORER:
//				rc = (p1.getSkills()[p1.getSkills().length-1].getScorer() < p2.getSkills()[p2.getSkills().length-1].getScorer()) ? -1 : 1;
//				break;
			case POSITION1:
				rc = (p1.getPositionTable()[0] < p2.getPositionTable()[0]) ? -1 : 1;
				break;
			case POSITION2:
				rc = (p1.getPositionTable()[1] < p2.getPositionTable()[1]) ? -1 : 1;
				break;
			case POSITION3:
				rc = (p1.getPositionTable()[2] < p2.getPositionTable()[2]) ? -1 : 1;
				break;
			case POSITION4:
				rc = (p1.getPositionTable()[3] < p2.getPositionTable()[3]) ? -1 : 1;
				break;
			case POSITION5:
				rc = (p1.getPositionTable()[4] < p2.getPositionTable()[4]) ? -1 : 1;
				break;
			case POSITION6:
				rc = (p1.getPositionTable()[5] < p2.getPositionTable()[5]) ? -1 : 1;
				break;
			case POSITION7:
				rc = (p1.getPositionTable()[6] < p2.getPositionTable()[6]) ? -1 : 1;
				break;
			case POSITION8:
				rc = (p1.getPositionTable()[7] < p2.getPositionTable()[7]) ? -1 : 1;
				break;
			case POSITION9:
				rc = (p1.getPositionTable()[8] < p2.getPositionTable()[8]) ? -1 : 1;
				break;
			case POSITION10:
				rc = (p1.getPositionTable()[9] < p2.getPositionTable()[9]) ? -1 : 1;
				break;
			case POSITION11:
				rc = (p1.getPositionTable()[10] < p2.getPositionTable()[10]) ? -1 : 1;
				break;
			case POSITION:
				rc = (p1.getPosition() < p2.getPosition()) ? -1 : 1;
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