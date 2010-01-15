package pl.pronux.sokker.ui.plugins;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Note;
import pl.pronux.sokker.model.Season;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.items.DayExpandItem;

public class ViewCalendar implements IPlugin {

	public static final int EVERY_DAY = 10;

	private Composite composite;

	private TreeItem treeItem;

	private DateTime calendar;

	private Group group;

	private DayExpandItem textDaySaturday;

	private DayExpandItem textDaySunday;

	private DayExpandItem textDayMonday;

	private DayExpandItem textDayTuesday;

	private DayExpandItem textDayWednesday;

	private DayExpandItem textDayThursday;

	private DayExpandItem textDayFriday;

	private DayExpandItem textDayEvery;

	private DecimalFormat doubleDigitFormat;

	private List<Integer> days = Arrays.asList(new Integer[] { EVERY_DAY, Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
															  Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY });

	public void clear() {

	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoCalendar");
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		doubleDigitFormat = new DecimalFormat("00");
		initComposite(composite);
		composite.setLayout(new FormLayout());
		composite.layout();
	}

	private void initComposite(Composite composite) {

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(100, 0);

		Composite compositeView = new Composite(composite, SWT.BORDER);
		compositeView.setLayoutData(formData);
		compositeView.setLayout(new FormLayout());

		compositeView.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		formData = new FormData();
		formData.width = 250;
		formData.left = new FormAttachment(50, -formData.width / 2);
		formData.top = new FormAttachment(0, 5);

		Composite groupCalendar = new Composite(compositeView, SWT.NONE);
		groupCalendar.setLayoutData(formData);
		groupCalendar.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, -5);

		calendar = new DateTime(groupCalendar, SWT.CALENDAR);
		calendar.setLayoutData(formData);
		calendar.setFont(ConfigBean.getFontMain());
		calendar.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		calendar.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				Calendar cal = Calendar.getInstance();
				cal.set(calendar.getYear(), calendar.getMonth(), calendar.getDay());
				getDayEvents(cal);
			}

		});

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(groupCalendar, 5);
		formData.bottom = new FormAttachment(100, -10);

		group = new Group(compositeView, SWT.NONE);
		group.setLayout(new FormLayout());
		group.setLayoutData(formData);
		group.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(50, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, 0);

		ExpandBar bar1 = new ExpandBar(group, SWT.V_SCROLL);
		bar1.setLayoutData(formData);
		bar1.setSpacing(5);

		textDayEvery = new DayExpandItem(bar1, SWT.NONE, 0);
		textDaySaturday = new DayExpandItem(bar1, SWT.NONE, 1);
		textDaySunday = new DayExpandItem(bar1, SWT.NONE, 2);
		textDayMonday = new DayExpandItem(bar1, SWT.NONE, 3);

		formData = new FormData();
		formData.left = new FormAttachment(50, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, 0);

		bar1 = new ExpandBar(group, SWT.V_SCROLL);
		bar1.setLayoutData(formData);
		bar1.setSpacing(5);

		textDayTuesday = new DayExpandItem(bar1, SWT.NONE, 0);
		textDayWednesday = new DayExpandItem(bar1, SWT.NONE, 1);
		textDayThursday = new DayExpandItem(bar1, SWT.NONE, 2);
		textDayFriday = new DayExpandItem(bar1, SWT.NONE, 3);

		getDayEvents(Calendar.getInstance());
	}

	private String formatDate(Calendar cal) {
		return cal.get(Calendar.YEAR) + "-" + doubleDigitFormat.format((cal.get(Calendar.MONTH) + 1)) + "-"
			   + doubleDigitFormat.format(cal.get(Calendar.DAY_OF_MONTH));
	}

	private void getDayEvents(Calendar calendar) {

		Season season = new Season(calendar.getTimeInMillis());
		group.setText(Messages.getString("training.season") + " " + season.getSeasonNumber() + " " + Messages.getString("training.week") + " "
					  + season.getSeasonWeek());

		for (int day : days) {
			getDayExpandItem(day).setText(getDescription(calendar, day));
			getDayExpandItem(day).fill(getEvents(day, season));
		}

		if (Cache.getNotes() != null) {

			for (Note note : Cache.getNotes()) {
				if (note.getAlertDate() != null) {
					if (note.getAlertDate() != null) {
						Calendar cal = Calendar.getInstance();
						Calendar cal2 = Calendar.getInstance();
						Calendar cal3 = Calendar.getInstance();
						cal2.setTimeInMillis(note.getAlertDate().getMillis());

						cal = getCalendarDay(calendar, Calendar.SATURDAY);
						cal.set(Calendar.HOUR, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);

						cal3 = getCalendarDay(calendar, Calendar.FRIDAY);
						cal3.set(Calendar.HOUR, 23);
						cal3.set(Calendar.MINUTE, 59);
						cal3.set(Calendar.SECOND, 59);
						cal3.set(Calendar.MILLISECOND, 999);

						if ((cal2.after(cal) && cal2.before(cal3)) || (cal2.compareTo(cal3) == 0) || (cal2.compareTo(cal) == 0)) {
							getDayExpandItem(cal2.get(Calendar.DAY_OF_WEEK)).append(note.getAlertDate().toDateString() + " " + note.getTitle());
						}
					}
				}
			}
		}

		getDayExpandItem(calendar.get(Calendar.DAY_OF_WEEK)).mark();
		for (int day : days) {
			getDayExpandItem(day).pack();
		}
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewCalendar"));
		this.treeItem.setImage(ImageResources.getImageResources("calendar.png"));
	}

	public void set() {
		getDayEvents(Calendar.getInstance());
	}

	public void dispose() {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void reload() {
	}

	private List<String> getEvents(int day, Season season) {
		List<String> events = new ArrayList<String>();

		switch (day) {
		case Calendar.SATURDAY:
			events.add(Messages.getString("calendar.day.event.29"));
			events.add(Messages.getString("calendar.day.event.7"));
			events.add(Messages.getString("calendar.day.event.28"));
			events.add(Messages.getString("calendar.day.event.8"));
			events.add(Messages.getString("calendar.day.event.9"));
			break;
		case Calendar.SUNDAY:
			if (season.getSeasonWeek() == 0) {
				events.add(Messages.getString("calendar.day.event.20"));
			} else if (season.getSeasonWeek() == 15) {
				events.add(Messages.getString("calendar.day.event.21"));
			} else {
				events.add(Messages.getString("calendar.day.event.23"));
			}
			events.add(Messages.getString("calendar.day.event.5"));
			events.add(Messages.getString("calendar.day.event.25"));
			break;
		case Calendar.MONDAY:
			if (season.getSeasonWeek() == 0) {
				events.add(Messages.getString("calendar.day.event.24"));
			}
			events.add(Messages.getString("calendar.day.event.10"));
			events.add(Messages.getString("calendar.day.event.11"));
			events.add(Messages.getString("calendar.day.event.12"));
			events.add(Messages.getString("calendar.day.event.13"));
			break;
		case Calendar.TUESDAY:
			events.add(Messages.getString("calendar.day.event.26"));
			if (season.getSeasonWeek() > 0 && season.getSeasonWeek() < 15) {
				events.add(Messages.getString("calendar.day.event.30"));
			}
			break;
		case Calendar.WEDNESDAY:
			events.add(Messages.getString("calendar.day.event.14"));
			events.add(Messages.getString("calendar.day.event.22"));
			events.add(Messages.getString("calendar.day.event.31"));
			break;
		case Calendar.THURSDAY:
			events.add(Messages.getString("calendar.day.event.12"));
			events.add(Messages.getString("calendar.day.event.15"));
			events.add(Messages.getString("calendar.day.event.16"));
			events.add(Messages.getString("calendar.day.event.17"));
			events.add(Messages.getString("calendar.day.event.18"));
			events.add(Messages.getString("calendar.day.event.32"));
			break;
		case Calendar.FRIDAY:
			if (season.getSeasonWeek() == 15) {
				events.add(Messages.getString("calendar.day.event.19"));
			}
			events.add(Messages.getString("calendar.day.event.33"));
			events.add(Messages.getString("calendar.day.event.34"));
			break;
		case EVERY_DAY:
			events.add(Messages.getString("calendar.day.event.1"));
			events.add(Messages.getString("calendar.day.event.2"));
			events.add(Messages.getString("calendar.day.event.3"));
			events.add(Messages.getString("calendar.day.event.4"));
			break;
		}
		return events;
	}

	private Calendar getCalendarDay(Calendar calendar, int day) {
		int diff = 0;
		if (day == Calendar.SATURDAY) {
			day = 0;
		}
		Calendar cal = Calendar.getInstance();
		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = day;
		} else {
			diff = day - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));
		return cal;
	}

	private String getDescription(Calendar calendar, int day) {
		if (day == EVERY_DAY) {
			return Messages.getString("calendar.day.0");
		}
		return Messages.getString("calendar.day." + day) + " " + "(" + formatDate(getCalendarDay(calendar, day)) + ")";
	}

	private DayExpandItem getDayExpandItem(int day) {
		switch (day) {
		case Calendar.SATURDAY:
			return textDaySaturday;
		case Calendar.SUNDAY:
			return textDaySunday;
		case Calendar.MONDAY:
			return textDayMonday;
		case Calendar.TUESDAY:
			return textDayTuesday;
		case Calendar.WEDNESDAY:
			return textDayWednesday;
		case Calendar.THURSDAY:
			return textDayThursday;
		case Calendar.FRIDAY:
			return textDayFriday;
		default:
			return textDayEvery;
		}
	}
}
