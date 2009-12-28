package pl.pronux.sokker.ui.widgets.tables;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.LeagueComparator;
import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.LeagueRound;
import pl.pronux.sokker.model.LeagueTeam;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class LeagueTable extends SVTable<LeagueRound> {

	private LeagueComparator leagueComparator;

	public LeagueTable(Composite parent, int style) {
		super(parent, style);
		this.setVisible(true);
		this.setLinesVisible(true);
		this.setHeaderVisible(false);
		this.setFont(ConfigBean.getFontTable());
		this.setBackground(parent.getBackground());

		String[] titles = new String[] {
				Messages.getString("league.position"), //$NON-NLS-1$
				Messages.getString("league.team"), //$NON-NLS-1$
				Messages.getString("matches"), //$NON-NLS-1$
				Messages.getString("league.points"), //$NON-NLS-1$
				Messages.getString("league.wins"), //$NON-NLS-1$
				Messages.getString("league.draws"), //$NON-NLS-1$
				Messages.getString("league.looses"), //$NON-NLS-1$
				Messages.getString("league.goals"), //$NON-NLS-1$
				// Messages.getString("league.goals.gained"),
				// Messages.getString("league.goals.lost"),
				// Messages.getString("league.ranktotal"),
				"" //$NON-NLS-1$
		};

		for (int i = 0; i < titles.length; i++) {
			TableColumn column;
			if (i == 1) {
				column = new TableColumn(this, SWT.LEFT);
			} else {
				column = new TableColumn(this, SWT.RIGHT);
			}
			// column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);

			if (titles[i].isEmpty()) {
				if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
			if (i == 0) {
				column.setWidth(20);
			} else if (i == 1) {
				column.setWidth(150);
			} else if (i > 1 && i < titles.length-1) {
				column.setWidth(40);
			}
		}

		TableItem item = new TableItem(this, SWT.NONE);
		for (int i = 0; i < titles.length; i++) {
			item.setText(i, titles[i]);
			item.setForeground(Colors.getBlueDescription());
		}

		leagueComparator = new LeagueComparator();
		leagueComparator.setColumn(LeagueComparator.POINTS);
		leagueComparator.setDirection(LeagueComparator.DESCENDING);
	}

	public void fill(LeagueRound leagueRound) {
		this.setRedraw(false);
		this.remove(1, this.getItemCount() - 1);
		if (leagueRound == null) {
			this.setRedraw(true);
			return;
		}

		List<LeagueTeam> alLeagueTeams = leagueRound.getLeagueTeams();

		// Collections.sort(alLeagueTeams, leagueComparator);
		for (LeagueTeam leagueTeam : alLeagueTeams) {
			TableItem item = new TableItem(this, SWT.NONE);
			int i = 0;
			item.setText(i++, String.valueOf(leagueTeam.getPosition()));
			if (leagueTeam.getTeamName() != null && leagueTeam.getTeamName().length() > 0) {
				item.setText(i++, leagueTeam.getTeamName());
			} else if (leagueTeam.getClub() != null && leagueTeam.getClub().getClubName().size() > 0) {
				Club club = leagueTeam.getClub();
				item.setText(i++, club.getClubName().get(0).getName());
			} else {
				item.setText(i++, String.valueOf(leagueTeam.getTeamID()));
			}

			item.setText(i++, String.valueOf(leagueRound.getRoundNumber()));
			item.setText(i++, String.valueOf(leagueTeam.getPoints()));
			item.setText(i++, String.valueOf(leagueTeam.getWins()));
			item.setText(i++, String.valueOf(leagueTeam.getDraws()));
			item.setText(i++, String.valueOf(leagueTeam.getLosses()));
			item.setText(i++, String.valueOf(leagueTeam.getGoalsScored() + "-" + leagueTeam.getGoalsLost())); //$NON-NLS-1$
			// item.setText(i++, String.valueOf(leagueTeam.getGoalsLost()));
			// item.setText(i++, String.valueOf(leagueTeam.getRankTotal()));

		}

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 1) {
				this.getItem(i).setBackground(ColorResources.getGray());
			}
		}
		this.setRedraw(true);
		// table.pack();
		// table.computeSize(SWT.DEFAULT, SWT.DEFAULT);

	}
}
