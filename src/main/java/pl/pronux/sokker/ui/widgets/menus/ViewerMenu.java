package pl.pronux.sokker.ui.widgets.menus;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.events.UpdateEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.shells.CurrencyCalculator;
import pl.pronux.sokker.ui.widgets.shells.LoginShell;
import pl.pronux.sokker.ui.widgets.shells.PluginsView;
import pl.pronux.sokker.ui.widgets.shells.RestoreDatabaseShell;
import pl.pronux.sokker.ui.widgets.shells.TaxCalculator;
import pl.pronux.sokker.ui.widgets.wizards.updater.UpdaterWizard;
import pl.pronux.sokker.ui.widgets.wizards.xmlimporter.ImporterWizard;
import pl.pronux.sokker.utils.file.Database;

public class ViewerMenu extends Menu {

	public ViewerMenu(Decorations decorations, int style) {
		super(decorations, style);

		final Shell mainShell = decorations.getShell();

		MenuItem mainShellSubmenuItemFile = new MenuItem(this, SWT.CASCADE);
		mainShellSubmenuItemFile.setText(Messages.getString("button.file")); 
		Menu mainShellSubmenuFile = new Menu(mainShellSubmenuItemFile);

		MenuItem mainShellMenuItemConnect = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemConnect.setText(Messages.getString("button.connect") + "\tF2");  
		mainShellMenuItemConnect.setAccelerator(SWT.F2);
		Listener mainShellConfList = new Listener() {
			public void handleEvent(Event event) {
				new LoginShell(mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}
		};
		mainShellMenuItemConnect.addListener(SWT.Selection, mainShellConfList);

		MenuItem mainShellMenuItemGet = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemGet.setText(Messages.getString("button.update") + "\tF3");  
		mainShellMenuItemGet.setAccelerator(SWT.F3);
		Listener mainShellGetList = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(true));
			}
		};
		mainShellMenuItemGet.addListener(SWT.Selection, mainShellGetList);
		mainShellMenuItemGet.setEnabled(true);

		MenuItem mainShellMenuItemRefresh = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemRefresh.setText(Messages.getString("button.reload") + "\tF5");  
		mainShellMenuItemRefresh.setAccelerator(SWT.F5);
		Listener mainShellRefreshList = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(false));
			}
		};
		mainShellMenuItemRefresh.addListener(SWT.Selection, mainShellRefreshList);
		mainShellMenuItemRefresh.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		MenuItem mainShellMenuItemImportXML = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemImportXML.setText(Messages.getString("button.import.xml")); 
		mainShellMenuItemImportXML.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(SettingsHandler.isLogged()) {
					ImporterWizard importer = new ImporterWizard(mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE);
					importer.open();
				}
			}
		});
		mainShellMenuItemImportXML.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		MenuItem mainShellMenuItemBackupDatabase = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemBackupDatabase.setText(Messages.getString("button.database.backup")); 
		mainShellMenuItemBackupDatabase.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(SettingsHandler.isLogged()) {
					try {
						boolean operation = Database.backup(SettingsHandler.getSokkerViewerSettings());
						if (operation) {
							MessageBox msg = new MessageBox(mainShell, SWT.OK | SWT.ICON_INFORMATION);
							msg.setText(Messages.getString("message.db.backup.title")); 
							msg.setMessage(Messages.getString("message.db.backup.text")); 
							msg.open();
						} else {
							MessageBox msg = new MessageBox(mainShell, SWT.OK | SWT.ICON_ERROR);
							msg.setText(Messages.getString("message.db.backup.title")); 
							msg.setMessage(Messages.getString("message.db.backup.error.text")); 
							msg.open();
						}
					} catch (IOException e) {
						new BugReporter(ViewerMenu.this.getDisplay()).openErrorMessage("Viewer -> Backup DB", e);
					}
				}
			}
		});
		mainShellMenuItemBackupDatabase.setEnabled(true);

		MenuItem mainShellMenuItemRestoreDatabase = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemRestoreDatabase.setText(Messages.getString("button.database.restore")); 
		mainShellMenuItemRestoreDatabase.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(SettingsHandler.isLogged()) {
					RestoreDatabaseShell shellResDB = new RestoreDatabaseShell(mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE);
					shellResDB.open();
				}
			}
		});
		mainShellMenuItemRestoreDatabase.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		MenuItem mainShellMenuItemClose = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		mainShellMenuItemClose.setText(Messages.getString("button.close")); 
		mainShellMenuItemClose.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				mainShell.close();
			}
		});

		mainShellSubmenuItemFile.setMenu(mainShellSubmenuFile);

		MenuItem mainShellSubmenuItemTools = new MenuItem(this, SWT.CASCADE);
		mainShellSubmenuItemTools.setText(Messages.getString("viewer.menu.tools")); 
		Menu mainShellSubmenuTools = new Menu(mainShellSubmenuItemTools);
		mainShellSubmenuItemTools.setMenu(mainShellSubmenuTools);

		MenuItem mainShellMenuItemPreferences = new MenuItem(mainShellSubmenuTools, SWT.PUSH);

		mainShellMenuItemPreferences.setText(Messages.getString("viewer.menu.tools.preferences") + "\tF4");  

		Listener preferencesListener = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().getConfigurator().setVisible(true);
			}
		};

		mainShellMenuItemPreferences.addListener(SWT.Selection, preferencesListener);
		mainShellMenuItemPreferences.setAccelerator(SWT.F4);

		MenuItem mainShellMenuItemTaxCalculator = new MenuItem(mainShellSubmenuTools, SWT.PUSH);
		mainShellMenuItemTaxCalculator.setText(Messages.getString("viewer.menu.tools.taxcalculator") + "\tF10");  
		mainShellMenuItemTaxCalculator.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new TaxCalculator(mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}
		});
		mainShellMenuItemTaxCalculator.setAccelerator(SWT.F10);

		MenuItem mainShellMenuItemCurrencyConverter = new MenuItem(mainShellSubmenuTools, SWT.PUSH);
		mainShellMenuItemCurrencyConverter.setText(Messages.getString("viewer.menu.tools.currencycalculator") + "\tF11");  
		mainShellMenuItemCurrencyConverter.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new CurrencyCalculator(mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}

		});
		mainShellMenuItemCurrencyConverter.setAccelerator(SWT.F11);

		MenuItem mainShellMenuItemPrintScreen = new MenuItem(mainShellSubmenuTools, SWT.PUSH);
		mainShellMenuItemPrintScreen.setText(Messages.getString("viewer.menu.tools.printscreen") + "\tAlt+P");  
		mainShellMenuItemPrintScreen.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Composite currentView = ViewerHandler.getViewer().getCurrentView();
				Point tableSize = currentView.getSize();
				GC gc = new GC(currentView);
				final Image image = new Image(currentView.getDisplay(), tableSize.x, tableSize.y);
				try {
					gc.copyArea(image, 0, 0);
					gc.dispose();

					String[] extensions = { "*.png" 
					};
					FileDialog fileDialog = new FileDialog(mainShell, SWT.SAVE);
					fileDialog.setFileName("screen.png"); 
					fileDialog.setFilterExtensions(extensions);
					String filename = fileDialog.open();
					if (filename != null) {
						File file = new File(filename);
						if (!file.exists() || (file.exists() && MessageDialog.openConfirmationMessage(ViewerHandler.getViewer(), Messages.getString("message.file.exists.warning")) == SWT.YES)) {
							ImageLoader loader = new ImageLoader();
							loader.data = new ImageData[] { image.getImageData() };
							loader.backgroundPixel = 0;
							loader.logicalScreenHeight = image.getImageData().height;
							loader.logicalScreenWidth = image.getImageData().width;
							loader.repeatCount = 0; // run forever
							loader.save(filename, SWT.IMAGE_PNG);
						}
					}
				} finally {
					image.dispose();
				}
			}
		});
		mainShellMenuItemPrintScreen.setAccelerator(SWT.ALT | 'P');

		MenuItem mainShellSubmenuItemHelp = new MenuItem(this, SWT.CASCADE);
		mainShellSubmenuItemHelp.setText(Messages.getString("button.help")); 
		Menu mainShellSubmenuHelp = new Menu(mainShellSubmenuItemHelp);
		mainShellSubmenuItemHelp.setMenu(mainShellSubmenuHelp);

		MenuItem mainShellMenuItemAbout = new MenuItem(mainShellSubmenuHelp, SWT.PUSH);

		mainShellMenuItemAbout.setText(Messages.getString("button.about")); 

		Listener aboutListener = new Listener() {

			public void handleEvent(Event event) {

				MessageBox messageBox = new MessageBox(mainShell, SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL | SWT.OK);
				messageBox.setText(Messages.getString("message.help.text")); 
				messageBox.setMessage(Messages.getString("message.help.message")); 
				messageBox.open();
			}

		};

		mainShellMenuItemAbout.addListener(SWT.Selection, aboutListener);

		new MenuItem(mainShellSubmenuHelp, SWT.SEPARATOR);

		MenuItem mainShellMenuItemUpdate = new MenuItem(mainShellSubmenuHelp, SWT.PUSH);

		mainShellMenuItemUpdate.setText(Messages.getString("viewer.menu.help.update") + "\tF12");  
		mainShellMenuItemUpdate.setAccelerator(SWT.F12);
		Listener updateListener = new Listener() {

			public void handleEvent(Event event) {
				new UpdaterWizard(mainShell).open();
			}

		};

		mainShellMenuItemUpdate.addListener(SWT.Selection, updateListener);

		new MenuItem(mainShellSubmenuHelp, SWT.SEPARATOR);

		MenuItem mainShellMenuItemPlugins = new MenuItem(mainShellSubmenuHelp, SWT.PUSH);

		mainShellMenuItemPlugins.setText(Messages.getString("viewer.menu.help.plugins")); 

		Listener pluginsListener = new Listener() {

			public void handleEvent(Event event) {
				new PluginsView(mainShell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}

		};

		mainShellMenuItemPlugins.addListener(SWT.Selection, pluginsListener);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
}
