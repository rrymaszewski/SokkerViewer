package pl.pronux.sokker.ui.widgets.tables;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Rank;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class StatisticsRankTable extends SVTable<Club> {

	public StatisticsRankTable(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontTable());
		
		this.setHeaderVisible(true);
		this.setLinesVisible(true);
		String[] columns3 = {
				Messages.getString("table.date"), //$NON-NLS-1$
				Messages.getString("statistics.rank"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int i = 0; i < columns3.length; i++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setText(columns3[i]);
			column.setMoveable(false);
			column.setResizable(false);

			column.setAlignment(SWT.RIGHT);

			if (columns3[i].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}

	}

	public void fill(Club club) {
		int[] columns = {
			1
		};
		ArrayList<Double> alDouble = new ArrayList<Double>();

		for (Rank rank : club.getRank()) {
			TableItem item = new TableItem(this, SWT.NONE);
			Date date = rank.getDate();
			item.setData("date", rank.getDate().getTrainingDate(date.getSokkerDate().getDay())); //$NON-NLS-1$
			item.setText(0, date.getTrainingDate(date.getSokkerDate().getDay()).toDateString());
			item.setText(1, SVNumberFormat.formatDouble(rank.getRank()));
			alDouble.add(rank.getRank());
			if(rank.getDate().getSokkerDate().getSeason() % 2 == 0) {
				item.setBackground(0, ColorResources.getColor(240, 240, 240));
			} else {
				item.setBackground(0, ColorResources.getWhite());
			}
		}
		Collections.reverse(alDouble);
		
		this.getColumn(1).setData("data", alDouble); //$NON-NLS-1$

		this.getChanges(columns);

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 15);
		}
	}
}
