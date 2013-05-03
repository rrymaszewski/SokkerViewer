package pl.pronux.sokker.ui.widgets.composites;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class PlayersDescriptionComposite extends DescriptionSingleComposite {

	public PlayersDescriptionComposite(Composite parent, int style) {
		super(parent, style);

		// this.setDescriptionStringFormat(40, 15);
		this.setDescriptionStringFormat("%-40s%-35s\r\n"); 
		this.setFirstColumnSize(40);
		this.setSecondColumnSize(35);

		this.setFont(ConfigBean.getFontDescription());
	}

	public void setDescription(List<Player> players) {
		int maxSkill = 0;
		double teamValuePast = 0;
		double teamSalaryPast = 0;
		double averAgePast = 0;
		double teamValue = 0;
		double teamSalary = 0;
		double averAge = 0;
		String[][] values;
		int textSize = 0;

		for (Player player : players) {
			maxSkill = player.getSkills().length - 1;
			teamValue += player.getSkills()[maxSkill].getValue().getDoubleValue();
			teamSalary += player.getSkills()[maxSkill].getSalary().getDoubleValue();
			averAge += player.getSkills()[maxSkill].getAge();

			if (maxSkill > 1) {
				teamValuePast += player.getSkills()[maxSkill - 1].getValue().getDoubleValue();
				teamSalaryPast += player.getSkills()[maxSkill - 1].getSalary().getDoubleValue();
				averAgePast += player.getSkills()[maxSkill - 1].getAge();
			}
		}

		// aktualizujemy srednie wartosci
		values = new String[6][2];

		values[0][0] = Messages.getString("player.allValue"); 
		values[1][0] = Messages.getString("player.averageValue"); 
		values[2][0] = Messages.getString("player.allSalary"); 
		values[3][0] = Messages.getString("player.averageSalary"); 
		values[4][0] = Messages.getString("player.averageAge"); 
		values[5][0] = Messages.getString("player.allPlayers"); 

		textSize = textSize + this.checkFirstTextSize(values[0][0]);

		values[0][1] = Money.formatDoubleCurrencySymbol(teamValue);

		if (teamValue - teamValuePast > 0) {

			values[0][1] += String.format(" (%s)", Money.formatDoubleSignCurrencySymbol(teamValue - teamValuePast)); 
			this.colorText(textSize, values[0][1].length(), ConfigBean.getColorIncreaseDescription());

		} else if (teamValue - teamValuePast < 0) {

			values[0][1] += String.format(" (%s)", Money.formatDoubleSignCurrencySymbol(teamValue - teamValuePast)); 
			this.colorText(textSize, values[0][1].length(), ConfigBean.getColorDecreaseDescription());
		}

		textSize = textSize + this.checkSecondTextSize(values[0][1]);

		if (players.size() > 0) {

			textSize = textSize + this.checkFirstTextSize(values[1][0]);

			values[1][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamValue / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).toString();

			textSize = textSize + this.checkSecondTextSize(values[1][1]);

			textSize = textSize + this.checkFirstTextSize(values[2][0]);

			values[2][1] = Money.formatDoubleCurrencySymbol(teamSalary);

			if (teamSalary - teamSalaryPast > 0) {
				values[2][1] += String.format(" (%s)", Money.formatDoubleSignCurrencySymbol(teamSalary - teamSalaryPast)); 
				this.colorText(textSize, values[2][1].length(), ConfigBean.getColorIncreaseDescription());
			} else if (teamSalary - teamSalaryPast < 0) {
				values[2][1] += String.format(" (%s)", Money.formatDoubleSignCurrencySymbol(teamSalary - teamSalaryPast)); 
				this.colorText(textSize, values[2][1].length(), ConfigBean.getColorDecreaseDescription());
			}

			textSize = textSize + this.checkSecondTextSize(values[2][1]);

			textSize = textSize + this.checkFirstTextSize(values[3][0]);

			values[3][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamSalary / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).toString();

			textSize = textSize + this.checkSecondTextSize(values[3][1]);

			textSize = textSize + this.checkFirstTextSize(values[4][0]);

			values[4][1] = BigDecimal.valueOf(averAge / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "   "; 

			textSize = textSize + this.checkSecondTextSize(values[4][1]);

			textSize = textSize + this.checkFirstTextSize(values[5][0]);

			values[5][1] = players.size() + "   "; 

			textSize = textSize + this.checkSecondTextSize(values[5][0]);

		} else {
			values[1][1] = Money.formatDouble(0);
			values[2][1] = Money.formatDoubleCurrencySymbol(teamSalary);
			values[3][1] = Money.formatDouble(0);
			values[4][1] = "0   "; 
			values[5][1] = "0   "; 
		}

		for (int i = 0; i < values.length; i++) {
			this.addText(values[i]);
		}

		this.setColor();
	}

}
