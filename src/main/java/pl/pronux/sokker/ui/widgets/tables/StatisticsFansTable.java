package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.resources.ColorResources;

public class StatisticsFansTable extends SVTable<Club> {

	public StatisticsFansTable(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontTable());
		
		this.setHeaderVisible(true);
		this.setLinesVisible(true);
		
		String[] columns = {
				Messages.getString("table.date"), //$NON-NLS-1$
				Messages.getString("statistics.fans.count"), //$NON-NLS-1$
				Messages.getString("statistics.fans.mood"), //$NON-NLS-1$
				Messages.getString("statistics.fans.diff"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(this, SWT.NONE);


			column.setText(columns[i]);
			column.setMoveable(false);
			column.setResizable(false);
			
			column.setAlignment(SWT.RIGHT);
//			if (i == 0) {
//				column.setAlignment(SWT.LEFT);
//			} else {
//				column.setAlignment(SWT.RIGHT);
//			}
			
			if (columns[i].equals("")) { //$NON-NLS-1$
				// column.setWidth(70);
				if (SettingsHandler.OS_TYPE == IPlugin.LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}
	}

	public void fill(Club club) {
		for (int i = club.getClubSupporters().size() - 1; i >= 0; i--) {
			TableItem item = new TableItem(this, SWT.NONE);
			item.setData("date", club.getClubSupporters().get(i).getDate()); //$NON-NLS-1$
			item.setText(0, club.getClubSupporters().get(i).getDate().getTrainingDate(SokkerDate.MONDAY).toDateString());
			item.setText(1, SVNumberFormat.formatInteger(club.getClubSupporters().get(i).getFanclubcount()));
			item.setText(2, String.valueOf(club.getClubSupporters().get(i).getFanclubmood()));
			if(i == 0) {
				item.setText(3, "0");
			} else {
				int diff = club.getClubSupporters().get(i).getFanclubcount() - club.getClubSupporters().get(i-1).getFanclubcount();
				item.setText(3, String.valueOf(diff));
				if(diff > 0) {
					item.setForeground(3, ColorResources.getDarkGreen());
				} else if (diff < 0) {
					item.setForeground(3, ColorResources.getRed());
				}
				
			}
			if(club.getClubSupporters().get(i).getDate().getSokkerDate().getSeason() % 2 == 0) {
				item.setBackground(0, ColorResources.getColor(240, 240, 240));
			} else {
				item.setBackground(0, ColorResources.getWhite());
			}
		}

		int[] columns = {
				1,
				2,
		};
		this.getChanges(columns);

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).setWidth(0); //without this line columns doesn't pack well. problem with tabs in statistics
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}

}
