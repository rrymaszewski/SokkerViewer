package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class JuniorTrainedAdditionsDescription extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public JuniorTrainedAdditionsDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		// this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontDescription());
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
		int max = junior.getSkills().length - 1 - index;
		String text;
		String temp;
		String value;
		this.setText(""); 

		this.addText(NEW_LINE);
		if(junior.getTrainersAverage() != null && junior.getTrainersAverage() > 0) {
			value = String.format("[%d]", junior.getTrainersAverage().intValue()); 
			temp = String.format("%s", Messages.getString("skill.a" + junior.getTrainersAverage().intValue()));  
			text = String.format("%-25s %s%s", Messages.getString("junior.trainer.average"), value, temp);  
			this.addText(text);
			this.addStyle(this.getText().length() - temp.length() - value.length(), value.length(), ColorResources.getDarkGray(), SWT.NORMAL);
			this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getDarkBlue(), SWT.BOLD);
			text = String.format(" (%.1f)", junior.getTrainersAverage()); 
			this.addText(text);
			this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkBlue(), SWT.NORMAL);
		} else {
			text = String.format("%-25s %s", Messages.getString("junior.trainer.average"), "-");   
			this.addText(text);
		}
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		temp = String.format("%d", junior.getSkills()[0].getWeeks()); 
		text = String.format("%-25s %s", Messages.getString("junior.weeksAll"), temp);  
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		
		this.addText(String.format(" %s" , Messages.getString("junior.weeks")));  
		this.addText(NEW_LINE);

		temp = String.format("%s", junior.getEndDate().toDateString()); 
		text = String.format("%-25s %s", Messages.getString("junior.exit.date"), temp);  
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		value = String.format("[%d]", junior.getSkills()[0].getSkill()); 
		temp = String.format("%s", Messages.getString("skill.a" + junior.getSkills()[0].getSkill()));  
		text = String.format("%-25s %s%s", Messages.getString("junior.skill.begin"), value, temp);  
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length() - value.length(), value.length(), ColorResources.getDarkGray(), SWT.NORMAL);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		Player player = junior.getPlayer();
		if (player != null) {
			temp = String.format("%d", player.getSkills()[0].getAge() - junior.getEstimatedAge()); 
			text = String.format("%-25s %s", Messages.getString("junior.age.begin"), temp);  
			this.addText(text);
			this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
			this.addText(NEW_LINE);
		}

		temp = String.format("%d", junior.getPops()); 
		text = String.format("%-25s %s", Messages.getString("junior.numberOfJumps"), temp);  
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		temp = String.format("%s", Money.formatIntegerCurrencySymbol((junior.getSkills()[0].getWeeks() - junior.getSkills()[max].getWeeks() + 1) * Junior.JUNIOR_COST.toInt())); 
		text = String.format("%-25s %s", Messages.getString("junior.money.spent"), temp);  
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length() - Money.getCurrencySymbol().length() - 1, ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
	}

}
