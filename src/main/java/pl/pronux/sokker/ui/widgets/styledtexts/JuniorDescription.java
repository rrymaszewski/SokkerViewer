package pl.pronux.sokker.ui.widgets.styledtexts;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class JuniorDescription extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public JuniorDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		// this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontDescription());
		// use a verify listener to keep the offsets up to date

	}

	private void addText(String text) {
		this.append(text);
	}

	public void setStatsJuniorInfo(Junior junior) {
		setStatsJuniorInfo(junior, 0);
	}

	private String getNumberSkill(int before, int now) {
		if (now - before != 0) {
			return String.format("[%s %s]", now, SVNumberFormat.formatIntegerWithSignZero(now - before)); 
		} else {
			return String.format("[%s]", now); 
		}
	}

	private void addSkill(String name, String value, int now, int before, int begin, int offset) {
		String text = ""; 
		text = String.format("%8s%s %s", getNumberSkill(begin, now), value, name); 
		this.addText(text);
		addStyle(offset, 8, ColorResources.getDarkGray(), SWT.NONE);
		if (now > before) {
			addStyle(offset + 8, value.length(), ConfigBean.getColorIncreaseDescription(), SWT.BOLD);
		} else {
			addStyle(offset + 8, value.length(), ColorResources.getBlack(), SWT.BOLD);
		}
	}

	private void addSkill(String name, String value, int now, int offset) {
		addSkill(name, value, now, now, now, offset);
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
		// Send all output to the Appendable object sb
		int max = junior.getSkills().length - 1 - index;
		String text;
		String text2;
		String text1;

		this.setText(""); 

		text = String.format(" %s %s ", junior.getName(), junior.getSurname()); 
		this.addText(text);
		addStyle(getText().length() - text.length(), text.length(), ColorResources.getBlack(), SWT.BOLD);

		text = String.format("%s: ", Messages.getString("player.age"));
		this.addText(text);

		if (junior.getSkills()[max].getAge() > 0) {
			text = String.valueOf(junior.getSkills()[max].getAge());
		} else {
			text = "-";
		}
		this.addText(text);

		text = String.format("\t(ID %d)", junior.getId()); 
		this.addText(text);
		addStyle(getText().length() - text.length(), text.length(), ColorResources.getDarkGray(), SWT.NONE);
		this.addText(NEW_LINE);

		if (max > 0) {
			addSkill(
					 Messages.getString("junior"), Messages.getString("skill.a" + junior.getSkills()[max].getSkill()), junior.getSkills()[max].getSkill(), junior.getSkills()[max - 1].getSkill(), junior.getSkills()[0].getSkill(), getText().length());  
		} else {
			addSkill(
					 Messages.getString("junior"), Messages.getString("skill.a" + junior.getSkills()[max].getSkill()), junior.getSkills()[max].getSkill(), getText().length());  
		}

		if (junior.getWeeksWithoutJump() > 0) {
			text = String.format(" %s", SVNumberFormat.formatIntegerWithSignZero(junior.getWeeksWithoutJump())); 
			this.addText(text);
		}

		if (junior.getAveragePops() > 0) {
			text = String.format(" (%.2f)", junior.getSkills()[max].getSkill() + (junior.getWeeksWithoutJump() / junior.getAveragePops())); 
			this.addText(text);
		}

		if (junior.getSkills()[0].getWeeks() == 0) {
			this.addText(NEW_LINE);
			text = String.format("%s", Messages.getString("junior.ready"));  
			this.addText(text);
			addStyle(getText().length() - text.length(), text.length(), ColorResources.getBlue(), SWT.BOLD);
		}
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		text = String.format(" %-25s %d %s", Messages.getString("junior.weeksLeft"), junior.getSkills()[max].getWeeks(), Messages.getString("junior.weeks"));   
		this.addText(text);

		text = String.format("\t(%s)", junior.getEndDate().toDateString()); 
		this.addText(text);
		addStyle(getText().length() - text.length(), text.length(), ColorResources.getDarkGray(), SWT.NORMAL);
		this.addText(NEW_LINE);

		text = String
			.format(
					" %-25s %d %s", Messages.getString("junior.weeksPast"), junior.getSkills()[0].getWeeks() - junior.getSkills()[max].getWeeks(), Messages.getString("junior.weeks"));   
		this.addText(text);
		this.addText(NEW_LINE);

		text = String.format(" %-25s %d %s", Messages.getString("junior.weeksAll"), junior.getSkills()[0].getWeeks(), Messages.getString("junior.weeks"));   
		this.addText(text);

		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		text = String.format(" %-25s %d", Messages.getString("junior.numberOfJumps"), junior.getPops());  
		this.addText(text);
		this.addText(NEW_LINE);

		text = String.format(" %-25s ", Messages.getString("junior.averageJumps"));
		this.addText(text);
		if (junior.getPops() < 2) {
			this.addText("~"); 
		}
		text = String.format("%.2f", new BigDecimal(junior.getAveragePops()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()); 
		this.addText(text);
		if (junior.getPops() < 2) {
			this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkGray(), SWT.BOLD);
		} else {
			this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getColor(142, 3, 121), SWT.BOLD);
		}

		this.addText(NEW_LINE);

		text = String.format(" %-25s", Messages.getString("junior.skill.begin"));
		this.addText(text);
		text1 = String.format("%s", Messages.getString("skill.a" + junior.getSkills()[0].getSkill())); 
		text2 = String.format("[%d]", junior.getSkills()[0].getSkill()); 
		text = String.format(" %s%s", text2, text1);
		this.addText(text);
		addStyle(getText().length() - text1.length(), text1.length(), ColorResources.getDarkGray(), SWT.NORMAL);
		this.addText(NEW_LINE);

		if (junior.getSkills()[0].getWeeks() != 0) {
			text = String.format(" %-25s", Messages.getString("junior.propablyOutLevelSkill")); 
			this.addText(text);
		}

		text1 = String.format("%s", Messages.getString("skill.a" + junior.getEstimatedSkill())); 
		text2 = String.format("[%d]", junior.getEstimatedSkill()); 
		if (junior.getPops() < 2) {
			text = String.format(" ~%s%s", text2, text1);
			this.addText(text);
			addStyle(getText().length() - text1.length(), text1.length(), ColorResources.getDarkGray(), SWT.BOLD);
		} else {
			text = String.format(" %s%s", text2, text1);
			this.addText(text);
			addStyle(getText().length() - text1.length(), text1.length(), ColorResources.getBlack(), SWT.BOLD);
		}

		this.addText(NEW_LINE);
		if (junior.getSkills()[0].getAge() > 0) {
			text = String.format(" %-25s %d", Messages.getString("junior.age.estimated"), junior.getEstimatedAge() + junior.getSkills()[0].getAge());
		} else {
			text = String.format(" %-25s \u2265 %d", Messages.getString("junior.age.estimated"), junior.getEstimatedAge() + Junior.MINIMUM_AGE);
		}

		this.addText(text);
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		for (int i = 0; i <= max; i++) {
			String value = String.valueOf(junior.getSkills()[i].getSkill());
			text = String.format("%s ", value); 
			this.addText(text);
			this.addStyle(this.getText().length() - value.length() - 1, value.length(), ColorResources.getDarkGray(), SWT.NORMAL);
			if (i > 0 && (junior.getSkills()[i].getSkill() - junior.getSkills()[i - 1].getSkill() > 0)) {
				this.addStyle(this.getText().length() - value.length() - 1, value.length(), ColorResources.getBlack(), SWT.BOLD);
			}
		}
		this.setRedraw(true);
	}
}
