package pl.pronux.sokker.ui.widgets.shells;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.resources.ColorResources;

public class PluginsView extends Shell {

	private Button button;

//	private String login;

	private Table pluginsTable;

	private Properties pluginsProperties;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public PluginsView(final Shell parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());
		this.setText(Messages.getString("viewer.plugins.title")); 
		
		FormData formData;

		pluginsProperties = new Properties();
		try {
			pluginsProperties.load(new FileInputStream(SettingsHandler.getSokkerViewerSettings().getBaseDirectory() + File.separator + "settings" + File.separator + "plugins.properties"));  
		} catch (FileNotFoundException e) {
			new BugReporter(PluginsView.this.getDisplay()).openErrorMessage("Plugins ViewComposite", e);
		} catch (IOException e) {
			new BugReporter(PluginsView.this.getDisplay()).openErrorMessage("Plugins ViewComposite", e);
		}

		String[] plugins = pluginsProperties.getProperty("plugins").split(";");  

		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		// formData = new FormData();
		// formData.top = new FormAttachment(0, 30);
		// formData.left = new FormAttachment(0, 20);
		// formData.right = new FormAttachment(100, -20);
		// formData.height = 450;

		pluginsTable = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		pluginsTable.setHeaderVisible(true);
		pluginsTable.setLinesVisible(true);

		pluginsTable.setLayoutData(formData);

		String[] columns = {
				Messages.getString("configure.plugins.table.name"), 
				Messages.getString("configure.plugins.table.className"), 
				Messages.getString("configure.plugins.table.author"), 
				Messages.getString("configure.plugins.table.version"), 
				Messages.getString("configure.plugins.table.description") 
		};

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(pluginsTable, SWT.NONE);
			column.setText(columns[i]);
			column.setResizable(false);
			column.setMoveable(false);
		}

		TableItem item;

		for (int i = 0; i < plugins.length; i++) {
			String viewName = plugins[i];
			String[] viewNameTmp = viewName.split("\\."); 
			String viewSimpleName = viewNameTmp[viewNameTmp.length - 1];

			item = new TableItem(pluginsTable, SWT.NONE);

			item.setText(0, Messages.getString("tree." + viewSimpleName)); 
			item.setText(1, viewName);
			item.setText(2, pluginsProperties.getProperty(viewName + ".author")); 
			item.setText(3, pluginsProperties.getProperty(viewName + ".version")); 
			item.setText(4, Messages.getString(viewName + ".description")); 
			item.setData("plugin", plugins[i]); 

			if(!pluginsProperties.getProperty(viewName + ".turn").equals("1")) {  
				item.setForeground(ColorResources.getGray());
			}
		}

		for (int i = 0; i < pluginsTable.getColumnCount(); i++) {
			pluginsTable.getColumn(i).pack();
		}

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = this.getDisplay().getPrimaryMonitor().getClientArea();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

		this.setDefaultButton(button);

		// shell.pack();
		this.computeSize(this.getClientArea().width, SWT.DEFAULT);

	}
	
}
