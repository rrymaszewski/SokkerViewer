package pl.pronux.sokker.ui.widgets.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.ui.resources.ImageResources;

public class Splash extends Shell {
	private CLabel splashStatus;

	private Monitor _monitor;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public Splash(Display display, int style) {
		super(display, style);
		
		_monitor = display.getPrimaryMonitor();
		this.setLayout(new FormLayout());
		FormData labelData = new FormData();
		labelData.right = new FormAttachment(100, 0);
		labelData.left = new FormAttachment(0, 0);
		Label label = new Label(this, SWT.NONE);
		label.setImage(ImageResources.getImageResources("splash.png")); //$NON-NLS-1$
		label.setLayoutData(labelData);

		FormData formData = new FormData();
		formData.right = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(label, 0);
		formData.height = 20;

		splashStatus = new CLabel(this, SWT.NONE);
		splashStatus.setLayoutData(formData);

		this.pack();
		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = _monitor.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);
	}

	public void setStatus(String text) {
		this.splashStatus.setText(text);
		this.splashStatus.update();
	}
}
