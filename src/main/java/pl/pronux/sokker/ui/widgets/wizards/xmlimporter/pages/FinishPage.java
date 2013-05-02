package pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class FinishPage extends Page {
	public static final String PAGE_NAME = "FINISH_PAGE"; //$NON-NLS-1$
	
	public FinishPage(Wizard parent) {
		super(parent, Messages.getString("importer.page.finishtitle"), PAGE_NAME); //$NON-NLS-1$
	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 30;
		container.setLayout(gridLayout);
	}
	
	@Override
	public void onEnterPage() {
		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getCancelButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);
		getWizard().getFinishButton().setEnabled(true);
		super.onEnterPage();
	}
}
