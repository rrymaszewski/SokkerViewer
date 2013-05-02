package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.JuniorsComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.SokkerViewerSettings;
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
import pl.pronux.sokker.ui.widgets.composites.JuniorTrainedDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.JuniorTrainedTable;
import pl.pronux.sokker.ui.widgets.tables.JuniorsTrainedTable;

public class ViewJuniorsTrained implements IPlugin, ISort {

	private PersonsManager personsManager = PersonsManager.instance();
	
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

	private JuniorTrainedDescriptionComposite juniorDesc;

	private List<Junior> juniors;

	private JuniorsTrainedTable juniorsTrainedTable;

	private JuniorTrainedTable juniorTrainedTable;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private Composite previousDesc;

	private Map<Integer, TreeItem> treeItemMap;

	// public ViewJuniorTrained(Composite parent, int style) {
	// super(parent, style);
	// }

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

		universalDescription.setDescriptionStringFormat("%-20s%-15s\r\n");
		universalDescription.setFirstColumnSize(20);
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
						Junior junior = currentJunior;
						try {
							personsManager.movePersonToTrash(junior);
							juniorsTrainedTable.fill(juniors);

							_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });

							setDescriptionComposite(juniors);
							showDescription(descriptionComposite);
							showView(juniorsTrainedTable);

							treeItemMap.get(junior.getId()).dispose();
						} catch (SQLException e) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewJuniorsTrained", e);
						}
					}
					ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_JUNIORS, new Event());
			}
		});

//		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
//		menuItem.setText(langProperties.getProperty("popup.refresh"));
//		menuItem.addListener(SWT.Selection, new Listener() {
//			public void handleEvent(Event e) {
//				fillTable(allJuniorsTable, juniors);
//				fillTree(juniors);
//			}
//		});

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

						showView(juniorsTrainedTable);
						showDescription(descriptionComposite);

						// allJuniorsTable.setRedraw(false);

						SQLSession.connect();

						for (Iterator<Junior> itr = juniors.iterator(); itr.hasNext();) {

							Junior junior = itr.next();
							itr.remove();
							
							personsManager.movePersonToTrash(junior);

							treeItemMap.get(junior.getId()).dispose();
						}

						juniorsTrainedTable.fill(juniors);

						SQLSession.close();

						// allJuniorsTable.setRedraw(true);

						_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });

						setDescriptionComposite(juniors);
						showDescription(descriptionComposite);
						showView(juniorsTrainedTable);

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_JUNIORS, new Event());

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));
					}

				} catch (SQLException e) {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewJuniorsTrained", e);
				}
			}
		});

	}

	private void addViewComposite() {
		juniorsTrainedTable = new JuniorsTrainedTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		juniorsTrainedTable.setLayoutData(viewFormData);

		juniorsTrainedTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event != null) {
					Junior junior = (Junior) event.item.getData(Junior.class.getName());
					juniorDesc.setStatsJuniorInfo(junior);
					showDescription(juniorDesc);
//					setCbData(juniorDesc);
				}
			}
		});

		juniorsTrainedTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = juniorsTrainedTable.getItem(pt);
					if (item != null) {
						Junior junior = (Junior) item.getData(Junior.class.getName());
						currentJunior = junior;
						juniorsTrainedTable.setMenu(menuPopUp);
						juniorsTrainedTable.getMenu().setVisible(true);
					} else {
						juniorsTrainedTable.setMenu(menuClear);
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

				if(k == JuniorTrainedTable.SKILL) {
					graphComposite.fillGraph(tempIntTable, tempDateTable,Calendar.THURSDAY,true, 18);
				} else {
					graphComposite.fillGraph(tempIntTable, tempDateTable,Calendar.THURSDAY,true, -1);
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
					Junior junior = ((Junior) item.getData(Junior.class.getName()));

					juniorDesc.setStatsJuniorInfo(junior);
					juniorTrainedTable.fill(junior);

					showDescription(juniorDesc);
					showView(juniorTrainedTable);

					if (event.button == 3) {
						currentJunior = junior;
//						setCbData(juniorDesc);

						_treeItem.getParent().setMenu(menuPopUp);
						// _treeItem.getParent().getMenu()._setVisible(true);
						_treeItem.getParent().getMenu().setVisible(true);
					}

				} else if (item != null && item.equals(_treeItem)) {
					showDescription(descriptionComposite);
					showView(juniorsTrainedTable);
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
						Junior junior = (Junior) item.getData(Junior.class.getName());
						juniorDesc.setStatsJuniorInfo(junior, 0);
						juniorTrainedTable.fill(junior);

						showDescription(juniorDesc);
						showView(juniorTrainedTable);
					}

					if (item != null && item.getParentItem() != null) {
						Junior junior = (Junior) item.getData(Junior.class.getName());
						juniorDesc.setStatsJuniorInfo(junior, 0);
						juniorTrainedTable.fill(junior);

						showDescription(juniorDesc);
						showView(juniorTrainedTable);
					}

				} else if (item != null && item.equals(_treeItem)) {
					showDescription(descriptionComposite);
					showView(juniorsTrainedTable);
				}
			}
		};
	}

	public void clear() {
		descriptionComposite.clearAll();
		juniorsTrainedTable.removeAll();
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

		Collections.sort(juniors, comparator);

		for (int i = 0; i < junior.size(); i++) {
			TreeItem item = new TreeItem(_treeItem, SWT.NONE);
			item.setData(Junior.class.getName(), junior.get(i));
			item.setText(junior.get(i).getSurname() + " " + junior.get(i).getName());
			item.setImage(FlagsResources.getFlag(Cache.getClub().getCountry()));

			treeItemMap.put(junior.get(i).getId(), item);
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
		return Messages.getString("progressBar.info.setInfoJuniorTrained");
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

		addPopupMenu();
		addPopupMenuParentTree();

		juniors = new ArrayList<Junior>();

		treeItemMap = new HashMap<Integer, TreeItem>();
		new HashMap<Integer, TableItem>();

		addViewListener();

		addViewComposite();
		addDescriptionComposite();

		currentDesc = descriptionComposite;
		currentView = juniorsTrainedTable;
	}

	public void reload() {
		// TODO Auto-generated method stub

	}

	//FIXME
//	private void setCbData(DescriptionSingleComposite juniorDesc) {
//		// TODO do poprawy !!!!!!!!
//		cbData = juniorDesc.getText();
//	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;

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
			values[0][1] = Messages.getString("skill.a" + BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(0, BigDecimal.ROUND_HALF_UP)) + " (" + BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")";
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

	private void setJuniorsTrainedTable(List<Junior> junior) {

		juniorsTrainedTable.fill(juniors);

		juniorsTrainedTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = juniorsTrainedTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = juniorsTrainedTable.getTopIndex();

				while (index < juniorsTrainedTable.getItemCount()) {
					boolean visible = false;
					TableItem item = juniorsTrainedTable.getItem(index);
					for (int i = 0; i < juniorsTrainedTable.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							// int currentIndex =
							// treeItem.getParent().indexOf(treeItem.getParent().getSelection()[0]);
							// TreeItem[] items =
							// {treeItem.getParent().getItem(currentIndex).getItem(((Integer)item.getData("id")).intValue())};
							// treeItem.getParent().setSelection(items);
							Junior junior = (Junior) item.getData(Junior.class.getName());
							_treeItem.getParent().setSelection(new TreeItem[] { (TreeItem) treeItemMap.get(junior.getId()) });
							
							juniorDesc.setStatsJuniorInfo(junior, 0);
							juniorTrainedTable.fill(junior);

							showDescription(juniorDesc);
							showView(juniorTrainedTable);

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

		values[0][1] = String.valueOf(junior.getId()).toString();
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
		_treeItem.setText(Messages.getString("tree.ViewJuniorsTrained"));
		_treeItem.setImage(ImageResources.getImageResources("trained_junior.png"));
	}

	public void set() {

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_JUNIORS_TRAINED, new Listener() {
			public void handleEvent(Event arg0) {
				setDescriptionComposite(juniors);
				juniorsTrainedTable.fill(juniors);
				fillTree(juniors);
			}
		});

		juniors = Cache.getJuniorsTrained();
		setViewComposite(juniors);
		setDescriptionComposite(juniors);
		fillTree(juniors);

		juniorDesc = new JuniorTrainedDescriptionComposite (composite, SWT.BORDER);
		juniorDesc.setLayoutData(descriptionFormData);
		juniorDesc.setVisible(false);

		juniorDesc.setFont(ConfigBean.getFontDescription());

		juniorTrainedTable = new JuniorTrainedTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		juniorTrainedTable.setLayoutData(viewFormData);

		juniorTrainedTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });
					showView(juniorsTrainedTable);
					showDescription(descriptionComposite);
				}
			}
		});

		juniorTrainedTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentView.getItem(point);
					if (item != null) {
						if (currentDesc instanceof ChartDateComposite) {
							((ChartDateComposite) currentDesc).setMarkers((Date)item.getData("date"), Calendar.THURSDAY, Integer.valueOf(item.getText(1)));
						} else if (currentDesc instanceof DescriptionSingleComposite) {
							Junior junior = (Junior) item.getParent().getData(Junior.class.getName());
							int index = item.getParent().indexOf(item);
							setStatsJuniorInfo(junior, universalDescription, index);
							showDescription(universalDescription);
						}
					}
				} else if (event.button == 3) {
					showDescription(previousDesc);
				}
			}

		});

		juniorTrainedTable.getColumn(JuniorTrainedTable.SKILL).addListener(SWT.Selection, graphList);


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

	private void setViewComposite(List<Junior> junior) {
		setJuniorsTrainedTable(junior);
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
