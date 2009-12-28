package pl.pronux.tools.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class OldXMLPage extends Page {

	private Label label;
	private Text windowsText;
	private Text linuxText;
	private Text macText;
	private Button windowsButton;
	private Button linuxButton;
	private Button macButton;
	public static String PAGE_NAME = "OLDXML_PAGE";
	public OldXMLPage(Wizard parent) {
		super(parent, "Old XML page", PAGE_NAME);
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
		label.setText("Windows");
		label.setLayoutData(gridData);

		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		windowsText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		windowsText.setLayoutData(gridData);

		windowsButton = new Button(container, SWT.PUSH);
		windowsButton.setText("...");
		windowsButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				String file = new FileDialog(getShell()).open();
				if (file != null) {
					windowsText.setText(file);
				} 
				onChanged();
			}
		});
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;

		label = new Label(container, SWT.SINGLE);
		label.setText("Linux");
		label.setLayoutData(gridData);
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		linuxText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		linuxText.setLayoutData(gridData);

		linuxButton = new Button(container, SWT.PUSH);
		linuxButton.setText("...");
		linuxButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				String file = new FileDialog(getShell()).open();
				if (file != null) {
					linuxText.setText(file);
				} 
				onChanged();
			}
		});
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;

		label = new Label(container, SWT.SINGLE);
		label.setText("MAC");
		label.setLayoutData(gridData);
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		macText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		macText.setLayoutData(gridData);

		macButton = new Button(container, SWT.PUSH);
		macButton.setText("...");
		macButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				String file = new FileDialog(getShell()).open();
				if (file != null) {
					macText.setText(file);
				} 
				onChanged();
			}
		});
		setContainer(container);
	}
	
	public String getWindowsXMLFile() {
		return windowsText.getText();
	}
	
	public String getLinuxXMLFile() {
		return linuxText.getText();
	}
	
	public String getMacXMLFile() {
		return macText.getText();
	}
	
	@Override
	public void onEnterPage() {
		if(windowsText.getText() == null || windowsText.getText().isEmpty()) {
			getWizard().getNextButton().setEnabled(false);	
		}
		super.onEnterPage();
	}
	
	private void onChanged() {
		if(windowsText.getText() == null || windowsText.getText().isEmpty() || linuxText.getText() == null || linuxText.getText().isEmpty() || macText.getText() == null || macText.getText().isEmpty()) {
			getWizard().getNextButton().setEnabled(false);
		} else {
			getWizard().getNextButton().setEnabled(true);
		}
	}
}
