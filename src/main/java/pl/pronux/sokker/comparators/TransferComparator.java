package pl.pronux.sokker.comparators;

import java.text.Collator;
import java.util.Locale;

import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Transfer;

public class TransferComparator implements SVComparator<Transfer>, ISort {

	public static final int IN_OUT = 0;

	public static final int DATE = 1;

	public static final int PLAYER_NAME = 2;

	public static final int PLAYER_SURNAME = 3;

	public static final int SELLER_NAME = 4;

	public static final int BUYER_NAME = 5;

	public static final int PRICE = 6;

	public static final int VALUE = 7;

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
	public int compare(Transfer t1, Transfer t2) {
		int rc = 0;
		Locale loc = Locale.getDefault();
		Collator coll = Collator.getInstance(loc);

		// Determine which field to sort on, then sort
		// on that field
		switch (column) {
			case IN_OUT:
				rc = (t1.getIsInOut() < t2.getIsInOut()) ? -1 : 1;
				break;
			case DATE:
				rc = t1.getDate().compareTo(t2.getDate());
				break;
			case SELLER_NAME:
				rc = coll.compare(t1.getSellerTeamName(), t2.getSellerTeamName());
//				rc = t1.getSellerTeamName().compareToIgnoreCase(t2.getSellerTeamName());
				break;
			case BUYER_NAME:
				rc = coll.compare(t1.getBuyerTeamName(), t2.getBuyerTeamName());
//				rc = t1.getBuyerTeamName().compareTo(t2.getBuyerTeamName());
				break;
			case PRICE:
				rc = t1.getPrice().compareTo(t2.getPrice());
				break;
			case VALUE:
				rc = t1.getPlayerValue().compareTo( t2.getPlayerValue() );
				break;
			case PLAYER_NAME:
				if(t1.getPlayer() != null && t2.getPlayer() != null) {
					rc = coll.compare(t1.getPlayer().getName(), t2.getPlayer().getName());
//					rc = t1.getPlayer().getName().compareTo(t2.getPlayer().getName());
				} else if(t1.getPlayer() == null && t2.getPlayer() != null) {
					rc = "".compareTo(t2.getPlayer().getName()); //$NON-NLS-1$
				} else if(t1.getPlayer() != null && t2.getPlayer() == null) {
					rc = t1.getPlayer().getName().compareTo(""); //$NON-NLS-1$
				} else if(t1.getPlayer() == null && t2.getPlayer() == null) {
					rc = "".compareTo(""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				break;
			case PLAYER_SURNAME:
				if(t1.getPlayer() != null && t2.getPlayer() != null) {
					rc = coll.compare(t1.getPlayer().getSurname(), t2.getPlayer().getSurname());
//					rc = t1.getPlayer().getSurname().compareTo(t2.getPlayer().getSurname());
				} else if(t1.getPlayer() == null && t2.getPlayer() != null) {
					rc = "".compareTo(t2.getPlayer().getSurname()); //$NON-NLS-1$
				} else if(t1.getPlayer() != null && t2.getPlayer() == null) {
					rc = t1.getPlayer().getSurname().compareTo(""); //$NON-NLS-1$
				} else if(t1.getPlayer() == null && t2.getPlayer() == null) {
					rc = "".compareTo(""); //$NON-NLS-1$ //$NON-NLS-2$
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