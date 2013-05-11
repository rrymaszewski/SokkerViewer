package pl.pronux.sokker.ui.widgets.tables;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class PlayerTable extends SVTable<Player> {

	public static final int VALUE = 1;
	
	public static final int SALARY = 2;
	
	public static final int AGE = 3;
	
	public static final int WEIGHT = 4;
	
	public static final int BMI = 5;

	public static final int FORM = 6;
	
	public static final int STAMINA = 7;
	
	public static final int PACE = 8;
	
	public static final int TECHNIQUE = 9;
	
	public static final int PASSING = 10;
	
	public static final int KEEPER = 11;
	
	public static final int DEFENDER = 12;
	
	public static final int PLAYMAKER = 13;
	
	public static final int SCORER = 14;
	
	
	public PlayerTable(Composite parent, int style) {
		super(parent, style);
		this.setVisible(false);
		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] titles = new String[] {
				Messages.getString("table.date"), 
				Messages.getString("table.value"), 
				Messages.getString("table.salary"), 
				Messages.getString("table.age"), 
				Messages.getString("table.weight"),
				Messages.getString("table.bmi"), 
				Messages.getString("table.form"), 
				Messages.getString("table.stamina"), 
				Messages.getString("table.pace"), 
				Messages.getString("table.technique"), 
				Messages.getString("table.passing"), 
				Messages.getString("table.keeper"), 
				Messages.getString("table.defender"), 
				Messages.getString("table.playmaker"), 
				Messages.getString("table.scorer"), 
				Messages.getString("table.discipline"), 
				Messages.getString("table.experience"), 
				Messages.getString("table.teamwork"), 
				Messages.getString("table.formation"), 
				Messages.getString("table.training.type"), 
				Messages.getString("table.1st"), 
				Messages.getString("table.2nd"), 
				"" 
		};
		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.RIGHT);
			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			if (titles[j].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
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
			item.setData("date", player.getSkills()[i].getDate()); 
			item.setData("player_skill", player.getSkills()[i]); 
			item.setText(c++, player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString());
			item.setText(c++, player.getSkills()[i].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[i].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[i].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getWeight()));
			item.setText(c++, String.valueOf(player.getSkills()[i].getBmi()));
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
					item.setText(c++, Messages.getString("formation." + Training.FORMATION_ALL)); 
				} else {
					item.setText(c++, Messages.getString("formation." + player.getSkills()[i].getTraining().getFormation())); 
				}
				item.setText(c++, Messages.getString("training.type." + player.getSkills()[i].getTraining().getType() + ".short"));  
			} else {
				item.setText(c++, ""); 
				item.setText(c++, ""); 
			}

			if (player.getPlayerMatchStatistics() != null) {
				int week = player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY).getSokkerDate().getWeek();
				for (PlayerStats playerStats : player.getPlayerMatchStatistics()) {
					if (playerStats.getMatch().getWeek() == week) {
						if (playerStats.getFormation() >= 0 && playerStats.getFormation() <= 4 && playerStats.getTimePlayed() > 0) {
							League league = playerStats.getMatch().getLeague();

							if ((league.getType() == League.TYPE_LEAGUE || league.getType() == League.TYPE_PLAYOFF) && league.getIsOfficial() == League.OFFICIAL) {
								item.setFont(20, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(20).getFontData()));
								item.setText(20, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()) , playerStats.getTimePlayed()));  
//								if(league.getType() == League.TYPE_LEAGUE) {
//									item.setImage(18, ImageResources.getImageResources("league.png"));
//								} else if(league.getType() == League.TYPE_PLAYOFF) {
//									item.setImage(18, ImageResources.getImageResources("playoff.png"));
//								}
								if (playerStats.getFormation() == PlayerStats.GK) {
									item.setBackground(20, Colors.getPositionGK());
								} else if (playerStats.getFormation() == PlayerStats.DEF) {
									item.setBackground(20, Colors.getPositionDEF());
								} else if (playerStats.getFormation() == PlayerStats.MID) {
									item.setBackground(20, Colors.getPositionMID());
								} else if (playerStats.getFormation() == PlayerStats.ATT) {
									item.setBackground(20, Colors.getPositionATT());
								}

							} else {
								c++;
								if (league.getIsOfficial() == League.OFFICIAL) {
									item.setFont(21, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(21).getFontData()));
//									item.setImage(19, ImageResources.getImageResources("cup.png"));
								}
								if (playerStats.getFormation() == PlayerStats.GK) {
								item.setBackground(21, Colors.getPositionGK());
							} else if (playerStats.getFormation() == PlayerStats.DEF) {
								item.setBackground(21, Colors.getPositionDEF());
							} else if (playerStats.getFormation() == PlayerStats.MID) {
								item.setBackground(21, Colors.getPositionMID());
							} else if (playerStats.getFormation() == PlayerStats.ATT) {
								item.setBackground(21, Colors.getPositionATT());
							}
								item.setText(21, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()) , playerStats.getTimePlayed()));  
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
				item.setText(20, ""); 
				item.setText(21, ""); 
			}
			if (i > 0) {
				PlayerSkills now = player.getSkills()[i];
				PlayerSkills before = player.getSkills()[i - 1];
				int idx = 1;
				compare(now.getValue().toInt(), before.getValue().toInt(), item, idx++);
				compare(now.getSalary().toInt(), before.getSalary().toInt(), item, idx++);
				compare(now.getAge(), before.getAge(), item, idx++);
				compare(now.getWeight(), before.getWeight(), item, idx++);
				compare(now.getBmi(), before.getBmi(), item, idx++);
				compare(now.getForm(), before.getForm(), item, idx++);
				compare(now.getStamina(), before.getStamina(), item, idx++);
				compare(now.getPace(), before.getPace(), item, idx++);
				compare(now.getTechnique(), before.getTechnique(), item, idx++);
				compare(now.getPassing(), before.getPassing(), item, idx++);
				compare(now.getKeeper(), before.getKeeper(), item, idx++);
				compare(now.getDefender(), before.getDefender(), item, idx++);
				compare(now.getPlaymaker(), before.getPlaymaker(), item, idx++);
				compare(now.getScorer(), before.getScorer(), item, idx++);
				compare(now.getDiscipline(), before.getDiscipline(), item, idx++);
				compare(now.getExperience(), before.getExperience(), item, idx++);
				compare(now.getTeamwork(), before.getTeamwork(), item, idx++);
			}
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
//			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 5);
		}

		if (this.getColumn(20).getWidth() < this.getColumn(21).getWidth()) {
			this.getColumn(20).setWidth(this.getColumn(21).getWidth());
		} else {
			this.getColumn(21).setWidth(this.getColumn(20).getWidth());
		}
	}

	private void compare(double now, double before, TableItem item, int index) {
		if (before < now) {
			item.setBackground(index, ConfigBean.getColorIncrease());
		} else if (before > now) {
			item.setBackground(index, ConfigBean.getColorDecrease());
		}
	}
}
