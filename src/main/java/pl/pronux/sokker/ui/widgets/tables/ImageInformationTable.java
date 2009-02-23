package pl.pronux.sokker.ui.widgets.tables;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class ImageInformationTable extends SVTable<Club> {
	private final static Map<Integer, String> extensions = new HashMap<Integer, String>();

	static {
		extensions.put(SWT.IMAGE_BMP, "bmp"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_BMP_RLE, "bmp"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_GIF, "gif"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_ICO, "ico"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_JPEG, "jpeg"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_PNG, "png"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_TIFF, "tiff"); //$NON-NLS-1$
		extensions.put(SWT.IMAGE_UNDEFINED, "undefined"); //$NON-NLS-1$
	}

	public ImageInformationTable(Composite parent, int style) {
		super(parent, style);

		TableItem item;
		TableColumn tableColumn;
		this.setLinesVisible(false);
		this.setHeaderVisible(false);
		this.setBackground(parent.getBackground());
		// this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontMain());

		String[] column = { "first", "second" }; //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0; i < column.length; i++) {
			tableColumn = new TableColumn(this, SWT.LEFT);
			tableColumn.setText(column[i]);
		}

		String[] firstColumn = { 
				Messages.getString("ImageInformationTable.userID"), //$NON-NLS-1$
				Messages.getString("ImageInformationTable.type"), //$NON-NLS-1$
				Messages.getString("ImageInformationTable.payment"), //$NON-NLS-1$
				Messages.getString("ImageInformationTable.file"), //$NON-NLS-1$
				Messages.getString("ImageInformationTable.size"), //$NON-NLS-1$
				Messages.getString("ImageInformationTable.date.publication") //$NON-NLS-1$
		};

		for (int i = 0; i < firstColumn.length; i++) {
			item = new TableItem(this, SWT.NONE);
			item.setText(0, firstColumn[i]);
		}

		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 0) {
				this.getItem(i).setBackground(ColorResources.getGray());
			}
		}

	}

	public void fill(GalleryImage galleryImage, Image image) {
		if (image == null || galleryImage == null) {
			return;
		}
		int firstColumn = 1;
		int c = 0;
		TableItem item;
		item = this.getItem(c++);
		item.setText(firstColumn, String.valueOf(galleryImage.getUserID()));
		item = this.getItem(c++);
		item.setText(firstColumn, Messages.getString("GalleryImage.type." + galleryImage.getType())); //$NON-NLS-1$
		item = this.getItem(c++);
		item.setText(firstColumn, Messages.getString("GalleryImage.payment." + galleryImage.getPayment())); //$NON-NLS-1$
		item = this.getItem(c++);
		item.setText(firstColumn, galleryImage.getFile());
		item = this.getItem(c++);
		item.setText(firstColumn, image.getImageData().width + " x " + image.getImageData().height); //$NON-NLS-1$
		item = this.getItem(c++);
		item.setText(firstColumn, galleryImage.getPublicationDate().toDateString());
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}

}
