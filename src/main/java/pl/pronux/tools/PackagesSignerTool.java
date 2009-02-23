package pl.pronux.tools;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.tools.pages.ConfigurationPage;
import pl.pronux.tools.pages.DestinationPage;
import pl.pronux.tools.pages.FilesPage;
import pl.pronux.tools.pages.FinishPage;
import pl.pronux.tools.pages.OldXMLPage;

public class PackagesSignerTool extends Wizard {

	public PackagesSignerTool(Shell parent, int decorator) {
		super(parent, decorator);
		addPage(new OldXMLPage(this));
		addPage(new FilesPage(this));
		addPage(new ConfigurationPage(this));
		addPage(new DestinationPage(this));
		addPage(new FinishPage(this));
	}
	
	public PackagesSignerTool(Display display) {
		super(display);
		addPage(new OldXMLPage(this));
		addPage(new FilesPage(this));
		addPage(new ConfigurationPage(this));
		addPage(new DestinationPage(this));
		addPage(new FinishPage(this));
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		PackagesSignerTool wizard = new PackagesSignerTool(display);
		wizard.open();
	}
}
