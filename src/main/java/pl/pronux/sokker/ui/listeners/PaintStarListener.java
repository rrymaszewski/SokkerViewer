package pl.pronux.sokker.ui.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.ui.resources.ImageResources;

public class PaintStarListener implements Listener {

	private static final Image STAR_FULL = ImageResources.getImageResources("star_full.png"); 
	private static final Image STAR_HALF = ImageResources.getImageResources("star_half.png"); 
	private static final Image STAR_EMPTY = ImageResources.getImageResources("star_empty.png"); 

	private int index;

	public PaintStarListener(int index) {
		this.index = index;
	}

	public void handleEvent(Event event) {
		if (event.index == index && event.item instanceof TableItem && event.item.getData(PaintStarListener.class.getName()) != null) {
			TableItem item = (TableItem) event.item;
			int rating = 0;
			if (item.getData(PaintStarListener.class.getName()) instanceof Integer) {
				rating = (Integer) item.getData(PaintStarListener.class.getName());
			}
			switch (event.type) {
			case SWT.PaintItem: {
				int x = event.x;
				Rectangle rect = STAR_FULL.getBounds();
				int offset = Math.max(0, (event.height - rect.height) / 2);
				int starsFull = rating / 10;
				for (int i = 0; i < starsFull; i++) {
					event.gc.drawImage(STAR_FULL, x + i * rect.width + 1, event.y + offset);
				}
				int starsHalf = (rating / 5) - (2 * starsFull);
				for (int i = starsFull; i < starsHalf + starsFull; i++) {
					event.gc.drawImage(STAR_HALF, x + i * rect.width + 1, event.y + offset);
				}
				int starsEmpty = 10 - starsFull - starsHalf;
				for (int i = starsFull + starsHalf; i < starsEmpty + starsFull + starsHalf; i++) {
					event.gc.drawImage(STAR_EMPTY, x + i * rect.width + 1, event.y + offset);
				}
				break;
			}
			}
		}
	}
}
