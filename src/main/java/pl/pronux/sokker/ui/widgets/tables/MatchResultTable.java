package pl.pronux.sokker.ui.widgets.tables;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerStats;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class MatchResultTable extends SVTable<Match> {

	private Table table;

	private List<PlayerStats> playersStats;

	public MatchResultTable(final Composite parent, int style) {
		super(parent, style);

		table = this;

		this.setLinesVisible(false);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontTable());

		new TableColumn(this, SWT.LEFT);
		new TableColumn(this, SWT.CENTER);
		new TableColumn(this, SWT.CENTER);
		new TableColumn(this, SWT.CENTER);

		// this.getColumn(0).setWidth(0);
		// this.getColumn(1).setWidth((width - 20)/2);
		// this.getColumn(2).setWidth(20);
		// this.getColumn(3).setWidth((width - 20)/2);

		TableItem item = new TableItem(this, SWT.NONE);
		item.setText(2, "-"); //$NON-NLS-1$

		item = new TableItem(this, SWT.NONE);
		item.setText(2, ":"); //$NON-NLS-1$

		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = parent.getClientArea();
				Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = table.getVerticalBar();
				int width = area.width - table.computeTrim(0, 0, 0, 0).width - vBar.getSize().x;
				if (size.y > area.height + table.getHeaderHeight()) {
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) {
					// table is getting smaller so make the columns
					// smaller first and then resize the table to
					// match the client area width
					table.getColumn(0).setWidth(0);
					table.getColumn(1).setWidth((width - 20) / 2);
					table.getColumn(2).setWidth(20);
					table.getColumn(3).setWidth((width - 20) / 2);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					table.getColumn(0).setWidth(0);
					table.getColumn(1).setWidth((width - 20) / 2);
					table.getColumn(2).setWidth(20);
					table.getColumn(3).setWidth((width - 20) / 2);
				}
			}
		});
	}

	private void fillColumn(List<PlayerStats> playersStats, int column, int startItem) {
		String sPlayer;
		for (PlayerStats playerStats : playersStats) {
			if (playerStats.getGoals() > 0) {
				
				Player player = playerStats.getPlayer();
				if (player != null && player.getExistsInSokker() != Player.EXISTS_IN_SOKKER_UNCHECKED) {
					if(player.getExistsInSokker() == Player.EXISTS_IN_SOKKER_TRUE || player.getExistsInSokker() == Player.EXISTS_IN_SOKKER_COMPLETED) {
						if (player.getName() == null || player.getSurname() == null || (player.getName().equals("") && player.getSurname().equals(""))) { //$NON-NLS-1$ //$NON-NLS-2$
							sPlayer = playerStats.getPlayerID() + " (n/d)"; //$NON-NLS-1$
						} else {
							if (player.getName().length() > 0) {
								sPlayer = String.format("%s %s.", player.getSurname(), player.getName().substring(0, 1)); //$NON-NLS-1$
							} else {
								sPlayer = playerStats.getPlayerID() + " (?)"; //$NON-NLS-1$
							}
						}
					} else {
						sPlayer = playerStats.getPlayerID() + " (n/a)"; //$NON-NLS-1$
					}
				} else {
					sPlayer = playerStats.getPlayerID() + " (n/d)"; //$NON-NLS-1$
				}
//				if (player != null) {
//					if (player.getName() != null && player.getSurname() != null) {
//						if (player.getName().length() > 0) {
//							sPlayer = player.getSurname() + " " + player.getName().substring(0, 1) + ".";
//						}
//					}
//				}

				if (this.getItemCount() > startItem) {
					this.getItem(startItem++).setText(column, String.format("%s (%d)", sPlayer, playerStats.getGoals() )); //$NON-NLS-1$
				} else {
					new TableItem(this, SWT.NONE).setText(column, String.format("%s (%d)", sPlayer, playerStats.getGoals())); //$NON-NLS-1$
					startItem++;
				}
			}
		}
	}
	
	public void fill(Match match) {
		
		this.setRedraw(false);
		this.remove(2, this.getItemCount() - 1);
		
		if(match.getIsFinished() == Match.NOT_FINISHED) {
			this.getItem(0).setText(1, String.valueOf(match.getHomeTeamID()));
			this.getItem(0).setText(3, String.valueOf(match.getAwayTeamID()));

			this.getItem(1).setText(1, "-"); //$NON-NLS-1$
			this.getItem(1).setText(3, "-"); //$NON-NLS-1$
			this.setRedraw(true);
			return;
		}
		
		this.getItem(0).setText(1, match.getHomeTeamName());
		this.getItem(0).setText(3, match.getAwayTeamName());

		this.getItem(1).setText(1, String.valueOf(match.getHomeTeamScore()));
		this.getItem(1).setText(3, String.valueOf(match.getAwayTeamScore()));

		if (match.getHomeTeamScore() > match.getAwayTeamScore()) {
			this.getItem(0).setFont(1, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
			this.getItem(1).setFont(1, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
			this.getItem(0).setFont(3, ConfigBean.getFontTable());
			this.getItem(1).setFont(3, ConfigBean.getFontTable());
		} else if (match.getHomeTeamScore() < match.getAwayTeamScore()) {
			this.getItem(0).setFont(3, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
			this.getItem(1).setFont(3, Fonts.getBoldFont(this.getDisplay(), this.getFont().getFontData()));
			this.getItem(0).setFont(1, ConfigBean.getFontTable());
			this.getItem(1).setFont(1, ConfigBean.getFontTable());
		} else {
			this.getItem(0).setFont(3, ConfigBean.getFontTable());
			this.getItem(1).setFont(3, ConfigBean.getFontTable());
			this.getItem(0).setFont(1, ConfigBean.getFontTable());
			this.getItem(1).setFont(1, ConfigBean.getFontTable());
			
		}

		playersStats = match.getHomeTeamStats().getPlayersStats();
		fillColumn(playersStats, 1, 2);

		playersStats = match.getAwayTeamStats().getPlayersStats();
		fillColumn(playersStats, 3, 2);

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 1) {
				this.getItem(i).setBackground(ColorResources.getSystemColor(SWT.COLOR_GRAY));
			}
		}
		this.setRedraw(true);
	}

}
