package pl.pronux.sokker.ui.widgets.wizards.xmlimporter.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class ApplicationsPage extends Page {

	public static final String PAGE_NAME = "APPLICATIONS_PAGE"; 
	public static final String SOKKER_ORGANIZER = "SokkerOrganizer";
	public static final String SOKKER_VIEWER = "SokkerViewer";
	public static final String SOKKER_MANAGER = "SokkerManager";
	public static final String APOLLO = "Apollo";
	private String application = SOKKER_VIEWER;
	private Button svButton;
	private Button soButton;
	private Button smButton;
	private Button apolloButton;
	
	public ApplicationsPage(Wizard parent) {
		super(parent, Messages.getString("importer.page.applicationstitle"), PAGE_NAME); 
	}
	
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 30;
		container.setLayout(gridLayout);

		svButton = new Button(container, SWT.RADIO);
		svButton.setSelection(true);
		svButton.setText(SOKKER_VIEWER); 
		svButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				setApplication(SOKKER_VIEWER);
			}
		});
		soButton = new Button(container, SWT.RADIO);
		soButton.setEnabled(true);
		soButton.setText(SOKKER_ORGANIZER); 
		soButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				setApplication(SOKKER_ORGANIZER);
			}
		});
		smButton = new Button(container, SWT.RADIO);
		smButton.setEnabled(false);
		smButton.setText(SOKKER_MANAGER); 
		apolloButton = new Button(container, SWT.RADIO);
		apolloButton.setEnabled(false);
		apolloButton.setText(APOLLO + String.format(" " + Messages.getString("info.since"), "0.13"));  

		setContainer(container);
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
}
