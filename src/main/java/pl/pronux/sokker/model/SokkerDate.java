package pl.pronux.sokker.model;

import java.util.Calendar;

import pl.pronux.sokker.interfaces.DateConst;

public class SokkerDate {

	public final static byte SATURDAY = 0;
	public final static byte SUNDAY = 1;
	public final static byte MONDAY = 2;
	public final static byte TUESDAY = 3;
	public final static byte WEDNESDAY = 4;
	public final static byte THURSDAY = 5;
	public final static byte FRIDAY = 6;


	public SokkerDate() {
	}

	public SokkerDate(long millis) {
		this.day = convertUpdateMillisToDay(millis);
		this.week = convertUpdateMillisToWeek(millis);
	}

	public SokkerDate(int day, int week) {
		this.day = day;
		this.week = week;
	}

//	public final static long beginDate = 1057470289859l - Date.day;
//	public final static long BEGIN_DATE = 1057379449859l - DateConst.HOUR * 6 - DateConst.MINUTE * 30 - DateConst.SECOND * 49 - 859;
	public final static long BEGIN_DATE = 1057356000000l + DateConst.SECOND;

	public static int convertMillisToDay(long millis) {
		int offset = 0 ;
		long timezone1 = 0;
		long begin;
//		begin = SokkerDate.BEGIN_DATE  - (6 * Date.HOUR) - (31 * Date.MINUTE);
		begin = SokkerDate.BEGIN_DATE;
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		timezone1 = ca1.get(Calendar.ZONE_OFFSET) - DateConst.HOUR;
//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));

		ca1.setTimeInMillis(begin + timezone1);
		ca2.setTimeInMillis(millis + timezone1);

//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);

		return Long.valueOf(((ca2.getTimeInMillis() - begin + offset ) / DateConst.DAY % 7)).intValue();
	}

	public static int convertUpdateMillisToDay(long millis) {
//		int offset;
//		long begin;
//		begin = SokkerDate.beginDate;
//		Calendar ca1 = Calendar.getInstance();
//		Calendar ca2 = Calendar.getInstance();
//		ca1.setTimeInMillis(begin);
//		ca2.setTimeInMillis(millis );
//		ca1.setTimeZone(Calendar.getInstance().getTimeZone());
//		ca2.setTimeZone(Calendar.getInstance().getTimeZone());
//		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);
		int offset = 0 ;
		long timezone1 = 0;
		long begin;
		begin = SokkerDate.BEGIN_DATE;
//		begin = SokkerDate.BEGIN_DATE  - (6 * Date.HOUR) - (31 * Date.MINUTE);
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		timezone1 = ca1.get(Calendar.ZONE_OFFSET) - DateConst.HOUR;
//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));

		ca1.setTimeInMillis(begin + timezone1);
		ca2.setTimeInMillis(millis + timezone1);

//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);

		return Long.valueOf(((ca2.getTimeInMillis() - begin + offset  ) / DateConst.DAY % 7)).intValue();
	}

	public static int convertMillisToWeek(long millis) {
		int offset = 0 ;
		long timezone1 = 0;
		long begin;
//		begin = SokkerDate.BEGIN_DATE  - (6 * Date.HOUR) - (31 * Date.MINUTE);
		begin = SokkerDate.BEGIN_DATE;
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		timezone1 = ca1.get(Calendar.ZONE_OFFSET) - DateConst.HOUR;
//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));

		ca1.setTimeInMillis(begin + timezone1);
		ca2.setTimeInMillis(millis + timezone1);

//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);

		return Long.valueOf(((ca2.getTimeInMillis() - begin + offset ) / DateConst.WEEK)).intValue();

	}

	public static int convertUpdateMillisToWeek(long millis) {
//		int offset;
//		long begin;
//		begin = SokkerDate.beginDate ;
//		Calendar ca1 = Calendar.getInstance();
//		Calendar ca2 = Calendar.getInstance();
//		ca1.setTimeInMillis(begin);
//		ca2.setTimeInMillis(millis);
//		ca1.setTimeZone(Calendar.getInstance().getTimeZone());
//		ca2.setTimeZone(Calendar.getInstance().getTimeZone());
//		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);
//		return Long.valueOf((ca2.getTimeInMillis() - begin   + offset) / DateConst.week).intValue();
		int offset = 0 ;
		long timezone1 = 0;
		long begin;
		begin = SokkerDate.BEGIN_DATE;
		Calendar ca1 = Calendar.getInstance();
		Calendar ca2 = Calendar.getInstance();
		timezone1 = ca1.get(Calendar.ZONE_OFFSET) - DateConst.HOUR;
//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));

		ca1.setTimeInMillis(begin + timezone1);
		ca2.setTimeInMillis(millis + timezone1);

//		ca1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
//		ca2.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
		offset = ca2.get(Calendar.DST_OFFSET) - ca1.get(Calendar.DST_OFFSET);
		return Long.valueOf((ca2.getTimeInMillis() - begin   + offset) / DateConst.WEEK).intValue();
	}
	private int day;

	private int week;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}
	
	public int getSeasonWeek() {
		return this.week % 16;
	}
	
	public int getSeason() {
		return this.week / 16;
	}
	
	public int getTrainingWeek() {
		if(this.day >= SokkerDate.THURSDAY) {
			return this.week;
		} else {
			return this.week-1;
		}
	}
}
