package pl.pronux.sokker.model;

import java.util.Calendar;

import pl.pronux.sokker.interfaces.DateConst;

public class Season {
	public static final long FIRST_SEASON_DATE = 1105570800000l;

	public static final int FIRST_SEASON = 5;
	
	public static final int FIRST_SEASON_WEEK = 0;
	
	private long millis;
	
	private int seasonNumber;

	private int seasonWeek;

	public long getMillis() {
		return millis;
	}

	public Season(long date) {
		this.millis = date;
		setSeason(date);
	}
	
	public void setSeason(long date) {
		this.millis = date;
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, 15);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2005);
		cal.set(Calendar.HOUR, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(date);
		cal2.set(Calendar.HOUR, 1);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		
		long diff = (cal2.getTimeInMillis() - cal.getTimeInMillis()) / DateConst.WEEK;

		this.seasonNumber = Long.valueOf(diff / 16 + FIRST_SEASON).intValue();
		this.seasonWeek = Long.valueOf(diff - (16 * (diff / 16)) + FIRST_SEASON_WEEK).intValue();
	}

	public int getSeasonNumber() {
		return seasonNumber;
	}

//	public void setSeasonNumber(int seasonNumber) {
//		this.seasonNumber = seasonNumber;
//	}

	public int getSeasonWeek() {
		return seasonWeek;
	}

//	public void setSeasonWeek(int seasonWeek) {
//		this.seasonWeek = seasonWeek;
//	}


//	public void setDate(Date date) {
//		this.date = date;
//	}
	
}
