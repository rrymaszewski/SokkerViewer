package pl.pronux.sokker.ui.widgets.tables;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class MatchInfoTable extends SVTable<Match> {

	private MatchInfoTable table;

	public MatchInfoTable(final Composite parent, int style) {
		super(parent, style);
		table = this;
		this.setLinesVisible(true);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontTable());

		new TableColumn(this, SWT.LEFT);
		new TableColumn(this, SWT.LEFT);
		new TableColumn(this, SWT.LEFT);

		TableItem item;
		
		item = new TableItem(this, SWT.NONE);
		item.setText(1,Messages.getString("table.date")); //$NON-NLS-1$
		
		item = new TableItem(this, SWT.NONE);
		item.setText(1,Messages.getString("match.type")); //$NON-NLS-1$
		
		item = new TableItem(this, SWT.NONE);
		item.setText(1,Messages.getString("match.stadium")); //$NON-NLS-1$
		
		item = new TableItem(this, SWT.NONE);
		item.setText(1,Messages.getString("match.supporters")); //$NON-NLS-1$
		
		for (int i = 0; i < this.getItemCount(); i++) {
			if ((i % 2) == 1) {
				this.getItem(i).setBackground(ColorResources.getSystemColor(SWT.COLOR_GRAY));
			}
		}

		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = parent.getClientArea();
				Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = table.getVerticalBar();
				int width = area.width - table.computeTrim(0,0,0,0).width - vBar.getSize().x;
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
					table.getColumn(1).pack();
					table.getColumn(1).setWidth(table.getColumn(1).getWidth() + 10);
					table.getColumn(2).setWidth(width - table.getColumn(1).getWidth());
					table.setSize(area.width, area.height);
				} else {
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					table.getColumn(0).setWidth(0);
					table.getColumn(1).pack();
					table.getColumn(1).setWidth(table.getColumn(1).getWidth() + 10);
					table.getColumn(2).setWidth(width - table.getColumn(1).getWidth());
				}
			}
		});
	}

	public void fill(Match match) {
		this.setRedraw(false);
		if(match.getIsFinished() == Match.NOT_FINISHED) {
			this.getItem(0).setText(2, match.getDateExpected().toDateString());
			this.getItem(1).setText(2, Messages.getString("match.type." + match.getLeague().getIsOfficial() + "-" + match.getLeague().getType() + "-" + match.getLeague().getIsCup())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.getItem(2).setText(2, ""); //$NON-NLS-1$
			this.getItem(3).setText(2, ""); //$NON-NLS-1$
		} else {
			this.getItem(0).setText(2, match.getDateStarted().toDateTimeString());
			if(match.getLeague() != null) {
				this.getItem(1).setText(2, Messages.getString("match.type." + match.getLeague().getIsOfficial() + "-" + match.getLeague().getType() + "-" + match.getLeague().getIsCup())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$				
			} else {
				this.getItem(2).setText(2, ""); //$NON-NLS-1$
			}
			if(match.getHomeTeam() != null && match.getHomeTeam().getArena() != null && match.getHomeTeam().getArena().getAlArenaName().size() > 0 && match.getHomeTeam().getArena().getAlArenaName().get(0).getArenaName() != null) {
				this.getItem(2).setText(2, match.getHomeTeam().getArena().getAlArenaName().get(match.getHomeTeam().getArena().getAlArenaName().size() - 1).getArenaName());
			} else {
				this.getItem(2).setText(2, "?");				 //$NON-NLS-1$
			}
			this.getItem(3).setText(2, String.valueOf(match.getSupporters()));
		}
		this.setRedraw(true);
	}

}
