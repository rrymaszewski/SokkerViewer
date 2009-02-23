package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Collections;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerStats;

public class SpyPlayersComparator implements SVComparator<Player>, ISort {
	public static final int COUNTRY = 0;

	public static final int NAME = 1;

	public static final int SURNAME = 2;

	public static final int VALUE = 3;

	public static final int SALARY = 4;

	public static final int AGE = 5;
	
	public static final int FORM = 6;

	public static final int DISCIPLINE = 7;

	public static final int EXPERIENCE = 8;

	public static final int TEAMWORK = 9;

	public static final int MATCHES = 10;

	public static final int GOALS = 11;

	public static final int ASSISTS = 12;

	public static final int RANKING_AVG = 13;
	
	public static final int RANKING_MAX = 14;
	
	public static final int RANKING_MIN = 15;
	
	public static final int PREFERRED_POSITION = 16;

	public static final int CARDS = 17;

	public static final int INJURY = 18;

	public static final int NOTE = 19;

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
			// rc = p1.getName().compareTo(p2.getName());
			break;
		case SURNAME:
			rc = coll.compare(p1.getSurname(), p2.getSurname());
			// rc = p1.getSurname().compareTo(p2.getSurname());
			break;
		case COUNTRY:
			rc = (p1.getCountryfrom() < p2.getCountryfrom()) ? -1 : 1;
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
		case DISCIPLINE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getDiscipline() < p2.getSkills()[p2.getSkills().length - 1].getDiscipline()) ? -1 : 1;
			break;
		case EXPERIENCE:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getExperience() < p2.getSkills()[p2.getSkills().length - 1].getExperience()) ? -1 : 1;
			break;
		case TEAMWORK:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getTeamwork() < p2.getSkills()[p2.getSkills().length - 1].getTeamwork()) ? -1 : 1;
			break;
		case MATCHES:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getMatches() < p2.getSkills()[p2.getSkills().length - 1].getMatches()) ? -1 : 1;
			break;
		case GOALS:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getGoals() < p2.getSkills()[p2.getSkills().length - 1].getGoals()) ? -1 : 1;
			break;
		case ASSISTS:
			rc = (p1.getSkills()[p1.getSkills().length - 1].getAssists() < p2.getSkills()[p2.getSkills().length - 1].getAssists()) ? -1 : 1;
			break;
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
		case RANKING_AVG:
			rc = (p1.getAvgRating() < p2.getAvgRating()) ? -1 : 1;
			break;
		case PREFERRED_POSITION:
			rc = (p1.getPreferredPosition() < p2.getPreferredPosition()) ? -1 : 1;
			break;
		case RANKING_MAX:
			if (p1.getPlayerMatchStatistics().size() == 0 && p2.getPlayerMatchStatistics().size() == 0) {
				rc = 0;
			} else if (p1.getPlayerMatchStatistics().size() > 0 && p2.getPlayerMatchStatistics().size() == 0) {
				rc = 1;
			} else if (p1.getPlayerMatchStatistics().size() == 0 && p2.getPlayerMatchStatistics().size() > 0) {
				rc = -1;
			} else {
				PlayerStats p1max = Collections.max(p1.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				PlayerStats p2max = Collections.max(p2.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				rc = (p1max.getRating() < p2max.getRating()) ? -1 : 1;
			}
			break;
		case RANKING_MIN:
			if (p1.getPlayerMatchStatistics().size() == 0 && p2.getPlayerMatchStatistics().size() == 0) {
				rc = 0;
			} else if (p1.getPlayerMatchStatistics().size() > 0 && p2.getPlayerMatchStatistics().size() == 0) {
				rc = 1;
			} else if (p1.getPlayerMatchStatistics().size() == 0 && p2.getPlayerMatchStatistics().size() > 0) {
				rc = -1;
			} else {
				PlayerStats p1min = Collections.min(p1.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				PlayerStats p2min = Collections.min(p2.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				rc = (p1min.getRating() < p2min.getRating()) ? -1 : 1;
			}
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