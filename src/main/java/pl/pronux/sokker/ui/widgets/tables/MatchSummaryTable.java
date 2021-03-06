package pl.pronux.sokker.ui.widgets.tables;

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
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.TeamStats;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class MatchSummaryTable extends SVTable<Match> {

	private Table table;

	public MatchSummaryTable(final Composite parent, int style) {
		super(parent, style);
		this.setLinesVisible(true);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontTable());
		table = this;
		String[] columns = { "", 
							"", 
							"", 
							"" 
		};

		for (int i = 0; i < columns.length; i++) {
			new TableColumn(this, SWT.CENTER);
		}

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
					table.getColumn(1).setWidth(width / 3);
					table.getColumn(2).setWidth(width / 3);
					table.getColumn(3).setWidth(width / 3);
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					table.getColumn(0).setWidth(0);
					table.getColumn(1).setWidth(width / 3);
					table.getColumn(2).setWidth(width / 3);
					table.getColumn(3).setWidth(width / 3);
				}
			}
		});

	}

	public void fill(Match match) {
		this.setRedraw(false);
		this.remove(0, this.getItemCount() - 1);
		if (match.getIsFinished() == Match.NOT_FINISHED) {

		}
		TeamStats homeTeamStats = match.getHomeTeamStats();
		TeamStats awayTeamStats = match.getAwayTeamStats();

		if (homeTeamStats.isEmpty() || awayTeamStats.isEmpty()) {
			this.setRedraw(true);
			return;
		}

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															homeTeamStats.getTacticName(), Messages.getString("match.tactic"), 
															awayTeamStats.getTacticName() });

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															String.valueOf(homeTeamStats.getShoots()), Messages.getString("match.shoots"), 
															String.valueOf(awayTeamStats.getShoots()) });

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															String.valueOf(homeTeamStats.getFouls()), Messages.getString("match.fouls"), 
															String.valueOf(awayTeamStats.getFouls()) });

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															String.valueOf(homeTeamStats.getOffsides()), Messages.getString("match.offsides"), 
															String.valueOf(awayTeamStats.getOffsides()) });

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															String.valueOf(homeTeamStats.getYellowCards()), Messages.getString("match.cards.yellow"), 
															String.valueOf(awayTeamStats.getYellowCards()) });

		new TableItem(this, SWT.NONE).setText(new String[] { "", 
															String.valueOf(homeTeamStats.getRedCards()), Messages.getString("match.cards.red"), 
															String.valueOf(awayTeamStats.getRedCards()) });

		new TableItem(this, SWT.NONE).setText(new String[] {
															"", 
															SVNumberFormat.formatDouble(homeTeamStats.getAverageRating()),
															Messages.getString("table.match.rating.average"), 
															SVNumberFormat.formatDouble(awayTeamStats.getAverageRating()) });

		new TableItem(this, SWT.NONE)
			.setText(new String[] {
								   "", 
								   String.format("%s [%d]", Messages.getString("skill.c" + homeTeamStats.getRatingScoring()), homeTeamStats.getRatingScoring()),  
								   Messages.getString("match.shooting"), 
								   String.format("%s [%d]", Messages.getString("skill.c" + awayTeamStats.getRatingScoring()), awayTeamStats.getRatingScoring())  
			});

		new TableItem(this, SWT.NONE)
			.setText(new String[] {
								   "", 
								   String.format("%s [%d]", Messages.getString("skill.c" + homeTeamStats.getRatingPassing()), homeTeamStats.getRatingPassing()),  
								   Messages.getString("match.passing"), 
								   String.format("%s [%d]", Messages.getString("skill.c" + awayTeamStats.getRatingPassing()), awayTeamStats.getRatingPassing())  
			});

		new TableItem(this, SWT.NONE)
			.setText(new String[] {
								   "", 
								   String
									   .format(
											   "%s [%d]", Messages.getString("skill.b" + homeTeamStats.getRatingDefending()), homeTeamStats.getRatingDefending()),  
								   Messages.getString("match.defending"), 
								   String
									   .format(
											   "%s [%d]", Messages.getString("skill.b" + awayTeamStats.getRatingDefending()), awayTeamStats.getRatingDefending())  
			});

		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 1) {
				this.getItem(i).setBackground(this.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			}
		}
		this.setRedraw(true);

	}

}
