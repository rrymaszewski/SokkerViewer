package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ScrollBar;

import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.tables.ImageInformationTable;

public class ImageInformationGroup extends Group {

	private ImageInformationTable imageInformationTable;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public ImageInformationGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);
		this.setLayout(new FormLayout());
		FormData formData;
		this.setFont(ConfigBean.getFontMain());
		this.setText(Messages.getString("ImageInformationGroup.info")); //$NON-NLS-1$
		this.setForeground(Colors.getBlueDescription());

		imageInformationTable = new ImageInformationTable(this, SWT.FULL_SELECTION);

		ScrollBar bar = imageInformationTable.getVerticalBar();

		formData = new FormData();
		formData.left = new FormAttachment(0, bar.getSize().x);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(100, -5);
		imageInformationTable.setLayoutData(formData);
	}

	public void fill(GalleryImage galleryImage, Image image) {
		imageInformationTable.fill(galleryImage, image);
	}

}
