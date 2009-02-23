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
	private MenuItem _mainShellMenuItemAbout;

	private MenuItem _mainShellMenuItemBackupDatabase;

	private MenuItem _mainShellMenuItemClose;

	private MenuItem _mainShellMenuItemConnect;

	private MenuItem _mainShellMenuItemGet;

	private MenuItem _mainShellMenuItemImportXML;

	private MenuItem _mainShellMenuItemRestoreDatabase;

	private Menu _mainShellSubmenuHelp;

	private MenuItem _mainShellSubmenuItemFile;

	private MenuItem _mainShellSubmenuItemHelp;

	private MenuItem _mainShellMenuItemUpdate;

	private MenuItem _mainShellSubmenuItemTools;

	private Menu _mainShellSubmenuTools;

	private MenuItem _mainShellMenuItemPreferences;
	private MenuItem _mainShellMenuItemPlugins;
	private MenuItem _mainShellMenuItemCurrencyConverter;
	private MenuItem _mainShellMenuItemTaxCalculator;
	private MenuItem _mainShellMenuItemRefresh;

	private MenuItem _mainShellMenuItemPrintScreen;

	private Shell _mainShell;

	public ViewerMenu(Decorations decorations, int style) {
		super(decorations, style);

		_mainShell = decorations.getShell();

		Menu mainShellSubmenuFile;

		_mainShellSubmenuItemFile = new MenuItem(this, SWT.CASCADE);
		_mainShellSubmenuItemFile.setText(Messages.getString("button.file")); //$NON-NLS-1$
		mainShellSubmenuFile = new Menu(_mainShellSubmenuItemFile);

		_mainShellMenuItemConnect = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemConnect.setText(Messages.getString("button.connect") + "\tF2"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemConnect.setAccelerator(SWT.F2);
		Listener mainShellConfList = new Listener() {
			public void handleEvent(Event event) {
				new LoginShell(_mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}
		};
		_mainShellMenuItemConnect.addListener(SWT.Selection, mainShellConfList);

		_mainShellMenuItemGet = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemGet.setText(Messages.getString("button.update") + "\tF3"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemGet.setAccelerator(SWT.F3);
		Listener mainShellGetList = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(true));
			}
		};
		_mainShellMenuItemGet.addListener(SWT.Selection, mainShellGetList);
		_mainShellMenuItemGet.setEnabled(true);

		_mainShellMenuItemRefresh = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemRefresh.setText(Messages.getString("button.reload") + "\tF5"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemRefresh.setAccelerator(SWT.F5);
		Listener mainShellRefreshList = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(false));
			}
		};
		_mainShellMenuItemRefresh.addListener(SWT.Selection, mainShellRefreshList);
		_mainShellMenuItemRefresh.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		_mainShellMenuItemImportXML = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemImportXML.setText(Messages.getString("button.import.xml")); //$NON-NLS-1$
		_mainShellMenuItemImportXML.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(SettingsHandler.isLogged()) {
					ImporterWizard importer = new ImporterWizard(_mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE);
					importer.open();
				}
			}
		});
		_mainShellMenuItemImportXML.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		_mainShellMenuItemBackupDatabase = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemBackupDatabase.setText(Messages.getString("button.database.backup")); //$NON-NLS-1$
		_mainShellMenuItemBackupDatabase.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					boolean operation = Database.backup(SettingsHandler.getSokkerViewerSettings());
					if (operation) {
						MessageBox msg = new MessageBox(_mainShell, SWT.OK | SWT.ICON_INFORMATION);
						msg.setText(Messages.getString("message.db.backup.title")); //$NON-NLS-1$
						msg.setMessage(Messages.getString("message.db.backup.text")); //$NON-NLS-1$
						msg.open();
					} else {
						MessageBox msg = new MessageBox(_mainShell, SWT.OK | SWT.ICON_ERROR);
						msg.setText(Messages.getString("message.db.backup.title")); //$NON-NLS-1$
						msg.setMessage(Messages.getString("message.db.backup.error.text")); //$NON-NLS-1$
						msg.open();
					}
				} catch (IOException e) {
					new BugReporter(ViewerMenu.this.getDisplay()).openErrorMessage("Viewer -> Backup DB", e);
				}
			}
		});
		_mainShellMenuItemBackupDatabase.setEnabled(true);

		_mainShellMenuItemRestoreDatabase = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemRestoreDatabase.setText(Messages.getString("button.database.restore")); //$NON-NLS-1$
		_mainShellMenuItemRestoreDatabase.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				RestoreDatabaseShell shellResDB = new RestoreDatabaseShell(_mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE);
				shellResDB.open();
			}
		});
		_mainShellMenuItemRestoreDatabase.setEnabled(true);

		new MenuItem(mainShellSubmenuFile, SWT.SEPARATOR);

		_mainShellMenuItemClose = new MenuItem(mainShellSubmenuFile, SWT.PUSH);
		_mainShellMenuItemClose.setText(Messages.getString("button.close")); //$NON-NLS-1$
		_mainShellMenuItemClose.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				_mainShell.close();
			}
		});

		_mainShellSubmenuItemFile.setMenu(mainShellSubmenuFile);

		_mainShellSubmenuItemTools = new MenuItem(this, SWT.CASCADE);
		_mainShellSubmenuItemTools.setText(Messages.getString("viewer.menu.tools")); //$NON-NLS-1$
		_mainShellSubmenuTools = new Menu(_mainShellSubmenuItemTools);
		_mainShellSubmenuItemTools.setMenu(_mainShellSubmenuTools);

		_mainShellMenuItemPreferences = new MenuItem(_mainShellSubmenuTools, SWT.PUSH);

		_mainShellMenuItemPreferences.setText(Messages.getString("viewer.menu.tools.preferences") + "\tF4"); //$NON-NLS-1$ //$NON-NLS-2$

		Listener preferencesListener = new Listener() {
			public void handleEvent(Event event) {
				ViewerHandler.getViewer().getConfigurator().setVisible(true);
			}
		};

		_mainShellMenuItemPreferences.addListener(SWT.Selection, preferencesListener);
		_mainShellMenuItemPreferences.setAccelerator(SWT.F4);

		_mainShellMenuItemTaxCalculator = new MenuItem(_mainShellSubmenuTools, SWT.PUSH);
		_mainShellMenuItemTaxCalculator.setText(Messages.getString("viewer.menu.tools.taxcalculator") + "\tF10"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemTaxCalculator.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new TaxCalculator(_mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}
		});
		_mainShellMenuItemTaxCalculator.setAccelerator(SWT.F10);

		_mainShellMenuItemCurrencyConverter = new MenuItem(_mainShellSubmenuTools, SWT.PUSH);
		_mainShellMenuItemCurrencyConverter.setText(Messages.getString("viewer.menu.tools.currencycalculator") + "\tF11"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemCurrencyConverter.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				new CurrencyCalculator(_mainShell, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
			}

		});
		_mainShellMenuItemCurrencyConverter.setAccelerator(SWT.F11);

		_mainShellMenuItemPrintScreen = new MenuItem(_mainShellSubmenuTools, SWT.PUSH);
		_mainShellMenuItemPrintScreen.setText(Messages.getString("viewer.menu.tools.printscreen") + "\tAlt+P"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemPrintScreen.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Composite currentView = ViewerHandler.getViewer().getCurrentView();
				Point tableSize = currentView.getSize();
				GC gc = new GC(currentView);
				final Image image = new Image(currentView.getDisplay(), tableSize.x, tableSize.y);
				try {
					gc.copyArea(image, 0, 0);
					gc.dispose();

					String[] extensions = { "*.png" //$NON-NLS-1$
					};
					FileDialog fileDialog = new FileDialog(_mainShell, SWT.SAVE);
					fileDialog.setFileName("screen.png"); //$NON-NLS-1$
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
		_mainShellMenuItemPrintScreen.setAccelerator(SWT.ALT | 'P');

		_mainShellSubmenuItemHelp = new MenuItem(this, SWT.CASCADE);
		_mainShellSubmenuItemHelp.setText(Messages.getString("button.help")); //$NON-NLS-1$
		_mainShellSubmenuHelp = new Menu(_mainShellSubmenuItemHelp);
		_mainShellSubmenuItemHelp.setMenu(_mainShellSubmenuHelp);

		_mainShellMenuItemAbout = new MenuItem(_mainShellSubmenuHelp, SWT.PUSH);

		_mainShellMenuItemAbout.setText(Messages.getString("button.about")); //$NON-NLS-1$

		Listener aboutListener = new Listener() {

			public void handleEvent(Event event) {

				MessageBox messageBox = new MessageBox(_mainShell, SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL | SWT.OK);
				messageBox.setText(Messages.getString("message.help.text")); //$NON-NLS-1$
				messageBox.setMessage(Messages.getString("message.help.message")); //$NON-NLS-1$
				messageBox.open();
			}

		};

		_mainShellMenuItemAbout.addListener(SWT.Selection, aboutListener);

		new MenuItem(_mainShellSubmenuHelp, SWT.SEPARATOR);

		_mainShellMenuItemUpdate = new MenuItem(_mainShellSubmenuHelp, SWT.PUSH);

		_mainShellMenuItemUpdate.setText(Messages.getString("viewer.menu.help.update") + "\tF12"); //$NON-NLS-1$ //$NON-NLS-2$
		_mainShellMenuItemUpdate.setAccelerator(SWT.F12);
		Listener updateListener = new Listener() {

			public void handleEvent(Event event) {
				new UpdaterWizard(_mainShell).open();
			}

		};

		_mainShellMenuItemUpdate.addListener(SWT.Selection, updateListener);

		new MenuItem(_mainShellSubmenuHelp, SWT.SEPARATOR);

		_mainShellMenuItemPlugins = new MenuItem(_mainShellSubmenuHelp, SWT.PUSH);

		_mainShellMenuItemPlugins.setText(Messages.getString("viewer.menu.help.plugins")); //$NON-NLS-1$

		Listener pluginsListener = new Listener() {

			public void handleEvent(Event event) {
				new PluginsView(_mainShell, SWT.CLOSE | SWT.APPLICATION_MODAL).open();
			}

		};

		_mainShellMenuItemPlugins.addListener(SWT.Selection, pluginsListener);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
}
