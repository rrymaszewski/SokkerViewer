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

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;

public class PlayerHistoryDescription extends StyledText implements IDescription {

	private Image[] images = new Image[0];

	private int[] offsets = new int[0];

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public PlayerHistoryDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
		// use a verify listener to keep the offsets up to date
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

	public void setStatsPlayerInfo(Player player) {
		setStatsPlayerInfo(player, 0);
	}

	private String getNumberSkill(int before, int now) {
		if (now - before != 0) {
			return String.format("[%s %s]", now, SVNumberFormat.formatIntegerWithSignZero(now - before)); //$NON-NLS-1$
		} else {
			return String.format("[%s]", now); //$NON-NLS-1$
		}
	}

	private String getPopSkill(int before, int now) {
		String text = ""; //$NON-NLS-1$
		if (now - before > 0) {
			text = String.format("(%s)", SVNumberFormat.formatIntegerWithSignZero(now - before)); //$NON-NLS-1$
		} else if (now - before < 0) {
			text = String.format("(%s)", SVNumberFormat.formatIntegerWithSignZero(now - before)); //$NON-NLS-1$
		}
		return text;
	}

	private void addSkill(String name, String value, int now, int begin, int offset) {
		addSkill(name, value, now, now, begin, offset);
	}

	private void addSkill(String name, String value, int now, int before, int begin, int offset) {
		String text1 = ""; //$NON-NLS-1$
		String text2 = ""; //$NON-NLS-1$
		text1 = String.format("%8s%s %s ", getNumberSkill(begin, now), value, name); //$NON-NLS-1$
		text2 = getPopSkill(before, now);
		this.addText(String.format("%-35s", text1 + text2)); //$NON-NLS-1$
		addStyle(offset + 8, value.length(), ColorResources.getBlack(), SWT.BOLD);
		addStyle(offset, 8, ColorResources.getDarkGray(), SWT.NONE);
		if (now > before) {
			addStyle(offset + 8, value.length(), ConfigBean.getColorIncreaseDescription(), SWT.BOLD);
			addStyle(offset + text1.length(), text2.length(), ConfigBean.getColorIncreaseDescription(), SWT.NONE);
		} else if (now < before) {
			addStyle(offset + 8, value.length(), ConfigBean.getColorDecreaseDescription(), SWT.BOLD);
			addStyle(offset + text1.length(), text2.length(), ConfigBean.getColorDecreaseDescription(), SWT.NONE);
		}
	}

	private void addStyle(int start, int length, Color color, int style) {
		StyleRange range = new StyleRange();
		range.foreground = color;
		range.start = start;
		range.fontStyle = style;
		range.length = length;
		this.setStyleRange(range);
	}

	public void setStatsPlayerInfo(Player player, int index) {
		this.setRedraw(false);
		// Send all output to the Appendable object sb
		int pop = 0;
		int max = player.getSkills().length - 1 - index;
		String text;
		String imageText = "\uFFFC"; //$NON-NLS-1$
		this.setText(imageText);
		this.addText(String.format(" %s %s, %s: %d", player.getName(), player.getSurname(), Messages.getString("player.age"), player.getSkills()[max].getAge())); 

		int start = 2;
		int length = player.getName().length() + player.getSurname().length() + 1;
		addStyle(start, length, ColorResources.getBlack(), SWT.BOLD);
		start += length + Messages.getString("player.age").length() + 4; 
		length = String.valueOf(player.getSkills()[max].getAge()).length();
		addStyle(start, length, ColorResources.getBlack(), SWT.BOLD);

		if (max > 0 && (pop = player.getSkills()[max].getAge() - player.getSkills()[0].getAge()) > 0) {
			text = String.format("[%s]", SVNumberFormat.formatIntegerWithSignZero(pop)); //$NON-NLS-1$
			this.addText(text);
			addStyle(getText().length() - text.length(), text.length(), ColorResources.getDarkGray(), SWT.NONE);
		}

		text = String.format(", %s: %d cm", Messages.getString("player.height"), player.getHeight());
		this.addText(text);
		addStyle(getText().length() - String.valueOf(player.getHeight()).length() - 3, String.valueOf(player.getHeight()).length(), ColorResources.getBlack(), SWT.BOLD);

		text = String.format("  (ID %d)", player.getId()); //$NON-NLS-1$
		this.addText(text);
		addStyle(getText().length() - text.length(), text.length(), ColorResources.getDarkGray(), SWT.NONE);
		this.addText(NEW_LINE);

		this.addText(String.format(" %s: %s", Messages.getString("player.value"), player.getSkills()[max].getValue().formatIntegerCurrencySymbol())); //$NON-NLS-1$ //$NON-NLS-2$
		addStyle(getText().length() - player.getSkills()[max].getValue().formatIntegerCurrencySymbol().length(), player.getSkills()[max].getValue().formatIntegerCurrencySymbol().length(), ColorResources.getBlack(), SWT.BOLD);

		this.addText(String.format(", %s: %s", Messages.getString("player.salary"), player.getSkills()[max].getSalary().formatIntegerCurrencySymbol())); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(NEW_LINE);

		addSkillDescription(Messages.getString("player.form"), Messages.getString("skill.b" + player.getSkills()[max].getForm()), player.getSkills()[max].getForm(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkillDescription(Messages.getString("player.discipline"), Messages.getString("skill.b" + player.getSkills()[max].getDiscipline()), player.getSkills()[max].getDiscipline(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		this.addText(NEW_LINE);

		addSkillDescription(Messages.getString("player.teamwork"), Messages.getString("skill.c" + player.getSkills()[max].getTeamwork()), player.getSkills()[max].getTeamwork(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkillDescription(Messages.getString("player.experience"), Messages.getString("skill.c" + player.getSkills()[max].getExperience()), player.getSkills()[max].getExperience(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		this.addText(NEW_LINE);
		int cards = player.getSkills()[max].getCards();
		if (cards > 0) {
			this.addText(String.format(" %s:%s ", Messages.getString("player.cards"), imageText)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		double injury = player.getSkills()[max].getInjurydays();
		if (injury > 0) {
			this.addText(String.format(" %s:%s%.0f", Messages.getString("player.injurydays"), imageText, injury)); //$NON-NLS-1$ //$NON-NLS-2$
			if (injury <= 7) {
				this.addText(String.format(" [%s]", Messages.getString("injury.lastDays"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		addSkill(Messages.getString("player.stamina"), Messages.getString("skill.b" + player.getSkills()[max].getStamina()), player.getSkills()[max].getStamina(), player.getSkills()[0].getStamina(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkill(Messages.getString("player.keeper"), Messages.getString("skill.a" + player.getSkills()[max].getKeeper()), player.getSkills()[max].getKeeper(), player.getSkills()[0].getKeeper(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		this.addText(NEW_LINE);

		addSkill(Messages.getString("player.pace"), Messages.getString("skill.b" + player.getSkills()[max].getPace()), player.getSkills()[max].getPace(), player.getSkills()[0].getPace(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkill(Messages.getString("player.defender"), Messages.getString("skill.a" + player.getSkills()[max].getDefender()), player.getSkills()[max].getDefender(), player.getSkills()[0].getDefender(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		this.addText(NEW_LINE);

		addSkill(Messages.getString("player.technique"), Messages.getString("skill.b" + player.getSkills()[max].getTechnique()), player.getSkills()[max].getTechnique(), player.getSkills()[0].getTechnique(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkill(Messages.getString("player.playmaker"), Messages.getString("skill.a" + player.getSkills()[max].getPlaymaker()), player.getSkills()[max].getPlaymaker(), player.getSkills()[0].getPlaymaker(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		this.addText(NEW_LINE);

		addSkill(Messages.getString("player.passing"), Messages.getString("skill.c" + player.getSkills()[max].getPassing()), player.getSkills()[max].getPassing(), player.getSkills()[0].getPassing(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		addSkill(Messages.getString("player.scorer"), Messages.getString("skill.a" + player.getSkills()[max].getScorer()), player.getSkills()[max].getScorer(), player.getSkills()[0].getScorer(), getText().length()); //$NON-NLS-1$ //$NON-NLS-2$

		List<Image> imagesList = new ArrayList<Image>();
		imagesList.add(FlagsResources.getFlag(player.getCountryfrom()));
		if (cards > 0) {
			if (cards == 1) {
				imagesList.add(ImageResources.getImageResources("yellow_card.png")); //$NON-NLS-1$
			} else if (cards == 2) {
				imagesList.add(ImageResources.getImageResources("2_yellow_cards.png")); //$NON-NLS-1$
			} else if (cards > 2) {
				imagesList.add(ImageResources.getImageResources("red_card.png")); //$NON-NLS-1$
			}
		}
		if (injury > 0) {
			imagesList.add(ImageResources.getImageResources("injury.png")); //$NON-NLS-1$
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

	private void addSkillDescription(String name, String value, int now, int offset) {
		addSkillDescription(name, value, now, now, now, offset);

	}

	private void addSkillDescription(String name, String value, int now, int before, int begin, int offset) {
		String text1 = ""; //$NON-NLS-1$
		String text2 = ""; //$NON-NLS-1$
		text1 = String.format(" %s %s ", value, name); //$NON-NLS-1$
		text2 = getPopSkill(before, now);
		this.addText(String.format("%s ", text1 + text2)); //$NON-NLS-1$

		if (now > before) {
			addStyle(offset, value.length() + 1, ConfigBean.getColorIncreaseDescription(), SWT.BOLD);
			addStyle(offset + text1.length(), text2.length(), ConfigBean.getColorIncreaseDescription(), SWT.NONE);
		} else if (now < before) {
			addStyle(offset, value.length() + 1, ConfigBean.getColorDecreaseDescription(), SWT.BOLD);
			addStyle(offset + text1.length(), text2.length(), ConfigBean.getColorDecreaseDescription(), SWT.NONE);
		} else {
			addStyle(offset, value.length() + 1, ColorResources.getBlack(), SWT.BOLD);
		}

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
