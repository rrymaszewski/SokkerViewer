package pl.pronux.sokker.ui.widgets.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.ReportComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class ReportsTable extends SVTable<Report> implements IViewSort<Report> {

	private List<Report> reports = new ArrayList<Report>();

	private ReportComparator comparator;

	public ReportsTable(Composite parent, int style) {
		super(parent, style);

		comparator = new ReportComparator();
		comparator.setColumn(ReportComparator.DATE);
		comparator.setDirection(ReportComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] columns = {
				" ",
				Messages.getString("table.date"), //$NON-NLS-1$
				Messages.getString("table.event"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int j = 0; j < columns.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setAlignment(SWT.LEFT);

			column.setText(columns[j]);
			column.setResizable(false);
			column.setMoveable(false);

			if (j == columns.length - 1) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				// column.setWidth(40);
				column.pack();
				column.addSelectionListener(new SortTableListener<Report>(this, comparator));
			}
		}
		
		this.setSortColumn(this.getColumn(comparator.getColumn()));
		this.setSortDirection(comparator.getDirection());
	}

	public void fill(List<Report> reports) {
		this.reports = reports;
		
		// Turn off drawing to avoid flicker
		this.setRedraw(false);
		
		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		for (Report report : reports) {
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(Report.IDENTIFIER, report); 
			item.setImage(c++, ImageResources.getImageResources("report_" + report.getType() + ".png" ));
			item.setText(c++, report.getDate().toDateString());
			item.setText(c++, report.getMessage());
			if (!report.isChecked()) {
				item.setFont(Fonts.getBoldFont(DisplayHandler.getDisplay(), item.getFont().getFontData()));
			}
			
			if(report.getStatus() == Report.COST) {
				item.setBackground(ColorResources.getColor(252,242,242));
			} else if (report.getStatus() == Report.INCOME) {
				item.setBackground(ColorResources.getColor(239,250,236));
			} else if (report.getStatus() == Report.INFO) {
				item.setBackground(ColorResources.getColor(229,232,249));
			}
		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
			this.getColumn(i).setWidth(this.getColumn(i).getWidth() + 5);
		}
		// Turn drawing back on
		this.setRedraw(true);
	}

	public void sort(SVComparator<Report> comparator) {
		if (reports != null) {
			Collections.sort(reports, comparator);
			fill(reports);
		}
	}

	public SVComparator<Report> getComparator() {
		return comparator;
	}

}
