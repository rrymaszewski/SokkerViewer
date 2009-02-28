package pl.pronux.sokker.ui.widgets.styledtexts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;

public class JuniorAdditionsDescription extends StyledText implements IDescription {

	private Image[] images = new Image[0];

	private int[] offsets = new int[0];

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public JuniorAdditionsDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		// this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontDescription());

		this.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				int start = e.start;
				int replaceCharCount = e.end - e.start;
				int newCharCount = e.text.length();
				for (int i = 0; i < offsets.length; i++) {
					int offset = offsets[i];
					if (start <= offset && offset < start + replaceCharCount) {
						// this image is being deleted from the text
						if (images[i] != null && !images[i].isDisposed()) {
							// images[i].dispose();
							images[i] = null;
						}

						offset = -1;
					}
					if (offset != -1 && offset >= start)
						offset += newCharCount - replaceCharCount;
					offsets[i] = offset;
				}
			}
		});

		this.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				GC gc = event.gc;
				StyleRange style = event.style;
				int start = style.start;
				for (int i = 0; i < offsets.length; i++) {
					int offset = offsets[i];
					if (start == offset) {
						Image image = images[i];
						int x = event.x;
						int y = event.y + event.ascent - style.metrics.ascent;
						if (gc != null && !gc.isDisposed()) {
							gc.drawImage(image, x, y);
						}
					}
				}
			}
		});
	}

	private void addText(String text) {
		this.append(text);
	}

	public void setStatsJuniorInfo(Junior junior) {
		setStatsJuniorInfo(junior, 0);
	}

	private void addStyle(int start, int length, Color color, int style) {
		StyleRange range = new StyleRange();
		range.foreground = color;
		range.start = start;
		range.fontStyle = style;
		range.length = length;
		this.setStyleRange(range);
	}

	public void setStatsJuniorInfo(Junior junior, int index) {
		this.setRedraw(false);
		int max = junior.getSkills().length - 1 - index;
		StyleRange style;
		String text;
		String value;
		List<Image> imagesList = new ArrayList<Image>();
		String imageText = "\uFFFC"; //$NON-NLS-1$
		this.setText(""); //$NON-NLS-1$

		if (junior.getSkills()[max].getTrainer() != null) {
			Coach trainer = junior.getSkills()[max].getTrainer();
			imagesList.add(FlagsResources.getFlag(trainer.getCountryfrom()));
			this.addText(imageText);
			this.addText(String.format(" %s %s, %s: %d ", trainer.getName(), trainer.getSurname(), Messages.getString("player.age"), trainer.getAge())); //$NON-NLS-1$ //$NON-NLS-2$

			addStyle(2, trainer.getName().length() + trainer.getSurname().length() + 1, ColorResources.getBlack(), SWT.BOLD);
			addStyle(getText().length() - 3, 2, ColorResources.getBlack(), SWT.BOLD);
			this.addText(NEW_LINE);

			text = String.format("[%d]", trainer.getGeneralskill()); //$NON-NLS-1$
			this.addText(text);

			value = Messages.getString("skill.a" + trainer.getGeneralskill()); //$NON-NLS-1$
			text = String.format("%s %s ", value, Messages.getString("coach")); //$NON-NLS-1$ //$NON-NLS-2$
			this.addText(String.format("%s", text)); //$NON-NLS-1$
			addStyle(this.getText().length() - text.length(), value.length(), Colors.getTrainerGeneralSkill(), SWT.BOLD);
			
			this.addText(NEW_LINE);
			
			value = trainer.getSalary().formatIntegerCurrencySymbol();
			text = String.format("%s: %s", Messages.getString("coach.salary"), value); //$NON-NLS-1$ //$NON-NLS-2$
			this.addText(text);
			addStyle(this.getText().length() - value.length(), value.length(), ColorResources.getBlack(), SWT.BOLD);
			
			this.addText(NEW_LINE);
			this.addText(NEW_LINE);
		}
		
		this.addText(Messages.getString("junior.cost.training")); //$NON-NLS-1$
		this.addText(NEW_LINE);
		
		text = String.format("* %-35s%-12s", Messages.getString("junior.cost.school"), junior.getMoneySpent().formatIntegerCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addText(NEW_LINE);
		
		value = "n/a (yet)"; //$NON-NLS-1$
		text = String.format("* %-35s%-12s", Messages.getString("junior.cost.trainer"), value); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		style = new StyleRange();
		style.start = this.getText().length() - 16;
		style.length = 16;
		style.underline = true;
		this.setStyleRange(style);
		
		this.addText(NEW_LINE);
		
		text = String.format("  %-35s%-12s", "", junior.getMoneySpent().formatIntegerCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		this.addText(NEW_LINE);
		
		this.addText(Messages.getString("junior.cost.rest")); //$NON-NLS-1$
		this.addText(NEW_LINE);
		
		text = String.format("* %-35s%-12s", Messages.getString("junior.cost.school"), junior.getRestMoneyToSpend().formatIntegerCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addText(NEW_LINE);
		
		value = "n/a (yet)"; //$NON-NLS-1$
		text = String.format("* %-35s%-12s", Messages.getString("junior.cost.trainer"), value); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		style = new StyleRange();
		style.start = this.getText().length() - 16;
		style.length = 16;
		style.underline = true;
		this.setStyleRange(style);
		
		this.addText(NEW_LINE);
		
		text = String.format("  %-35s%-12s", "", junior.getRestMoneyToSpend().formatIntegerCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);
		
		text = String.format("  %-35s%-12s", Messages.getString("junior.sum"), junior.getAllMoneyToSpend().formatIntegerCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		
		images = imagesList.toArray(new Image[imagesList.size()]);
		offsets = new int[images.length];
		int lastOffset = 0;

		for (int i = 0; i < images.length; i++) {
			int offset = getText().indexOf(imageText, lastOffset);
			offsets[i] = offset;
			addImage(images[i], offset);
			lastOffset = offset + 1;
		}
		this.setRedraw(true);
	}

	private void addImage(Image image, int offset) {
		StyleRange style = new StyleRange();
		style.start = offset;
		style.length = 1;
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		this.setStyleRange(style);
	}

}
