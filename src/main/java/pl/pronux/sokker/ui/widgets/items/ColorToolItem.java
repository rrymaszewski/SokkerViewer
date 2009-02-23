package pl.pronux.sokker.ui.widgets.items;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import pl.pronux.sokker.ui.resources.ColorResources;

public class ColorToolItem extends ToolItem {

	public ColorToolItem(ToolBar parent, int style) {
		super(parent, style);
	}

	private Color color;


	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		Point size = new Point(this.getWidth(),this.getWidth());

		Color white = ColorResources.getSystemColor(SWT.COLOR_WHITE);
		Color black = ColorResources.getSystemColor(SWT.COLOR_BLACK);

		Image image = new Image(this.getDisplay(), size.x - 10, size.y - 10);
		GC gc = new GC(image);
		gc.setBackground(color);

		gc.fillRectangle(image.getBounds());
		gc.setForeground(ColorResources.getSystemColor(SWT.COLOR_BLACK));
		gc.setLineWidth(2);
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.fillRectangle(image.getBounds());
		gc.drawRectangle(image.getBounds());

		gc.dispose();
		ImageData imageData = image.getImageData();

		PaletteData palette = new PaletteData(new RGB[] {
				new RGB(0, 0, 0),
				new RGB(0xFF, 0xFF, 0xFF),
		});
		ImageData maskData = new ImageData(image.getBounds().width, image.getBounds().height, 1, palette);
		Image mask = new Image(this.getDisplay(), maskData);
		gc = new GC(mask);
		gc.setBackground(black);
		gc.fillRectangle(image.getBounds());
		gc.setBackground(white);
		gc.fillRectangle(image.getBounds());

		gc.dispose();
		maskData = mask.getImageData();

		Image icon = new Image(this.getDisplay(), imageData, maskData);
		this.setImage(icon);
	}

}
