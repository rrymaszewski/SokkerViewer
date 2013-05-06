package pl.pronux.sokker.ui.widgets.composites;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class PlayersHistoryDescriptionComposite extends DescriptionSingleComposite {

	public PlayersHistoryDescriptionComposite(Composite parent, int style) {
		super(parent, style);

		// this.setDescriptionStringFormat(40, 15);
		this.setDescriptionStringFormat("%-40s%-35s\r\n"); 
		this.setFirstColumnSize(40);
		this.setSecondColumnSize(35);

		this.setFont(ConfigBean.getFontDescription());
	}

	public void setDescription(List<Player> players) {
		int maxSkill = 0;
//		double teamValuePast = 0;
//		double teamSalaryPast = 0;
//		double averAgePast = 0;
		double teamValue = 0;
		double teamSalary = 0;
		double averAge = 0;

		this.setText("");
		for (int i = 0; i < players.size(); i++) {
			maxSkill = players.get(i).getSkills().length - 1;
			teamValue += players.get(i).getSkills()[maxSkill].getValue().getDoubleValue();
			teamSalary += players.get(i).getSkills()[maxSkill].getSalary().getDoubleValue();
			averAge += players.get(i).getSkills()[maxSkill].getAge();

//			if (maxSkill > 1) {
//				teamValuePast += players.get(i).getSkills()[maxSkill - 1].getValue().getDoubleValue();
//				teamSalaryPast += players.get(i).getSkills()[maxSkill - 1].getSalary().getDoubleValue();
//				averAgePast += players.get(i).getSkills()[maxSkill - 1].getAge();
//			}
		}

		// aktualizujemy srednie wartosci
		String[][] values = new String[6][2];

		values[0][0] = Messages.getString("player.allValue");
		values[1][0] = Messages.getString("player.averageValue");
		values[2][0] = Messages.getString("player.allSalary");
		values[3][0] = Messages.getString("player.averageSalary");
		values[4][0] = Messages.getString("player.averageAge");
		values[5][0] = Messages.getString("player.allPlayers");

		values[0][1] = Money.formatDoubleCurrencySymbol(teamValue);

		if (players.size() > 0) {

			values[1][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamValue / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

			values[2][1] = Money.formatDoubleCurrencySymbol(teamSalary);

			values[3][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamSalary / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

			values[4][1] = BigDecimal.valueOf(averAge / players.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "   ";

			values[5][1] = String.valueOf(players.size()).toString() + "   ";

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
