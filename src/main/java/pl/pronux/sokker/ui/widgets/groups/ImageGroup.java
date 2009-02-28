package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class ImageGroup extends Group {

	private CLabel imageLabel;

	public ImageGroup(Composite arg0, int arg1) {
		super(arg0, arg1);
		setLayout(new FillLayout());
		imageLabel = new CLabel(this, SWT.CENTER);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());
	}

	public void setImage(Image image) {
		this.imageLabel.setImage(image);
	}
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

}
