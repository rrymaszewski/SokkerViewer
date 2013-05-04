package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.Sort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.PlayerStats;

public class PlayerStatsComparator implements SVComparator<PlayerStats>, Sort {
	private int column;

	private int direction;

	public static final int EMPTY = 0;

	public static final int DATE = 1;

	public static final int TEAM_HOME = 2;

	public static final int TEAM_AWAY = 3;

	public static final int FORMATION = 4;

	public static final int TIME = 5;

	public static final int RATING = 6;

	public static final int STARS = 7;
	
	public static final int GOALS = 8;

	public static final int SHOOTS = 9;
	
	public static final int ASSISTS = 10;

	public static final int FOULS = 11;

	public static final int INJURY = 12;
	
	public static final int CARDS = 13;

	public PlayerStatsComparator() {
	}

	public PlayerStatsComparator(int column, int direction) {
		this.column = column;
		this.direction = direction;
	}


	public int compare(PlayerStats ps1, PlayerStats ps2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		switch (column) {
		case EMPTY:
			break;
		case DATE:
			rc = ps1.getMatch().getDateStarted().compareTo(ps2.getMatch().getDateStarted());
			break;
		case TEAM_HOME:
			rc = coll.compare(ps1.getMatch().getHomeTeamName(), ps2.getMatch().getHomeTeamName());
			break;
		case TEAM_AWAY:
			rc = coll.compare(ps1.getMatch().getAwayTeamName(), ps2.getMatch().getAwayTeamName());
			break;
		case FORMATION:
			rc = (ps1.getFormation() < ps2.getFormation()) ? -1 : 1;
			break;
		case RATING:
			rc = (ps1.getRating() < ps2.getRating()) ? -1 : 1;
			break;
		case STARS:
			rc = (ps1.getRating() < ps2.getRating()) ? -1 : 1;
			break;
		case GOALS:
			rc = (ps1.getGoals() < ps2.getGoals()) ? -1 : 1;
			break;
		case SHOOTS:
			rc = (ps1.getShoots() < ps2.getShoots()) ? -1 : 1;
			break;
		case ASSISTS:
			rc = (ps1.getAssists() < ps2.getAssists()) ? -1 : 1;
			break;
		case FOULS:
			rc = (ps1.getFouls() < ps2.getFouls()) ? -1 : 1;
			break;
		case INJURY:
			rc = (ps1.getIsInjured() < ps2.getIsInjured()) ? -1 : 1;
			break;
		case TIME:
			rc = (ps1.getTimePlayed() < ps2.getTimePlayed()) ? -1 : 1;
			break;
		case CARDS:
			if(ps1.getRedCards() < ps2.getRedCards()) {
				rc = -1;
			} else if(ps1.getRedCards() > ps2.getRedCards()) {
				rc = 1;
			} else {
				if(ps1.getYellowCards() < ps2.getYellowCards()) {
					rc = -1;
				} else {
					rc = 1;
				}
			}
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
	 * @see pl.pronux.sokker.ExtendedComparator#setDirection(int)
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

}
