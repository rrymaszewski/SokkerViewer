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

public class JuniorTrainedAdditionsDescriptionStyledText extends StyledText {
	private static String NEW_LINE = System.getProperty("line.separator"); //$NON-NLS-1$

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public JuniorTrainedAdditionsDescriptionStyledText(Composite parent, int style) {
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
		Player player;
		this.setText(""); //$NON-NLS-1$

		this.addText(NEW_LINE);
		if(junior.getTrainersAverage() != null && junior.getTrainersAverage() > 0) {
			value = String.format("[%d]", junior.getTrainersAverage().intValue()); //$NON-NLS-1$
			temp = String.format("%s", Messages.getString("skill.a" + junior.getTrainersAverage().intValue())); //$NON-NLS-1$ //$NON-NLS-2$
			text = String.format("%-25s %s%s", Messages.getString("junior.trainer.average"), value, temp); //$NON-NLS-1$ //$NON-NLS-2$
			this.addText(text);
			this.addStyle(this.getText().length() - temp.length() - value.length(), value.length(), ColorResources.getDarkGray(), SWT.NORMAL);
			this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getDarkBlue(), SWT.BOLD);
			text = String.format(" (%.1f)", junior.getTrainersAverage()); //$NON-NLS-1$
			this.addText(text);
			this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkBlue(), SWT.NORMAL);
		} else {
			text = String.format("%-25s %s", Messages.getString("junior.trainer.average"), "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.addText(text);
		}
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		temp = String.format("%d", junior.getSkills()[0].getWeeks()); //$NON-NLS-1$
		text = String.format("%-25s %s", Messages.getString("junior.weeksAll"), temp); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		
		this.addText(String.format(" %s" , Messages.getString("junior.weeks"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(NEW_LINE);

		temp = String.format("%s", junior.getEndDate().toDateString()); //$NON-NLS-1$
		text = String.format("%-25s %s", Messages.getString("junior.exit.date"), temp); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		value = String.format("[%d]", junior.getSkills()[0].getSkill()); //$NON-NLS-1$
		temp = String.format("%s", Messages.getString("skill.a" + junior.getSkills()[0].getSkill())); //$NON-NLS-1$ //$NON-NLS-2$
		text = String.format("%-25s %s%s", Messages.getString("junior.skill.begin"), value, temp); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length() - value.length(), value.length(), ColorResources.getDarkGray(), SWT.NORMAL);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		if ((player = junior.getPlayer()) != null) {
			temp = String.format("%d", player.getSkills()[0].getAge() - junior.getEstimatedAge()); //$NON-NLS-1$
			text = String.format("%-25s %s", Messages.getString("junior.age.begin"), temp); //$NON-NLS-1$ //$NON-NLS-2$
			this.addText(text);
			this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
			this.addText(NEW_LINE);
		}

		temp = String.format("%d", junior.getPops()); //$NON-NLS-1$
		text = String.format("%-25s %s", Messages.getString("junior.numberOfJumps"), temp); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length(), ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
		
		temp = String.format("%s", Money.formatIntegerCurrencySymbol((junior.getSkills()[0].getWeeks() - junior.getSkills()[max].getWeeks() + 1) * Junior.juniorCost.toInt())); //$NON-NLS-1$
		text = String.format("%-25s %s", Messages.getString("junior.money.spent"), temp); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - temp.length(), temp.length() - Money.getCurrencySymbol().length() - 1, ColorResources.getBlack(), SWT.BOLD);
		this.addText(NEW_LINE);
	}

}
