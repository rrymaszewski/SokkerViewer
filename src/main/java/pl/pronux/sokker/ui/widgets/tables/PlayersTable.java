package pl.pronux.sokker.ui.widgets.tables;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.PlayerComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.League;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class PlayersTable extends SVTable<Player> implements IViewSort<Player> {
	private PlayerComparator comparator;
	
	public PlayersTable(Composite parent, int style) {
		super(parent, style);

		comparator = new PlayerComparator();
		comparator.setColumn(PlayerComparator.SURNAME);
		comparator.setDirection(PlayerComparator.ASCENDING);
		
		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());
		
		String[] titles = {
				"", //$NON-NLS-1$
				Messages.getString("table.name"), //$NON-NLS-1$
				Messages.getString("table.surname"), //$NON-NLS-1$
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
				Messages.getString("table.cards"), //$NON-NLS-1$
				Messages.getString("table.injury"), //$NON-NLS-1$
				Messages.getString("table.note.short"), //$NON-NLS-1$
				Messages.getString("table.1st"), //$NON-NLS-1$
				Messages.getString("table.2nd"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);

			if (j > 2) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			// if (titles[j].equals(Messages.getString("table.value"))) {
			// column.setWidth(100);
			// } else if (titles[j].equals(Messages.getString("table.salary"))) {
			// column.setWidth(70);
			// } else
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
		
		this.addLabelsListener();
	}

	public void fill(List<Player> players) {
		int max = 0;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.removeAll();
		Collections.sort(players, comparator);
		for (Player player : players) {
			max = player.getSkills().length - 1;
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(Player.class.getName(), player);
			item.setImage(c++, FlagsResources.getFlag(player.getCountryfrom()));
			
			if(!player.getSkills()[max].isPassTraining()) {
				item.setForeground(ColorResources.getDarkGray());
			}
			
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, player.getSkills()[max].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[max].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[max].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getForm()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getStamina()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getPace()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getTechnique()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getPassing()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getKeeper()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getDefender()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getPlaymaker()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getScorer()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getDiscipline()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getExperience()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getTeamwork()));
			if (player.getSkills()[max].getCards() == 1) {
				item.setImage(c++, ImageResources.getImageResources("yellow_card.png")); //$NON-NLS-1$
			} else if (player.getSkills()[max].getCards() == 2) {
				item.setImage(c++, ImageResources.getImageResources("2_yellow_cards.png")); //$NON-NLS-1$
			} else if (player.getSkills()[max].getCards() >= 3) {
				item.setImage(c++, ImageResources.getImageResources("red_card.png")); //$NON-NLS-1$
			} else {
				c++;
			}

			if (player.getSkills()[max].getInjurydays() > 0) {
				item.setImage(c, ImageResources.getImageResources("injury.png")); //$NON-NLS-1$
				item.setText(c++, BigDecimal.valueOf(player.getSkills()[max].getInjurydays()).setScale(0, BigDecimal.ROUND_UP).toString());
			} else {
				c++;
			}
			
			if (player.getNote() != null) {
				if (player.getNote().isEmpty()) {
					c++;
				} else {
					item.setImage(c++, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
				}
			}
			
			if (player.getPlayerMatchStatistics() != null) {
				int week = Cache.getDate().getSokkerDate().getWeek();
				for (PlayerStats playerStats : player.getPlayerMatchStatistics()) {
					if (playerStats.getMatch().getWeek() == week) {
						if (playerStats.getFormation() >= 0 && playerStats.getFormation() <= 4 && playerStats.getTimePlayed() > 0) {
							League league = playerStats.getMatch().getLeague();

							if ((league.getType() == League.TYPE_LEAGUE || league.getType() == League.TYPE_PLAYOFF) && league.getIsOfficial() == League.OFFICIAL) {
								item.setFont(PlayerComparator.MATCH_SUNDAY, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(PlayerComparator.MATCH_SUNDAY).getFontData()));
								item.setText(PlayerComparator.MATCH_SUNDAY, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()), playerStats.getTimePlayed())); //$NON-NLS-1$ //$NON-NLS-2$
								if (playerStats.getFormation() == PlayerStats.GK) {
									item.setBackground(PlayerComparator.MATCH_SUNDAY, Colors.getPositionGK());
								} else if (playerStats.getFormation() == PlayerStats.DEF) {
									item.setBackground(PlayerComparator.MATCH_SUNDAY, Colors.getPositionDEF());
								} else if (playerStats.getFormation() == PlayerStats.MID) {
									item.setBackground(PlayerComparator.MATCH_SUNDAY, Colors.getPositionMID());
								} else if (playerStats.getFormation() == PlayerStats.ATT) {
									item.setBackground(PlayerComparator.MATCH_SUNDAY, Colors.getPositionATT());
								}
							} else {
								if (league.getIsOfficial() == League.OFFICIAL) {
									item.setFont(PlayerComparator.MATCH_WEDNESDAY, Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont(PlayerComparator.MATCH_SUNDAY).getFontData()));
								}
								item.setText(PlayerComparator.MATCH_WEDNESDAY, String.format("%s (%d')", Messages.getString("formation." + playerStats.getFormation()), playerStats.getTimePlayed())); //$NON-NLS-1$ //$NON-NLS-2$
								if (playerStats.getFormation() == PlayerStats.GK) {
									item.setBackground(PlayerComparator.MATCH_WEDNESDAY, Colors.getPositionGK());
								} else if (playerStats.getFormation() == PlayerStats.DEF) {
									item.setBackground(PlayerComparator.MATCH_WEDNESDAY, Colors.getPositionDEF());
								} else if (playerStats.getFormation() == PlayerStats.MID) {
									item.setBackground(PlayerComparator.MATCH_WEDNESDAY, Colors.getPositionMID());
								} else if (playerStats.getFormation() == PlayerStats.ATT) {
									item.setBackground(PlayerComparator.MATCH_WEDNESDAY, Colors.getPositionATT());
								}
							}
						}
					}
				}
			} else {
				item.setText(c++, ""); //$NON-NLS-1$
				item.setText(c++, ""); //$NON-NLS-1$
			}
			
			if (max > 0) {
				this.getChanges(player.getSkills()[max].getValue().toInt(), player.getSkills()[max - 1].getValue().toInt(), item, PlayerComparator.VALUE);
				this.getChanges(player.getSkills()[max].getSalary().toInt(), player.getSkills()[max - 1].getSalary().toInt(), item, PlayerComparator.SALARY);
				this.getChanges(player.getSkills()[max].getAge(), player.getSkills()[max - 1].getAge(), item, PlayerComparator.AGE);
				this.getChanges(player.getSkills()[max].getForm(), player.getSkills()[max - 1].getForm(), item, PlayerComparator.FORM);
				this.getChanges(player.getSkills()[max].getStamina(), player.getSkills()[max - 1].getStamina(), item, PlayerComparator.STAMINA);
				this.getChanges(player.getSkills()[max].getPace(), player.getSkills()[max - 1].getPace(), item, PlayerComparator.PACE);
				this.getChanges(player.getSkills()[max].getTechnique(), player.getSkills()[max - 1].getTechnique(), item, PlayerComparator.TECHNIQUE);
				this.getChanges(player.getSkills()[max].getPassing(), player.getSkills()[max - 1].getPassing(), item, PlayerComparator.PASSING);
				this.getChanges(player.getSkills()[max].getKeeper(), player.getSkills()[max - 1].getKeeper(), item, PlayerComparator.KEEPER);
				this.getChanges(player.getSkills()[max].getDefender(), player.getSkills()[max - 1].getDefender(), item, PlayerComparator.DEFENDER);
				this.getChanges(player.getSkills()[max].getPlaymaker(), player.getSkills()[max - 1].getPlaymaker(), item, PlayerComparator.PLAYMAKER);
				this.getChanges(player.getSkills()[max].getScorer(), player.getSkills()[max - 1].getScorer(), item, PlayerComparator.SCORER);
				this.getChanges(player.getSkills()[max].getDiscipline(), player.getSkills()[max - 1].getDiscipline(), item, PlayerComparator.DISCIPLINE);
				this.getChanges(player.getSkills()[max].getExperience(), player.getSkills()[max - 1].getExperience(), item, PlayerComparator.EXPERIENCE);
				this.getChanges(player.getSkills()[max].getTeamwork(), player.getSkills()[max - 1].getTeamwork(), item, PlayerComparator.TEAMWORK);
			} else {
				item.setBackground(1, ConfigBean.getColorNewTableObject());
				item.setBackground(2, ConfigBean.getColorNewTableObject());
			}
			if (player.getTransferList() > 0) {
				item.setBackground(1, ConfigBean.getColorTransferList());
				item.setBackground(2, ConfigBean.getColorTransferList());
			}
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			// table.getColumn(i).setWidth(table.getColumn(i).getWidth());
		}

		// table.getColumn(PlayerComparator.CARDS).setWidth(30);
		// table.getColumn(PlayerComparator.NOTE).setWidth(30);
		// Turn drawing back on
		this.setRedraw(true);
	}


	public void filterTable(String text) {
		if (text.equalsIgnoreCase(Messages.getString("view.jumps"))) { //$NON-NLS-1$
			for (int i = 0; i < this.getItemCount(); i++) {
				for (int j = 7; j < this.getColumnCount(); j++) {
					if (this.getItem(i).getBackground(j).equals(ConfigBean.getColorIncrease()) || this.getItem(i).getBackground(j).equals(ConfigBean.getColorDecrease())) {
						break;
					}
					if (j == this.getColumnCount() - 1) {
						this.remove(i);
						i--;
					}
				}
			}
		}
	}

	public PlayerComparator getComparator() {
		return comparator;
	}
	
	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column >= PlayerComparator.VALUE &&  column <= PlayerComparator.TEAMWORK) {
			Player player = (Player) item.getData(Player.class.getName());
			int maxSkill = player.getSkills().length - 1;
			int[] temp1 = player.getSkills()[maxSkill].getStatsTable();
			if (maxSkill > 0) {
				int[] temp2 = player.getSkills()[maxSkill - 1].getStatsTable();
				if (column >= PlayerComparator.FORM) {
					if (temp1[column - 3] - temp2[column - 3] > 0) {
						label.setText(Messages.getString("skill.a" + temp1[column - 3]) + " (" + SVNumberFormat.formatIntegerWithSignZero(temp1[column - 3] - temp2[column - 3]) + ")");
						label.setForeground(ConfigBean.getColorIncreaseDescription());
					} else if (temp1[column - 3] - temp2[column - 3] < 0) {
						label.setText(Messages.getString("skill.a" + temp1[column - 3]) + " (" + SVNumberFormat.formatIntegerWithSignZero(temp1[column - 3] - temp2[column - 3]) + ")");
						label.setForeground(ConfigBean.getColorDecreaseDescription());
					} else {
						label.setText(Messages.getString("skill.a" + temp1[column - 3]) + " (" + String.valueOf(temp1[column - 3] - temp2[column - 3]) + ")");
					}

				} else {
					if (temp1[column - 3] - temp2[column - 3] > 0) {
						label.setText(SVNumberFormat.formatIntegerWithSignZero(temp1[column - 3] - temp2[column - 3]));
						label.setForeground(ConfigBean.getColorIncreaseDescription());
					} else if (temp1[column - 3] - temp2[column - 3] < 0) {
						label.setText(SVNumberFormat.formatIntegerWithSignZero(temp1[column - 3] - temp2[column - 3]));
						label.setForeground(ConfigBean.getColorDecreaseDescription());
					} else {
						label.setText(String.valueOf(temp1[column - 3] - temp2[column - 3]));
					}
				}
			} else {
				if (column >= PlayerComparator.FORM) {
					label.setText(Messages.getString("skill.a" + temp1[column - 3]) + " (0)");
				} else {
					label.setText("0");
				}
			}
			label.pack();
		} else if (column == PlayerComparator.NOTE) {
			int minSizeX = 200;
			int minSizeY = 80;

			int maxSizeX = 400;
			int maxSizeY = 200;

			Player player = (Player) item.getData(Player.class.getName());
			if (player.getNote() != null) {
				if (!player.getNote().isEmpty()) {
					label.setText(player.getNote());

					Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);

					if (size.x < minSizeX) {
						size.x = minSizeX;
					}
					if (size.y < minSizeY) {
						size.y = minSizeY;
					}

					if (size.x > maxSizeX) {
						size.x = maxSizeX;
					}

					if (size.y > maxSizeY) {
						size.y = maxSizeY;
					}
					label.setSize(size);
				}
			}
		}
		super.setLabel(label, column, item);
	}

}
