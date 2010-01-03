package pl.pronux.sokker.ui.plugins;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.SchedulerManager;
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.NoteComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.DateConst;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Note;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewNotepad implements IPlugin, ISort {

	private SchedulerManager schedulerManager = SchedulerManager.instance();
	
	private Listener clearListener;

	private Composite composite;

	private FormData formData;

	private boolean newDocument;

	private Listener newNoteListener;

	private Listener openListener;

	private Listener saveListener;

	private ToolBar toolBar;

	private TreeItem treeItem;

	private Composite viewNoteComposite;

	private StyledText viewTitle;

	private Table viewTableNote;

	private StyledText viewText;

	private ArrayList<Note> notes;

	private Listener showTableListener;

	private Composite alertComposite;

	private Button alertButton;

	private NoteComparator comparator;

	private Listener deleteListener;

	private DateTime datePicker;

	private DateTime timePicker;

	private void addListeners() {
		openListener = new Listener() {

			public void handleEvent(Event event) {
				TableItem[] items = viewTableNote.getSelection();
				if (items.length == 1) {

					Note note = (Note) items[0].getData(Note.class.getName());
					viewTitle.setText(note.getTitle());
					viewText.setText(note.getText());

					if (note.getAlertDate() != null) {
						Calendar cal = note.getAlertDate().getCalendar();
						alertButton.setSelection(true);
						datePicker.setEnabled(true);
						timePicker.setEnabled(true);
						timePicker.setHours(cal.get(Calendar.HOUR_OF_DAY));
						timePicker.setMinutes(cal.get(Calendar.MINUTE));
						datePicker.setYear(cal.get(Calendar.YEAR));
						datePicker.setMonth(cal.get(Calendar.MONTH));
						datePicker.setDay(cal.get(Calendar.DAY_OF_MONTH));
					} else {
						alertButton.setSelection(false);
						datePicker.setEnabled(false);
						timePicker.setEnabled(false);
					}

					viewTableNote.setVisible(false);
					viewNoteComposite.setVisible(true);
					newDocument = false;

					toolBar.getItem(0).setEnabled(false);
					toolBar.getItem(1).setEnabled(false);
					toolBar.getItem(3).setEnabled(true);
					toolBar.getItem(5).setEnabled(true);
					toolBar.getItem(6).setEnabled(true);
					toolBar.getItem(7).setEnabled(true);
				}
			}
		};

		newNoteListener = new Listener() {

			public void handleEvent(Event event) {
				viewText.setText(""); //$NON-NLS-1$
				viewTitle.setText(""); //$NON-NLS-1$
				alertButton.setSelection(false);
				datePicker.setEnabled(false);
				timePicker.setEnabled(false);

				viewTableNote.setVisible(false);
				viewNoteComposite.setVisible(true);
				newDocument = true;

				viewTableNote.setSelection(new TableItem[] {});

				toolBar.getItem(0).setEnabled(false);
				toolBar.getItem(1).setEnabled(false);
				toolBar.getItem(3).setEnabled(false);

				toolBar.getItem(5).setEnabled(true);
				toolBar.getItem(6).setEnabled(true);
				toolBar.getItem(7).setEnabled(true);
			}
		};

		showTableListener = new Listener() {

			public void handleEvent(Event event) {

				if (newDocument) {
					MessageBox msg = new MessageBox(viewNoteComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);

					msg.setText(Messages.getString("message.note.save.title")); //$NON-NLS-1$
					msg.setMessage(Messages.getString("message.note.save.text")); //$NON-NLS-1$

					if (msg.open() == SWT.YES) {
						saveNote();
					}
				} else {
					TableItem[] items = viewTableNote.getSelection();
					if (items.length == 1) {
						boolean check = false;
						Note note = (Note) items[0].getData(Note.class.getName()); 

						if (note.getAlertDate() != null && alertButton.getSelection()) {
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, datePicker.getYear());
							cal.set(Calendar.MONTH, datePicker.getMonth());
							cal.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
							cal.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
							cal.set(Calendar.MINUTE, timePicker.getMinutes());
							cal.set(Calendar.SECOND, note.getAlertDate().getCalendar().get(Calendar.SECOND));
							cal.set(Calendar.MILLISECOND, note.getAlertDate().getCalendar().get(Calendar.MILLISECOND));
							Date date = new Date(cal);
							if (note.getAlertDate().compareTo(date) != 0) {
								check = true;
							}
						} else if (note.getAlertDate() == null && alertButton.getSelection()) {
							check = true;
						} else if (note.getAlertDate() != null && !alertButton.getSelection()) {
							check = true;
						}

						if (!note.getText().equals(viewText.getText()) || !note.getTitle().equals(viewTitle.getText())) {
							check = true;
						}

						if (check) {
							MessageBox msg = new MessageBox(viewNoteComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);

							msg.setText(Messages.getString("message.note.save.title")); //$NON-NLS-1$
							msg.setMessage(Messages.getString("message.note.save.text")); //$NON-NLS-1$

							if (msg.open() == SWT.YES) {
								saveNote();
							}
						}

					}
				}

				viewNoteComposite.setVisible(false);
				viewTableNote.setVisible(true);

				toolBar.getItem(0).setEnabled(true);
				toolBar.getItem(1).setEnabled(true);
				toolBar.getItem(3).setEnabled(true);

				toolBar.getItem(5).setEnabled(false);
				toolBar.getItem(6).setEnabled(false);
				toolBar.getItem(7).setEnabled(false);

			}
		};

		clearListener = new Listener() {

			public void handleEvent(Event event) {

				MessageBox msg = new MessageBox(viewNoteComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);

				msg.setText(Messages.getString("message.note.eraser.title")); //$NON-NLS-1$
				msg.setMessage(Messages.getString("message.note.eraser.text")); //$NON-NLS-1$

				if (msg.open() == SWT.YES) {
					viewTitle.setText(""); //$NON-NLS-1$
					viewText.setText(""); //$NON-NLS-1$
					alertButton.setSelection(false);
					datePicker.setEnabled(false);
					timePicker.setEnabled(false);
				}
			}
		};

		deleteListener = new Listener() {

			public void handleEvent(Event event) {
				TableItem[] items = viewTableNote.getSelection();
				if (items.length > 0) {
					MessageBox msg = new MessageBox(viewTableNote.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					msg.setText(Messages.getString("message.note.delete.title")); //$NON-NLS-1$
					msg.setMessage(Messages.getString("message.note.delete.text")); //$NON-NLS-1$
					if (msg.open() == SWT.YES) {
						for (int i = 0; i < items.length; i++) {
							Note note = (Note) items[i].getData(Note.class.getName());
							try {
								schedulerManager.dropNote(note.getId());
								notes.remove(note);
								items[i].dispose();
							} catch (SQLException e) {
								new BugReporter(composite.getDisplay()).openErrorMessage("ViewNotepad", e);
							}
						}

						toolBar.getItem(0).setEnabled(true);
						toolBar.getItem(1).setEnabled(true);
						toolBar.getItem(3).setEnabled(true);
						toolBar.getItem(5).setEnabled(false);
						toolBar.getItem(6).setEnabled(false);
						toolBar.getItem(7).setEnabled(false);

						if (viewNoteComposite.isVisible()) {
							viewNoteComposite.setVisible(false);
							viewTableNote.setVisible(true);
						}

					}
				}
			}
		};

		saveListener = new Listener() {

			public void handleEvent(Event event) {

				saveNote();

				toolBar.getItem(0).setEnabled(true);
				toolBar.getItem(1).setEnabled(true);
				toolBar.getItem(3).setEnabled(true);
				toolBar.getItem(5).setEnabled(false);
				toolBar.getItem(6).setEnabled(false);
				toolBar.getItem(7).setEnabled(false);

				viewNoteComposite.setVisible(false);
				viewTableNote.setVisible(true);
			}
		};

	}

	private void saveNote() {
		if (newDocument == true) {
			Note note = new Note();
			note.setText(viewText.getText());
			note.setTitle(viewTitle.getText());
			if (alertButton.getSelection()) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, datePicker.getYear());
				cal.set(Calendar.MONTH, datePicker.getMonth());
				cal.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
				cal.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
				cal.set(Calendar.MINUTE, timePicker.getMinutes());
				Date date = new Date(cal);
				note.setAlertDate(date);
			} else {
				note.setAlertDate(null);
			}

			GregorianCalendar cal = new GregorianCalendar();
			Date modificationDate = new Date(cal.getTimeInMillis());
			note.setModificationDate(modificationDate);

			notes.add(note);
			fillTable(viewTableNote, notes);
			try {
				schedulerManager.insertNote(note);
			} catch (SQLException e) {
				new BugReporter(composite.getDisplay()).openErrorMessage("ViewNotepad", e);
			}
		} else {
			TableItem[] items = viewTableNote.getSelection();
			if (items.length == 1) {
				Note note = (Note) items[0].getData(Note.class.getName()); 
				note.setText(viewText.getText());
				note.setTitle(viewTitle.getText());
				if (alertButton.getSelection()) {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, datePicker.getYear());
					cal.set(Calendar.MONTH, datePicker.getMonth());
					cal.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
					cal.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
					cal.set(Calendar.MINUTE, timePicker.getMinutes());
					Date date = new Date(cal);
					note.setAlertDate(date);
				} else {
					note.setAlertDate(null);
				}

				GregorianCalendar cal = new GregorianCalendar();
				Date modificationDate = new Date(cal.getTimeInMillis());
				note.setModificationDate(modificationDate);

				fillTable(viewTableNote, notes);
				try {
					schedulerManager.updateNote(note);
				} catch (SQLException e) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewNotepad", e);
				}
			}
		}

		for (int i = 0; i < viewTableNote.getColumnCount() - 1; i++) {
			viewTableNote.getColumn(i).pack();
		}

	}

	private void addNoteListView() {
		viewTableNote = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.CHECK);
		viewTableNote.setHeaderVisible(true);
		viewTableNote.setLinesVisible(true);
		viewTableNote.setLayoutData(formData);
		viewTableNote.setFont(ConfigBean.getFontTable());

		String[] titles = {
				Messages.getString("notepad.date"), //$NON-NLS-1$
				Messages.getString("notepad.title"), //$NON-NLS-1$
				Messages.getString("notepad.text"), //$NON-NLS-1$
				Messages.getString("notepad.alert"), //$NON-NLS-1$
				Messages.getString("notepad.modification"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(viewTableNote, SWT.NONE);

			column.setResizable(false);
			column.setMoveable(false);

			column.setText(titles[i]);
			if (titles[i].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}

		}
		comparator = new NoteComparator();
		comparator.setColumn(NoteComparator.DATE);
		comparator.setDirection(NoteComparator.ASCENDING);
		notes = new ArrayList<Note>();

		viewTableNote.setSortColumn(viewTableNote.getColumn(NoteComparator.DATE));
		viewTableNote.setSortDirection(SWT.UP);

		final TableColumn[] columns = viewTableNote.getColumns();

		for (int i = 0; i < columns.length - 1; i++) {
			columns[i].addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					viewTableNote.setRedraw(false);
					int column = viewTableNote.indexOf((TableColumn) event.widget);
					if (column != comparator.getColumn()) {
						comparator.setColumn(column);
						comparator.setDirection(NoteComparator.ASCENDING);
						viewTableNote.setSortColumn(viewTableNote.getColumn(column));
						viewTableNote.setSortDirection(SWT.UP);
					} else {
						if (comparator.getDirection() == NoteComparator.ASCENDING) {
							viewTableNote.setSortDirection(SWT.DOWN);
							comparator.reverseDirection();
						} else {
							viewTableNote.setSortDirection(SWT.UP);
							comparator.reverseDirection();
						}
					}
					fillTable(viewTableNote, notes);
					viewTableNote.setRedraw(true);
				}
			});
		}

		viewTableNote.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				TableItem item = (TableItem) arg0.item;
				Note note = (Note) item.getData(Note.class.getName()); 
				boolean check = arg0.detail == SWT.CHECK ? true : false;
				if (check) {
					note.setChecked(item.getChecked());
					try {
						schedulerManager.updateNote(note);
					} catch (SQLException e) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewNotepad", e);
					}
				}
			}
		});
		viewTableNote.addListener(SWT.MouseDoubleClick, openListener);
	}

	private void addNoteView() {
		viewNoteComposite = new Composite(composite, SWT.NONE);
		viewNoteComposite.setLayoutData(formData);

		viewNoteComposite.setLayout(new FormLayout());
		viewNoteComposite.setVisible(false);

		addSubject(viewNoteComposite);

		addAlert(viewNoteComposite);

		addView(viewNoteComposite);
		Control[] tabList1 = new Control[] {
				viewTitle,
				viewText,
				alertComposite
		};
		viewNoteComposite.setTabList(tabList1);
	}

	private void addAlert(Composite viewNoteComposite) {
		FormData formData = new FormData();
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(100, -35);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;

		alertComposite = new Composite(viewNoteComposite, SWT.BORDER);
		alertComposite.setLayout(layout);
		alertComposite.setLayoutData(formData);

		GridData layoutData = new GridData();
		layoutData.grabExcessVerticalSpace = true;
		layoutData.verticalAlignment = GridData.CENTER;
		layoutData.widthHint = 40;

		alertButton = new Button(alertComposite, SWT.CHECK);
		alertButton.setLayoutData(layoutData);
		alertButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (alertButton.getSelection()) {
					datePicker.setEnabled(true);
					timePicker.setEnabled(true);
				} else {
					datePicker.setEnabled(false);
					timePicker.setEnabled(false);
				}
			}
		});

		alertButton.setImage(ImageResources.getImageResources("reminder.png")); //$NON-NLS-1$
		alertButton.setToolTipText(Messages.getString("notepad.button.alert.description")); //$NON-NLS-1$
		alertButton.pack();

		layoutData = new GridData();
		layoutData.grabExcessVerticalSpace = true;
		layoutData.verticalAlignment = GridData.CENTER;
		layoutData.widthHint = 100;

		datePicker = new DateTime(alertComposite, SWT.DATE | SWT.MEDIUM);
		datePicker.setLayoutData(layoutData);
		datePicker.setFont(ConfigBean.getFontMain());

		layoutData = new GridData();
		layoutData.grabExcessVerticalSpace = true;
		layoutData.verticalAlignment = GridData.CENTER;

		timePicker = new DateTime(alertComposite, SWT.TIME | SWT.SHORT);
		timePicker.setLayoutData(layoutData);
		timePicker.setFont(ConfigBean.getFontMain());
	}

	private void addSubject(Composite composite) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.height = 15;

		viewTitle = new StyledText(composite, SWT.SINGLE | SWT.BORDER);
		viewTitle.setLayoutData(formData);
		viewTitle.setFont(ConfigBean.getFontMain());
		viewTitle.setToolTipText(Messages.getString("notepad.title")); //$NON-NLS-1$
	}

	private void addToolBar() {
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		toolBar = new ToolBar(composite, SWT.FLAT | SWT.HORIZONTAL);
		toolBar.setLayoutData(formData);

		toolBar.setEnabled(false);

		ToolItem item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("new_note.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("new_note_bw.png")); //$NON-NLS-1$
		item.addListener(SWT.Selection, newNoteListener);
		item.setToolTipText(Messages.getString("notepad.button.new.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.new")); //$NON-NLS-1$

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("open.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("open_bw.png")); //$NON-NLS-1$
		item.addListener(SWT.Selection, openListener);
		item.setToolTipText(Messages.getString("notepad.button.open.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.open")); //$NON-NLS-1$

		item = new ToolItem(toolBar, SWT.SEPARATOR);

		item = new ToolItem(toolBar, SWT.NONE);
		item.setWidth(32);
		item.setImage(ImageResources.getImageResources("delete.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("delete_bw.png")); //$NON-NLS-1$
		item.addListener(SWT.Selection, deleteListener);
		item.setToolTipText(Messages.getString("notepad.button.delete.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.delete")); //$NON-NLS-1$

		item = new ToolItem(toolBar, SWT.SEPARATOR);

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("save.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("save_bw.png")); //$NON-NLS-1$
		item.setEnabled(false);
		item.setToolTipText(Messages.getString("notepad.button.save.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.save")); //$NON-NLS-1$

		item.addListener(SWT.Selection, saveListener);

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("eraser.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("eraser_bw.png")); //$NON-NLS-1$
		item.addListener(SWT.Selection, clearListener);
		item.setEnabled(false);
		item.setToolTipText(Messages.getString("notepad.button.eraser.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.eraser")); //$NON-NLS-1$

		item = new ToolItem(toolBar, SWT.NONE);
		item.setImage(ImageResources.getImageResources("list.png")); //$NON-NLS-1$
		item.setDisabledImage(ImageResources.getImageResources("list_bw.png")); //$NON-NLS-1$
		item.addListener(SWT.Selection, showTableListener);
		item.setEnabled(false);
		item.setToolTipText(Messages.getString("notepad.button.list.description")); //$NON-NLS-1$
		item.setText(Messages.getString("notepad.button.list")); //$NON-NLS-1$

	}

	private void addView(Composite composite) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(viewTitle, 5);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(alertComposite, 0);

		viewText = new StyledText(composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		viewText.setLayoutData(formData);
		viewText.setFont(ConfigBean.getFontMain());
	}

	private void addViewFormData() {
		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(toolBar, 5);
		formData.bottom = new FormAttachment(100, 0);
	}

	public void clear() {
		viewText.setText(""); //$NON-NLS-1$
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
		return Messages.getString("progressBar.info.setInfoNotepad"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());

		addListeners();

		addToolBar();

		addViewFormData();

		addNoteView();
		addNoteListView();

		composite.layout(true);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		treeItem.setText(Messages.getString("tree.ViewNotepad")); //$NON-NLS-1$
		treeItem.setImage(ImageResources.getImageResources("notepad.png")); //$NON-NLS-1$
	}

	public void set() {
		toolBar.setEnabled(true);
		notes = Cache.getNotes();
		setViewTableNote();
		reminder(notes);
	}

	private void setViewTableNote() {
		fillTable(viewTableNote, notes);
	}

	private void reminder(ArrayList<Note> notes) {
		int counter = 0;
		GregorianCalendar cal = new GregorianCalendar();
		for (Note note : notes) {
			if (note.getAlertDate() != null) {
				if ((cal.getTimeInMillis() + DateConst.DAY > note.getAlertDate().getMillis()) && (cal.getTimeInMillis() - DateConst.DAY < note.getAlertDate().getMillis())) {
					counter++;
				}
			}
		}
		if (counter > 0) {
			final int finalCounter = counter;
			composite.getDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageBox msg = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_INFORMATION);
					msg.setText(Messages.getString("message.reminder.title")); //$NON-NLS-1$
					msg.setMessage(Messages.getString("message.reminder.text") + " " + finalCounter + Messages.getString("message.reminder.question")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (msg.open() == SWT.YES) {
						treeItem.getParent().setSelection(treeItem);
						ViewerHandler.getViewer().notifyListeners(IEvents.SHOW, new Event());
					}
				}
			});

		}

	}

	private void fillTable(Table table, ArrayList<Note> notes) {
		table.setRedraw(false);

		table.remove(0, table.getItemCount() - 1);

		Collections.sort(notes, comparator);
		for (Note note : notes) {
			TableItem item = new TableItem(viewTableNote, SWT.NONE);

			int c = 0;
			item.setData(Note.class.getName(), note); 
			item.setChecked(note.isChecked());

			if (note.getDate() != null) {
				item.setText(c++, note.getDate().toDateTimeString());
			} else {
				item.setText(c++, ""); //$NON-NLS-1$
			}

			if (note.getTitle().length() > 20) {
				item.setText(c++, note.getTitle().substring(0, 20).replaceAll("\n", "") + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			} else {
				item.setText(c++, note.getTitle());
			}

			if (note.getText().length() > 40) {
				item.setText(c++, note.getText().substring(0, 40).replaceAll("\n", "") + "..."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				item.setText(c++, note.getText());
			}

			if (note.getAlertDate() != null) {
				if (note.getAlertDate() == null) {
					item.setText(c++, ""); //$NON-NLS-1$
				} else {
					if (new GregorianCalendar().getTimeInMillis() > note.getAlertDate().getMillis()) {
						item.setForeground(c, ConfigBean.getColorError());
						item.setText(c++, note.getAlertDate().toDateTimeString());
					} else {
						item.setText(c++, note.getAlertDate().toDateTimeString());
					}
				}
			} else {
				item.setText(c++, ""); //$NON-NLS-1$
			}

			if (note.getModificationDate() != null) {
				item.setText(c++, note.getModificationDate().toDateTimeString());
			} else {
				item.setText(c++, ""); //$NON-NLS-1$
			}

		}

		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
		table.setRedraw(true);
	}

	public void dispose() {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void reload() {
	}
}
