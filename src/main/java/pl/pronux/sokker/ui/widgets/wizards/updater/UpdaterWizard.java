package pl.pronux.sokker.ui.widgets.wizards.updater;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.PropertiesSession;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.DownloadPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.MirrorsPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.PackagesPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.XMLPage;

public class UpdaterWizard extends Wizard {
	private String versionType;
	private String osType;

	private void init() {
		if (SV.VERSION_TYPE == SV.TESTING) {
			versionType = "testing"; //$NON-NLS-1$
		} else {
			versionType = "stable"; //$NON-NLS-1$
		}

		String osName = System.getProperty("os.name"); //$NON-NLS-1$
		if (osName != null) {
			if (osName.toLowerCase().contains("mac")) { //$NON-NLS-1$
				osType = "/mac"; //$NON-NLS-1$
			} else if (osName.toLowerCase().contains("linux")) { //$NON-NLS-1$
				osType = "/linux"; //$NON-NLS-1$
			} else {
				osType = "/windows"; //$NON-NLS-1$
			}

		} else {
			if (SettingsHandler.IS_WINDOWS) {
				osType = "/windows"; //$NON-NLS-1$
			} else if (SettingsHandler.IS_LINUX) {
				osType = "/linux"; //$NON-NLS-1$
			} else if (SettingsHandler.IS_MACOSX) {
				osType = "/mac"; //$NON-NLS-1$
			}
		}
	}

	public UpdaterWizard(Shell parent) {
		super(parent);
		addPage(new MirrorsPage(this));
		addPage(new XMLPage(this));
		addPage(new PackagesPage(this));
		addPage(new DownloadPage(this));
		init();

	}

	public UpdaterWizard(Display display) {
		super(display);
		addPage(new MirrorsPage(this));
		addPage(new XMLPage(this));
		addPage(new PackagesPage(this));
		addPage(new DownloadPage(this));
		init();
	}

	public static void main(String[] args) {

		PropertiesSession properties;
		Display display = new Display();
		try {
			properties = PropertiesDatabase.getSession();

			SokkerViewerSettings settings = new SokkerViewerSettingsDao(properties).getSokkerViewerSettings();
			SettingsHandler.setSokkerViewerSettings(settings);

			// base directory settings
			settings.setBaseDirectory(System.getProperty("user.dir")); //$NON-NLS-1$

			
			UpdaterWizard wizard = new UpdaterWizard(display);
			wizard.open();
		} catch (FileNotFoundException e) {
			new BugReporter(display).openErrorMessage("Wizard", e); //$NON-NLS-1$
		} catch (IOException e) {
			new BugReporter(display).openErrorMessage("Wizard", e); //$NON-NLS-1$
		}
	}

	public String getVersionType() {
		return versionType;
	}

	public String getOsType() {
		return osType;
	}
}
