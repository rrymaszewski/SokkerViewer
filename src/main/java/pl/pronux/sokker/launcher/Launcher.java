package pl.pronux.sokker.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.downloader.Synchronizer;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.ui.Viewer;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.utils.Log;
import pl.pronux.sokker.utils.file.PropertiesChecker;

public class Launcher {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {

		try {
			PropertiesSession properties = PropertiesDatabase.getSession();
			SokkerViewerSettings settings = new SokkerViewerSettingsDao(properties).getSokkerViewerSettings();
			if (settings.isCheckProperties()) {
				new PropertiesChecker().checkAll();
				PropertiesDatabase.reload();
				settings = new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).getSokkerViewerSettings();
				settings.setCheckProperties(false);
				new SokkerViewerSettingsDao(properties).updateSokkerViewerSettings(settings);
			}

			SettingsHandler.setSokkerViewerSettings(settings);

			// base directory settings
			settings.setBaseDirectory(System.getProperty("user.dir")); //$NON-NLS-1$
			ProxySettings proxySettings = SettingsHandler.getSokkerViewerSettings().getProxySettings();

			if (proxySettings.isEnabled()) {
				System.setProperty("proxySet", "true"); //$NON-NLS-1$ //$NON-NLS-2$
				System.setProperty("http.proxyHost", proxySettings.getHostname()); //$NON-NLS-1$
				System.setProperty("http.proxyPort", String.valueOf(proxySettings.getPort())); //$NON-NLS-1$
				System.setProperty("http.proxyUser", proxySettings.getUsername()); //$NON-NLS-1$
				System.setProperty("http.proxyPassword", proxySettings.getPassword()); //$NON-NLS-1$
			}

			if (settings.isCheckProperties()) {
				new PropertiesChecker().checkAll();
				settings.setCheckProperties(false);
				new SokkerViewerSettingsDao(properties).updateSokkerViewerSettings(settings);
			}

			if (args.length == 1 && args[0].equals("--download-only")) { //$NON-NLS-1$
				SQLQuery.setSettings(settings);
				new Synchronizer(settings, Synchronizer.DOWNLOAD_ALL).run(new Monitor());
			} else if (args.length == 0) {
				Display display = new Display();
				try {
					new Viewer(display, SWT.SHELL_TRIM).open();
				} catch (Exception e) {
					new BugReporter(display).openErrorMessage("SokkerViewer", e); //$NON-NLS-1$
				} finally {
					display.dispose();
				}

			} else {
				System.out.println(showHelp());
			}

		} catch (Exception e) {
			Log.error("Error Viewer", e); //$NON-NLS-1$
		} finally {
			Log.close();
			try {
				SQLSession.close();
			} catch (SQLException e) {
			}
		}
	}

	private static String showHelp() {
		return "--donwload-only\r\n" + //$NON-NLS-1$
			   "--help"; //$NON-NLS-1$
	}
}
