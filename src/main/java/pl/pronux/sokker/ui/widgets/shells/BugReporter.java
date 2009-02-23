package pl.pronux.sokker.ui.widgets.shells;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.utils.file.SVLogger;
import pl.pronux.sokker.utils.net.BugReportAction;

public class BugReporter extends Shell {

	private SokkerViewerSettings settings;

	private Label imageLabel;

	private Label messageLabel;

	private Button detailButton;

	private Button reportButton;

	private Text detailText;

	private Button closeButton;

	private void init() {
		Display display = BugReporter.this.getDisplay();
		Monitor monitor = display.getPrimaryMonitor();
		settings = SettingsHandler.getSokkerViewerSettings();
		this.setText("Error"); //$NON-NLS-1$
		this.setSize(450, 150);
		this.setLayout(new FormLayout());

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = monitor.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

		final Image image = display.getSystemImage(SWT.ICON_ERROR);

		FormData formData;

		formData = new FormData(60, 60);
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);

		imageLabel = new Label(this, SWT.NONE);
		imageLabel.setLayoutData(formData);
		imageLabel.setImage(image);

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(imageLabel, 5);
		formData.right = new FormAttachment(100, 0);
		formData.height = 60;

		messageLabel = new Label(this, SWT.WRAP);
		messageLabel.setLayoutData(formData);

		formData = new FormData();
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, -5);
		
		closeButton = new Button(this, SWT.NONE);
		closeButton.setText(Messages.getString("button.close"));
		closeButton.setLayoutData(formData);
		closeButton.pack();
		closeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				BugReporter.this.close();
			}
		});
		
		formData = new FormData();
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(closeButton, -5);


		detailButton = new Button(this, SWT.NONE);
		detailButton.setText(Messages.getString("button.details"));
		detailButton.setLayoutData(formData);
		detailButton.pack();

		detailButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (detailText.isVisible()) {
					detailText.setVisible(false);
					BugReporter.this.setSize(450, 150);
				} else {
					BugReporter.this.setSize(450, 400);
					detailText.setVisible(true);
				}
			}
		});

		formData = new FormData();
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(detailButton, -5);

		reportButton = new Button(this, SWT.NONE);
		reportButton.setText(Messages.getString("button.report.bug"));
		reportButton.setLayoutData(formData);
		reportButton.pack();
		reportButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				String value = "";
				String message = "";
				try {
					message += messageLabel.getText() + "##" + detailText.getText();
					value = new BugReportAction().sendBug(message, settings);
					if(value != null && value.equals("1")) {
						MessageDialog.openInformationMessage(BugReporter.this, Messages.getString("BugReporter.thankyou"));	
					} else {
						MessageDialog.openErrorMessage(BugReporter.this, Messages.getString("BugReporter.error"));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openErrorMessage(BugReporter.this, Messages.getString("error.send"));
				}
				BugReporter.this.close();
			}
		});
		
		formData = new FormData();
		formData.top = new FormAttachment(messageLabel, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(detailButton, -5);

		detailText = new Text(this, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		detailText.setLayoutData(formData);
		detailText.setVisible(false);

	}
	
	public BugReporter(Shell shell) {
		super(shell);
		init();
	}
	
	public BugReporter(Display display) {
		super(display);
		init();
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public void openErrorMessage(final String message) {
		new SVLogger(Level.WARNING, message);
		messageLabel.setText(message);
		detailButton.setEnabled(false);
		detailText.setText("");
		open();
	}

	@Override
	public void open() {
		super.open();
		while (!this.isDisposed()) {
			if (!this.getDisplay().readAndDispatch()) {
				this.getDisplay().sleep();
			}
		}
	}

	public void openErrorMessage(final String message, final Throwable e) {
		new SVLogger(Level.WARNING, message, e);
		messageLabel.setText(String.format("%s", message)); //$NON-NLS-1$

		detailText.setText(String.format("%s\n%s", detailText.getText(), e.toString())); //$NON-NLS-1$
		for (int i = 0; i < e.getStackTrace().length; i++) {
			detailText.setText(String.format("%s\n%s", detailText.getText(), e.getStackTrace()[i])); //$NON-NLS-1$
		}
		Throwable caused = e.getCause();
		while (caused != null) {
			detailText.setText(String.format("%s\nCaused by: %s", detailText.getText(), caused.toString())); //$NON-NLS-1$
			for (int i = 0; i < caused.getStackTrace().length; i++) {
				detailText.setText(String.format("%s\n%s", detailText.getText(), caused.getStackTrace()[i])); //$NON-NLS-1$
			}
			caused = caused.getCause();
		}
		open();
	}

}