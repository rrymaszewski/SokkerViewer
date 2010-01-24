package pl.pronux.sokker.ui.plugins;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.Transfer;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.tables.TransfersTable;

public class ViewTransfers implements IPlugin {

	private Composite composite;

	private TreeItem treeItem;

	private TransfersTable table;

	private ArrayList<Transfer> alTransfers;

	private int idClub;

	private Listener viewListener;

	private Menu menuPopUpParentTree;

	private FormData viewFormData;

	private FormData descriptionFormData;

	private Table descriptionTable;

	public void clear() {
	}

	public void dispose() {
	}

	public Composite getComposite() {
		return this.composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoTransfers"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return this.treeItem;
	}

	public void init(Composite composite) {
		
		this.composite = new ViewComposite(composite.getParent(), composite.getStyle());
		
		viewFormData = ((ViewComposite) this.composite).getViewFormData();
		descriptionFormData = ((ViewComposite) this.composite).getDescriptionFormData();
		
		composite.dispose();
		
		addPopupMenuParentTree();
		addView(this.composite);
		addDescription(this.composite);
		addListeners();
		this.composite.layout();
	}

	private void addListeners() {
		viewListener = new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(point);
				if (item != null && item.equals(treeItem)) {
					if (item.equals(treeItem) && event.button == 3) {
						if (menuPopUpParentTree != null) {
							treeItem.getParent().setMenu(menuPopUpParentTree);
							treeItem.getParent().getMenu().setVisible(true);
						}
					}
				}
			}
		};
	}

	private void addDescription(Composite composite) {
		TableItem item;
		TableColumn tableColumn;

		descriptionTable = new Table(composite, SWT.FULL_SELECTION);
		descriptionTable.setLayoutData(descriptionFormData);
		descriptionTable.setLinesVisible(false);
		descriptionTable.setHeaderVisible(false);
		descriptionTable.setBackground(composite.getBackground());
		descriptionTable.setFont(ConfigBean.getFontTable());

		String[] column = {
				"first", //$NON-NLS-1$
				"second", //$NON-NLS-1$
				"third" //$NON-NLS-1$
		};

		for (int i = 0; i < column.length; i++) {
			tableColumn = new TableColumn(descriptionTable, SWT.LEFT);
			if (i > 0) {
				tableColumn.setAlignment(SWT.RIGHT);
			}
			tableColumn.setText(column[i]);
		}

		String[] firstColumn = {
				Messages.getString("transfers.income"), //$NON-NLS-1$
				Messages.getString("transfers.expenses"), //$NON-NLS-1$
				Messages.getString("transfers.income.average"), //$NON-NLS-1$
				Messages.getString("transfers.expenses.average"), //$NON-NLS-1$
				Messages.getString("transfers.sales.number"), //$NON-NLS-1$
				Messages.getString("transfers.purchases.number"), //$NON-NLS-1$
				Messages.getString("transfers.balance"), //$NON-NLS-1$
				Messages.getString("transfers.juniors.income") //$NON-NLS-1$
		};

		for (int i = 0; i < firstColumn.length; i++) {
			item = new TableItem(descriptionTable, SWT.NONE);
			item.setText(0, firstColumn[i]);
		}

		for (int i = 0; i < descriptionTable.getColumnCount(); i++) {
			descriptionTable.getColumn(i).pack();
			descriptionTable.getColumn(i).setWidth(descriptionTable.getColumn(i).getWidth() + 15);
		}

		for (int i = 0; i < descriptionTable.getItemCount(); i++) {
			if ((i % 2) == 0) {
				descriptionTable.getItem(i).setBackground(descriptionTable.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			}
		}
	}

	private void addView(Composite composite) {
		// FormData formData = new FormData();
		// formData.left = new FormAttachment(0,0);
		// formData.top = new FormAttachment(0,0);
		// formData.right = new FormAttachment(100,0);
		// formData.bottom = new FormAttachment(100,0);
		table = new TransfersTable(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setLayoutData(viewFormData);
	}

	private void setDescription(Table table, ArrayList<Transfer> alTransfers) {
		long income = 0;
		long expanses = 0;
		double incomeAverage = 0.0;
		double expansesAverage = 0.0;
		int purchases = 0;
		int sales = 0;
		long balance = 0;
		long juniors = 0;

		for (Transfer transfer : alTransfers) {
			if (transfer.getIsInOut() == Transfer.IN) {
				expanses += transfer.getPrice().toInt();
				purchases++;
			} else if (transfer.getIsInOut() == Transfer.OUT) {
				income += transfer.getPrice().toInt();
				sales++;
				if (transfer.getPlayer() != null) {
					if (transfer.getPlayer().getYouthTeamID() == idClub) {
						juniors += transfer.getPrice().toInt();
					}
				}
			}
		}
		if (sales > 0) {
			incomeAverage = income / sales;
		}

		if (purchases > 0) {
			expansesAverage = expanses / purchases;
		}

		balance = income - expanses;

		table.getItem(0).setText(1, new Money(income).formatIntegerCurrencySymbol());
		table.getItem(1).setText(1, new Money(expanses).formatIntegerCurrencySymbol());
		table.getItem(2).setText(1, new Money(incomeAverage).formatIntegerCurrencySymbol());
		table.getItem(3).setText(1, new Money(expansesAverage).formatIntegerCurrencySymbol());
		table.getItem(4).setText(1, String.valueOf(sales));
		table.getItem(5).setText(1, String.valueOf(purchases));
		table.getItem(6).setText(1, new Money(balance).formatIntegerSignCurrencySymbol());
		table.getItem(7).setText(1, new Money(juniors).formatIntegerSignCurrencySymbol());

		table.getColumn(1).pack();
		table.getColumn(1).setWidth(table.getColumn(1).getWidth() + 15);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {

	}

	private void addPopupMenuParentTree() {
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewTransfers")); //$NON-NLS-1$
		this.treeItem.setImage(ImageResources.getImageResources("transfers.png")); //$NON-NLS-1$
		treeItem.getParent().addListener(SWT.MouseDown, viewListener);
	}

	public void set() {
		idClub = Cache.getClub().getId();
		alTransfers = Cache.getTransfers();
		table.fill(alTransfers, idClub);
		setDescription(descriptionTable, alTransfers);
	}

	public void reload() {
	}

}
