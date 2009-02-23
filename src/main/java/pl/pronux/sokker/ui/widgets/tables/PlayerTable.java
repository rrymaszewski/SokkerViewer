package pl.pronux.sokker.ui.widgets.tables;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class PlayerTable extends SVTable<Player> implements SV {

	public PlayerTable(Composite parent, int style) {
		super(parent, style);
		this.setVisible(false);
		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] titles = new String[] {
				Messages.getString("table.date"), //$NON-NLS-1$
				Messages.getString("table.value"), //$NON-NLS-1$
				Messages.getString("table.salary"), //$NON-NLS-1$
				Messages.getString("table.age"), //$NON-NLS-1$
				Messages.getString("table.form"), //$NON-NLS-1$
				Messages.getString("table.stamina"), //$NON-NLS-1$
				Messages.getString("table.pace"), //$NON-NLS-1$
				Messages.getString("table.technique"), //$NON-NLS-1$
				Messages.getString("table.passing"), //$NON-NLS-1$
				Messages.getString("table.keeper"), //$NON-NLS-1$
				Messages.getString("table.defender"), //$NON-NLS-1$
				Messages.getString("table.playmaker"), //$NON-NLS-1$
				Messages.getString("table.scorer"), //$NON-NLS-1$
				Messages.getString("table.discipline"), //$NON-NLS-1$
				Messages.getString("table.experience"), //$NON-NLS-1$
				Messages.getString("table.teamwork"), //$NON-NLS-1$
				Messages.getString("table.formation"), //$NON-NLS-1$
				Messages.getString("table.training.type"), //$NON-NLS-1$
				Messages.getString("table.1st"), //$NON-NLS-1$
				Messages.getString("table.2nd"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};
		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.RIGHT);
			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			if (titles[j].equals("")) { //$NON-NLS-1$
				// column.setWidth(70);
				if (SettingsHandler.OS_TYPE == LINUX) {
					column.pack();
				}
			} else {
				// column.setWidth(40);
				column.pack();
			}
		}

	}

	public void fill(Player player) {
		int max = 0;
		
		max = player.getSkills().length;
		for (int i = max - 1; i >= 0; i--) {
			int c = 0;
			TableItem item = new TableItem(this, SWT.NONE);
			if(!player.getSkills()[i].isPassTraining()) {
				item.setForeground(ColorResources.getDarkGray());
			}
			item.setData("date", player.getSkills()[i].getDate()); //$NON-NLS-1$
			item.setData("player_skill", player.getSkills()[i]); //$NON-NLS-1$
			item.setText(c++, player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			item.setText(c++, player.getSkills()[i].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[i].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[i].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getForm()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getStamina()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getPace()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getTechnique()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getPassing()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getKeeper()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getDefender()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getPlaymaker()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getScorer()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getDiscipline()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getExperience()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getTeamwork()));
			if (player.getSkills()[i].getTraining() != null) {
				if (player.getSkills()[i].getTraining().getType() == Training.TYPE_PACE || player.getSkills()[i].getTraining().getType() == Training.TYPE_STAMINA) {
					item.setText(c++, Messages.getString("formation." + Training.FORMATION_ALL)); //$NON-NLS-1$
				} else {
					item.setText(c++, Messages.getString("formation." + player.getSkills()[i].getTraining().getFormation())); //$NON-NLS-1$
				}
				item.setText(c++, Messages.getString("training.type." + player.getSkills()[i].getTraining().getType() + ".short")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				item.setText(c++, ""); //$NON-NLS-1$
				item.setText(c++, ""); //$NON-NLS-1$
			}

			if (player.getPlayerMatchStatistics() != null) {
				int week = player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).getSokkerDate().getWeek();
				for (PlayerStats playerStats : player.getPlayerMatchStatistics()) {
					if (playerStats.getMatch().getWeek() == week) {
						if (playerStats.getFormation() >= 0 && playerStats.getFormation() <= 4 && playerStats.getTimePlayed() > 0) {
							League league = playerStats.getMatch().getLeague();

							if ((league.getType() == League.TYPE_LEAGUE || league.getType() == League.TYPE_PLAYOFF) && league.getIsOfficial() == League.OFFICIAL) {
								item.setFont(18, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(18).getFontData()));
								item.setText(18, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()) , playerStats.getTimePlayed())); //$NON-NLS-1$ //$NON-NLS-2$
//								if(league.getType() == League.TYPE_LEAGUE) {
//									item.setImage(18, ImageResources.getImageResources("league.png"));
//								} else if(league.getType() == League.TYPE_PLAYOFF) {
//									item.setImage(18, ImageResources.getImageResources("playoff.png"));
//								}
								if (playerStats.getFormation() == PlayerStats.GK) {
									item.setBackground(18, ColorResources.getColor(221, 255, 255));
								} else if (playerStats.getFormation() == PlayerStats.DEF) {
									item.setBackground(18, ColorResources.getColor(255, 230, 214));
								} else if (playerStats.getFormation() == PlayerStats.MID) {
									item.setBackground(18, ColorResources.getColor(255, 255, 208));
								} else if (playerStats.getFormation() == PlayerStats.ATT) {
									item.setBackground(18, ColorResources.getColor(226, 255, 208));
								}

							} else {
								c++;
								if (league.getIsOfficial() == League.OFFICIAL) {
									item.setFont(19, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(19).getFontData()));
//									item.setImage(19, ImageResources.getImageResources("cup.png"));
								}
								if (playerStats.getFormation() == PlayerStats.GK) {
								item.setBackground(19, ColorResources.getColor(221, 255, 255));
							} else if (playerStats.getFormation() == PlayerStats.DEF) {
								item.setBackground(19, ColorResources.getColor(255, 230, 214));
							} else if (playerStats.getFormation() == PlayerStats.MID) {
								item.setBackground(19, ColorResources.getColor(255, 255, 208));
							} else if (playerStats.getFormation() == PlayerStats.ATT) {
								item.setBackground(19, ColorResources.getColor(226, 255, 208));
							}
								item.setText(19, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()) , playerStats.getTimePlayed())); //$NON-NLS-1$ //$NON-NLS-2$
//								if(league.getType() == League.TYPE_LEAGUE) {
//									item.setImage(19, ImageResources.getImageResources("friendly_league.png"));
//								} else if(league.getType() == League.TYPE_FRIENDLY_MATCH) {
//									item.setImage(19, ImageResources.getImageResources("friendly_match.png"));
//								}
							}
						}
					}
				}
			} else {
				item.setText(18, ""); //$NON-NLS-1$
				item.setText(19, ""); //$NON-NLS-1$
			}
			if (i > 0) {
				PlayerSkills now = player.getSkills()[i];
				PlayerSkills before = player.getSkills()[i - 1];
				compare(now.getValue().toInt(), before.getValue().toInt(), item, 1);
				compare(now.getSalary().toInt(), before.getSalary().toInt(), item, 2);
				compare(now.getAge(), before.getAge(), item, 3);
				compare(now.getForm(), before.getForm(), item, 4);
				compare(now.getStamina(), before.getStamina(), item, 5);
				compare(now.getPace(), before.getPace(), item, 6);
				compare(now.getTechnique(), before.getTechnique(), item, 7);
				compare(now.getPassing(), before.getPassing(), item, 8);
				compare(now.getKeeper(), before.getKeeper(), item, 9);
				compare(now.getDefender(), before.getDefender(), item, 10);
				compare(now.getPlaymaker(), before.getPlaymaker(), item, 11);
				compare(now.getScorer(), before.getScorer(), item, 12);
				compare(now.getDiscipline(), before.getDiscipline(), item, 13);
				compare(now.getExperience(), before.getExperience(), item, 14);
				compare(now.getTeamwork(), before.getTeamwork(), item, 15);
			}
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 5);
		}

		if (this.getColumn(18).getWidth() < this.getColumn(19).getWidth()) {
			this.getColumn(18).setWidth(this.getColumn(19).getWidth());
		} else {
			this.getColumn(19).setWidth(this.getColumn(18).getWidth());
		}
	}

	private void compare(int now, int before, TableItem item, int index) {
		if (before < now) {
			item.setBackground(index, ConfigBean.getColorIncrease());
		} else if (before > now) {
			item.setBackground(index, ConfigBean.getColorDecrease());
		}
	}

}
