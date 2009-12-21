package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.JuniorsComparator;
import pl.pronux.sokker.enums.OperatingSystem;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class JuniorsFiredTable extends SVTable<Junior> implements IViewSort<Junior> {

	private JuniorsComparator comparator;
	private List<Junior> juniors;

	public JuniorsFiredTable(Composite parent, int style) {
		super(parent, style);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		comparator = new JuniorsComparator();
		comparator.setColumn(JuniorsComparator.SURNAME);
		comparator.setDirection(JuniorsComparator.ASCENDING);

		// tworzymy kolumny dla trenerow

		String[] titles = {
				Messages.getString("table.name"), //$NON-NLS-1$
				Messages.getString("table.surname"), //$NON-NLS-1$
				Messages.getString("table.skill"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);

			if (j > 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			if (titles[j].equals("")) { //$NON-NLS-1$
				// potrzebne do dopelnienia tabel w Linuxie
				if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}

		this.setSortColumn(this.getColumn(JuniorsComparator.SURNAME));
		this.setSortDirection(SWT.UP);

		final TableColumn[] columns = this.getColumns();
		for (int i = 0; i < columns.length - 1; i++) {
			columns[i].addSelectionListener(new SortTableListener<Junior>(this, comparator));
		}
	}
	
	public void fill(List<Junior> juniors) {
		// Turn off drawing to avoid flicker
		this.setRedraw(false);
		this.juniors = juniors;
		// We remove all the this entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		Collections.sort(juniors, comparator);
		for (Junior junior : juniors) {
			TableItem item = new TableItem(this, SWT.NONE);

			int c = 0;
			item.setData(Junior.IDENTIFIER, junior); 
			item.setText(c++, junior.getName());
			item.setText(c++, junior.getSurname());
			item.setText(c++, String.valueOf(junior.getSkills()[junior.getSkills().length - 1].getSkill()));
		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}
		// Turn drawing back on
		this.setRedraw(true);
	}
	
	public void sort(SVComparator<Junior> comparator) {
		if(juniors != null) {
			Collections.sort(juniors, comparator);
			fill(juniors);
		}
		
	}

	public SVComparator<Junior> getComparator() {
		return comparator;
	}

}
