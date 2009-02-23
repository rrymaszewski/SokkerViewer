package pl.pronux.sokker.ui.widgets.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.GalleryComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.GalleryImage;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class GalleryTable extends SVTable<GalleryImage> implements IViewSort<GalleryImage> {

	public static Map<Integer, String> formats = new HashMap<Integer, String>();
	static {
		formats.put(SWT.IMAGE_BMP, "bmp"); //$NON-NLS-1$
		formats.put(SWT.IMAGE_BMP_RLE, "bmp"); //$NON-NLS-1$
		formats.put(SWT.IMAGE_GIF, "gif"); //$NON-NLS-1$
		formats.put(SWT.IMAGE_ICO, "ico"); //$NON-NLS-1$
		formats.put(SWT.IMAGE_JPEG, "jpg"); //$NON-NLS-1$
		formats.put(SWT.IMAGE_PNG, "png"); //$NON-NLS-1$
	}
	
	private List<GalleryImage> alGalleryImages = new ArrayList<GalleryImage>();

	private GalleryComparator comparator;

	public GalleryTable(Composite parent, int style) {
		super(parent, style);

		comparator = new GalleryComparator();
		comparator.setColumn(GalleryComparator.TYPE);
		comparator.setDirection(GalleryComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] columns = {
				" ", //$NON-NLS-1$
				Messages.getString("GalleryTable.date.publication"), //$NON-NLS-1$
				Messages.getString("GalleryTable.image.name"), //$NON-NLS-1$
				Messages.getString("GalleryTable.author"), //$NON-NLS-1$
//				Messages.getString("table.format"),
//				Messages.getString("table.size"),
//				Messages.getString("GalleryTable.payment"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int j = 0; j < columns.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);

			if (j > 5 && j < columns.length - 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(columns[j]);
			column.setResizable(false);
			column.setMoveable(false);

			if (j == columns.length - 1) {
				// column.setWidth(70);
				if (SettingsHandler.OS_TYPE == LINUX) {
					column.pack();
				}
			} else {
				// column.setWidth(40);
				column.pack();
				column.addSelectionListener(new SortTableListener<GalleryImage>(this, comparator));
			}
		}

		this.setSortColumn(this.getColumn(comparator.getColumn()));
		this.setSortDirection(comparator.getDirection());
	}

	public void fill(List<GalleryImage> alGalleryImages) {
		this.alGalleryImages = alGalleryImages;

		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		for (GalleryImage galleryImage : alGalleryImages) {
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(GalleryImage.IDENTIFIER, galleryImage);
			if (galleryImage.getType() == GalleryImage.IMPORT) {
				item.setImage(ImageResources.getImageResources("galleryuser.png")); //$NON-NLS-1$
			} else if (galleryImage.getType() == GalleryImage.LOGOMAKER) {
				item.setImage(ImageResources.getImageResources("gallerysv.png")); //$NON-NLS-1$
			}
			c++;
			item.setText(c++, galleryImage.getPublicationDate().toDateString());
			item.setText(c++, galleryImage.getName());
			item.setText(c++, galleryImage.getUserID());
//			item.setText(c++, galleryImage.getFormat());
			
//			item.setText(c++, galleryImage.getImageData().height + "x" + galleryImage.getImageData().width);
//			item.setText(c++, galleryImage.getPayment().formatDoubleCurrencySymbol());
//			item.setText(c++, String.valueOf(galleryImage.getPayment()));
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 5);
		}
		// Turn drawing back on
		this.setRedraw(true);
	}

	public void sort(SVComparator<GalleryImage> comparator) {
		if (alGalleryImages != null) {
			Collections.sort(alGalleryImages, comparator);
			fill(alGalleryImages);
		}
	}

	public SVComparator<GalleryImage> getComparator() {
		return comparator;
	}

}
