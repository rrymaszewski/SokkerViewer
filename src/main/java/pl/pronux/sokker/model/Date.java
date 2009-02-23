package pl.pronux.sokker.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import pl.pronux.sokker.interfaces.DateConst;

public class Date implements Serializable, DateConst, Comparable<Date> {
	private static DecimalFormat doubleDigitFormat = new DecimalFormat("00"); //$NON-NLS-1$

	private SokkerDate sokkerDate;

	public Season getSeason() {
		Season season = null;
		if (season == null) {
			season = new Season(this.getMillis());
		} else {
			if (season.getMillis() != this.getMillis()) {
				season = new Season(this.getMillis());
			}
		}
		return season;
	}

	private static NumberFormat monthFormat;

	/**
	 *
	 */
	private static final long serialVersionUID = -2630931821155301188L;

	/**
	 * sql date conversion in hsqldb
	 */
	public static long dateTimeToMillis(String date, String time) {
		if (date == null || time == null) {
			return -1;
		} else {
			Calendar cal = Calendar.getInstance();
			String[] dateTable = date.split("-"); //$NON-NLS-1$
			String[] timeTable = time.split(":"); //$NON-NLS-1$
			cal.set(Integer.valueOf(dateTable[0]), Integer.valueOf(dateTable[1]) - 1, Integer.valueOf(dateTable[2]), Integer.valueOf(timeTable[0]), Integer.valueOf(timeTable[1]));
			return cal.getTimeInMillis();
		}
	}

	private Calendar calendar;

	/**
	 *
	 * @param date
	 *          string format yyyy-mm-dd hh:mm:ss
	 * @return Calendar
	 */
	public Calendar parseDate(String date) {
		Calendar cal = Calendar.getInstance();
		if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}")) { //$NON-NLS-1$
			String[] dateTable = date.split("-|_"); //$NON-NLS-1$
			cal.set(Calendar.YEAR, Integer.valueOf(dateTable[0]));
			cal.set(Calendar.MONTH, Integer.valueOf(dateTable[1]) - 1);
			cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateTable[2]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dateTable[3]));
			cal.set(Calendar.MINUTE, Integer.valueOf(dateTable[4]));
		} else {
			String[] dateTable = date.split("-| |:"); //$NON-NLS-1$
			if (dateTable.length > 2) {
				cal.set(Calendar.YEAR, Integer.valueOf(dateTable[0]));
				cal.set(Calendar.MONTH, Integer.valueOf(dateTable[1]) - 1);
				cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateTable[2]));
			}

			if (dateTable.length > 4) {
				cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dateTable[3]));
				cal.set(Calendar.MINUTE, Integer.valueOf(dateTable[4]));
			}

			if (dateTable.length > 5) {
				cal.set(Calendar.SECOND, Integer.valueOf(dateTable[5]));
			}
		}
		return cal;
	}

	public Date(Calendar calendar) {
		this.calendar = calendar;
	}

	public Date(long millis) {
		this.calendar = Calendar.getInstance();
		this.calendar.setTimeInMillis(millis);
	}
	
	public Date(Timestamp timestamp) {
		this.calendar = Calendar.getInstance();
		this.calendar.setTimeInMillis(timestamp.getTime());
	}

	public Date(String date) {
		this.calendar = parseDate(date);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public long getMillis() {
		return calendar.getTimeInMillis();
	}

	public NumberFormat getMonthFormat() {
		return monthFormat;
	}

	public int mondayFirstDayOfWeek() {
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 2:
			return 0;
		case 3:
			return 1;
		case 4:
			return 2;
		case 5:
			return 3;
		case 6:
			return 4;
		case 7:
			return 5;
		case 1:
			return 6;
			default:
				break;
		}
		return -1;
	}

	public int saturdayFirstDayOfWeek() {
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 7:
			return 0;
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 4;
		case 5:
			return 5;
		case 6:
			return 6;
		}
		return -1;
	}

	public static long getDiffrentDateInDays(java.util.Date date1, java.util.Date date2) {
		Calendar tren1 = Calendar.getInstance();
		Calendar tren2 = Calendar.getInstance();

		tren1.setTime(date1);
		tren2.setTime(date2);

		tren2.set(Calendar.HOUR, tren1.get(Calendar.HOUR));
		tren2.set(Calendar.AM_PM, tren1.get(Calendar.AM_PM));
		tren2.set(Calendar.MINUTE, tren1.get(Calendar.MINUTE));
		tren2.set(Calendar.SECOND, tren1.get(Calendar.SECOND));
		tren2.set(Calendar.MILLISECOND, tren1.get(Calendar.MILLISECOND));

		long diffMillis = tren1.getTimeInMillis() - tren2.getTimeInMillis();

		long diffDays = diffMillis / (24 * 60 * 60 * 1000); // 7

		return diffDays;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public void setMonthFormat(NumberFormat monthFormat) {
		Date.monthFormat = monthFormat;
	}

	public int thursdayFirstDayOfWeek() {
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 5:
			return 0;
		case 6:
			return 1;
		case 7:
			return 2;
		case 1:
			return 3;
		case 2:
			return 4;
		case 3:
			return 5;
		case 4:
			return 6;
		}
		return -1;
	}

	public String toDateString() {
		if (sokkerDate == null) {
			return String.format("%d-%s-%s", calendar.get(Calendar.YEAR), doubleDigitFormat.format((calendar.get(Calendar.MONTH) + 1)), doubleDigitFormat.format(calendar.get(Calendar.DAY_OF_MONTH))); //$NON-NLS-1$
		} else {
			return String.format("%d-%s-%s (%d)", calendar.get(Calendar.YEAR), doubleDigitFormat.format((calendar.get(Calendar.MONTH) + 1)), doubleDigitFormat.format(calendar.get(Calendar.DAY_OF_MONTH)), sokkerDate.getWeek()); //$NON-NLS-1$
		}

	}

	public String toDateTimeString() {
		return String.format("%s (%s)",toDateString(),toTimeString()); //$NON-NLS-1$
	}

	public String toTimeString() {
		return String.format("%s:%s", doubleDigitFormat.format(calendar.get(Calendar.HOUR_OF_DAY)), doubleDigitFormat.format(calendar.get(Calendar.MINUTE))); //$NON-NLS-1$
	}

	public Date getTrainingDate(int firstDay) {
		Date trainingDate = null;
		if (sokkerDate != null) {
			long begin;
			long offset;
			int week = 0;
			// if(sokkerDate.getDay() >= SokkerDate.THURSDAY && sokkerDate.getDay() <=
			// SokkerDate.FRIDAY) {
			// if(sokkerDate.getDay() >= firstDay && sokkerDate.getDay() <=
			// SokkerDate.FRIDAY) {
			if (sokkerDate.getDay() < firstDay) {
				week = sokkerDate.getWeek() - 1;
			} else {
				week = sokkerDate.getWeek();
			}
			begin = SokkerDate.BEGIN_DATE;
			Calendar ca1 = Calendar.getInstance();
			Calendar ca2 = Calendar.getInstance();
			ca1.setTimeInMillis(begin);
			ca2.setTimeInMillis(begin + Date.WEEK * week + firstDay * Date.DAY);
			offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);
			ca2.setTimeInMillis(ca2.getTimeInMillis() - offset);
			trainingDate = new Date(ca2);
			trainingDate.setSokkerDate(new SokkerDate(firstDay, week));

		}
		return trainingDate;
	}

	public static int getSokkerDay(long millis) {
		return 0;
	}

	public static int getSokkerWeek(long millis) {
		return 0;
	}

	public SokkerDate getSokkerDate() {
		if (sokkerDate == null) {
			sokkerDate = new SokkerDate(this.getMillis());
		}
		return sokkerDate;
	}

	public void setSokkerDate(SokkerDate sokkerDate) {
		this.sokkerDate = sokkerDate;
	}

	public int compareTo(Date o) {
		if (this.getMillis() < o.getMillis()) {
			return -1;
		} else if (this.getMillis() > o.getMillis()) {
			return 1;
		}
		return 0;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(this.calendar.getTimeInMillis());
	}
}
