package pl.pronux.sokker.ui.widgets.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class StatisticsEarnsTable extends SVTable<Club> {

	public static final int CLUB_MONEY = 1;

	public StatisticsEarnsTable(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontTable());

		this.setHeaderVisible(true);
		this.setLinesVisible(true);
		String[] columns2 = { Messages.getString("table.date"), 
							 Messages.getString("statistics.earns"), 
							 "" 
		};

		for (int i = 0; i < columns2.length; i++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setText(columns2[i]);
			column.setMoveable(false);
			column.setResizable(false);

			column.setAlignment(SWT.RIGHT);

			if (columns2[i].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}

	}

	public void fill(Club club) {

		for (int i = club.getClubBudget().size() - 1; i >= 0; i--) {
			TableItem item = new TableItem(this, SWT.NONE);
			item.setData("date", club.getClubBudget().get(i).getDate()); 
			item.setText(0, club.getClubBudget().get(i).getDate().getTrainingDate(SokkerDate.SATURDAY).toDateString());
			item.setText(1, club.getClubBudget().get(i).getMoney().formatIntegerCurrency());
			if (club.getClubBudget().get(i).getDate().getSokkerDate().getSeason() % 2 == 0) {
				item.setBackground(0, ColorResources.getColor(240, 240, 240));
			} else {
				item.setBackground(0, ColorResources.getWhite());
			}
		}

		int[] columns = { 1 };
		this.getChanges(columns);

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}

}
