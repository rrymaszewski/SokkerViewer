package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.ui.beans.ConfigBean;

public class StatusBar extends Composite {
	private CLabel lastUpdateLabel;
	private CLabel versionLabel;

	public StatusBar(Composite composite, int style) {
		super(composite, style);
		this.setLayout(new FormLayout());
		
		lastUpdateLabel = new CLabel(this, SWT.NONE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 5);
		formData.width = 300;
		
		lastUpdateLabel.setLayoutData(formData);
		lastUpdateLabel.setFont(ConfigBean.getFontMain());

		versionLabel = new CLabel(this, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(lastUpdateLabel, 10);
		formData.width = 250;
		
		versionLabel.setLayoutData(formData);
		versionLabel.setFont(ConfigBean.getFontMain());
	}

	public void setVersion(String version) {
		this.versionLabel.setText(version);
	}
	
	public void setLastDate(String date) {
		this.lastUpdateLabel.setText(date);
	}
	
	public CLabel getVersionLabel() {
		return versionLabel;
	}
}
