package pl.pronux.sokker.ui.widgets.shells;

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.MatchesManager;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;

public class AddMatchShell extends Shell {
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	private MatchesManager matchesManager = MatchesManager.instance();
	
	public AddMatchShell(final Shell parent, int style) {
		super(parent, style);
		this.setSize(new Point(300, 120));

		this.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 10);

		CLabel label = new CLabel(this, SWT.NONE);
		label.setText(Messages.getString("addmatchshell.label")); //$NON-NLS-1$
		label.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(label, 5);

		final Text text = new Text(this, SWT.BORDER);
		text.setLayoutData(formData);
		// text.addListener(SWT.Verify, new VerifyDigitsAction());

		formData = new FormData();
		formData.left = new FormAttachment(text, 0, SWT.CENTER);
		formData.top = new FormAttachment(text, 5);

		Button button = new Button(this, SWT.PUSH);
		button.setText(Messages.getString("button.import")); //$NON-NLS-1$
		button.setLayoutData(formData);
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				try {
					MessageBox msg;
					switch (matchesManager.importMatch(text.getText())) {
					case 1:
						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_MATCHES, new Event());
						msg = new MessageBox(ViewerHandler.getViewer(), SWT.OK | SWT.ICON_INFORMATION);
						msg.setMessage(Messages.getString("download.match.error.0")); //$NON-NLS-1$
						msg.open();
						break;
					case 2:
						msg = new MessageBox(ViewerHandler.getViewer(), SWT.OK | SWT.ICON_ERROR);
						msg.setMessage(Messages.getString("download.match.error.-1")); //$NON-NLS-1$
						msg.open();
						break;
					case 3:
						msg = new MessageBox(ViewerHandler.getViewer(), SWT.OK | SWT.ICON_ERROR);
						msg.setMessage(Messages.getString("download.match.error.-3")); //$NON-NLS-1$
						msg.open();
						break;
					case 4:
						msg = new MessageBox(ViewerHandler.getViewer(), SWT.OK | SWT.ICON_ERROR);
						msg.setMessage(Messages.getString("download.match.error.-4")); //$NON-NLS-1$
						msg.open();
						break;
					default:
						break;
					}
				} catch (NumberFormatException e) {
					MessageDialog.openErrorMessage(ViewerHandler.getViewer(), Messages.getString("download.match.error.-5")); //$NON-NLS-1$
				} catch (IOException e) {
					MessageDialog.openErrorMessage(ViewerHandler.getViewer(), Messages.getString("download.match.error.-5")); //$NON-NLS-1$
				} catch (SAXException e) {
					MessageDialog.openErrorMessage(ViewerHandler.getViewer(), Messages.getString("download.match.error.-5")); //$NON-NLS-1$
				} catch (SQLException e) {
					MessageDialog.openErrorMessage(ViewerHandler.getViewer(), Messages.getString("download.match.error.-5")); //$NON-NLS-1$
				} catch (SVException e) {
					MessageDialog.openErrorMessage(ViewerHandler.getViewer(), e.getMessage());
				}
			}

		});
		button.pack();

		this.setDefaultButton(button);

		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = DisplayHandler.getDisplay().getPrimaryMonitor().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);

	}
}
