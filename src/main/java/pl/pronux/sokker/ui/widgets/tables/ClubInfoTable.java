package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class ClubInfoTable extends SVTable<Club> {

	public ClubInfoTable(Composite parent, int style) {
		super(parent, style);
		
		TableItem item;
		TableColumn tableColumn;
		this.setLinesVisible(false);
		this.setHeaderVisible(false);
		this.setBackground(parent.getBackground());
//		this.setFont(ConfigBean.getFontTable());
		this.setFont(ConfigBean.getFontMain());

		String[] column = {
				"first", //$NON-NLS-1$
				"second", //$NON-NLS-1$
				"third" //$NON-NLS-1$
		};

		for (int i = 0; i < column.length; i++) {
			tableColumn = new TableColumn(this, SWT.LEFT);
			tableColumn.setText(column[i]);
		}

		String[] firstColumn = {
				Messages.getString("club.owner"), //$NON-NLS-1$
				Messages.getString("club.id"), //$NON-NLS-1$
				Messages.getString("club.name"), //$NON-NLS-1$
				Messages.getString("club.date.created"), //$NON-NLS-1$
				Messages.getString("club.rank"), //$NON-NLS-1$
				Messages.getString("club.country"), //$NON-NLS-1$
				Messages.getString("club.region"), //$NON-NLS-1$
				Messages.getString("club.arenaname"), //$NON-NLS-1$
				Messages.getString("club.money"), //$NON-NLS-1$
				Messages.getString("club.credit.interest"), //$NON-NLS-1$
				Messages.getString("club.fanclubcount"), //$NON-NLS-1$
				Messages.getString("club.fanclubmood"), //$NON-NLS-1$
				Messages.getString("club.end.seasons.fanclub") //$NON-NLS-1$
//				Messages.getString("club.end.seasons.sponsors")
				
		};

		for (int i = 0; i < firstColumn.length; i++) {
			item = new TableItem(this, SWT.NONE);
			item.setText(0, firstColumn[i]);
		}

		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 0) {
				this.getItem(i).setBackground(ColorResources.getGray());
			} else {
				this.getItem(i).setBackground(this.getBackground());
			}
		}

	}
	
	public void fill(Club club) {
		if(club == null) {
			return;
		}
		int firstColumn = 1;
		int secondColumn = 2;
		
		int diffrents;
		TableItem item;
		
		int maxSkillMoney = club.getClubBudget().size();
		int maxSkillFanclub = club.getClubSupporters().size();
//		int maxRank = club.getRank().size();

		int c = 0;

		item = this.getItem(c++);
		item.setText(firstColumn, SettingsHandler.getSokkerViewerSettings().getUsername());

		item = this.getItem(c++);
		item.setText(firstColumn, String.valueOf(club.getId()));

		item = this.getItem(c++);
		item.setText(firstColumn, club.getClubName().get(club.getClubName().size() - 1).getName());
		if (club.getClubBudget().get(maxSkillMoney - 1).getMoney().toInt() < -1100000) {
			item.setText(secondColumn, Messages.getString("club.warning")); //$NON-NLS-1$
			item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
		}

		item = this.getItem(c++);
		if(club.getDateCreated() != null) {
			item.setText(firstColumn, club.getDateCreated().toDateTimeString());
		} else {
			item.setText(firstColumn, "-"); //$NON-NLS-1$
		}


		item = this.getItem(c++);
		if(club.getRank().size() > 0) {
			item.setText(firstColumn, String.valueOf(club.getRank().get(0).getRank()));
		} else {
			item.setText(firstColumn, "-"); //$NON-NLS-1$
		}

		if(club.getRank().size() > 1) {
			double diffrentsDouble = club.getRank().get(0).getRank() - club.getRank().get(1).getRank();
			if (diffrentsDouble > 0) {
				item.setText(secondColumn, SVNumberFormat.formatDoubleWithSign(diffrentsDouble));
				item.setForeground(secondColumn, ConfigBean.getColorIncreaseDescription());
			} else if (diffrentsDouble < 0) {
				item.setText(secondColumn,  SVNumberFormat.formatDoubleWithSign(diffrentsDouble));
				item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
			} else {
				item.setForeground(secondColumn, this.getForeground());
			}
		}


		item = this.getItem(c++);
		item.setText(firstColumn, Messages.getString("country." + club.getCountry() + ".name")); //$NON-NLS-1$ //$NON-NLS-2$


		item = this.getItem(c++);
		item.setText(firstColumn, club.getRegion().getName());

		item = this.getItem(c++);
		item.setText(firstColumn, club.getArena().getArenaNames().get(club.getArena().getArenaNames().size() - 1).getArenaName());

		item = this.getItem(c++);
		item.setText(firstColumn, club.getClubBudget().get(maxSkillMoney - 1).getMoney().formatDoubleCurrencySymbol());

		if (club.getClubBudget().get(maxSkillMoney - 1).getMoney().toInt() > 0) {
			item.setForeground(secondColumn, ConfigBean.getColorIncreaseDescription());
		} else if (club.getClubBudget().get(maxSkillMoney - 1).getMoney().toInt() < 0) {
			item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
		} else {
			item.setForeground(secondColumn, this.getForeground());
		}

		if (maxSkillMoney > 1) {
			diffrents = club.getClubBudget().get(maxSkillMoney - 1).getMoney().toInt() - club.getClubBudget().get(maxSkillMoney - 2).getMoney().toInt();

			if (diffrents > 0) {
				item.setText(secondColumn, Money.formatIntegerSingCurrencySymbol(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorIncreaseDescription());
			} else if (diffrents < 0) {
				item.setText(secondColumn, Money.formatIntegerSingCurrencySymbol(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
			} else {
				item.setForeground(secondColumn, this.getForeground());
			}
		}

		item = this.getItem(c++);
		item.setText(firstColumn, club.getCreditInterest().formatIntegerCurrencySymbol());
		
		item = this.getItem(c++);
		item.setText(firstColumn, String.valueOf(club.getClubSupporters().get(maxSkillFanclub - 1).getFanclubcount()));

		if (maxSkillFanclub > 1) {
			diffrents = club.getClubSupporters().get(maxSkillFanclub - 1).getFanclubcount() - club.getClubSupporters().get(maxSkillFanclub - 2).getFanclubcount();

			if (diffrents > 0) {
				item.setText(secondColumn, SVNumberFormat.formatIntegerWithSignZero(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorIncreaseDescription());
			} else if (diffrents < 0) {
				item.setText(secondColumn, SVNumberFormat.formatIntegerWithSignZero(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
			} else {
				item.setText(secondColumn, ""); //$NON-NLS-1$
				item.setForeground(secondColumn, this.getForeground());
			}
		} else {
			item.setText(secondColumn, ""); //$NON-NLS-1$
			item.setForeground(secondColumn, this.getForeground());
		}

		item = this.getItem(c++);
		item.setText(firstColumn, String.format("%s [%d/6]", Messages.getString("fanclubmood." + club.getClubSupporters().get(maxSkillFanclub - 1).getFanclubmood()), club.getClubSupporters().get(maxSkillFanclub - 1).getFanclubmood())); //$NON-NLS-1$ //$NON-NLS-2$

		if (maxSkillFanclub > 1) {
			diffrents = club.getClubSupporters().get(maxSkillFanclub - 1).getFanclubmood() - club.getClubSupporters().get(maxSkillFanclub - 2).getFanclubmood();
			if (diffrents > 0) {
				item.setText(secondColumn, SVNumberFormat.formatIntegerWithSignZero(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorIncreaseDescription());
			} else if (diffrents < 0) {
				item.setText(secondColumn, SVNumberFormat.formatIntegerWithSignZero(diffrents));
				item.setForeground(secondColumn, ConfigBean.getColorDecreaseDescription());
			} else {
				item.setText(secondColumn, ""); //$NON-NLS-1$
				item.setForeground(secondColumn, this.getForeground());
			}
		} else {
			item.setText(secondColumn, ""); //$NON-NLS-1$
			item.setForeground(secondColumn, this.getForeground());
		}
		
		item = this.getItem(c++);
		item.setText(firstColumn, club.getFanclubSupport().formatIntegerCurrencySymbol());

//		item = this.getItem(c++);
//		item.setText(firstColumn, club.getSponsorsBonus().formatIntegerCurrencySymbol() );

		
		for (int i = 0; i < this.getColumnCount(); i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}

}
