package pl.pronux.sokker.ui.widgets.wizards.updater;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.DownloadPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.MirrorsPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.PackagesPage;
import pl.pronux.sokker.ui.widgets.wizards.updater.pages.XMLPage;

public class UpdaterWizard extends Wizard {

	private String versionType;
	private String osType;

	private void init() {
		versionType = "stable"; //$NON-NLS-1$

		if (SettingsHandler.IS_WINDOWS) {
			osType = "/windows"; //$NON-NLS-1$
		} else if (SettingsHandler.IS_LINUX) {
			osType = "/linux"; //$NON-NLS-1$
		} else if (SettingsHandler.IS_MACOSX) {
			osType = "/mac"; //$NON-NLS-1$
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

	public String getVersionType() {
		return versionType;
	}

	public String getOsType() {
		return osType;
	}
}
