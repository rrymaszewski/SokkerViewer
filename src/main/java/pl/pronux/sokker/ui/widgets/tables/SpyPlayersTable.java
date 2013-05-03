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

import pl.pronux.sokker.comparators.PlayerStatsComparator;
import pl.pronux.sokker.comparators.SpyPlayersComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerSkills;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.PaintStarListener;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class SpyPlayersTable extends SVTable<Player> implements IViewSort<Player> {
	private SpyPlayersComparator comparator;
	private List<Player> players;

	public SpyPlayersTable(Composite parent, int style) {
		super(parent, style);

		comparator = new SpyPlayersComparator();
		comparator.setColumn(SpyPlayersComparator.SURNAME);
		comparator.setDirection(SpyPlayersComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] titles = { "", 
				Messages.getString("table.name"), 
				Messages.getString("table.surname"), 
				Messages.getString("table.height"),
				Messages.getString("table.value"), 
				Messages.getString("table.salary"), 
				Messages.getString("table.age"), 
				Messages.getString("table.form"), 
				Messages.getString("table.discipline"), 
				Messages.getString("table.experience"), 
				Messages.getString("table.teamwork"), 
				Messages.getString("table.matches"), 
				Messages.getString("table.goals"), 
				Messages.getString("table.assists"), 
				Messages.getString("table.rating.avg.short"),
				Messages.getString("table.rating.max.short"), 
				Messages.getString("table.rating.min.short"), 
				Messages.getString("table.formation"),
				Messages.getString("table.cards"), 
				Messages.getString("table.injury"), 
				// Messages.getString("table.note.short"), 
				"" 
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
			// } else if (titles[j].equals(Messages.getString("table.salary")))
			// {
			// column.setWidth(70);
			// } else
			if (j == titles.length - 1) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else if ( j == SpyPlayersComparator.INJURY) {
				column.setWidth(50);
			} else if ( j == SpyPlayersComparator.RANKING_AVG) {
				column.setWidth(120);
			} else {
				// column.setWidth(40);
				column.setWidth(40);
//				column.pack();
			}
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).addSelectionListener(new SortTableListener<Player>(this, comparator));
		}

		this.addListener(SWT.PaintItem, new PaintStarListener(SpyPlayersComparator.RANKING_AVG));

		this.setSortColumn(this.getColumn(comparator.getColumn()));
		this.setSortDirection(comparator.getDirection());

		this.addLabelsListener();
	}

	public void fill(List<Player> players) {

		this.players = players;
		int max = 0;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		Collections.sort(players, comparator);
		for (Player player : players) {
			max = player.getSkills().length - 1;
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(Player.class.getName(), player);
			item.setData(PaintStarListener.class.getName(), player.getAvgRating());

			item.setImage(c++, FlagsResources.getFlag(player.getCountryfrom()));

			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, String.valueOf(player.getHeight()));
			item.setText(c++, player.getSkills()[max].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[max].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[max].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getForm()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getDiscipline()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getExperience()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getTeamwork()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getMatches()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getGoals()));
			item.setText(c++, String.valueOf(player.getSkills()[max].getAssists()));
			c++;
			if (player.getPlayerMatchStatistics().size() > 0) {
				PlayerStats maxPlayerMatchStatistics = Collections.max(player.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				PlayerStats minPlayerMatchStatistics = Collections.min(player.getPlayerMatchStatistics(), new PlayerStatsComparator(PlayerStatsComparator.RATING, PlayerStatsComparator.ASCENDING));
				item.setText(c++, String.format("%d (%d')", maxPlayerMatchStatistics.getRating(), maxPlayerMatchStatistics.getTimePlayed()));
				item.setText(c++, String.format("%d (%d')", minPlayerMatchStatistics.getRating(), minPlayerMatchStatistics.getTimePlayed()));
			} else {
				c++;
				c++;
			}
			item.setText(c++, Messages.getString("formation." + player.getPreferredPosition()));

			if (player.getSkills()[max].getCards() == 1) {
				item.setImage(c++, ImageResources.getImageResources("yellow_card.png")); 
			} else if (player.getSkills()[max].getCards() == 2) {
				item.setImage(c++, ImageResources.getImageResources("2_yellow_cards.png")); 
			} else if (player.getSkills()[max].getCards() >= 3) {
				item.setImage(c++, ImageResources.getImageResources("red_card.png")); 
			} else {
				c++;
			}

			if (player.getSkills()[max].getInjurydays() > 0) {
				item.setImage(c, ImageResources.getImageResources("injury.png")); 
				item.setText(c++, BigDecimal.valueOf(player.getSkills()[max].getInjurydays()).setScale(0, BigDecimal.ROUND_UP).toString());
			} else {
				c++;
			}

			// if (player.getNote() != null) {
			// if (player.getNote().equals("")) { 
			// c++;
			// } else {
			// item.setImage(c++, ImageResources.getImageResources("note.png"));
			// 
			// }
			// }

			if (player.getTransferList() > 0) {
				item.setBackground(1, ConfigBean.getColorTransferList());
				item.setBackground(2, ConfigBean.getColorTransferList());
			}
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			if (i == SpyPlayersComparator.INJURY) {
//				this.getColumn(i).setWidth(50);
			} else if (i == SpyPlayersComparator.RANKING_AVG) {
//				this.getColumn(i).setWidth(120);
			} else {
				this.getColumn(i).pack();
				// this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 5);
			}
		}

		this.setRedraw(true);
	}

	public SpyPlayersComparator getComparator() {
		return comparator;
	}

	public void sort(SVComparator<Player> comparator) {
		if (players != null) {
			Collections.sort(players, comparator);
			fill(players);
		}
	}

	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column >= SpyPlayersComparator.FORM && column <= SpyPlayersComparator.TEAMWORK || column == SpyPlayersComparator.RANKING_AVG) {
			Player player = (Player) item.getData(Player.class.getName());
			PlayerSkills skills = player.getSkills()[0];
			switch (column) {
			case SpyPlayersComparator.FORM:
				label.setText(Messages.getString("skill.a" + skills.getForm()));
				break;
			case SpyPlayersComparator.DISCIPLINE:
				label.setText(Messages.getString("skill.a" + skills.getDiscipline()));
				break;
			case SpyPlayersComparator.EXPERIENCE:
				label.setText(Messages.getString("skill.a" + skills.getExperience()));
				break;
			case SpyPlayersComparator.TEAMWORK:
				label.setText(Messages.getString("skill.a" + skills.getTeamwork()));
				break;
			case SpyPlayersComparator.RANKING_AVG:
				label.setText(String.valueOf(player.getAvgRating()));
				break;
			default:
				break;
			}
			label.pack();
		} else if (column == SpyPlayersComparator.NOTE) {
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
