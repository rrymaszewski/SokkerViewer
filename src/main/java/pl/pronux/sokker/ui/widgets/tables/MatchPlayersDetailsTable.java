package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.MatchPlayersComparator;
import pl.pronux.sokker.comparators.MatchPlayersDetailsComparator;
import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.PaintStarListener;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class MatchPlayersDetailsTable extends SVTable<PlayerStats> implements IViewSort<PlayerStats> {

	private List<PlayerStats> playersStats;
	private MatchPlayersDetailsComparator comparator;

	public MatchPlayersDetailsTable(Composite parent, int style) {
		super(parent, style);

		comparator = new MatchPlayersDetailsComparator();
		comparator.setColumn(MatchPlayersDetailsComparator.NUMBER);
		comparator.setDirection(MatchPlayersDetailsComparator.ASCENDING);
		
		String[] columnsTooltips = { Messages.getString("table.match.substitutions.tooltip"), Messages.getString("table.match.number.tooltip"), Messages.getString("table.match.player.tooltip"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Messages.getString("table.match.formation.tooltip"), Messages.getString("table.match.time.tooltip"), Messages.getString("table.match.rating.tooltip"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Messages.getString("table.match.goals.tooltip"), Messages.getString("table.match.shoots.tooltip"), Messages.getString("table.match.assists.tooltip"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Messages.getString("table.match.fouls.tooltip"), Messages.getString("table.match.injury.tooltip"), Messages.getString("table.match.cards.tooltip"), " " }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		String[] columns = { " ", Messages.getString("table.match.number"), Messages.getString("table.match.player"), Messages.getString("table.match.formation"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				Messages.getString("table.match.time"), Messages.getString("table.match.rating"), Messages.getString("table.match.goals"), Messages.getString("table.match.shoots"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				Messages.getString("table.match.assists"), Messages.getString("table.match.fouls"), Messages.getString("table.match.injury"), Messages.getString("table.match.cards"), " " }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(this, SWT.LEFT);
			column.setText(columns[i]);
			column.setResizable(false);
			column.setMoveable(false);
			if (i == 0) {
				column.setWidth(25);
			} else if (i == columns.length - 1) {
				if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
					column.pack();
				}
			} else if (i == MatchPlayersDetailsComparator.STARS) {
				column.setWidth(120);
			} else {
				column.pack();
				column.setToolTipText(columnsTooltips[i]);
			}
			// column.pack();
		}
		
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).addSelectionListener(new SortTableListener<PlayerStats>(this, comparator));
		}

		this.setLinesVisible(false);
		this.setHeaderVisible(true);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontTable());
		
		this.setSortColumn(this.getColumn(MatchPlayersDetailsComparator.NUMBER));
		this.setSortDirection(MatchPlayersDetailsComparator.ASCENDING);

		this.addListener(SWT.PaintItem, new PaintStarListener(MatchPlayersComparator.RATING));
	}

	public void fill(List<PlayerStats> playersStats) {
		this.playersStats = playersStats;
		this.setRedraw(false);
		this.remove(0, this.getItemCount() - 1);
		if (playersStats == null) {
			this.setRedraw(true);
			return;
		}
		for (PlayerStats playerStats : playersStats) {
			TableItem item = new TableItem(this, SWT.NONE);
			if (playerStats.getFormation() == PlayerStats.GK) {
				item.setBackground(Colors.getPositionGK());
			} else if (playerStats.getFormation() == PlayerStats.DEF) {
				item.setBackground(Colors.getPositionDEF());
			} else if (playerStats.getFormation() == PlayerStats.MID) {
				item.setBackground(Colors.getPositionMID());
			} else if (playerStats.getFormation() == PlayerStats.ATT) {
				item.setBackground(Colors.getPositionATT());
			}
			int i = 0;
			if (playerStats.getTimeOut() > 0 && playerStats.getTimeIn() == 0) {
				item.setImage(i++, ImageResources.getImageResources("down_icon.png")); //$NON-NLS-1$
			} else if (playerStats.getTimeIn() > 0 && playerStats.getTimeOut() == 0) {
				item.setImage(i++, ImageResources.getImageResources("up_icon.png")); //$NON-NLS-1$
			} else if (playerStats.getTimeIn() > 0 && playerStats.getTimeOut() > 0) {
				item.setImage(i++, ImageResources.getImageResources("in_out.png")); //$NON-NLS-1$
			} else {
				i++;
			}

			if (playerStats.getTimeIn() > 0) {
				item.setText(i++, "R" + playerStats.getNumber()); //$NON-NLS-1$
			} else if (playerStats.getTimePlayed() == 0) {
				item.setText(i++, "R"); //$NON-NLS-1$
			} else {
				item.setText(i++, String.valueOf(playerStats.getNumber()));
			}

			Player player = playerStats.getPlayer();
			if (player != null && player.getExistsInSokker() != Player.EXISTS_IN_SOKKER_UNCHECKED) {
				if (player.getExistsInSokker() == Player.EXISTS_IN_SOKKER_TRUE || player.getExistsInSokker() == Player.EXISTS_IN_SOKKER_COMPLETED) {
					item.setImage(i, FlagsResources.getFlag(player.getCountryfrom()));
					if (player.getName() == null || player.getSurname() == null || (player.getName().isEmpty() && player.getSurname().isEmpty())) {
						item.setText(i++, String.valueOf(playerStats.getPlayerID()));
					} else {
						if(player.getName().isEmpty()) {
							item.setText(i++, String.format("%s", player.getSurname())); //$NON-NLS-1$
						} else {
							item.setText(i++, String.format("%s %s.", player.getSurname(), player.getName().substring(0, 1))); //$NON-NLS-1$
						}
					}
				} else {
					item.setImage(i, FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
					item.setText(i++, String.valueOf(playerStats.getPlayerID() + " (n/a)")); //$NON-NLS-1$
				}
			} else {
				item.setImage(i, FlagsResources.getFlag(FlagsResources.QUESTION_FLAG));
				item.setText(i++, String.valueOf(playerStats.getPlayerID() + " (n/d)")); //$NON-NLS-1$
			}

			if (playerStats.getFormation() >= 0 && playerStats.getFormation() <= 4) {
				item.setText(i++, Messages.getString("formation." + playerStats.getFormation())); //$NON-NLS-1$
			} else {
				item.setText(i++, ""); //$NON-NLS-1$
			}

			if (playerStats.getTimePlayed() == 0) {
				item.setForeground(ColorResources.getDarkGray());
				item.setBackground(ColorResources.getWhite());
			}
			item.setText(i++, playerStats.getTimePlayed() + "'"); //$NON-NLS-1$

			item.setData(PaintStarListener.IDENTIFIER, playerStats.getRating());
			i++;
			// item.setText(i++, playerStats.getRating() + "%"); //$NON-NLS-1$
			item.setText(i++, String.valueOf(playerStats.getGoals()));
			item.setText(i++, String.valueOf(playerStats.getShoots()));
			item.setText(i++, String.valueOf(playerStats.getAssists()));
			item.setText(i++, String.valueOf(playerStats.getFouls()));

			item.setText(i, ""); //$NON-NLS-1$
			if (SettingsHandler.OS_TYPE == OperatingSystem.WINDOWS) {
				item.setBackground(i, this.getBackground());
			}

			if (playerStats.getIsInjured() == PlayerStats.INJURED) {
				item.setImage(i++, ImageResources.getImageResources("injury.png")); //$NON-NLS-1$
			} else {
				i++;
			}

			item.setText(i, ""); //$NON-NLS-1$
			if (SettingsHandler.OS_TYPE == OperatingSystem.WINDOWS) {
				item.setBackground(i, this.getBackground());
			}
			if (playerStats.getYellowCards() < 2 && playerStats.getRedCards() > 0) {
				item.setImage(i++, ImageResources.getImageResources("red_card.png")); //$NON-NLS-1$
			} else if (playerStats.getYellowCards() > 1 && playerStats.getRedCards() > 0) {
				item.setImage(i++, ImageResources.getImageResources("2_yellow_cards_1_red_card.png")); //$NON-NLS-1$
			} else if (playerStats.getYellowCards() == 1 && playerStats.getRedCards() < 1) {
				item.setImage(i++, ImageResources.getImageResources("yellow_card.png")); //$NON-NLS-1$
			} else if (playerStats.getYellowCards() > 1 && playerStats.getRedCards() < 1) {
				item.setImage(i++, ImageResources.getImageResources("2_yellow_cards.png")); //$NON-NLS-1$
			} else {
				i++;
			}
			// item.setText(i++, String.valueOf(playerStats.getYellowCards() + "
			// " +
			// playerStats.getRedCards()));
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			if (i == 0 || i == MatchPlayersDetailsComparator.STARS) {
				// this.getColumn(i).setWidth(25);
			} else {
				this.getColumn(i).pack();
			}
			// else {
			// this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 3);
			// }
		}

		// for (int i = 0; i < this.getItemCount(); i++) {
		// if ((i % 2) == 1) {
		// this.getItem(i).setBackground(this.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		// }
		// }
		this.setRedraw(true);
		// table.pack();
		// table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	public void translate(ResourceBundle langResources) {
	}

	public void sort(SVComparator<PlayerStats> comparator) {
		if (playersStats != null) {
			Collections.sort(playersStats, comparator);
			fill(playersStats);
		}
	}

	public SVComparator<PlayerStats> getComparator() {
		return comparator;
	}

}
