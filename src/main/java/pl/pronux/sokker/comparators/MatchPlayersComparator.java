package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.PlayerStats;

public class MatchPlayersComparator implements SVComparator<PlayerStats>, ISort {
	private int column;

	private int direction;

	public static final int SUBSTITUTIONS = 0;

	public static final int NUMBER = 1;

	public static final int PLAYER = 2;

	public static final int FORMATION = 3;

	public static final int TIME = 4;

	public static final int RATING = 5;

	public static final int GOALS = 6;

	public static final int SHOOTS = 7;

	public static final int ASSISTS = 8;
	
	public static final int FOULS = 9;

	public static final int INJURY = 10;

	public static final int CARDS = 11;
	
	public MatchPlayersComparator() {
	}

	public MatchPlayersComparator(int column, int direction) {
		this.column = column;
		this.direction = direction;
	}

	public int compare(PlayerStats ps1, PlayerStats ps2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		switch (column) {
		case SUBSTITUTIONS:
			break;
		case NUMBER:
			if(ps1.getNumber() < ps2.getNumber()) {
				rc = -1;
			} else if(ps1.getNumber() > ps2.getNumber()) {
				rc = 1;
			} else {
				if(ps1.getTimeIn() < ps2.getTimeIn()) {
					rc = -1;
				} else {
					rc = 1;
				}
			}
			break;
		case PLAYER:
			if(ps1.getPlayer() == null && ps2.getPlayer() == null) {
				rc = (ps1.getPlayerID() < ps2.getPlayerID()) ? -1 : 1;
			} else if(ps1.getPlayer() != null && ps2.getPlayer() == null) {
				rc = 1;
			} else if(ps1.getPlayer() == null && ps2.getPlayer() != null) {
				rc = -1;
			} else if(ps1.getPlayer() != null && ps2.getPlayer() != null) {
				if(ps1.getPlayer().getSurname() == null && ps2.getPlayer().getSurname() == null) {
					rc = (ps1.getPlayerID() < ps2.getPlayerID()) ? -1 : 1;
				} else if(ps1.getPlayer().getSurname() == null) {
					rc = 1;
				} else if(ps2.getPlayer().getSurname() == null) {
					rc = -1;
				} else {
					rc = coll.compare(ps1.getPlayer().getSurname(), ps2.getPlayer().getSurname());	
				}
			}
			break;
		case FORMATION:
			rc = (ps1.getFormation() < ps2.getFormation()) ? -1 : 1;
			break;
		case RATING:
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
