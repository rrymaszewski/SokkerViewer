package pl.pronux.sokker.ui.widgets.wizards.xmlimporter;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages.ApplicationsPage;
import pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages.ChooseFilePage;
import pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages.DirectoryPage;
import pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages.ImportPage;

public class ImporterWizard extends Wizard {

	public ImporterWizard(Shell parent, int decorator) {
		super(parent, decorator);
		addPage(new ApplicationsPage(this));
		addPage(new DirectoryPage(this));
		addPage(new ChooseFilePage(this));
		addPage(new ImportPage(this));
//		addPage(new FinishPage(this));
	}
	
	public ImporterWizard(Display display) {
		super(display);
		addPage(new ApplicationsPage(this));
		addPage(new DirectoryPage(this));
		addPage(new ChooseFilePage(this));
		addPage(new ImportPage(this));
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		Cache.setClub(new Club());
		Cache.getClub().setId(17008);
		ImporterWizard wizard = new ImporterWizard(display);
		wizard.open();
	}
}
