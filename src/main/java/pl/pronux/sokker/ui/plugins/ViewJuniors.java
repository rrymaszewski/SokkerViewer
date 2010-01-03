package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.JuniorsComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.JuniorHistoryTable;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.ChartDateComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.JuniorDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.composites.views.JuniorChartsComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.shells.NoteShell;
import pl.pronux.sokker.ui.widgets.tables.JuniorTable;
import pl.pronux.sokker.ui.widgets.tables.JuniorsTable;

public class ViewJuniors implements IPlugin, ISort {

	private class Configure implements IViewConfigure {

		private ConfigurationManager configurationManager = ConfigurationManager.instance();
		
		private Composite composite;

		private Spinner spinner;

		private TreeItem treeItem;
		
		public void applyChanges() {
			if (SQLSession.getConnection() != null) {
				try {
					configurationManager.setJuniorMinimumPop(spinner.getSelection() / 10.0);
					Junior.minimumPop = spinner.getSelection() / 10.0;
					for (int i = 0; i < juniors.size(); i++) {
						juniors.get(i).reload();
					}
					juniorsTable.fill(juniors);
				} catch (SQLException e) {
					new BugReporter(composite.getShell()).openErrorMessage("Junior settings -> mimimum pop", e); //$NON-NLS-1
				}
			}
		}

		public void clear() {
			// TODO Auto-generated method stub

		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public Composite getComposite() {
			return this.composite;
		}

		public TreeItem getTreeItem() {
			return this.treeItem;
		}

		public void init(Composite composite) {
			this.composite = composite;
			this.composite.setLayout(new FormLayout());

			FormData formData;
			formData = new FormData();
			formData.left = new FormAttachment(0, 5);
			formData.top = new FormAttachment(0, 5);

			Label label = new Label(composite, SWT.NONE);
			label.setText(Messages.getString("junior.pop.average.min")); //$NON-NLS-1$
			label.setFont(ConfigBean.getFontMain());
			label.setLayoutData(formData);

			formData = new FormData();
			formData.left = new FormAttachment(label, 30);
			formData.top = new FormAttachment(0, 5);

			spinner = new Spinner(composite, SWT.READ_ONLY | SWT.BORDER);
			spinner.setValues(45, 30, 100, 1, 1, 1);
			spinner.setBackground(ColorResources.getWhite());
			spinner.setLayoutData(formData);
		}

		public void restoreDefaultChanges() {
			this.spinner.setSelection(Double.valueOf(Junior.minimumPop * 10).intValue());

		}

		public void set() {
			this.spinner.setSelection(Double.valueOf(Junior.minimumPop * 10).intValue());
		}

		public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
			// TODO Auto-generated method stub

		}

		public void setTreeItem(TreeItem treeItem) {
			this.treeItem = treeItem;
			this.treeItem.setText(Messages.getString("tree.ViewJuniors")); //$NON-NLS-1$

		}
	}

	private TreeItem _treeItem;

	private Clipboard cb;

	private String cbData;

	protected Combo comboFilter;

	private Combo comboGraph;

	private Composite composite;

	private Composite currentComposite;

	private Composite currentDesc;

	private Table currentView;

	private DescriptionSingleComposite descriptionComposite;

	private FormData descriptionFormData;

	private Composite detailStatusComposite;

	private Composite generalStatusComposite;

	private ChartDateComposite graphComposite;

	private Listener graphList;

	private JuniorChartsComposite graphsComposite;

	private HashMap<Integer, TreeItem> itemMap;

	private JuniorDescriptionComposite juniorDesc;

	private List<Junior> juniors;

	private int juniorsMax;

	private JuniorsTable juniorsTable;

	private JuniorTable juniorView;

	private CLabel labelGraph;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private Listener mouseListener;

	private Composite previousDesc;

	private Composite toolBarComposite;

	private JuniorDescriptionComposite universalComposite;

	private ViewComposite vComposite;

	private FormData viewFormData;

	private void addDescriptionComposite() {
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);

		// descriptionComposite.setDescriptionStringFormat(40, 15);
		descriptionComposite.setDescriptionStringFormat("%-40s%-15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setFirstColumnSize(40);
		descriptionComposite.setSecondColumnSize(15);

		descriptionComposite.setFont(ConfigBean.getFontDescription());

		graphComposite = new ChartDateComposite(vComposite, SWT.BORDER);
		graphComposite.setLayoutData(descriptionFormData);
		graphComposite.setVisible(false);

		universalComposite = new JuniorDescriptionComposite(vComposite, SWT.BORDER);
		universalComposite.setLayoutData(descriptionFormData);
		universalComposite.setVisible(false);
	}

	private void addListeners() {
		mouseListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);
				if (item != null) {
					if (item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {
						Junior junior = (Junior) item.getData(Junior.class.getName());

						generalStatusComposite.setVisible(false);
						detailStatusComposite.setVisible(true);

						comboGraph.select(0);

						juniorDesc.setStatsJuniorInfo(junior);
						juniorView.fill(junior);

						showView(juniorView);
						showDescription(juniorDesc);

						if (event.button == 3) {

							// setCbData(juniorDesc);
							menuPopUp.setData("item", item); //$NON-NLS-1$
							_treeItem.getParent().setMenu(menuPopUp);
							// _treeItem.getParent().getMenu()._setVisible(true);
							_treeItem.getParent().getMenu().setVisible(true);
						}

						showMainView(vComposite);

					} else if (item.equals(_treeItem)) {

						generalStatusComposite.setVisible(true);
						detailStatusComposite.setVisible(false);

						showDescription(descriptionComposite);
						showView(juniorsTable);

						showMainView(vComposite);
						if (item.equals(_treeItem) && event.button == 3) {

							cbData = descriptionComposite.getText();

							_treeItem.getParent().setMenu(menuPopUpParentTree);
							_treeItem.getParent().getMenu().setVisible(true);

							showMainView(vComposite);
						}
					} else if (item.getData("juniorCharts") != null) { //$NON-NLS-1$

						if (item.getParentItem().getParentItem().equals(_treeItem)) {
							if (item.getData("juniorCharts") instanceof Junior) { //$NON-NLS-1$
								Junior junior = (Junior) item.getData("juniorCharts"); //$NON-NLS-1$
								graphsComposite.fill(junior);
								showMainView(graphsComposite);
							}
						}
					}
				}

			}
		};
	}

	private void addPopupMenu() {
		// added popup menu

		menuPopUp = new Menu(vComposite.getShell(), SWT.POP_UP);
		MenuItem menuItem;

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.note.open")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) { //$NON-NLS-1$
					Item item = (Item) menuPopUp.getData("item"); //$NON-NLS-1$
					if (item.getData(Junior.class.getName()) != null) {
						openNote(item);
					}
				}
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				if (cbData != null) {
					cb.setContents(new Object[] {
						cbData
					}, new Transfer[] {
						textTransfer
					});
				}
			}
		});

		menuClear = new Menu(vComposite.getShell(), SWT.POP_UP);

	}

	private void addPopupMenuParentTree() {
		// added popup menu
		menuPopUpParentTree = new Menu(vComposite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] {
					cbData
				}, new Transfer[] {
					textTransfer
				});
			}
		});
	}

	protected void addToolBar() {

		toolBarComposite = new Composite(vComposite, SWT.BORDER);

		FormData toolBarFormData = new FormData();
		toolBarFormData.top = new FormAttachment(100, -35);
		toolBarFormData.left = new FormAttachment(0, 0);
		toolBarFormData.right = new FormAttachment(100, 0);
		toolBarFormData.bottom = new FormAttachment(100, 0);

		toolBarComposite.setLayoutData(toolBarFormData);

		toolBarComposite.setLayout(new FormLayout());

		viewFormData.bottom = new FormAttachment(toolBarComposite, 0);

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		generalStatusComposite = new Composite(toolBarComposite, SWT.NONE);
		generalStatusComposite.setLayoutData(formData);
		generalStatusComposite.setLayout(new FormLayout());
		generalStatusComposite.setVisible(true);

		detailStatusComposite = new Composite(toolBarComposite, SWT.NONE);
		detailStatusComposite.setLayoutData(formData);
		detailStatusComposite.setLayout(new FormLayout());
		detailStatusComposite.setVisible(false);

		comboFilter = new Combo(generalStatusComposite, SWT.READ_ONLY);

		comboFilter.add(Messages.getString("view.all")); //$NON-NLS-1$
		comboFilter.add(Messages.getString("view.jumps")); //$NON-NLS-1$
		comboFilter.setText(Messages.getString("view.all")); //$NON-NLS-1$
		comboFilter.setFont(ConfigBean.getFontMain());

		formData = new FormData(100, 25);
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);

		comboFilter.setLayoutData(formData);

		comboFilter.addListener(SWT.Selection, comboFilterListener(juniorsTable));

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);

		labelGraph = new CLabel(detailStatusComposite, SWT.NONE);
		labelGraph.setText(Messages.getString("canvasShell.graph")); //$NON-NLS-1$
		labelGraph.setLayoutData(formData);
		labelGraph.setFont(ConfigBean.getFontMain());
		labelGraph.pack();

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(labelGraph, 5);

		comboGraph = new Combo(detailStatusComposite, SWT.READ_ONLY);
		comboGraph.add(Messages.getString("view.description")); //$NON-NLS-1$
		comboGraph.add(Messages.getString("view.skill")); //$NON-NLS-1$
		comboGraph.setText(Messages.getString("view.description")); //$NON-NLS-1$
		comboGraph.setFont(ConfigBean.getFontMain());

		comboGraph.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (comboGraph.getSelectionIndex() == 0) {
					showDescription(juniorDesc);
				} else {
					int[] tempIntTable = new int[currentView.getItemCount()];
					String[] tempDateTable = new String[currentView.getItemCount()];

					// sprawdzamy czy tabela zawiera elementy
					int k = JuniorHistoryTable.SKILL;
					for (int x = 0; x < juniorView.getItemCount(); x++) {
						tempIntTable[x] = Integer.parseInt(juniorView.getItem(x).getText(k));
						tempDateTable[x] = juniorView.getItem(x).getText(0);
					}

					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, 18);

					showDescription(graphComposite);
				}

			}

		});

		comboGraph.setLayoutData(formData);
		comboGraph.pack();

		// comboGraph.addListener(SWT.Selection, );

	}

	private void addViewComposite() {

		final TableColumn[] columns = juniorsTable.getColumns();

		for (int i = 0; i < columns.length - 1; i++) {

			columns[i].addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					int column = juniorsTable.indexOf((TableColumn) event.widget);
					JuniorsComparator comparator = juniorsTable.getComparator();
					if (column != comparator.getColumn()) {
						if (comparator.getDirection() == 0) {
							juniorsTable.getColumn(comparator.getColumn()).setText(juniorsTable.getColumn(comparator.getColumn()).getText().replaceAll(ARROW_UP, "")); //$NON-NLS-1$
						} else {
							juniorsTable.getColumn(comparator.getColumn()).setText(juniorsTable.getColumn(comparator.getColumn()).getText().replaceAll(ARROW_DOWN, "")); //$NON-NLS-1$
						}
						comparator.setDirection(0);
						juniorsTable.getColumn(column).setText(juniorsTable.getColumn(column).getText() + ARROW_UP);
					} else {
						if (comparator.getDirection() == 0) {
							juniorsTable.getColumn(comparator.getColumn()).setText(juniorsTable.getColumn(comparator.getColumn()).getText().replaceAll(ARROW_UP, ARROW_DOWN));
						} else {
							juniorsTable.getColumn(comparator.getColumn()).setText(juniorsTable.getColumn(comparator.getColumn()).getText().replaceAll(ARROW_DOWN, ARROW_UP));
						}
						comparator.reverseDirection();
					}

					comparator.setColumn(column);

					juniorsTable.setRedraw(false);

					juniorsTable.fill(juniors);

					filterTable(juniorsTable);

					juniorsTable.setRedraw(true);

				}
			});
		}

		juniorsTable.setLayoutData(viewFormData);
		juniorsTable.setVisible(true);
	}

	public void clear() {
		descriptionComposite.clearAll();
		juniorsTable.removeAll();
		_treeItem.removeAll();
		juniors.clear();
		// List the entries using entrySet()
		itemMap.clear();
	}

	protected Listener comboFilterListener(final JuniorsTable table) {
		return new Listener() {
			public void handleEvent(Event event) {
				String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
				if (text.equalsIgnoreCase(Messages.getString("view.all"))) { //$NON-NLS-1$
					table.setRedraw(false);
					table.clearAll();
					table.fill(juniors);
					table.setRedraw(true);
				} else if (text.equalsIgnoreCase(Messages.getString("view.jumps"))) { //$NON-NLS-1$
					table.setRedraw(false);
					filterTable(table);
					table.setRedraw(true);
				}
			}
		};
	}

	public void dispose() {

	}

	protected Listener doubleClickAllTableListener(final Table allTable, final Combo comboFilter, final TreeItem treeItem, final HashMap<Integer, TreeItem> itemMap) {
		return new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = allTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = allTable.getTopIndex();

				while (index < allTable.getItemCount()) {
					boolean visible = false;
					TableItem item = allTable.getItem(index);
					for (int i = 0; i < allTable.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt) && i != JuniorsComparator.NOTE) {

							generalStatusComposite.setVisible(false);
							detailStatusComposite.setVisible(true);
							comboGraph.select(0);

							Junior junior = (Junior) item.getData(Junior.class.getName()); 

							juniorDesc.setStatsJuniorInfo(junior);
							juniorView.fill(junior);

							showView(juniorView);
							showDescription(juniorDesc);
							treeItem.getParent().setSelection(itemMap.get(junior.getId()));
						} else if (rect.contains(pt) && i == JuniorsComparator.NOTE) {
							openNote(item);
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

		};
	}

	private void fillTree(List<Junior> junior) {

		_treeItem.removeAll();

		JuniorsComparator comparator = new JuniorsComparator();
		comparator.setColumn(JuniorsComparator.WEEKS);
		comparator.setDirection(JuniorsComparator.ASCENDING);

		Collections.sort(juniors, comparator);

		for (int i = 0; i < junior.size(); i++) {
			TreeItem item = new TreeItem(_treeItem, SWT.NONE);
			// item.setData("id", junior.get(i).getId());
			item.setData(Junior.class.getName(), junior.get(i)); 
			item.setText(junior.get(i).getSurname() + " " + junior.get(i).getName()); //$NON-NLS-1$
			item.setImage(FlagsResources.getFlag(Cache.getClub().getCountry()));

			if (junior.get(i).getSkills().length == 1) {
				item.setForeground(ConfigBean.getColorNewTreeObject());
			}
			if (junior.get(i).getSkills()[junior.get(i).getSkills().length - 1].getWeeks() == 0) {
				item.setForeground(ConfigBean.getColorTrainedJunior());
			}

			TreeItem chartItem = new TreeItem(item, SWT.NONE);
			chartItem.setText(Messages.getString("charts")); //$NON-NLS-1$
			chartItem.setData("juniorCharts", junior.get(i)); //$NON-NLS-1$
			chartItem.setImage(ImageResources.getImageResources("chart_blue.png")); //$NON-NLS-1$
			
			itemMap.put(junior.get(i).getId(), item);
		}
	}

	protected void filterTable(Table table) {
		if (comboFilter.getText().equalsIgnoreCase(Messages.getString("view.jumps"))) { //$NON-NLS-1$
			for (int i = 0; i < table.getItemCount(); i++) {
				if (!(table.getItem(i).getBackground(2).equals(ConfigBean.getColorIncrease()) || table.getItem(i).getBackground(2).equals(ConfigBean.getColorDecrease()))) {
					table.remove(i);
					i--;
				}
			}
		}
	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return new Configure();
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoJuniors"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	protected Listener graphListener(final ChartDateComposite graphComposite) {

		return new Listener() {
			public void handleEvent(Event event) {

				TableColumn tempColumn = (TableColumn) event.widget;
				Table tempTable = (tempColumn).getParent();

				int[] tempIntTable = new int[tempTable.getItemCount()];
				String[] tempDateTable = new String[tempTable.getItemCount()];

				// sprawdzamy czy tabela zawiera elementy
				int k = tempTable.indexOf(tempColumn);
				for (int x = 0; x < tempTable.getItemCount(); x++) {
					tempIntTable[x] = Integer.parseInt(tempTable.getItem(x).getText(k));
					tempDateTable[x] = tempTable.getItem(x).getText(0);
				}
				// graphComposite.setGraph(tempIntTable, 17);

				graphComposite.setColumn(k);

				if (k == JuniorHistoryTable.SKILL) {
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, 18);
				} else {
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, -1);
				}

				showDescription(graphComposite);
			}
		};
	}

	public void init(Composite composite) {

		this.composite = composite;
		composite.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		this.vComposite = new ViewComposite(composite, SWT.NONE);
		this.vComposite.setLayoutData(formData);

		viewFormData = this.vComposite.getViewFormData();
		descriptionFormData = this.vComposite.getDescriptionFormData();

		cb = ViewerHandler.getClipboard();

		itemMap = new HashMap<Integer, TreeItem>();

		juniorsTable = new JuniorsTable(this.vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		descriptionComposite = new DescriptionSingleComposite(this.vComposite, SWT.BORDER);

		graphsComposite = new JuniorChartsComposite(this.composite, SWT.NONE);
		graphsComposite.setLayoutData(formData);
		graphsComposite.setVisible(false);

		addToolBar();

		currentView = juniorsTable;
		currentDesc = descriptionComposite;

		juniors = new ArrayList<Junior>();

		addListeners();
		addViewComposite();
		addDescriptionComposite();
		addPopupMenu();
		addPopupMenuParentTree();

		showMainView(vComposite);
	}

	protected Listener mouseDownAllTableListener(final Composite descriptionComposite) {
		return new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = juniorsTable.getItem(pt);
					if (item != null) {
						// Junior junior = (Junior) item.getData(Junior.class.getName());
						menuPopUp.setData("item", item); //$NON-NLS-1$
						juniorsTable.setMenu(menuPopUp);
						juniorsTable.getMenu().setVisible(true);
					} else {
						juniorsTable.setMenu(menuClear);
						showDescription(descriptionComposite);
					}

				}
			}
		};
	}

	// private void setStatsJuniorInfo(Junior junior, DescriptionDoubleComposite
	// description, int index) {
	//
	// int maxSkill = junior.getSkills().length - 1 - index;
	//
	// description.clearAll();
	//
	// // int maxSkill = junior.getSkills().length - 1;
	//
	// String[][] values;
	//
	// values = new String[11][2];
	// values[0][0] = langResource.getString("junior.id");
	// values[1][0] = langResource.getString("junior.name");
	// values[2][0] = langResource.getString("junior.surname");
	// values[3][0] = langResource.getString("junior.skill");
	// values[4][0] = langResource.getString("junior.weeksLeft");
	// values[5][0] = langResource.getString("junior.weeksPast");
	// values[6][0] = langResource.getString("junior.weeksAll");
	// values[7][0] = langResource.getString("junior.numberOfJumps");
	// values[8][0] = langResource.getString("junior.averageJumps");
	// values[9][0] = langResource.getString("junior.propablyOutLevelSkill");
	// values[10][0] = langResource.getString("junior.trainer");
	//
	// values[0][1] = String.valueOf(junior.getId());
	// values[1][1] = junior.getName();
	// values[2][1] = junior.getSurname();
	// values[3][1] = langResource.getString("skill.a" +
	// junior.getSkills()[maxSkill].getSkill()) + " [" +
	// junior.getSkills()[maxSkill].getSkill() + "]";
	// values[4][1] = String.valueOf(junior.getSkills()[maxSkill].getWeeks());
	// values[5][1] = String.valueOf(junior.getSkills()[0].getWeeks() -
	// junior.getSkills()[maxSkill].getWeeks());
	// values[6][1] = String.valueOf(junior.getSkills()[0].getWeeks());
	// values[7][1] = String.valueOf(junior.getJumps());
	// values[8][1] = String.valueOf(new
	// BigDecimal(junior.getAverageJumps()).setScale(2,
	// BigDecimal.ROUND_HALF_UP));
	// values[9][1] = langResource.getString("skill.a" +
	// junior.getPropablySkill()) + " [" + junior.getPropablySkill() + "]";
	// if (junior.getJumps() < 2) {
	// values[9][1] = "~" + langResource.getString("skill.a" +
	// junior.getPropablySkill()) + " [" + junior.getPropablySkill() + "]";
	// } else {
	// values[9][1] = langResource.getString("skill.a" +
	// junior.getPropablySkill()) + " [" + junior.getPropablySkill() + "]";
	// }
	//
	// if (junior.getSkills()[maxSkill].getTraining() != null) {
	// Coach coach =
	// junior.getSkills()[maxSkill].getTraining().getJuniorCoach();
	// if (coach != null) {
	// int value = coach.getGeneralskill();
	// values[10][1] = langResource.getString("skill.a" + value) + " [" + value
	// + "]";
	// } else {
	// values[10][1] = "-";
	// }
	// } else {
	// values[10][1] = "-";
	// }
	//
	// for (int i = 0; i < values.length; i++) {
	// description.addLeftText(values[i]);
	// }
	//
	// values = new String[6][2];
	// values[0][0] = langResource.getString("junior.money.spent");
	// values[1][0] = langResource.getString("junior.money.left");
	// values[2][0] = langResource.getString("junior.money.all");
	// values[3][0] = langResource.getString("junior.exit.date");
	// values[4][0] = langResource.getString("junior.weeks.withoutJump");
	// values[5][0] = langResource.getString("junior.skill.begin");
	//
	// values[0][1] = junior.getMoneySpent().formatIntegerCurrencySymbol();
	// values[1][1] =
	// junior.getRestMoneyToSpend().formatIntegerCurrencySymbol();
	// values[2][1] = junior.getAllMoneyToSpend().formatIntegerCurrencySymbol();
	// values[3][1] = junior.getEndDate().toDateString();
	// values[4][1] = String.valueOf(junior.getWeeksWithoutJump());
	// values[5][1] = langResource.getString("skill.a" +
	// junior.getSkills()[0].getSkill()) + " [" +
	// junior.getSkills()[0].getSkill() + "]";
	//
	// for (int i = 0; i < values.length; i++) {
	// description.addRightText(values[i]);
	// }
	// }

	private void openNote(Item item) {
		Junior junior = (Junior) item.getData(Junior.class.getName());
		final NoteShell noteShell = new NoteShell(vComposite.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
		noteShell.setPerson(junior);
		noteShell.open();
		if (item instanceof TreeItem) {
			juniorsTable.fill(juniors);
		} else if (item instanceof TableItem) {
			if (junior.getNote() != null) {
				if (junior.getNote().isEmpty()) {
					((TableItem) item).setImage(JuniorsComparator.NOTE, null);
				} else {
					((TableItem) item).setImage(JuniorsComparator.NOTE, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
				}
			}
		}
	}

	public void reload() {
		// TODO Auto-generated method stub

	}

	private Listener selectionAllTableListener() {
		return new Listener() {
			public void handleEvent(Event event) {
				if (event != null) {
					Junior junior = (Junior) event.item.getData(Junior.class.getName()); 
					juniorDesc.setStatsJuniorInfo(junior);
					showDescription(juniorDesc);
					// setCbData(juniorDesc);
				}
			}

		};
	}

	public void set() {
		juniors = Cache.getJuniors();
		juniorsMax = Cache.getClub().getJuniorsMax();
		juniorsTable.fill(juniors);

		setDescriptionComposite(juniors);
		fillTree(juniors);

		graphList = graphListener(graphComposite);

		juniorDesc = new JuniorDescriptionComposite(vComposite, SWT.BORDER);
		juniorDesc.setLayoutData(descriptionFormData);
		juniorDesc.setVisible(false);

		juniorView = new JuniorTable(vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		juniorView.setLayoutData(viewFormData);
		juniorView.setVisible(false);

		juniorView.addListener(SWT.MouseDoubleClick, viewMouseDoubleClickListener(_treeItem, juniorsTable, descriptionComposite));

		// juniorView.addListener(SWT.MouseDown, historyBackListener);

		juniorView.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentView.getItem(point);
					if (item != null) {
						if (currentDesc instanceof ChartDateComposite) {
							((ChartDateComposite) currentDesc).setMarkers((Date) item.getData("date"), Calendar.THURSDAY, Integer.valueOf(item.getText(1))); //$NON-NLS-1$
						} else if (currentDesc instanceof DescriptionDoubleComposite) {
							int index = item.getParent().indexOf(item);
							universalComposite.setStatsJuniorInfo((Junior) item.getParent().getData(Junior.class.getName()), index); 
							showDescription(universalComposite);
						}
					}
				} else if (event.button == 3) {
					showDescription(previousDesc);
				}
			}

		});

		juniorView.getColumn(JuniorHistoryTable.SKILL).addListener(SWT.Selection, graphList);

		juniorsTable.addListener(SWT.MouseDoubleClick, doubleClickAllTableListener(juniorsTable, comboFilter, _treeItem, itemMap));
		juniorsTable.addListener(SWT.Selection, selectionAllTableListener());
		juniorsTable.addListener(SWT.MouseDown, mouseDownAllTableListener(descriptionComposite));
		
		_treeItem.getParent().addListener(SWT.MouseDown, mouseListener);
		_treeItem.getParent().addListener(SWT.KeyUp, viewKeyListener(juniorsTable, descriptionComposite));
		vComposite.layout(true);
	}

	private void setDescriptionComposite(List<Junior> juniors) {
		int maxSkill = 0;
		int maxSize = 30;
		double averJuniorSkill = 0;
		String[][] values;

		for (int i = 0; i < juniors.size(); i++) {
			maxSkill = juniors.get(i).getSkills().length - 1;
			averJuniorSkill += juniors.get(i).getSkills()[maxSkill].getSkill();
		}
		values = new String[5][2];

		if (juniors.size() != 0) {
			values[0][0] = Messages.getString("junior.averageSkill"); //$NON-NLS-1$
			values[0][1] = Messages.getString("skill.a" + BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(0, BigDecimal.ROUND_HALF_UP)) + " (" + BigDecimal.valueOf(averJuniorSkill / juniors.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			values[0][0] = Messages.getString("junior.averageSkill"); //$NON-NLS-1$
			values[0][1] = "0"; //$NON-NLS-1$
		}

		values[1][0] = Messages.getString("junior.allJuniors"); //$NON-NLS-1$
		values[1][1] = String.format("%d/%d", juniors.size(), juniorsMax); //$NON-NLS-1$

		values[2][0] = Messages.getString("junior.size.max"); //$NON-NLS-1$
		values[2][1] = String.format("%d/%d",juniorsMax, maxSize); //$NON-NLS-1$

		values[3][0] = Messages.getString("junior.juniorsCosts"); //$NON-NLS-1$
		values[3][1] = Money.formatDoubleCurrencySymbol(juniors.size() * Junior.juniorCost.toInt());

		values[4][0] = Messages.getString("junior.cost.single"); //$NON-NLS-1$
		values[4][1] = Junior.juniorCost.formatDoubleCurrencySymbol();

		for (int i = 0; i < values.length; i++) {
			descriptionComposite.addText(values[i]);
		}
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;

	}

	public void setSvBean(SvBean svBean) {

	}

	public void setTreeItem(TreeItem treeItem) {
		this._treeItem = treeItem;
		_treeItem.setText(Messages.getString("tree.ViewJuniors")); //$NON-NLS-1$
		_treeItem.setImage(ImageResources.getImageResources("junior.png")); //$NON-NLS-1$
	}

	//FIXME: 
//	private void setCbData(DescriptionDoubleComposite juniorDesc) {
//		cbData = juniorDesc.getText();
//	}

	protected void showDescription(Composite composite) {
		if (!(currentDesc instanceof ChartDateComposite)) {
			previousDesc = currentDesc;
		}

		currentDesc.setVisible(false);
		currentDesc = composite;
		currentDesc.setVisible(true);
	}

	private void showMainView(Composite composite) {

		composite.getParent().setRedraw(false);
		if (currentComposite != null) {
			currentComposite.setVisible(false);
		}
		// currentComposite.setVisible(false);
		currentComposite = composite;
		currentComposite.setVisible(true);

		composite.getParent().setRedraw(true);
	}

	protected void showView(Table table) {
		currentView.setVisible(false);
		currentView = table;
		currentView.setVisible(true);
	}

	protected Listener viewKeyListener(final Table allTable, final Composite descriptionComposite) {
		Listener viewKeyListener = new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < _treeItem.getParent().getSelection().length; i++) {
					item = _treeItem.getParent().getSelection()[i];
				}

				if (item != null && item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {

					if (item != null && item.getParentItem() == null) {

						generalStatusComposite.setVisible(true);
						detailStatusComposite.setVisible(false);

						Junior junior = (Junior) item.getData(Junior.class.getName());

						juniorDesc.setStatsJuniorInfo(junior);
						juniorView.fill(junior);

						showView(juniorView);
						showDescription(juniorDesc);
						showMainView(vComposite);
					}

					if (item != null && item.getParentItem() != null) {

						generalStatusComposite.setVisible(false);
						detailStatusComposite.setVisible(true);
						comboGraph.select(0);

						Junior junior = (Junior) item.getData(Junior.class.getName());

						juniorDesc.setStatsJuniorInfo(junior);
						juniorView.fill(junior);

						showView(juniorView);
						showDescription(juniorDesc);

						showMainView(vComposite);
					}
				} else if (item != null && item.equals(_treeItem)) {

					generalStatusComposite.setVisible(true);
					detailStatusComposite.setVisible(false);

					showDescription(descriptionComposite);
					showView(allTable);
				} else if (item.getData("juniorCharts") != null) { //$NON-NLS-1$

					if (item.getParentItem().getParentItem().equals(_treeItem)) {
						if (item.getData("juniorCharts") instanceof Junior) { //$NON-NLS-1$
							Junior junior = (Junior) item.getData("juniorCharts"); //$NON-NLS-1$
							graphsComposite.fill(junior);
							showMainView(graphsComposite);
						}
					}
				}
			}
		};

		return viewKeyListener;
	}

	private Listener viewMouseDoubleClickListener(final TreeItem treeItem, final Table allTable, final Composite descriptionComposite) {
		return new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					detailStatusComposite.setVisible(false);
					generalStatusComposite.setVisible(true);
					comboGraph.select(0);

					treeItem.getParent().setSelection(new TreeItem[] {
						treeItem
					});
					showView(allTable);
					showDescription(descriptionComposite);
				}
			}
		};
	}
}
