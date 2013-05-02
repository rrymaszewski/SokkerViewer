package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.PersonsManager;
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.CoachTrashComparator;
import pl.pronux.sokker.comparators.JuniorTrashComparator;
import pl.pronux.sokker.comparators.PlayerTrashComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.JuniorsDao;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.TabComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewTrash implements IPlugin, ISort {

	private PersonsManager personsManager = PersonsManager.instance();

	private TreeItem _treeItem;

	private CoachTrashComparator coachComparator;

	private List<Coach> coaches;

	private Table coachesTable;

	private Composite composite;

	private CTabItem cTabItemCoaches;

	private CTabItem cTabItemJuniors;

	private CTabItem cTabItemPlayers;

	private TabComposite currentComposite;

	private TableItem currentItem;

	private List<Junior> juniors;

	private Table juniorsTable;

	private JuniorTrashComparator juniorTrashComparator;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpAll;

	private Menu menuPopUpCoachesFired;

	private Menu menuPopUpJuniors;

	private Menu menuPopUpPlayersHistory;

	private PlayerTrashComparator playerComparator;

	private List<Player> players;

	private Table playersTable;

	private TreeItem treeCoachItem;

	private TreeItem treeJuniorItem;

	private TreeItem treePlayerItem;

	private CTabFolder viewFolder;

	public void clear() {
		for (int i = 0; i < viewFolder.getItems().length; i++) {
			((TabComposite) viewFolder.getItem(i).getControl()).clearAll();
		}
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
		return Messages.getString("progressBar.info.setInfoTrash");
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		players = new ArrayList<Player>();
		coaches = new ArrayList<Coach>();
		juniors = new ArrayList<Junior>();

		composite.setLayout(new FormLayout());
		composite.layout(true);
		addListeners();
		addViewComposite();

		addPopupMenu();
		addPopupMenuTree();
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;
	}

	public void setTreeItem(TreeItem treeItem) {

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewTrash"));

		_treeItem.setImage(ImageResources.getImageResources("trash.png"));

		treePlayerItem = new TreeItem(_treeItem, SWT.NONE);
		treePlayerItem.setText(Messages.getString("tree.ViewPlayers"));
		treePlayerItem.setImage(ImageResources.getImageResources("player.png"));

		treeJuniorItem = new TreeItem(_treeItem, SWT.NONE);
		treeJuniorItem.setText(Messages.getString("tree.ViewJuniors"));
		treeJuniorItem.setImage(ImageResources.getImageResources("junior.png"));

		treeCoachItem = new TreeItem(_treeItem, SWT.NONE);
		treeCoachItem.setText(Messages.getString("tree.ViewCoaches"));
		treeCoachItem.setImage(ImageResources.getImageResources("whistle.png"));
		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(treeCoachItem)) {
						viewFolder.setSelection(cTabItemCoaches);

						currentComposite = (TabComposite) cTabItemCoaches.getControl();

						if (event.button == 3) {
							_treeItem.getParent().setMenu(menuPopUpCoachesFired);
							_treeItem.getParent().getMenu().setVisible(true);
						}
					}
				}
			}
		});

	}

	public void set() {

		addListeners();

		players = Cache.getPlayersTrash();
		coaches = Cache.getCoachesTrash();
		juniors = Cache.getJuniorsTrash();

		// addFans((TabComposite)cTabItemFans.getControl());
		addPlayers((TabComposite) cTabItemPlayers.getControl());
		addCoaches((TabComposite) cTabItemCoaches.getControl());
		addJuniors((TabComposite) cTabItemJuniors.getControl());

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);
				if (item != null) {
					if (item.equals(_treeItem)) {

						if (event.button == 3) {
							_treeItem.getParent().setMenu(menuPopUpAll);
							_treeItem.getParent().getMenu().setVisible(true);
						}
					}
				}
			}
		});

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(treeJuniorItem)) {
						viewFolder.setSelection(cTabItemJuniors);

						currentComposite = (TabComposite) cTabItemJuniors.getControl();
						if (event.button == 3) {
							_treeItem.getParent().setMenu(menuPopUpJuniors);
							_treeItem.getParent().getMenu().setVisible(true);
						}
					}
				}
			}
		});

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(treePlayerItem)) {
						viewFolder.setSelection(cTabItemPlayers);

						currentComposite = (TabComposite) cTabItemPlayers.getControl();

						if (event.button == 3) {
							_treeItem.getParent().setMenu(menuPopUpPlayersHistory);
							_treeItem.getParent().getMenu().setVisible(true);
						}
					}
				}
			}
		});
	}

	private void addCoaches(TabComposite tabComposite) {
		Table tempTable = tabComposite.getViewTable();
		fillCoachesTable(tempTable);
	}

	private void addCTabItemCoaches() {
		cTabItemCoaches = new CTabItem(viewFolder, SWT.NONE);
		cTabItemCoaches.setText(Messages.getString("trash.coaches"));
		cTabItemCoaches.setFont(ConfigBean.getFontMain());

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		TabComposite tabComposite = new TabComposite(viewFolder, SWT.NONE);

		coachesTable = new Table(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		coachesTable.setHeaderVisible(true);
		coachesTable.setLinesVisible(true);
		coachesTable.setLayoutData(tabComposite.getViewFormData());
		tabComposite.setLayoutData(formData);

		String[] titles = { Messages.getString("table.name"), Messages.getString("table.surname"), Messages.getString("table.salary"),
						   Messages.getString("table.age"), Messages.getString("table.generallSkill"), Messages.getString("table.stamina"),
						   Messages.getString("table.pace"), Messages.getString("table.technique"), Messages.getString("table.passing"),
						   Messages.getString("table.keeper"), Messages.getString("table.defender"), Messages.getString("table.playmaker"),
						   Messages.getString("table.scorer"), "" };

		tabComposite.setViewTable(coachesTable);

		coachesTable.setFont(ConfigBean.getFontTable());

		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(coachesTable, SWT.NONE);
			if (i > 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}
			column.setText(titles[i]);
			column.setMoveable(false);
			column.setResizable(false);

			if (titles[i].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			}

		}

		coachComparator = new CoachTrashComparator();
		coachComparator.setColumn(CoachTrashComparator.SURNAME);
		coachComparator.setDirection(CoachTrashComparator.ASCENDING);
		coachesTable.setSortColumn(coachesTable.getColumn(CoachTrashComparator.SURNAME));
		coachesTable.setSortDirection(SWT.UP);

		coachesTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					setStatsCoachInfo((Coach) event.item.getData("person"), currentComposite.getDescriptionComposite());
				}
			}

		});

		coachesTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					Point pt = new Point(event.x, event.y);
					TableItem item = coachesTable.getItem(pt);
					if (item != null) {

						currentItem = item;
						coachesTable.setMenu(menuPopUp);
						coachesTable.getMenu().setVisible(true);
					} else {
						coachesTable.setMenu(menuClear);
					}
				}
			}
		});

		final TableColumn[] columns = coachesTable.getColumns();

		for (int i = 0; i < columns.length - 1; i++) {

			columns[i].addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					int column = coachesTable.indexOf((TableColumn) event.widget);

					if (column != coachComparator.getColumn()) {
						coachComparator.setColumn(column);
						coachComparator.setDirection(CoachTrashComparator.ASCENDING);

						coachesTable.setSortColumn(coachesTable.getColumn(column));
						coachesTable.setSortDirection(SWT.UP);
					} else {

						if (coachComparator.getDirection() == CoachTrashComparator.ASCENDING) {
							coachesTable.setSortDirection(SWT.DOWN);
							coachComparator.reverseDirection();
						} else {
							coachesTable.setSortDirection(SWT.UP);
							coachComparator.reverseDirection();
						}
					}
					fillCoachesTable(coachesTable);
				}
			});
		}

		cTabItemCoaches.setControl(tabComposite);
	}

	private void addCTabItemJuniors() {
		cTabItemJuniors = new CTabItem(viewFolder, SWT.NONE);
		cTabItemJuniors.setText(Messages.getString("trash.juniors"));
		cTabItemJuniors.setFont(ConfigBean.getFontMain());

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		TabComposite tabComposite = new TabComposite(viewFolder, SWT.NONE);
		juniorsTable = new Table(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		juniorsTable.setHeaderVisible(true);
		juniorsTable.setLinesVisible(true);
		juniorsTable.setLayoutData(tabComposite.getViewFormData());
		tabComposite.setLayoutData(formData);
		String[] titles = { Messages.getString("table.name"), Messages.getString("table.surname"), Messages.getString("table.formation"),
						   Messages.getString("table.skill"), Messages.getString("table.status"), "" };
		tabComposite.setViewTable(juniorsTable);

		juniorsTable.setFont(ConfigBean.getFontTable());

		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(juniorsTable, SWT.NONE);
			if (i > 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}
			column.setText(titles[i]);
			column.setMoveable(false);
			column.setResizable(false);

			if (titles[i].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			}

		}

		juniorTrashComparator = new JuniorTrashComparator();
		juniorTrashComparator.setColumn(JuniorTrashComparator.SURNAME);
		juniorTrashComparator.setDirection(JuniorTrashComparator.ASCENDING);
		juniorsTable.setSortColumn(juniorsTable.getColumn(JuniorTrashComparator.SURNAME));
		juniorsTable.setSortDirection(SWT.UP);

		juniorsTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					setStatsJuniorInfo((Junior) event.item.getData("person"), currentComposite.getDescriptionComposite());
				}
			}

		});

		juniorsTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					Point pt = new Point(event.x, event.y);
					TableItem item = juniorsTable.getItem(pt);
					if (item != null) {
						currentItem = item;
						juniorsTable.setMenu(menuPopUp);
						juniorsTable.getMenu().setVisible(true);
					} else {
						juniorsTable.setMenu(menuClear);
					}
				}
			}
		});

		final TableColumn[] columns = tabComposite.getViewTable().getColumns();

		for (int i = 0; i < columns.length - 1; i++) {

			columns[i].addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					int column = juniorsTable.indexOf((TableColumn) event.widget);

					if (column != juniorTrashComparator.getColumn()) {
						juniorTrashComparator.setColumn(column);
						juniorTrashComparator.setDirection(JuniorTrashComparator.ASCENDING);

						juniorsTable.setSortColumn(juniorsTable.getColumn(column));
						juniorsTable.setSortDirection(SWT.UP);
					} else {

						if (juniorTrashComparator.getDirection() == JuniorTrashComparator.ASCENDING) {
							juniorsTable.setSortDirection(SWT.DOWN);
							juniorTrashComparator.reverseDirection();
						} else {
							juniorsTable.setSortDirection(SWT.UP);
							juniorTrashComparator.reverseDirection();
						}
					}
					fillJuniorsTable(juniorsTable);
				}
			});
		}

		cTabItemJuniors.setControl(tabComposite);

	}

	private void addCTabItemPlayers() {
		cTabItemPlayers = new CTabItem(viewFolder, SWT.NONE);
		cTabItemPlayers.setText(Messages.getString("trash.players"));
		cTabItemPlayers.setFont(ConfigBean.getFontMain());

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		TabComposite tabComposite = new TabComposite(viewFolder, SWT.NONE);
		tabComposite.setLayoutData(formData);

		playersTable = new Table(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		playersTable.setHeaderVisible(true);
		playersTable.setLinesVisible(true);
		playersTable.setLayoutData(tabComposite.getViewFormData());

		String[] title = { Messages.getString("table.name"), Messages.getString("table.surname"), Messages.getString("table.height"),
						  Messages.getString("table.value"), Messages.getString("table.salary"), Messages.getString("table.age"),
						  Messages.getString("table.form"), Messages.getString("table.stamina"), Messages.getString("table.pace"),
						  Messages.getString("table.technique"), Messages.getString("table.passing"), Messages.getString("table.keeper"),
						  Messages.getString("table.defender"), Messages.getString("table.playmaker"), Messages.getString("table.scorer"),
						  Messages.getString("table.sold"), "" };

		tabComposite.setViewTable(playersTable);

		playersTable.setFont(ConfigBean.getFontTable());

		for (int i = 0; i < title.length; i++) {
			TableColumn column = new TableColumn(playersTable, SWT.NONE);
			if (i > 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}
			column.setText(title[i]);
			column.setMoveable(false);
			column.setResizable(false);

			if (title[i].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			}

		}

		playerComparator = new PlayerTrashComparator();
		playerComparator.setColumn(PlayerTrashComparator.SURNAME);
		playerComparator.setDirection(PlayerTrashComparator.ASCENDING);
		playersTable.setSortColumn(playersTable.getColumn(PlayerTrashComparator.SURNAME));
		playersTable.setSortDirection(SWT.UP);

		playersTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					setStatsPlayerInfo((Player) event.item.getData("person"), currentComposite.getDescriptionComposite());
				}
			}

		});

		playersTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					Point pt = new Point(event.x, event.y);
					TableItem item = playersTable.getItem(pt);
					if (item != null) {
						currentItem = item;
						playersTable.setMenu(menuPopUp);
						playersTable.getMenu().setVisible(true);
					} else {
						playersTable.setMenu(menuClear);
					}
				}
			}
		});

		final TableColumn[] columns = tabComposite.getViewTable().getColumns();

		for (int i = 0; i < columns.length - 1; i++) {

			columns[i].addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					int column = playersTable.indexOf((TableColumn) event.widget);

					if (column != playerComparator.getColumn()) {
						playerComparator.setColumn(column);
						playerComparator.setDirection(PlayerTrashComparator.ASCENDING);

						playersTable.setSortColumn(playersTable.getColumn(column));
						playersTable.setSortDirection(SWT.UP);
					} else {

						if (playerComparator.getDirection() == PlayerTrashComparator.ASCENDING) {
							playersTable.setSortDirection(SWT.DOWN);
							playerComparator.reverseDirection();
						} else {
							playersTable.setSortDirection(SWT.UP);
							playerComparator.reverseDirection();
						}
					}

					fillPlayersTable(playersTable);
				}
			});
		}

		cTabItemPlayers.setControl(tabComposite);
	}

	private void addJuniors(TabComposite tabComposite) {
		Table tempTable = tabComposite.getViewTable();
		fillJuniorsTable(tempTable);
	}

	private void addListeners() {
		ViewerHandler.getViewer().addListener(IEvents.REFRESH_TRASH_COACHES, new Listener() {

			public void handleEvent(Event arg0) {
				fillCoachesTable(coachesTable);
			}

		});

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_TRASH_JUNIORS, new Listener() {

			public void handleEvent(Event arg0) {
				fillJuniorsTable(juniorsTable);
			}

		});

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_TRASH_PLAYERS, new Listener() {

			public void handleEvent(Event arg0) {
				fillPlayersTable(playersTable);
			}

		});
	}

	private void addPlayers(TabComposite tabComposite) {
		Table tempTable = tabComposite.getViewTable();
		fillPlayersTable(tempTable);

	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restore"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				Person person = (Person) currentItem.getData("person");
				if (person instanceof Player) {

					try {
						personsManager.restorePersonFromTrash(person);
						fillPlayersTable(playersTable);
						((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();
						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_PLAYERS_HISTORY, new Event());
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restorePlayer", e1);
					}
				} else if (person instanceof Junior) {
					try {
						if (person.getStatus() == Junior.STATUS_TRAINED + 10) {
							personsManager.restorePersonFromTrash(person);
							ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_TRAINED, new Event());
						} else if (person.getStatus() == Junior.STATUS_SACKED + 10) {
							personsManager.restorePersonFromTrash(person);
							ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_FIRED, new Event());
						}
						fillJuniorsTable(juniorsTable);
						((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreJunior", e1);
					}
				} else if (person instanceof Coach) {

					try {
						personsManager.restorePersonFromTrash(person);
						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_COACHES_FIRED, new Event());
						fillCoachesTable(coachesTable);
						((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreTrainer", e1);
					}
				}
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.delete"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				Person person = (Person) currentItem.getData("person");
				if (person instanceof Player) {
					messageBox.setMessage(Messages.getString("message.playerDelete.text"));
					messageBox.setText(Messages.getString("message.playerDelete.title"));

					if (messageBox.open() == SWT.YES) {
						try {
							personsManager.removePersonFromTrash(person);
							fillPlayersTable(playersTable);
							((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();
						} catch (SQLException e1) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->deletePlayer", e1);
						}
					}

				} else if (person instanceof Junior) {
					messageBox.setMessage(Messages.getString("message.juniorDelete.text"));
					messageBox.setText(Messages.getString("message.juniorDelete.title"));

					if (messageBox.open() == SWT.YES) {

						try {
							SQLSession.connect();
							if (JuniorsDao.usingJunior(person.getId())) {
								messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
								messageBox.setMessage(person.getName() + " " + person.getSurname() + " " + Messages.getString("message.juniorUsed.text"));
								messageBox.setText(Messages.getString("message.juniorUsed.title"));
								messageBox.open();

							} else {
								personsManager.removePersonFromTrash(person);
								fillJuniorsTable(juniorsTable);
								((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();
							}

							SQLSession.close();

						} catch (SQLException e1) {
							try {
								SQLSession.close();
							} catch (SQLException e2) {
							}
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->juniorDelete", e1);
						}
					}

				} else if (person instanceof Coach) {
					messageBox.setMessage(Messages.getString("message.coachDelete.text"));
					messageBox.setText(Messages.getString("message.coachDelete.title"));

					if (messageBox.open() == SWT.YES) {

						try {
							personsManager.removePersonFromTrash(person);
							fillCoachesTable(coachesTable);
							((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();
						} catch (SQLException e1) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->deleteCoach", e1);
						}
					}
				}
			}

		});

		menuClear = new Menu(composite.getShell(), SWT.POP_UP);

	}

	// private void addFans(TabComposite tabComposite) {
	// Table tempTable = tabComposite.getViewTable();
	//
	// }

	private void addPopupMenuTree() {
		// added popup menu
		menuPopUpJuniors = new Menu(composite.getShell(), SWT.POP_UP);

		MenuItem menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				try {
					SQLSession.connect();
					for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

						Junior junior = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(junior);

					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_FIRED, new Event());
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_TRAINED, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreAllJuniors", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}
				fillJuniorsTable(juniorsTable);
				((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();

			}
		});
		menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAllTrainedJuniors"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				try {
					SQLSession.connect();
					for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {
						Junior junior = itr.next();
						if (junior.getStatus() == Junior.STATUS_TRAINED + 10) {
							itr.remove();
							personsManager.restorePersonFromTrash(junior);
						}

					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_TRAINED, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAllTrainers", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}
				fillJuniorsTable(juniorsTable);
				((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();

			}
		});

		// added popup menu
		menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAllFiredJuniors"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				try {
					SQLSession.connect();
					for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

						Junior junior = itr.next();

						if (junior.getStatus() == Junior.STATUS_SACKED + 10) {
							itr.remove();
							personsManager.restorePersonFromTrash(junior);
						}

					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_FIRED, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreJuniors", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}

				fillJuniorsTable(juniorsTable);
				((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();
			}
		});

		menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.juniorDeleteAll.text"));
				messageBox.setText(Messages.getString("message.juniorDeleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();

						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

							Junior junior = itr.next();

							if (JuniorsDao.usingJunior(junior.getId())) {
								messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
								messageBox.setMessage(junior.getName() + " " + junior.getSurname() + " " + Messages.getString("message.juniorUsed.text"));
								messageBox.setText(Messages.getString("message.juniorUsed.title"));
								messageBox.open();

							} else {
								itr.remove();
								personsManager.removePersonFromTrash(junior);
							}

						}
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAllJuniors", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}
					fillJuniorsTable(juniorsTable);
					((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();

				}
			}
		});

		menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAllTrainedJuniors"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.juniorTrainedDeleteAll.text"));
				messageBox.setText(Messages.getString("message.juniorTrainedDeleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();
						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

							Junior junior = itr.next();

							if (junior.getStatus() == 11) {

								if (JuniorsDao.usingJunior(junior.getId())) {
									messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
									messageBox.setMessage(junior.getName() + " " + junior.getSurname() + " " + Messages.getString("message.juniorUsed.text"));
									messageBox.setText(Messages.getString("message.juniorUsed.title"));
									messageBox.open();

								} else {
									itr.remove();
									personsManager.removePersonFromTrash(junior);
								}
							}
						}
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAllJuniorsTrained", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}

					fillJuniorsTable(juniorsTable);
					((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();

				}
			}
		});

		menuItem = new MenuItem(menuPopUpJuniors, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAllFiredJuniors"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.juniorFiredDeleteAll.text"));
				messageBox.setText(Messages.getString("message.juniorFiredDeleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();

						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

							Junior junior = itr.next();

							if (junior.getStatus() == 12) {

								if (JuniorsDao.usingJunior(junior.getId())) {
									messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
									messageBox.setMessage(junior.getName() + " " + junior.getSurname() + " " + Messages.getString("message.juniorUsed.text"));
									messageBox.setText(Messages.getString("message.juniorUsed.title"));
									messageBox.open();

								} else {
									itr.remove();
									personsManager.removePersonFromTrash(junior);
								}

							}

						}

					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAllFiredJuniors", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}

					fillJuniorsTable(juniorsTable);
					((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();

				}
			}
		});

		// added popup menu
		menuPopUpCoachesFired = new Menu(composite.getShell(), SWT.POP_UP);
		menuItem = new MenuItem(menuPopUpCoachesFired, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				try {
					SQLSession.connect();
					for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {

						Coach coach = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(coach);

					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_COACHES_FIRED, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreAllTrainers", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}

				fillCoachesTable(coachesTable);
				((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();

			}
		});

		menuItem = new MenuItem(menuPopUpCoachesFired, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.coachDeleteAll.text"));
				messageBox.setText(Messages.getString("message.coachDeleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();
						for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {

							Coach coach = itr.next();
							itr.remove();
							personsManager.removePersonFromTrash(coach);

						}
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAllTrainers", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}
					fillCoachesTable(coachesTable);
					((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();

				}
			}
		});

		// added popup menu
		menuPopUpPlayersHistory = new Menu(composite.getShell(), SWT.POP_UP);
		menuItem = new MenuItem(menuPopUpPlayersHistory, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				try {
					SQLSession.connect();
					for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {
						Player player = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(player);
					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_PLAYERS_HISTORY, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreAllPlayers", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}
				fillPlayersTable(playersTable);
				((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();

			}
		});

		menuItem = new MenuItem(menuPopUpPlayersHistory, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.playerDeleteAll.text"));
				messageBox.setText(Messages.getString("message.playerDeleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();
						for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {

							Player player = itr.next();
							itr.remove();
							personsManager.restorePersonFromTrash(player);

						}
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->deleteAllPlayers", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}
					fillPlayersTable(playersTable);
					((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();

				}
			}
		});

		// added popup menu
		menuPopUpAll = new Menu(composite.getShell(), SWT.POP_UP);

		menuItem = new MenuItem(menuPopUpAll, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.restoreAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				try {
					SQLSession.connect();
					for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {

						Player player = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(player);

					}

					fillPlayersTable(playersTable);
					((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();

					for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {
						Coach coach = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(coach);

					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_PLAYERS_HISTORY, new Event());

					fillCoachesTable(coachesTable);
					((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();

					for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {
						Junior junior = itr.next();
						itr.remove();
						personsManager.restorePersonFromTrash(junior);
					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_COACHES_FIRED, new Event());
				} catch (SQLException e1) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->restoreAll", e1);
				} finally {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
				}
				fillJuniorsTable(juniorsTable);
				((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();
				ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_FIRED, new Event());
				ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_JUNIORS_TRAINED, new Event());
			}
		});

		menuItem = new MenuItem(menuPopUpAll, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.deleteAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);

				messageBox.setMessage(Messages.getString("message.deleteAll.text"));
				messageBox.setText(Messages.getString("message.deleteAll.title"));
				if (messageBox.open() == SWT.YES) {
					try {
						SQLSession.connect();
						for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {

							Player player = (Player) itr.next();
							itr.remove();
							personsManager.removePersonFromTrash(player);
						}
						fillPlayersTable(playersTable);
						((TabComposite) cTabItemPlayers.getControl()).getDescriptionComposite().clearAll();

						for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {
							Coach coach = itr.next();
							itr.remove();
							personsManager.removePersonFromTrash(coach);
						}

						fillCoachesTable(coachesTable);
						((TabComposite) cTabItemCoaches.getControl()).getDescriptionComposite().clearAll();
						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {
							Junior junior = itr.next();

							if (JuniorsDao.usingJunior(junior.getId())) {
								messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
								messageBox.setMessage(junior.getName() + " " + junior.getSurname() + " " + Messages.getString("message.juniorUsed.text"));
								messageBox.setText(Messages.getString("message.juniorUsed.title"));
								messageBox.open();

							} else {
								itr.remove();
								personsManager.removePersonFromTrash(junior);
							}
						}
						fillJuniorsTable(juniorsTable);
						((TabComposite) cTabItemJuniors.getControl()).getDescriptionComposite().clearAll();
					} catch (SQLException e1) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewTrash->removeAll", e1);
					} finally {
						try {
							SQLSession.close();
						} catch (SQLException e1) {
						}
					}

				}
			}
		});

	}

	private void addViewComposite() {

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		viewFolder = new CTabFolder(composite, SWT.BORDER);
		viewFolder.setLayoutData(formData);
		viewFolder.setLayout(new FormLayout());
		viewFolder.setSimple(false);
		viewFolder.setVisible(true);

		viewFolder.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					currentComposite = (TabComposite) ((CTabItem) event.item).getControl();
					if (((CTabItem) event.item).equals(cTabItemPlayers)) {
						_treeItem.getParent().setSelection(new TreeItem[] { treePlayerItem });
					} else if (((CTabItem) event.item).equals(cTabItemJuniors)) {
						_treeItem.getParent().setSelection(new TreeItem[] { treeJuniorItem });
					} else if (((CTabItem) event.item).equals(cTabItemCoaches)) {
						_treeItem.getParent().setSelection(new TreeItem[] { treeCoachItem });
					}
				}
			}
		});

		addCTabItemPlayers();
		addCTabItemJuniors();
		addCTabItemCoaches();

		viewFolder.setSelection(cTabItemPlayers);
		currentComposite = (TabComposite) cTabItemPlayers.getControl();
	}

	private void fillCoachesTable(Table table) {
		// Turn off drawing to avoid flicker
		table.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		table.remove(0, table.getItemCount() - 1);
		Collections.sort(coaches, coachComparator);
		for (Iterator<Coach> itr = coaches.iterator(); itr.hasNext();) {
			Coach coach = itr.next();
			TableItem item = new TableItem(table, SWT.NONE);

			int c = 0;
			item.setData("person", coach);
			item.setText(c++, coach.getName());
			item.setText(c++, coach.getSurname());
			item.setText(c++, coach.getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(coach.getAge()));
			item.setText(c++, String.valueOf(coach.getGeneralskill()));
			item.setText(c++, String.valueOf(coach.getStamina()));
			item.setText(c++, String.valueOf(coach.getPace()));
			item.setText(c++, String.valueOf(coach.getTechnique()));
			item.setText(c++, String.valueOf(coach.getPassing()));
			item.setText(c++, String.valueOf(coach.getKeepers()));
			item.setText(c++, String.valueOf(coach.getDefenders()));
			item.setText(c++, String.valueOf(coach.getPlaymakers()));
			item.setText(c++, String.valueOf(coach.getScorers()));
		}
		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
		// Turn drawing back on
		table.setRedraw(true);
	}

	private void fillJuniorsTable(Table table) {
		// Turn off drawing to avoid flicker
		table.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries

		table.remove(0, table.getItemCount() - 1);
		Collections.sort(juniors, juniorTrashComparator);
		for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {
			Junior junior = itr.next();
			TableItem item = new TableItem(table, SWT.NONE);

			int c = 0;
			item.setData("person", junior);
			item.setText(c++, junior.getName());
			item.setText(c++, junior.getSurname());
			if (junior.getFormation() >= 0) {
				item.setText(c++, Messages.getString("junior.formation." + junior.getFormation()));
			} else {
				item.setText(c++, "-");
			}
			item.setText(c++, String.valueOf(junior.getSkills()[junior.getSkills().length - 1].getSkill()));
			if (junior.getStatus() == 11) {
				item.setText(c++, Messages.getString("junior.status.trained"));
			} else if (junior.getStatus() == 12) {
				item.setText(c++, Messages.getString("junior.status.fired"));
			}

		}
		// Turn drawing back on
		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
		table.setRedraw(true);
	}

	private void fillPlayersTable(Table table) {
		int maxSkill = 0;
		// Turn off drawing to avoid flicker
		table.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		table.remove(0, table.getItemCount() - 1);
		Collections.sort(players, playerComparator);
		for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {
			Player player = itr.next();
			maxSkill = player.getSkills().length - 1;
			TableItem item = new TableItem(table, SWT.NONE);

			int c = 0;
			item.setData("person", player);
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, String.valueOf(player.getHeight()));
			item.setText(c++, player.getSkills()[maxSkill].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[maxSkill].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getForm()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getStamina()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPace()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getTechnique()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPassing()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getKeeper()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getDefender()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPlaymaker()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getScorer()));
			if (player.getTransferSell() != null) {
				if (player.getTransferSell().getPrice().toInt() > 0) {
					item.setText(c, player.getTransferSell().getPrice().formatIntegerCurrency());
				} else {
					item.setText(c++, Messages.getString("player.fired"));
				}
			} else {
				if (player.getSoldPrice().toInt() > 0) {
					item.setText(c, player.getSoldPrice().formatIntegerCurrency());
				} else {
					item.setText(c++, Messages.getString("player.fired"));
				}
			}
		}
		// Turn drawing back on
		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}
		table.setRedraw(true);
	}

	private void setStatsCoachInfo(Coach coach, DescriptionDoubleComposite description) {

		String[][] values;

		description.clearAll();

		values = new String[7][2];
		values[0][0] = Messages.getString("coach.id");
		values[1][0] = Messages.getString("coach.name");
		values[2][0] = Messages.getString("coach.surname");
		values[3][0] = Messages.getString("coach.general");
		values[4][0] = Messages.getString("coach.age");
		values[5][0] = Messages.getString("coach.country");
		values[6][0] = Messages.getString("coach.salary");

		values[0][1] = String.valueOf(coach.getId());
		values[1][1] = coach.getName();
		values[2][1] = coach.getSurname();
		values[3][1] = Messages.getString("skill.a" + coach.getGeneralskill()) + " [" + coach.getGeneralskill() + "]";
		values[4][1] = String.valueOf(coach.getAge());
		values[5][1] = Messages.getString("country." + coach.getCountryfrom() + ".name");
		values[6][1] = coach.getSalary().formatDoubleCurrencySymbol();

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);
		}

		// statsLeft.setStyleRanges(leftStyle);

		values = new String[10][2];
		values[0][0] = Messages.getString("coach.training");
		values[1][0] = Messages.getString("coach.stamina");
		values[2][0] = Messages.getString("coach.pace");
		values[3][0] = Messages.getString("coach.technique");
		values[4][0] = Messages.getString("coach.passing");
		values[5][0] = Messages.getString("coach.keeper");
		values[6][0] = Messages.getString("coach.defender");
		values[7][0] = Messages.getString("coach.playmaker");
		values[8][0] = Messages.getString("coach.scorer");
		values[9][0] = Messages.getString("coach.general");
		values[0][1] = "";
		values[1][1] = Messages.getString("skill.a" + coach.getStamina()) + " [" + coach.getStamina() + "]";
		values[2][1] = Messages.getString("skill.a" + coach.getPace()) + " [" + coach.getPace() + "]";
		values[3][1] = Messages.getString("skill.a" + coach.getTechnique()) + " [" + coach.getTechnique() + "]";
		values[4][1] = Messages.getString("skill.a" + coach.getPassing()) + " [" + coach.getPassing() + "]";
		values[5][1] = Messages.getString("skill.a" + coach.getKeepers()) + " [" + coach.getKeepers() + "]";
		values[6][1] = Messages.getString("skill.a" + coach.getDefenders()) + " [" + coach.getDefenders() + "]";
		values[7][1] = Messages.getString("skill.a" + coach.getPlaymakers()) + " [" + coach.getPlaymakers() + "]";
		values[8][1] = Messages.getString("skill.a" + coach.getScorers()) + " [" + coach.getScorers() + "]";
		values[9][1] = "[" + coach.getSummarySkill() + "]";

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
	}

	private void setStatsJuniorInfo(Junior junior, DescriptionDoubleComposite description) {

		description.clearAll();

		int maxSkill = junior.getSkills().length - 1;
		description.clearAll();

		String[][] values;

		values = new String[8][2];
		values[0][0] = Messages.getString("junior.id");
		values[1][0] = Messages.getString("junior.name");
		values[2][0] = Messages.getString("junior.surname");
		values[3][0] = Messages.getString("junior.formation");
		values[4][0] = Messages.getString("junior.skill");
		values[5][0] = Messages.getString("junior.weeksAll");
		values[6][0] = Messages.getString("junior.numberOfJumps");
		values[7][0] = Messages.getString("junior.averageJumps");

		values[0][1] = String.valueOf(junior.getId()).toString();
		values[1][1] = junior.getName();
		values[2][1] = junior.getSurname();
		if (junior.getFormation() < 0) {
			values[3][1] = "-";
		} else {
			values[3][1] = Messages.getString("junior.formation." + junior.getFormation());
		}
		values[4][1] = Messages.getString("skill.a" + junior.getSkills()[maxSkill].getSkill()) + " [" + junior.getSkills()[maxSkill].getSkill() + "]";
		values[5][1] = String.valueOf(junior.getSkills()[0].getWeeks()).toString();
		values[6][1] = String.valueOf(junior.getPops()).toString();
		values[7][1] = String.valueOf(new BigDecimal(junior.getAveragePops()).setScale(2, BigDecimal.ROUND_HALF_UP));

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);
		}

	}

	private void setStatsPlayerInfo(Player player, DescriptionDoubleComposite description) {

		description.clearAll();

		int maxSkill = player.getSkills().length;

		int diffrents = 0;
		// int maxSkill = player.getSkills().length;
		int textSize = 0;

		String cards = "\u2588";
		String[][] values;

		values = new String[12][2];
		values[0][0] = Messages.getString("player.id");
		values[1][0] = Messages.getString("player.name");
		values[2][0] = Messages.getString("player.surname");
		values[3][0] = Messages.getString("player.country");
		values[4][0] = Messages.getString("player.value");
		values[5][0] = Messages.getString("player.salary");
		values[6][0] = Messages.getString("player.matches");
		values[7][0] = Messages.getString("player.goals");
		values[8][0] = Messages.getString("player.assists");
		values[9][0] = Messages.getString("player.cards");
		values[10][0] = Messages.getString("player.injurydays");
		values[11][0] = Messages.getString("player.soldPrice");

		textSize = 0;

		textSize = textSize + description.checkLeftFirstTextSize(values[0][0]);

		values[0][1] = String.valueOf(player.getId());

		textSize = textSize + description.checkLeftSecondTextSize(values[0][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[1][0]);

		values[1][1] = player.getName();

		textSize = textSize + description.checkLeftSecondTextSize(values[1][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[2][0]);

		values[2][1] = player.getSurname();

		textSize = textSize + description.checkLeftSecondTextSize(values[2][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[3][0]);

		values[3][1] = Messages.getString("country." + player.getCountryfrom() + ".name");

		textSize = textSize + description.checkLeftSecondTextSize(values[3][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[4][0]);

		values[4][1] = player.getSkills()[maxSkill - 1].getValue().formatDoubleCurrencySymbol();

		textSize = textSize + description.checkLeftSecondTextSize(values[4][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[5][0]);

		values[5][1] = player.getSkills()[maxSkill - 1].getSalary().formatDoubleCurrencySymbol();

		textSize = textSize + description.checkLeftSecondTextSize(values[5][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[6][0]);

		values[6][1] = String.valueOf(player.getSkills()[maxSkill - 1].getMatches());

		textSize = textSize + description.checkLeftSecondTextSize(values[6][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[7][0]);

		values[7][1] = String.valueOf(player.getSkills()[maxSkill - 1].getGoals());

		textSize = textSize + description.checkLeftSecondTextSize(values[7][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[8][0]);

		values[8][1] = String.valueOf(player.getSkills()[maxSkill - 1].getAssists());

		textSize = textSize + description.checkLeftSecondTextSize(values[8][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[9][0]);

		if (player.getSkills()[maxSkill - 1].getCards() == 2) {
			cards += " " + cards;
		} else if (player.getSkills()[maxSkill - 1].getCards() == 0) {
			cards = String.valueOf(player.getSkills()[maxSkill - 1].getCards());
		}

		values[9][1] = cards;

		diffrents = player.getSkills()[maxSkill - 1].getCards();
		if (diffrents < 3 && diffrents > 0) {
			description.leftColorText(textSize, values[9][1].length(), ColorResources.getYellow());
		} else if (diffrents >= 3) {
			description.leftColorText(textSize, values[9][1].length(), ConfigBean.getColorDecreaseDescription());
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[9][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[10][0]);

		double injury = player.getSkills()[maxSkill - 1].getInjurydays();
		if (injury > 7) {
			values[10][1] = new BigDecimal(injury).setScale(0, BigDecimal.ROUND_UP) + " " + Messages.getString("injury.days") + " [" + injury + "]";
		} else if (injury > 0 && injury < 7) {
			values[10][1] = Messages.getString("injury.lastDays") + " [" + injury + "]";
		} else {
			values[10][1] = "[" + injury + "]";
		}

		if (player.getSkills()[maxSkill - 1].getInjurydays() > 0) {
			description.leftColorText(textSize, values[10][1].length(), ConfigBean.getColorInjuryBg(), ConfigBean.getColorInjuryFg());
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[10][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[11][0]);

		if (player.getTransferSell() != null) {
			if (player.getTransferSell().getPrice().toInt() > 0) {
				values[11][1] = player.getTransferSell().getPrice().formatDoubleCurrencySymbol();
			} else {
				values[11][1] = Messages.getString("player.fired");
			}
		} else {
			if (player.getSoldPrice().toInt() > 0) {
				values[11][1] = player.getSoldPrice().formatDoubleCurrencySymbol();
			} else {
				values[11][1] = Messages.getString("player.fired");
			}
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[11][1]);

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);

		}

		description.setLeftColor();

		values = new String[11][2];
		values[0][0] = Messages.getString("player.age");
		values[1][0] = Messages.getString("player.form");
		values[2][0] = Messages.getString("player.stamina");
		values[3][0] = Messages.getString("player.pace");
		values[4][0] = Messages.getString("player.technique");
		values[5][0] = Messages.getString("player.passing");
		values[6][0] = Messages.getString("player.keeper");
		values[7][0] = Messages.getString("player.defender");
		values[8][0] = Messages.getString("player.playmaker");
		values[9][0] = Messages.getString("player.scorer");
		values[10][0] = Messages.getString("player.general");

		values[0][1] = String.valueOf(player.getSkills()[maxSkill - 1].getAge());
		values[0][1] += " [" + player.getSkills()[maxSkill - 1].getAge() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getAge() - player.getSkills()[0].getAge()) + "] ";

		values[1][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getForm());
		values[1][1] += " [" + player.getSkills()[maxSkill - 1].getForm() + "] ";

		values[2][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getStamina());
		values[2][1] += " [" + player.getSkills()[maxSkill - 1].getStamina() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getStamina() - player.getSkills()[0].getStamina()) + "] ";

		values[3][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getPace());
		values[3][1] += " [" + player.getSkills()[maxSkill - 1].getPace() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPace() - player.getSkills()[0].getPace()) + "] ";

		values[4][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getTechnique());
		values[4][1] += " [" + player.getSkills()[maxSkill - 1].getTechnique() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getTechnique() - player.getSkills()[0].getTechnique())
						+ "] ";

		values[5][1] = Messages.getString("skill.c" + player.getSkills()[maxSkill - 1].getPassing());
		values[5][1] += " [" + player.getSkills()[maxSkill - 1].getPassing() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPassing() - player.getSkills()[0].getPassing()) + "] ";

		values[6][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getKeeper());
		values[6][1] += " [" + player.getSkills()[maxSkill - 1].getKeeper() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getKeeper() - player.getSkills()[0].getKeeper()) + "] ";

		values[7][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getDefender());
		values[7][1] += " [" + player.getSkills()[maxSkill - 1].getDefender() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getDefender() - player.getSkills()[0].getDefender()) + "] ";

		values[8][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getPlaymaker());
		values[8][1] += " [" + player.getSkills()[maxSkill - 1].getPlaymaker() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPlaymaker() - player.getSkills()[0].getPlaymaker())
						+ "] ";

		values[9][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getScorer());
		values[9][1] += " [" + player.getSkills()[maxSkill - 1].getScorer() + " "
						+ SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getScorer() - player.getSkills()[0].getScorer()) + "] ";

		values[10][1] = "["
						+ player.getSkills()[maxSkill - 1].getSummarySkill()
						+ " "
						+ SVNumberFormat
							.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getSummarySkill() - player.getSkills()[0].getSummarySkill()) + "] ";

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
		description.setRightColor();
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
