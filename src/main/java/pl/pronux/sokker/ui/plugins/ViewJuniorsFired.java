package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
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
import pl.pronux.sokker.comparators.JuniorsComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorHistoryTable;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.CursorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.ChartDateComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.JuniorTrainedTable;
import pl.pronux.sokker.ui.widgets.tables.JuniorsFiredTable;

public class ViewJuniorsFired implements IPlugin, ISort {

	private TreeItem _treeItem;

	private Clipboard cb;

	private String cbData;

	private Composite composite;

	private Composite currentDesc;

	private Junior currentJunior;

	private Table currentView;

	private DescriptionSingleComposite descriptionComposite;

	private FormData descriptionFormData;

	private ChartDateComposite graphComposite;

	private Listener graphList;

	private DescriptionSingleComposite juniorDesc;

	private List<Junior> juniors;

	private JuniorsFiredTable juniorsTable;

	private JuniorTrainedTable juniorTable;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private Composite previousDesc;

	// public ViewJuniorTrained(Composite parent, int style) {
	// super(parent, style);
	// }

	private HashMap<Integer, TreeItem> treeItemMap;

	private DescriptionSingleComposite universalDescription;

	private FormData viewFormData;

	private Listener viewKeyListener;

	private Listener viewListener;

	private void addDescriptionComposite() {
		descriptionComposite = new DescriptionSingleComposite(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);

		// descriptionComposite.setDescriptionStringFormat(40, 15);
		descriptionComposite.setDescriptionStringFormat("%-40s%-15s\r\n");
		descriptionComposite.setFirstColumnSize(40);
		descriptionComposite.setSecondColumnSize(15);

		descriptionComposite.setFont(ConfigBean.getFontDescription());

		graphComposite = new ChartDateComposite(composite, SWT.BORDER);
		graphComposite.setLayoutData(descriptionFormData);
		graphComposite.setVisible(false);

		universalDescription = new DescriptionSingleComposite(composite, SWT.BORDER);
		universalDescription.setLayoutData(descriptionFormData);
		universalDescription.setVisible(false);

		universalDescription.setDescriptionStringFormat("%-25s%-15s\r\n");
		universalDescription.setFirstColumnSize(25);
		universalDescription.setSecondColumnSize(15);

		universalDescription.setFont(ConfigBean.getFontDescription());

	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard"));
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { cbData }, new Transfer[] { textTransfer });
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.move"));
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setMessage(Messages.getString("message.juniorMove.text"));
				messageBox.setText(Messages.getString("message.juniorMove.title"));

				if (messageBox.open() == SWT.YES) {
					// Player player = (Player)currentItem.getData(Player.IDENTIFIER);
					Junior junior = currentJunior;
					// SqlQuery.dropPlayer(junior.getId());
					try {
						PersonsManager.movePersonToTrash(junior);
						juniorsTable.fill(juniors);

						_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });

						setDescriptionComposite(juniors);
						showDescription(descriptionComposite);
						showView(juniorsTable);

						treeItemMap.get(junior.getId()).dispose();

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_JUNIORS, new Event());
					} catch (SQLException e) {
						new BugReporter(composite.getDisplay()).openErrorMessage("ViewJuniorsFired", e);
					}
				}
			}
		});

		menuClear = new Menu(composite.getShell(), SWT.POP_UP);

	}

	private void addPopupMenuParentTree() {
		// added popup menu
		menuPopUpParentTree = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard"));
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { cbData }, new Transfer[] { textTransfer });
			}
		});

		menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.moveAll"));
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					MessageBox messageBox = new MessageBox(composite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					messageBox.setMessage(Messages.getString("message.juniorMoveAll.text"));
					messageBox.setText(Messages.getString("message.juniorMoveAll.title"));

					if (messageBox.open() == SWT.YES) {

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));

						showView(juniorsTable);
						showDescription(descriptionComposite);

						// allJuniorsTable.setRedraw(false);

						SQLSession.connect();

						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

							Junior junior = itr.next();
							itr.remove();

							PersonsManager.movePersonToTrash(junior);

							treeItemMap.get(junior.getId()).dispose();
						}

						juniorsTable.fill(juniors);

						SQLSession.close();
						
						// allJuniorsTable.setRedraw(true);

						_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });

						setDescriptionComposite(juniors);
						showDescription(descriptionComposite);
						showView(juniorsTable);

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_JUNIORS, new Event());

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));
					}

				} catch (SQLException e) {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewJuniorsFired", e);
				}
			}
		});

	}

	private void addViewComposite() {
		juniorsTable = new JuniorsFiredTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		juniorsTable.setLayoutData(viewFormData);
		juniorsTable.setVisible(true);

		juniorsTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					Junior junior = (Junior) event.item.getData(Junior.IDENTIFIER);
					setStatsJuniorInfo(junior, juniorDesc, 0);
					showDescription(juniorDesc);
					setCbData(juniorDesc);
				}
			}

		});

		juniorsTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = juniorsTable.getItem(pt);
					if (item != null) {
						Junior junior = (Junior) item.getData(Junior.IDENTIFIER);
						currentJunior = junior;
						juniorsTable.setMenu(menuPopUp);
						juniorsTable.getMenu().setVisible(true);
					} else {
						juniorsTable.setMenu(menuClear);
						showDescription(descriptionComposite);
					}
				}
			}
		});

	}

	private void addViewListener() {

		graphList = new Listener() {
			public void handleEvent(Event event) {

				TableColumn tempColumn = (TableColumn) event.widget;
				Table tempTable = (tempColumn).getParent();

				int[] tempIntTable = new int[tempTable.getItemCount()];
				String[] tempDateTable = new String[tempTable.getItemCount()];

				// sprawdzamy czy tabela zawiera elementy
				int k = tempTable.indexOf(tempColumn);
				for (int x = 0; x < tempTable.getItemCount(); x++) {
					tempIntTable[x] = Integer.valueOf(tempTable.getItem(x).getText(k)).intValue();
					tempDateTable[x] = tempTable.getItem(x).getText(0);
				}

				graphComposite.setColumn(k);

				if (k == JuniorHistoryTable.SKILL) {
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, 18);
				} else {
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, -1);
				}
				// graphComposite.setGraph(tempIntTable, 17);

				showDescription(graphComposite);
			}
		};

		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);

				if (item != null && item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {
					Junior junior = ((Junior) item.getData(Junior.IDENTIFIER));
					juniorTable.fill(junior);
					setStatsJuniorInfo(junior, juniorDesc, 0);
					showDescription(juniorDesc);
					showView(juniorTable);

					if (event.button == 3) {
						currentJunior = junior;
						setCbData(juniorDesc);

						_treeItem.getParent().setMenu(menuPopUp);
						// _treeItem.getParent().getMenu()._setVisible(true);
						_treeItem.getParent().getMenu().setVisible(true);
					}

				} else if (item != null && item.equals(_treeItem)) {
					showDescription(descriptionComposite);
					showView(juniorsTable);
				}
			}
		};

		viewKeyListener = new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < _treeItem.getParent().getSelection().length; i++) {
					item = _treeItem.getParent().getSelection()[i];
				}

				if (item != null && item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {

					if (item != null && item.getParentItem() == null) {
						Junior junior = (Junior) item.getData(Junior.IDENTIFIER);
						juniorTable.fill(junior);
						setStatsJuniorInfo(junior, juniorDesc, 0);
						showDescription(juniorDesc);
						showView(juniorTable);
					}

					if (item != null && item.getParentItem() != null) {
						Junior junior = (Junior) item.getData(Junior.IDENTIFIER);
						juniorTable.fill(junior);
						setStatsJuniorInfo(junior, juniorDesc, 0);
						showDescription(juniorDesc);
						showView(juniorTable);
					}

				} else if (item != null && item.equals(_treeItem)) {
					showDescription(descriptionComposite);
					showView(juniorsTable);
				}
			}
		};
	}

	public void clear() {
		descriptionComposite.clearAll();
		juniorsTable.removeAll();
		_treeItem.removeAll();
		juniors.clear();
		// List the entries using entrySet()
		treeItemMap.clear();
	}

	public void dispose() {

	}

	private void fillTree(List<Junior> junior) {

		_treeItem.removeAll();

		JuniorsComparator comparator = new JuniorsComparator();
		comparator.setColumn(JuniorsComparator.SURNAME);
		comparator.setDirection(JuniorsComparator.ASCENDING);

		Collections.sort(junior, comparator);

		_treeItem.getParent().setRedraw(false);
		for (int i = 0; i < junior.size(); i++) {
			TreeItem item = new TreeItem(_treeItem, SWT.NONE);
			item.setText(junior.get(i).getSurname() + " " + junior.get(i).getName());
			item.setImage(FlagsResources.getFlag(Cache.getClub().getCountry()));

			item.setData(Junior.IDENTIFIER, junior.get(i));

			treeItemMap.put(junior.get(i).getId(), item);
		}
		_treeItem.getParent().setRedraw(true);
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
		return Messages.getString("progressBar.info.setInfoJuniorFired");
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public void init(Composite composite) {
		this.composite = new ViewComposite(composite.getParent(), composite.getStyle());
		viewFormData = ((ViewComposite) this.composite).getViewFormData();
		descriptionFormData = ((ViewComposite) this.composite).getDescriptionFormData();
		composite.dispose();
		cb = ViewerHandler.getClipboard();

		addPopupMenuParentTree();

		addPopupMenu();

		juniors = new ArrayList<Junior>();

		treeItemMap = new HashMap<Integer, TreeItem>();

		addViewListener();

		addViewComposite();
		addDescriptionComposite();

		currentDesc = descriptionComposite;
		currentView = juniorsTable;
	}

	public void reload() {
		// TODO Auto-generated method stub

	}

	public void set() {
		ViewerHandler.getViewer().addListener(IEvents.REFRESH_JUNIORS_FIRED, new Listener() {
			public void handleEvent(Event arg0) {
				setDescriptionComposite(juniors);
				juniorsTable.fill(juniors);
				fillTree(juniors);
			}
		});

		juniors = Cache.getJuniorsFired();

		setViewComposite(juniors);

		setDescriptionComposite(juniors);

		fillTree(juniors);

		juniorDesc = new DescriptionSingleComposite(composite, SWT.BORDER);
		juniorDesc.setLayoutData(descriptionFormData);
		juniorDesc.setVisible(false);

		juniorDesc.setDescriptionStringFormat("%-30s%-15s\r\n");
		juniorDesc.setFirstColumnSize(30);
		juniorDesc.setSecondColumnSize(15);

		juniorDesc.setFont(ConfigBean.getFontDescription());

		juniorTable = new JuniorTrainedTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		juniorTable.setLayoutData(viewFormData);
		juniorTable.setVisible(false);

		juniorTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });
					showView(juniorsTable);
					showDescription(descriptionComposite);
				}
			}
		});

		juniorTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentView.getItem(point);
					if (item != null) {
						if (currentDesc instanceof ChartDateComposite) {
							((ChartDateComposite) currentDesc)
									.setMarkers((Date) item.getData("date"), Calendar.THURSDAY, Integer.valueOf(item.getText(((ChartDateComposite) currentDesc).getColumn())));
						} else if (currentDesc instanceof DescriptionSingleComposite) {
							int index = item.getParent().indexOf(item);
							Junior junior = (Junior) item.getParent().getData(Junior.IDENTIFIER);
							setStatsJuniorInfo(junior, universalDescription, index);
							showDescription(universalDescription);
						}
					}
				} else if (event.button == 3) {
					showDescription(previousDesc);
				}
			}

		});

		juniorTable.getColumn(JuniorHistoryTable.SKILL).addListener(SWT.Selection, graphList);

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);

				if (item != null) {
					if (item.equals(_treeItem) && event.button == 3) {

						cbData = descriptionComposite.getText();

						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}

		});
		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);
		_treeItem.getParent().addListener(SWT.KeyUp, viewKeyListener);

		composite.layout(true);
	}

	private void setCbData(DescriptionSingleComposite juniorDesc) {
		// TODO do poprawy dodac nowe okno z raportem i przyciskiem kopiuj do
		// schowka!!!!!!!!
		cbData = juniorDesc.getText();
	}

	private void setDescriptionComposite(List<Junior> juniors) {

		int maxSkill = 0;
		double averJuniorSkill = 0;
		String[][] values;

		descriptionComposite.setText("");

		for (int i = 0; i < juniors.size(); i++) {
			maxSkill = juniors.get(i).getSkills().length - 1;
			averJuniorSkill += juniors.get(i).getSkills()[maxSkill].getSkill();
		}
		values = new String[2][2];

		if (juniors.size() != 0) {
			values[0][0] = Messages.getString("junior.averageSkill");
			values[0][1] = Messages.getString("skill.a" + BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(0, BigDecimal.ROUND_HALF_UP)) + " ("
					+ BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")";
		} else {
			values[0][0] = Messages.getString("junior.averageSkill");
			values[0][1] = "0";
		}

		values[1][0] = Messages.getString("junior.allJuniors");
		values[1][1] = String.valueOf(juniors.size()).toString();

		for (int i = 0; i < values.length; i++) {
			descriptionComposite.addText(values[i]);
		}
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;

	}

	private void setStatsJuniorInfo(Junior junior, DescriptionSingleComposite description, int index) {

		int maxSkill = junior.getSkills().length - 1 - index;

		description.clearAll();

		String[][] values;

		values = new String[7][2];
		values[0][0] = Messages.getString("junior.id");
		values[1][0] = Messages.getString("junior.name");
		values[2][0] = Messages.getString("junior.surname");
		values[3][0] = Messages.getString("junior.skill");
		values[4][0] = Messages.getString("junior.weeksAll");
		values[5][0] = Messages.getString("junior.numberOfJumps");
		values[6][0] = Messages.getString("junior.averageJumps");

		values[0][1] = String.valueOf(junior.getId());
		values[1][1] = junior.getName();
		values[2][1] = junior.getSurname();
		values[3][1] = Messages.getString("skill.a" + junior.getSkills()[maxSkill].getSkill()) + " [" + junior.getSkills()[maxSkill].getSkill() + "]";
		values[4][1] = String.valueOf(junior.getSkills()[0].getWeeks()).toString();
		values[5][1] = String.valueOf(junior.getPops()).toString();
		values[6][1] = String.valueOf(new BigDecimal(junior.getAveragePops()).setScale(2, BigDecimal.ROUND_HALF_UP));

		for (int i = 0; i < values.length; i++) {
			description.addText(values[i]);
		}

	}

	public void setSvBean(SvBean svBean) {

	}

	public void setTreeItem(TreeItem treeItem) {
		this._treeItem = treeItem;
		_treeItem.setText(Messages.getString("tree.ViewJuniorsFired"));
		_treeItem.setImage(ImageResources.getImageResources("sacked_junior.png"));
	}

	private void setViewComposite(List<Junior> junior) {
		juniorsTable.fill(juniors);

		// for (int i = 0; i < junior.size(); i++) {
		// juniors.add(junior.get(i));
		// fillTable(allJuniorsTable,juniors);
		// }

		juniorsTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = juniorsTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = juniorsTable.getTopIndex();

				while (index < juniorsTable.getItemCount()) {
					boolean visible = false;
					TableItem item = juniorsTable.getItem(index);
					for (int i = 0; i < juniorsTable.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							// int currentIndex =
							// treeItem.getParent().indexOf(treeItem.getParent().getSelection()[0]);
							// TreeItem[] items =
							// {treeItem.getParent().getItem(currentIndex).getItem(((Integer)item.getData("id")).intValue())};
							// treeItem.getParent().setSelection(items);
							Junior junior = (Junior) item.getData(Junior.IDENTIFIER);
							_treeItem.getParent().setSelection(new TreeItem[] { (TreeItem) treeItemMap.get(junior.getId()) });
							
							juniorTable.fill(junior);
							setStatsJuniorInfo(junior, juniorDesc, 0);
							showDescription(juniorDesc);
							showView(juniorTable);
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible) {
						return;
					}
					index++;
				}
			}
		});
	}

	private void showDescription(Composite composite) {
		if (!(currentDesc instanceof ChartDateComposite)) {
			previousDesc = currentDesc;
		}
		currentDesc.setVisible(false);
		currentDesc = composite;
		currentDesc.setVisible(true);
	}

	private void showView(Table table) {
		currentView.setVisible(false);
		currentView = table;
		currentView.setVisible(true);
	}

}
