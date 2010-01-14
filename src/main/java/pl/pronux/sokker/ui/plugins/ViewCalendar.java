package pl.pronux.sokker.ui.plugins;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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

public class ViewCalendar implements IPlugin {

	private Composite composite;

	private TreeItem treeItem;

	private Table text1;

	private DateTime calendar;

	private Table text2;

	private Table text3;

	private Table text4;

	private Table text5;

	private Table text6;

	private Table text7;

	private Table text8;

	private Group group;

	private ExpandItem textDaySaturday;

	private ExpandItem textDaySunday;

	private ExpandItem textDayMonday;

	private ExpandItem textDayTuesday;

	private ExpandItem textDayWednesday;

	private ExpandItem textDayThursday;

	private ExpandItem textDayFriday;

	private ExpandItem textDayEvery;

	private DecimalFormat doubleDigitFormat;

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

		Composite composite2;

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

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text1 = new Table(composite2, SWT.FULL_SELECTION);
		text1.setBackground(composite2.getBackground());
		text1.setLayoutData(formData);
		text1.setLinesVisible(false);
		text1.setHeaderVisible(false);
		text1.setFont(ConfigBean.getFontTable());

		new TableColumn(text1, SWT.LEFT);

		textDayEvery = new ExpandItem(bar1, SWT.NONE, 0);
		textDayEvery.setControl(composite2);
		textDayEvery.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayEvery.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text2 = new Table(composite2, SWT.LEFT);
		text2.setBackground(composite2.getBackground());
		text2.setLayoutData(formData);

		text2.setLinesVisible(false);
		text2.setHeaderVisible(false);
		text2.setFont(ConfigBean.getFontTable());

		new TableColumn(text2, SWT.LEFT);

		textDaySaturday = new ExpandItem(bar1, SWT.NONE, 1);
		textDaySaturday.setControl(composite2);
		textDaySaturday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDaySaturday.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text3 = new Table(composite2, SWT.READ_ONLY);
		text3.setBackground(composite2.getBackground());
		text3.setLayoutData(formData);
		text3.setFont(ConfigBean.getFontTable());

		text3.setLinesVisible(false);
		text3.setHeaderVisible(false);

		new TableColumn(text3, SWT.LEFT);

		textDaySunday = new ExpandItem(bar1, SWT.NONE, 2);
		textDaySunday.setControl(composite2);
		textDaySunday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDaySunday.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text4 = new Table(composite2, SWT.READ_ONLY);
		text4.setBackground(composite2.getBackground());
		text4.setLayoutData(formData);

		text4.setLinesVisible(false);
		text4.setHeaderVisible(false);
		text4.setFont(ConfigBean.getFontTable());

		new TableColumn(text4, SWT.LEFT);

		textDayMonday = new ExpandItem(bar1, SWT.NONE, 3);
		textDayMonday.setControl(composite2);
		textDayMonday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayMonday.setExpanded(true);

		formData = new FormData();
		formData.left = new FormAttachment(50, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, 0);

		bar1 = new ExpandBar(group, SWT.V_SCROLL);
		bar1.setLayoutData(formData);
		bar1.setSpacing(5);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text5 = new Table(composite2, SWT.READ_ONLY);
		text5.setBackground(composite2.getBackground());
		text5.setLayoutData(formData);

		text5.setLinesVisible(false);
		text5.setHeaderVisible(false);
		text5.setFont(ConfigBean.getFontTable());

		new TableColumn(text5, SWT.LEFT);

		textDayTuesday = new ExpandItem(bar1, SWT.NONE, 0);
		textDayTuesday.setControl(composite2);
		textDayTuesday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayTuesday.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text6 = new Table(composite2, SWT.READ_ONLY);
		text6.setBackground(composite2.getBackground());
		text6.setLayoutData(formData);
		text6.setFont(ConfigBean.getFontTable());

		text6.setLinesVisible(false);
		text6.setHeaderVisible(false);

		new TableColumn(text6, SWT.LEFT);

		textDayWednesday = new ExpandItem(bar1, SWT.NONE, 1);
		textDayWednesday.setControl(composite2);
		textDayWednesday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayWednesday.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text7 = new Table(composite2, SWT.READ_ONLY);
		text7.setBackground(composite2.getBackground());
		text7.setLayoutData(formData);

		text7.setLinesVisible(false);
		text7.setHeaderVisible(false);
		text7.setFont(ConfigBean.getFontTable());

		new TableColumn(text7, SWT.LEFT);

		textDayThursday = new ExpandItem(bar1, SWT.NONE, 2);
		textDayThursday.setControl(composite2);
		textDayThursday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayThursday.setExpanded(true);

		composite2 = new Composite(bar1, SWT.NONE);
		composite2.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite2.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		text8 = new Table(composite2, SWT.READ_ONLY);
		text8.setBackground(composite2.getBackground());
		text8.setLayoutData(formData);
		text8.setFont(ConfigBean.getFontTable());

		text8.setLinesVisible(false);
		text8.setHeaderVisible(false);

		new TableColumn(text8, SWT.LEFT);

		textDayFriday = new ExpandItem(bar1, SWT.NONE, 3);
		textDayFriday.setControl(composite2);
		textDayFriday.setHeight(composite2.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		textDayFriday.setExpanded(true);

		getDayEvents(Calendar.getInstance());
	}

	private String formatDate(Calendar cal) {
		return cal.get(Calendar.YEAR) + "-" + doubleDigitFormat.format((cal.get(Calendar.MONTH) + 1)) + "-"
			   + doubleDigitFormat.format(cal.get(Calendar.DAY_OF_MONTH));
	}

	private void getDayEvents(Calendar calendar) {
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Calendar cal3 = Calendar.getInstance();

		int diff = 0;
		Season season = new Season(calendar.getTimeInMillis());
		group.setText(Messages.getString("training.season") + " " + season.getSeasonNumber() + " " + Messages.getString("training.week") + " "
					  + season.getSeasonWeek());

		textDayEvery.setText(Messages.getString("calendar.day.0"));

		text1.removeAll();

		new TableItem(text1, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.1"));
		new TableItem(text1, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.2"));
		new TableItem(text1, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.3"));
		new TableItem(text1, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.4"));

		text1.getColumn(0).pack();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = 0;
		} else {
			diff = -calendar.get(Calendar.DAY_OF_WEEK);
		}

		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDaySaturday.setText(Messages.getString("calendar.day." + Calendar.SATURDAY) + " " + "(" + formatDate(cal) + ")");

		text2.removeAll();

		new TableItem(text2, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.29"));
		new TableItem(text2, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.7"));
		new TableItem(text2, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.28"));
		new TableItem(text2, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.8"));
		new TableItem(text2, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.9"));

		text2.getColumn(0).pack();
		text3.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.SUNDAY;
		} else {
			diff = Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDaySunday.setText(Messages.getString("calendar.day." + Calendar.SUNDAY) + " " + "(" + formatDate(cal) + ")");

		if (season.getSeasonWeek() == 0) {
			new TableItem(text3, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.20"));
		} else if (season.getSeasonWeek() == 15) {
			new TableItem(text3, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.21"));
		} else {
			new TableItem(text3, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.23"));
		}
		new TableItem(text3, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.5"));
		new TableItem(text3, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.25"));

		text3.getColumn(0).pack();
		text4.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.MONDAY;
		} else {
			diff = Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDayMonday.setText(Messages.getString("calendar.day." + Calendar.MONDAY) + " " + "(" + formatDate(cal) + ")");

		if (season.getSeasonWeek() == 0) {
			new TableItem(text4, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.24"));
		}

		new TableItem(text4, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.10"));
		new TableItem(text4, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.11"));
		new TableItem(text4, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.12"));
		new TableItem(text4, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.13"));

		text4.getColumn(0).pack();
		text5.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.TUESDAY;
		} else {
			diff = Calendar.TUESDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}

		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDayTuesday.setText(Messages.getString("calendar.day." + Calendar.TUESDAY) + " " + "(" + formatDate(cal) + ")");

		new TableItem(text5, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.26"));

		text5.getColumn(0).pack();
		text6.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.WEDNESDAY;
		} else {
			diff = Calendar.WEDNESDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDayWednesday.setText(Messages.getString("calendar.day." + Calendar.WEDNESDAY) + " " + "(" + formatDate(cal) + ")");

		new TableItem(text6, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.14"));
		new TableItem(text6, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.22"));

		text6.getColumn(0).pack();
		text7.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.THURSDAY;
		} else {
			diff = Calendar.THURSDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDayThursday.setText(Messages.getString("calendar.day." + Calendar.THURSDAY) + " " + "(" + formatDate(cal) + ")");

		new TableItem(text7, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.12"));
		new TableItem(text7, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.15"));
		new TableItem(text7, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.16"));
		new TableItem(text7, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.17"));
		new TableItem(text7, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.18"));

		text7.getColumn(0).pack();
		text8.removeAll();

		if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
			diff = Calendar.FRIDAY;
		} else {
			diff = Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK);
		}
		cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));

		textDayFriday.setText(Messages.getString("calendar.day." + Calendar.FRIDAY) + " " + "(" + formatDate(cal) + ")");

		if (season.getSeasonWeek() == 15) {
			new TableItem(text8, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.19"));
		} else {
			new TableItem(text8, SWT.NONE).setText("- " + Messages.getString("calendar.day.event.6"));
		}

		text8.getColumn(0).pack();

		text2.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text3.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text4.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text5.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text6.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text7.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		text8.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SATURDAY:
			text2.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDaySaturday.setExpanded(true);
			break;
		case Calendar.SUNDAY:
			text3.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDaySunday.setExpanded(true);
			break;
		case Calendar.MONDAY:
			text4.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDayMonday.setExpanded(true);
			break;
		case Calendar.TUESDAY:
			text5.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDayTuesday.setExpanded(true);
			break;
		case Calendar.WEDNESDAY:
			text6.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDayWednesday.setExpanded(true);
			break;
		case Calendar.THURSDAY:
			text7.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDayThursday.setExpanded(true);
			break;
		case Calendar.FRIDAY:
			text8.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
			textDayFriday.setExpanded(true);
			break;

		}

		if (Cache.getNotes() != null) {

			for (Note note : Cache.getNotes()) {
				if (note.getAlertDate() != null) {
					if (note.getAlertDate() != null) {
						cal2.setTimeInMillis(note.getAlertDate().getMillis());

						if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
							diff = 0;
						} else {
							diff = -calendar.get(Calendar.DAY_OF_WEEK);
						}
						cal.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));
						cal.set(Calendar.HOUR, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);

						if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
							diff = Calendar.FRIDAY;
						} else {
							diff = Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK);
						}
						cal3.setTimeInMillis(calendar.getTimeInMillis() + diff * (24 * 60 * 60 * 1000));
						cal3.set(Calendar.HOUR, 23);
						cal3.set(Calendar.MINUTE, 59);
						cal3.set(Calendar.SECOND, 59);
						cal3.set(Calendar.MILLISECOND, 999);

						if ((cal2.after(cal) && cal2.before(cal3)) || (cal2.compareTo(cal3) == 0) || (cal2.compareTo(cal) == 0)) {
							switch (cal2.get(Calendar.DAY_OF_WEEK)) {
							case Calendar.SATURDAY:
								new TableItem(text2, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.SUNDAY:
								new TableItem(text3, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.MONDAY:
								new TableItem(text4, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.TUESDAY:
								new TableItem(text5, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.WEDNESDAY:
								new TableItem(text6, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.THURSDAY:
								new TableItem(text7, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							case Calendar.FRIDAY:
								new TableItem(text8, SWT.NONE).setText(note.getAlertDate().toDateString() + " " + note.getTitle());
								break;
							}
						}
					}
				}
			}
		}

		text1.pack();
		textDayEvery.setHeight(text1.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text2.pack();
		textDaySaturday.setHeight(text2.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text2.setItemCount(text2.getItemCount());
		text3.pack();
		textDaySunday.setHeight(text3.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text4.pack();
		textDayMonday.setHeight(text4.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text5.pack();
		textDayTuesday.setHeight(text5.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text6.pack();
		textDayWednesday.setHeight(text6.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text7.pack();
		textDayThursday.setHeight(text7.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		text8.pack();
		textDayFriday.setHeight(text8.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
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
}
