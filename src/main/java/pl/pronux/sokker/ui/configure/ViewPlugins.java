package pl.pronux.sokker.ui.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewPlugins implements IViewConfigure {

	private TreeItem treeItem;

	private Composite composite;

	// private Group proxyGroup;
	private SVProperties pluginsProperties;

	private Table pluginsTable;

//	private SVProperties confProperties;

	private String[] plugins;

	private SokkerViewerSettings settings;

	public void clear() {

	}

	public void dispose() {

	}

	public Composite getComposite() {
		return composite;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());
		FormData formData;

		pluginsProperties = new SVProperties();
		try {
			pluginsProperties.load(new FileInputStream(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "plugins.properties")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite plugins", e);
		} catch (IOException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite plugins", e);
		}

		plugins = pluginsProperties.getProperty("plugins").split(";"); //$NON-NLS-1$ //$NON-NLS-2$

		formData = new FormData(50, 25);
		formData.left = new FormAttachment(50, -formData.width - 10);
		formData.bottom = new FormAttachment(100, -10);

		Button upButton = new Button(composite, SWT.ARROW | SWT.UP);
		upButton.setLayoutData(formData);
		upButton.setFont(ConfigBean.getFontTable());
		upButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				TableItem[] tableItems = pluginsTable.getSelection();
				if (tableItems.length == 1) {
					int index = pluginsTable.indexOf(tableItems[0]);
					if (index > 0) {
						TableItem tableItem = new TableItem(pluginsTable, SWT.NONE, index - 1);

						for (int i = 0; i < pluginsTable.getItemCount(); i++) {
							tableItem.setText(i, tableItems[0].getText(i));
						}

						tableItem.setData("plugin", tableItems[0].getData("plugin")); //$NON-NLS-1$ //$NON-NLS-2$
						tableItem.setChecked(tableItems[0].getChecked());
						tableItems[0].dispose();
						pluginsTable.setSelection(index - 1);
					}

				}
			}

		});

		formData = new FormData(50, 25);
		formData.left = new FormAttachment(upButton, 20);
		formData.bottom = new FormAttachment(100, -10);

		Button downButton = new Button(composite, SWT.ARROW | SWT.DOWN);
		downButton.setLayoutData(formData);
		downButton.setFont(ConfigBean.getFontTable());
		downButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				TableItem[] tableItems = pluginsTable.getSelection();
				if (tableItems.length == 1) {
					int index = pluginsTable.indexOf(tableItems[0]);
					if (index < pluginsTable.getItemCount() - 1) {
						TableItem tableItem = new TableItem(pluginsTable, SWT.NONE, index + 2);
						for (int i = 0; i < pluginsTable.getItemCount(); i++) {
							tableItem.setText(i, tableItems[0].getText(i));
						}

						tableItem.setData("plugin", tableItems[0].getData("plugin")); //$NON-NLS-1$ //$NON-NLS-2$
						tableItem.setChecked(tableItems[0].getChecked());
						tableItems[0].dispose();
						pluginsTable.setSelection(index + 1);
					}
				}
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(downButton, -10);

		// formData = new FormData();
		// formData.top = new FormAttachment(0, 30);
		// formData.left = new FormAttachment(0, 20);
		// formData.right = new FormAttachment(100, -20);
		// formData.height = 450;

		pluginsTable = new Table(composite, SWT.BORDER | SWT.CHECK | SWT.SINGLE | SWT.FULL_SELECTION);
		pluginsTable.setHeaderVisible(true);
		pluginsTable.setLinesVisible(true);
		pluginsTable.setFont(ConfigBean.getFontMain());

		pluginsTable.setLayoutData(formData);

		String[] columns = {
				Messages.getString("configure.plugins.table.name"), //$NON-NLS-1$
				Messages.getString("configure.plugins.table.description") //$NON-NLS-1$
		};

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(pluginsTable, SWT.NONE);
			column.setText(columns[i]);
			column.setResizable(false);
			column.setMoveable(false);
		}

		treeItem.setText(Messages.getString("configure.plugins")); //$NON-NLS-1$
		
		fillTable(pluginsTable, plugins);

		composite.layout(true);
	}

	private void fillTable(Table table, String[] plugins) {
		table.remove(0, table.getItemCount() - 1);
		
		TableItem item;

		for (int i = 0; i < plugins.length; i++) {
			String viewName = plugins[i];
			String[] viewNameTmp = viewName.split("\\."); //$NON-NLS-1$
			String viewSimpleName = viewNameTmp[viewNameTmp.length - 1];

			int pluginTurn = Integer.valueOf(pluginsProperties.getProperty(viewName + ".turn")).intValue(); //$NON-NLS-1$

			item = new TableItem(pluginsTable, SWT.NONE);

			item.setText(0, Messages.getString("tree." + viewSimpleName)); //$NON-NLS-1$
			item.setText(1, Messages.getString(viewName + ".description")); //$NON-NLS-1$
			item.setData("plugin", plugins[i]); //$NON-NLS-1$

			if (pluginTurn == 1) {
				item.setChecked(true);
			} else {
				item.setChecked(false);
			}

		}

		for (int i = 0; i < pluginsTable.getColumnCount(); i++) {
			pluginsTable.getColumn(i).pack();
		}
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void set() {
	}

	public void applyChanges() {

		StringBuffer pluginsDirection = new StringBuffer();
		for (int i = 0; i < pluginsTable.getItemCount(); i++) {
			TableItem item = pluginsTable.getItem(i);
			pluginsDirection.append((String) item.getData("plugin")).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
			if (item.getChecked() == true) {
				pluginsProperties.setProperty((String) item.getData("plugin") + ".turn", "1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				pluginsProperties.setProperty((String) item.getData("plugin") + ".turn", "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}

		pluginsProperties.setProperty("plugins", pluginsDirection.toString()); //$NON-NLS-1$

		try {
			pluginsProperties.store(new FileOutputStream(new File(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "plugins.properties")), ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite plugins", e);
		} catch (IOException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite plugins", e);
		}
	}
	public void restoreDefaultChanges() {
		fillTable(pluginsTable, plugins);
	}
}