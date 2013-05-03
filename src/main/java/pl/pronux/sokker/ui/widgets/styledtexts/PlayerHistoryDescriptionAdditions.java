package pl.pronux.sokker.ui.widgets.styledtexts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.FlagsResources;

public class PlayerHistoryDescriptionAdditions extends StyledText implements IDescription {

	private Image[] images = new Image[0];

	private int[] offsets = new int[0];

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public PlayerHistoryDescriptionAdditions(Composite parent, int style) {
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
					if (offset != -1 && offset >= start) {
						offset += newCharCount - replaceCharCount;
					}
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

	public void setStatsPlayerInfo(Player player) {
		setStatsPlayerInfo(player, 0);
	}

	public void setStatsPlayerInfo(Player player, int index) {
		this.setRedraw(false);
		List<Image> imagesList = new ArrayList<Image>();
		int max = player.getSkills().length - 1 - index;
		String imageText = "\uFFFC"; 
		this.setText(""); 

//		this.addText(String.format("%s: %s ", Messages.getString("player.club"), player.getClub()));
//		this.addText(NEW_LINE);

		this.addText(String.format("%s: %d, %s: %d, %s: %d", Messages.getString("player.matches.short"), player.getSkills()[max].getMatches(), Messages.getString("player.goals.short"), player.getSkills()[max].getGoals(), Messages.getString("player.assists.short"), player.getSkills()[max].getAssists()));    

		this.addText(NEW_LINE);

		this.addText(String.format("%s: %s", Messages.getString("player.position"), Messages.getString("assistant.position." + player.getPosition())));   
		this.addText(NEW_LINE);

		if (max > 0) {
			int diff1 = player.getSkills()[max].getSummarySkill() - player.getSkills()[0].getSummarySkill();
			if (diff1 != 0) {
				this.addText(String.format("%s: [%d %s]", Messages.getString("player.sum"), player.getSkills()[max].getSummarySkill(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[max].getSummarySkill() - player.getSkills()[0].getSummarySkill())));  
			} else {
				this.addText(String.format("%s: [%d]", Messages.getString("player.sum"), player.getSkills()[max].getSummarySkill()));  
			}
		} else {
			this.addText(String.format("%s: [%d]", Messages.getString("player.sum"), player.getSkills()[max].getSummarySkill()));  
		}

		this.addText(NEW_LINE);
		if(max > 0) {
			this.addText(String.format("%s: %d", Messages.getString("player.weeks.in.club"), player.getSkills()[max].getDate().getSokkerDate().getWeek() - player.getSkills()[0].getDate().getSokkerDate().getWeek()));  
		} else {
			this.addText(String.format("%s: %d", Messages.getString("player.weeks.in.club"), 0));  
		}
		
		this.addText(NEW_LINE);
		if(player.getTeam() != null) {
			if(player.getTeamId() == player.getYouthTeamId()) {
				this.addText(Messages.getString("player.from.school"));
			} else if(player.getTransferBuy() != null) {
				this.addText(String.format("%s: %s", Messages.getString("player.from.transferlist"), player.getTransferBuy().getPrice().formatIntegerCurrencySymbol()));
			}
		}
		
		this.addText(NEW_LINE);
		if (player.getTransferSell() != null) {
			if (player.getTransferSell().getPrice().toInt() > 0) {
				this.addText(String.format("%s: %s", Messages.getString("player.soldPrice"), player.getTransferSell().getPrice().formatDoubleCurrencySymbol()));
			} else {
				this.addText(String.format("%s: %s", Messages.getString("player.soldPrice"), Messages.getString("player.fired"))); 
			}
		} else {
			if (player.getSoldPrice().toInt() > 0) {
				this.addText(String.format("%s: %s", Messages.getString("player.soldPrice"), player.getSoldPrice().formatDoubleCurrencySymbol()));
			} else {
				this.addText(String.format("%s: %s", Messages.getString("player.soldPrice"), Messages.getString("player.fired")));
			}
		}
		
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);
		
		if (player.getNational() > 0 || (player.getNtSkills() != null && player.getNtSkills().length > 0 && player.getNtSkills()[player.getNtSkills().length - 1].getNtMatches() > 0)) {

			this.addText(imageText);

			if (player.getNational() > 0) {
				imagesList.add(FlagsResources.getFlag(player.getCountryfrom()));
			} else {
				imagesList.add(FlagsResources.getFlagLight(player.getCountryfrom()));
			}

			this.addText(String.format(" %s: %s ", Messages.getString("player.country"), Messages.getString("country." + player.getCountryfrom() + ".name")));    
			this.addText(NEW_LINE);

			// if(player.getNational() > 0) {
			// this.addText(Messages.getString("powolany"));
			// } else {
			// this.addText(Messages.getString("brak powolania"));
			// }

			if (player.getNtSkills() != null && player.getNtSkills().length > 0) {
				int maxNT = player.getNtSkills().length - 1;
				this.addText(String.format("%s: %d, %s: %d, %s: %d", Messages.getString("player.matches.short"), player.getNtSkills()[maxNT].getNtMatches(), Messages.getString("player.goals.short"), player.getNtSkills()[maxNT].getNtGoals(), Messages.getString("player.assists.short"), player.getNtSkills()[maxNT].getNtAssists()));    
			} else {
				this.addText(String.format("%s: -, %s: -, %s: -", Messages.getString("player.matches.short"), Messages.getString("player.goals.short"), Messages.getString("player.assists.short")));    
			}
		}
		

		
		images = imagesList.toArray(new Image[imagesList.size()]);
		offsets = new int[images.length];
		int lastOffset = 0;

		for (int i = 0; i < images.length; i++) {
			int offset = getText().indexOf(imageText, lastOffset);
			offsets[i] = offset;
			addImage(images[i], offset);
			lastOffset = offset + 1;
		}
		// parseText(getText());

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
