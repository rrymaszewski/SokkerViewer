package pl.pronux.sokker.ui.plugins;

import java.util.ArrayList;
import java.util.Calendar;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.TabComposite;
import pl.pronux.sokker.ui.widgets.tables.StatisticsEarnsTable;
import pl.pronux.sokker.ui.widgets.tables.StatisticsFansTable;
import pl.pronux.sokker.ui.widgets.tables.StatisticsRankTable;

public class ViewStatistics implements IPlugin {

	private Club club;

	private TreeItem _treeItem;

	private CTabFolder viewFolder;

	private CTabItem cTabItemFans;

	private CTabItem cTabItemEarns;

	private Listener graphList;

	private TabComposite currentComposite;

	private Composite composite;

	private CTabItem cTabItemRank;

	private StatisticsFansTable statisticsFansTable;

	private StatisticsEarnsTable statisticsEarnsTable;

	private StatisticsRankTable statisticsRankTable;

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
				currentComposite = (TabComposite) ((CTabItem) event.item).getControl();
			}
		});

		cTabItemFans = new CTabItem(viewFolder, SWT.NONE);
		cTabItemFans.setText(Messages.getString("statistics.fans")); //$NON-NLS-1$
		cTabItemFans.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		TabComposite tabComposite = new TabComposite(viewFolder, SWT.NONE);
		tabComposite.setLayoutData(formData);

		statisticsFansTable = new StatisticsFansTable(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		statisticsFansTable.setLayoutData(tabComposite.getViewFormData());
		tabComposite.setViewTable(statisticsFansTable);

		statisticsFansTable.getColumn(StatisticsFansTable.FANCLUB_COUNT).addListener(SWT.Selection, graphList);
		statisticsFansTable.getColumn(StatisticsFansTable.FANCLUB_MOOD).addListener(SWT.Selection, graphList);
		statisticsFansTable.getColumn(StatisticsFansTable.FANCLUB_DIFF).addListener(SWT.Selection, graphList);

		cTabItemFans.setControl(tabComposite);

		cTabItemEarns = new CTabItem(viewFolder, SWT.NONE);
		cTabItemEarns.setText(Messages.getString("statistics.earns")); //$NON-NLS-1$
		cTabItemEarns.setFont(ConfigBean.getFontMain());

		tabComposite = new TabComposite(viewFolder, SWT.NONE);
		tabComposite.setLayoutData(formData);

		statisticsEarnsTable = new StatisticsEarnsTable(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		statisticsEarnsTable.setLayoutData(tabComposite.getViewFormData());
		tabComposite.setViewTable(statisticsEarnsTable);

		statisticsEarnsTable.getColumn(StatisticsEarnsTable.CLUB_MONEY).addListener(SWT.Selection, graphList);

		cTabItemEarns.setControl(tabComposite);

		cTabItemRank = new CTabItem(viewFolder, SWT.NONE);
		cTabItemRank.setText(Messages.getString("statistics.rank")); //$NON-NLS-1$
		cTabItemRank.setFont(ConfigBean.getFontMain());

		tabComposite = new TabComposite(viewFolder, SWT.NONE);
		tabComposite.setLayoutData(formData);

		statisticsRankTable = new StatisticsRankTable(tabComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		statisticsRankTable.setLayoutData(tabComposite.getViewFormData());
		tabComposite.setViewTable(statisticsRankTable);

		statisticsRankTable.getColumn(StatisticsRankTable.RANK).addListener(SWT.Selection, graphList);

		cTabItemRank.setControl(tabComposite);

		viewFolder.setSelection(cTabItemFans);
		currentComposite = (TabComposite) cTabItemFans.getControl();
	}

	public void clear() {
		statisticsEarnsTable.removeAll();
		statisticsFansTable.removeAll();
		statisticsRankTable.removeAll();
	}

	public void set() {
		club = Cache.getClub();

		/*
		 * Listener listener = new Listener() { public void handleEvent(Event event) { if(event.button == 1) { Point point = new Point(event.x, event.y);
		 * TableItem item = currentComposite.getViewTable().getItem(point); if (item != null) { if ( currentComposite.getGraphComposite().getVisible())
		 * { currentComposite.getGraphComposite().setMarkers((Date)item.getData("date"), Calendar.MONDAY,
		 * Integer.valueOf(item.getText(currentComposite.getGraphComposite().getColumn()).replaceAll("[^0-9-]", ""))); } else
		 * if(currentComposite.getDescriptionComposite().getVisible()) { // int index = item.getParent().indexOf(item); //
		 * setStatsJuniorInfo((Junior)item.getParent().getData("juniorObject"), universalJuniorComposite, index); // showDescription(universalJuniorComposite);
		 * } } } else if(event.button == 3) { // showDescription(previousDesc); } } };
		 */

		statisticsRankTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentComposite.getViewTable().getItem(point);
					if (item != null) {
						if (currentComposite.getGraphComposite().getVisible()) {
							TableColumn tableColumn = item.getParent().getColumn(currentComposite.getGraphComposite().getColumn());
							ArrayList<?> al = (ArrayList<?>) tableColumn.getData("data"); //$NON-NLS-1$
							Object object = al.get(al.size() - 1 - item.getParent().indexOf(item));
							currentComposite.getGraphComposite().setMarkers((Date) item.getData("date"), -1, object); //$NON-NLS-1$
						} else if (currentComposite.getDescriptionComposite().getVisible()) {
							// int index = item.getParent().indexOf(item);
							// setStatsJuniorInfo((Junior)item.getParent().getData("juniorObject"), universalJuniorComposite, index);
							// showDescription(universalJuniorComposite);
						}
					}
				} else if (event.button == 3) {
					// showDescription(previousDesc);
				}
			}

		});

		statisticsFansTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentComposite.getViewTable().getItem(point);
					if (item != null) {
						if (currentComposite.getGraphComposite().getVisible()) {
							currentComposite
								.getGraphComposite()
								.setMarkers(
											(Date) item.getData("date"), Calendar.MONDAY, Integer.valueOf(item.getText(currentComposite.getGraphComposite().getColumn()).replaceAll("[^0-9-]", ""))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						} else if (currentComposite.getDescriptionComposite().getVisible()) {
							// int index = item.getParent().indexOf(item);
							// setStatsJuniorInfo((Junior)item.getParent().getData("juniorObject"), universalJuniorComposite, index);
							// showDescription(universalJuniorComposite);
						}
					}
				} else if (event.button == 3) {
					// showDescription(previousDesc);
				}
			}

		});
		statisticsEarnsTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentComposite.getViewTable().getItem(point);
					if (item != null) {
						if (currentComposite.getGraphComposite().getVisible()) {
							currentComposite
								.getGraphComposite()
								.setMarkers(
											(Date) item.getData("date"), Calendar.SATURDAY, Integer.valueOf(item.getText(currentComposite.getGraphComposite().getColumn()).replaceAll("[^0-9-]", ""))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						} else if (currentComposite.getDescriptionComposite().getVisible()) {
							// int index = item.getParent().indexOf(item);
							// setStatsJuniorInfo((Junior)item.getParent().getData("juniorObject"), universalJuniorComposite, index);
							// showDescription(universalJuniorComposite);
						}
					}
				} else if (event.button == 3) {
					// showDescription(previousDesc);
				}
			}

		});
		statisticsFansTable.fill(club);
		statisticsEarnsTable.fill(club);
		statisticsRankTable.fill(club);

	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());
		composite.layout(true);
		addListeners();
		addViewComposite();
	}

	public void setTreeItem(TreeItem treeItem) {

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewStatistics")); //$NON-NLS-1$

		_treeItem.setImage(ImageResources.getImageResources("statistics.png")); //$NON-NLS-1$
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoStatistics"); //$NON-NLS-1$
	}

	public Composite getComposite() {
		return composite;
	}

	private void addListeners() {
		graphList = new Listener() {

			@SuppressWarnings("unchecked")//$NON-NLS-1$
			public void handleEvent(Event event) {

				TableColumn tempColumn = (TableColumn) event.widget;
				Table tempTable = tempColumn.getParent();

				int[] tempIntTable = new int[tempTable.getItemCount()];
				String[] tempDateTable = new String[tempTable.getItemCount()];
				// sprawdzamy czy tabela zawiera elementy
				int k = tempTable.indexOf(tempColumn);
				for (int x = 0; x < tempTable.getItemCount(); x++) {
					tempIntTable[x] = Double.valueOf(tempTable.getItem(x).getText(k).replaceAll("[^0-9-]", "")).intValue(); //$NON-NLS-1$ //$NON-NLS-2$
					tempDateTable[x] = tempTable.getItem(x).getText(0);
				}

				// graphComposite.setGraph(tempIntTable, 17);

				currentComposite.getGraphComposite().setColumn(k);

				if (viewFolder.getSelection().equals(cTabItemFans)) {
					//					if(tempColumn.getText().equals(Messages.getString("statistics.fans.count"))) { //$NON-NLS-1$
					currentComposite.getGraphComposite().fillGraph(tempIntTable, tempDateTable, Calendar.MONDAY, false, -1);
					// } else {
					// currentComposite.getGraphComposite().fillGraph(tempIntTable, tempDateTable,Calendar.MONDAY,true, -1);
					// }
				} else if (viewFolder.getSelection().equals(cTabItemEarns)) {
					currentComposite.getGraphComposite().fillGraph(tempIntTable, tempDateTable, Calendar.SATURDAY, true, -1);
				} else if (viewFolder.getSelection().equals(cTabItemRank)) {
					ArrayList<Number> al = (ArrayList<Number>) tempColumn.getData("data"); //$NON-NLS-1$
					currentComposite.getGraphComposite().fillGraph(al, tempDateTable, -1, false, -1);
				}

				currentComposite.getGraphComposite().setVisible(true);
				currentComposite.getDescriptionComposite().setVisible(false);
			}
		};
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
