package pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class DirectoryPage extends Page {

	private Label label;
	private Text text;
	private Button button;
	public static String PAGE_NAME = "DIRECTORY_PAGE"; //$NON-NLS-1$
	public DirectoryPage(Wizard parent) {
		super(parent, Messages.getString("importer.page.directory.title"), PAGE_NAME); //$NON-NLS-1$
	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 10;
		container.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;

		label = new Label(container, SWT.SINGLE);
		label.setText(Messages.getString("importer.page.directory.label")); //$NON-NLS-1$
		label.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		text.setLayoutData(gridData);

		button = new Button(container, SWT.PUSH);
		button.setText("..."); //$NON-NLS-1$
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				String directory = new DirectoryDialog(getShell()).open();
				if (directory != null) {
					text.setText(directory);
					getWizard().getNextButton().setEnabled(true);
				} else if(text.getText().isEmpty()){
					getWizard().getNextButton().setEnabled(false);
				}
				
			}
		});
		setContainer(container);
	}
	
	public String getDirectory() {
		return text.getText();
	}
	
	@Override
	public void onEnterPage() {
		if(text.getText() == null || text.getText().isEmpty()) {
			getWizard().getNextButton().setEnabled(false);	
		}
		super.onEnterPage();
	}
}
