package pl.pronux.sokker.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.SettingsManager;
import pl.pronux.sokker.data.properties.SVProperties;
import pl.pronux.sokker.downloader.VersionDownloader;
import pl.pronux.sokker.enums.Language;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.resources.PropertiesResources;
import pl.pronux.sokker.ui.actions.CoreAction;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.configure.Configurator;
import pl.pronux.sokker.ui.events.UpdateEvent;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.StatusBar;
import pl.pronux.sokker.ui.widgets.dialogs.ProgressBarDialog;
import pl.pronux.sokker.ui.widgets.groups.TreeGroup;
import pl.pronux.sokker.ui.widgets.menus.ViewerMenu;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.shells.LoginShell;
import pl.pronux.sokker.ui.widgets.shells.Splash;
import pl.pronux.sokker.ui.widgets.tray.SVTrayItem;
import pl.pronux.sokker.ui.widgets.tree.SVTree;
import pl.pronux.sokker.ui.widgets.wizards.updater.UpdaterWizard;
import pl.pronux.sokker.utils.Log;

public class Viewer extends Shell {

	private SettingsManager settingsManager = SettingsManager.getInstance();
	
	private Properties defaultProperties;

	private Display display;

	private Sash mainShellSashVertical;

	private Monitor monitor;

	private StatusBar statusBar;

	private SVProperties userProperties;

	private List<IPlugin> plugins;

	private Clipboard cb;

	private Configurator configurator;

	private Composite currentView;

	private Font fontCurrent;

	private SokkerViewerSettings settings;

	private Splash splash;

	private TrayItem trayItem;

	private TreeGroup treeGroup;

	private Group viewGroup;

	public Viewer(Display display, int style) throws IOException, SVException {
		super(display, style);
		monitor = display.getPrimaryMonitor();
		// checking OS
		ViewerHandler.setViewer(this);

		this.display = display;

		splash = new Splash(display, SWT.ON_TOP);
		splash.setStatus(this.getClass().getSimpleName());
		splash.open();

		addColors();

		cb = new Clipboard(display);

		ViewerHandler.setClipboard(cb);
		settings = SettingsHandler.getSokkerViewerSettings();

		// loading language properties
		if (settings.getLangCode().isEmpty()) {
			settings.setLangCode(Language.en_EN.name());
			settingsManager.updateSettings(settings);
		}

		String[] table = settings.getLangCode().split("_"); 
		// Messages = Messages.getLangResources(new Locale(table[0], table[1]));
		Messages.setDefault(new Locale(table[0], table[1]));

		/*
		 * SETTINGS MAIN VIEW
		 */

		// add body
		this.setLayout(new FormLayout());
		// set title of mainShell
		this.setText(Messages.getString("mainShell.title") + " " + SV.SK_VERSION);  

		this.setLocation(monitor.getClientArea().x, monitor.getClientArea().y);
		this.setSize(monitor.getClientArea().width, monitor.getClientArea().height);
		this.setEnabled(false);

		// settings mainShell ICO
		this.setImage(ImageResources.getImageResources("sokkerViewer_ico[32x32].png")); 

		// adding listener for checking if window is close
		this.addListener(SWT.Close, new Listener() {

			public void handleEvent(Event event) {
				if (settings.isInfoClose()) {
					MessageBox messageBox = new MessageBox(Viewer.this, SWT.ICON_QUESTION | SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
					messageBox.setText(Messages.getString("message.exit.title")); 
					messageBox.setMessage(Messages.getString("message.mainShell.exit.text")); 
					event.doit = messageBox.open() == SWT.YES;
				}
			}
		});

		// settings fonts
		addFonts();

		addTrayItem(this);
		defaultProperties = PropertiesResources.getProperties("default.properties"); 
		SettingsHandler.setDefaultProperties(defaultProperties);

		// loading default colors
		if (new File(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "user.properties").exists()) {  
			userProperties = new SVProperties();
			userProperties.loadFile(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "user.properties");  
			SettingsHandler.setUserProperties(userProperties);
			ConfigBean.setDefaults(userProperties);
		} else {
			ConfigBean.setDefaults(defaultProperties);
		}

		// adding menu
		this.setMenuBar(new ViewerMenu(this, SWT.BAR));

		// adding statusBar
		addStatusBar(this);

		// adding sash
		addSashVertical(this);

		// adding tree
		addTree(this);

		getVersionInfo();

		/*
		 * SETTINGS FOR VIEW
		 */

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(statusBar, 0);
		formData.left = new FormAttachment(mainShellSashVertical, 0);
		formData.right = new FormAttachment(100, -5);

		viewGroup = new Group(this, SWT.NONE);
		viewGroup.setLayout(new FormLayout());
		viewGroup.setLayoutData(formData);

		SVProperties pluginsProperties = new SVProperties();

		pluginsProperties.loadFile(settings.getBaseDirectory() + File.separator + "settings" + File.separator + "plugins.properties");  
		String[] viewClass = pluginsProperties.getProperty("plugins").split(";");  

		plugins = new ArrayList<IPlugin>();

		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		for (int i = 0; i < viewClass.length; i++) {
			if (Integer.valueOf(pluginsProperties.getProperty(viewClass[i] + ".turn")).intValue() == 0) { 
				continue;
			}
			IPlugin view = null;
			try {
				Class<?> runnerFactory = Class.forName(viewClass[i]);
				Constructor<?>[] constructors = runnerFactory.getConstructors();
				for (int j = 0; j < constructors.length; j++) {
					if (constructors[j].getParameterTypes().length == 0) {
						view = (IPlugin) constructors[j].newInstance();
					}
				}
				splash.setStatus(view.getClass().getSimpleName());
				view.setSettings(settings);
				view.init(new Composite(viewGroup, SWT.NONE));
				view.getComposite().setLayoutData(formData);
				view.setTreeItem(new TreeItem(Viewer.this.getTree(), SWT.NONE));
				view.getInfo();
				view.getTreeItem().setData(IPlugin.IDENTIFIER, view.getComposite());
				view.getComposite().setVisible(false);
				plugins.add(view);
			} catch (IllegalArgumentException e) {
				new BugReporter(display).openErrorMessage("Viewer", e);
			} catch (InstantiationException e) {
				new BugReporter(display).openErrorMessage("Viewer", e);
			} catch (IllegalAccessException e) {
				new BugReporter(display).openErrorMessage("Viewer", e);
			} catch (InvocationTargetException e) {
				new BugReporter(display).openErrorMessage("Viewer", e);
			} catch (ClassNotFoundException e) {
				new BugReporter(display).openErrorMessage("Viewer", e);
			}

		}

		// settings current ViewComposite
		if (plugins.size() > 0) {
			currentView = ((IPlugin) plugins.get(0)).getComposite();
			currentView.setVisible(true);
		}

		configurator = new Configurator(this, SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.RESIZE);
		configurator.setVisible(false);

		this.addListener(IEvents.LOAD_DATA, new Listener() {

			public void handleEvent(Event event) {
				if (event instanceof UpdateEvent) {
					UpdateEvent updateEvent = (UpdateEvent) event;

					final ProgressBarDialog dialog = new ProgressBarDialog(Viewer.this, SWT.PRIMARY_MODAL | SWT.CLOSE);
					try {
						dialog.run(false, false, true, new CoreAction(updateEvent.isUpdate()));
					} catch (InterruptedException e) {
						new BugReporter(Viewer.this).openErrorMessage("Viewer", e);
					} catch (InvocationTargetException e) {
						new BugReporter(Viewer.this).openErrorMessage("Viewer", e);
					}

					Thread monitorThread = new Thread(new Runnable() {

						public void run() {
							final pl.pronux.sokker.ui.widgets.custom.Monitor monitor = dialog.getProgressMonitor();

							while (!monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
								}
							}

							if (monitor.isCanceled() || monitor.isInterrupted()) {
								Viewer.this.clear();
							}
						}
					});
					monitorThread.start();
				}
			}
		});

		splash.close();
	}

	@Override
	public void open() {
		if (settings.isStartup()) {
			this.notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(settings.isUpdate()));
			this.setEnabled(true);
		} else {
			new LoginShell(this, SWT.PRIMARY_MODAL | SWT.CLOSE).open();
		}
		super.open();
		while (!this.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		cb.dispose();
	}

	private void addColors() {
		ConfigBean.setColorDecrease(ColorResources.getColor(255, 210, 210));
		ConfigBean.setColorDecreaseDescription(ColorResources.getColor(255, 0, 0));
		ConfigBean.setColorError(ColorResources.getColor(255, 0, 0));
		ConfigBean.setColorFont(ColorResources.getColor(255, 0, 0));
		ConfigBean.setColorIncrease(ColorResources.getColor(233, 252, 224));
		ConfigBean.setColorIncreaseDescription(ColorResources.getColor(10, 150, 0));
		ConfigBean.setColorInjuryBg(ColorResources.getColor(255, 255, 255));
		ConfigBean.setColorInjuryFg(ColorResources.getColor(255, 0, 0));
		ConfigBean.setColorNewTableObject(ColorResources.getColor(220, 222, 245));
		ConfigBean.setColorNewTreeObject(ColorResources.getColor(0, 0, 255));
		ConfigBean.setColorTrainedJunior(ColorResources.getColor(10, 150, 0));
		ConfigBean.setColorTransferList(ColorResources.getColor(221, 255, 255));
	}

	private void addFonts() {
		// ustawiam fonty
		ConfigBean.setFontCurrent(this.getFont());
		fontCurrent = ConfigBean.getFontCurrent();

		if (SettingsHandler.IS_WINDOWS) {
			ConfigBean.setFontMain(Fonts.getFont(display, fontCurrent.getFontData()[0].getName(), fontCurrent.getFontData()[0].height, SWT.NORMAL));
			ConfigBean.setFontDescription(Fonts.getFont(display,
														"Bitstream Vera Sans Mono, Luxi Mono,Nimbus Mono L", fontCurrent.getFontData()[0].height, SWT.NORMAL)); 
			ConfigBean.setFontTable(Fonts.getFont(display, fontCurrent.getFontData()[0].getName(), fontCurrent.getFontData()[0].height, SWT.NORMAL));
			ConfigBean.setFontItalic(Fonts.getFont(display, fontCurrent.getFontData()[0].getName(), fontCurrent.getFontData()[0].height, SWT.ITALIC));
		} else {
			ConfigBean.setFontMain(Fonts.getFont(display, "Arial", fontCurrent.getFontData()[0].height, SWT.NORMAL));
			ConfigBean.setFontDescription(Fonts.getFont(display, "Courier New", fontCurrent.getFontData()[0].height + 1, SWT.NORMAL));
			ConfigBean.setFontTable(Fonts.getFont(display, "Arial", fontCurrent.getFontData()[0].height, SWT.NORMAL));
			ConfigBean.setFontItalic(Fonts.getFont(display, "Courier New", fontCurrent.getFontData()[0].height + 1, SWT.ITALIC));
		}
	}

	private void addSashVertical(Shell parent) {
		mainShellSashVertical = new Sash(parent, SWT.VERTICAL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(statusBar, 0);
		formData.left = new FormAttachment(0, 200);
		mainShellSashVertical.setLayoutData(formData);
		mainShellSashVertical.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				((FormData) mainShellSashVertical.getLayoutData()).left = new FormAttachment(0, event.x);
				mainShellSashVertical.getParent().layout();
			}
		});
	}

	private void addStatusBar(Shell parent) {

		FormData statusBarFormData = new FormData();
		statusBarFormData.top = new FormAttachment(100, -20);
		statusBarFormData.bottom = new FormAttachment(100, 0);
		statusBarFormData.left = new FormAttachment(0, 5);
		statusBarFormData.right = new FormAttachment(100, -5);

		statusBar = new StatusBar(parent, SWT.NONE);
		statusBar.setLayoutData(statusBarFormData);

	}

	private void addTrayItem(Shell parent) {
		final Tray tray = parent.getDisplay().getSystemTray();
		if (tray != null) {
			trayItem = new SVTrayItem(tray, SWT.NONE);
		}
	}

	private void addTree(Shell parent) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(statusBar, 0);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(mainShellSashVertical, 0);

		treeGroup = new TreeGroup(parent, SWT.NONE);
		treeGroup.setLayoutData(formData);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public void clear() {
		DisplayHandler.getDisplay().syncExec(new Runnable() {

			public void run() {
				for (int i = 0; i < plugins.size(); i++) {
					plugins.get(i).clear();
				}
				if (plugins.size() > 0) {
					showView(plugins.get(0).getComposite());
				}
				Viewer.this.getTree().selectTopItem();
			}
		});
	}

	public SVTree getTree() {
		return this.treeGroup.getTree();
	}

	public Configurator getConfigurator() {
		return configurator;
	}

	public Composite getCurrentView() {
		return currentView;
	}

	private void getVersionInfo() {

		new Thread() {

			public void run() {
				try {
					final String version = new VersionDownloader(settings).getVersion();
					if (version != null && !version.equals(VersionDownloader.NO_UPDATES)) {
						display.asyncExec(new Runnable() {

							public void run() {
								statusBar.setVersion(Messages.getString("statusBar.versionLabel.text") + version); 
								statusBar.getVersionLabel().addListener(SWT.MouseDoubleClick, new Listener() {

									public void handleEvent(Event event) {
										new UpdaterWizard(Viewer.this).open();
										statusBar.getVersionLabel().setData("listener", this);
									}
								});

								if (trayItem != null) {
									final ToolTip trayToolTip = new ToolTip(Viewer.this, SWT.BALLOON | SWT.ICON_INFORMATION);
									trayItem.setToolTip(trayToolTip);
									trayToolTip.setMessage(String.format(Messages.getString("message.update.info"), new Object[] { version }));
									trayToolTip.setAutoHide(true);
									trayToolTip.setVisible(true);
								}
							}
						});
					}
				} catch (Exception e) {
					Log.warning("Version Info", e);
				}
			}
		}.start();

	}

	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}

	public void setCurrentView(Composite currentView) {
		this.currentView = currentView;
	}

	public void setPlugin(final IPlugin plugin) {
		DisplayHandler.getDisplay().syncExec(new Runnable() {

			public void run() {
				plugin.set();
			}
		});
	}

	public void setView() {
		this.getDisplay().syncExec(new Runnable() {

			public void run() {
				Viewer.this.getConfigurator().setView();
				Viewer.this.update();
				Viewer.this.layout();
			}
		});
	}

	public void showView(Composite composite) {
		currentView.setVisible(false);
		currentView = composite;
		currentView.setVisible(true);
	}

	public List<IPlugin> getPlugins() {
		return plugins;
	}

	public void setPluginsList(List<IPlugin> pluginsList) {
		this.plugins = pluginsList;
	}

	public void setLastUpdateDate(final String date) {
		this.getDisplay().syncExec(new Runnable() {

			public void run() {
				statusBar.setLastDate(date);
			}
		});
	}

}
