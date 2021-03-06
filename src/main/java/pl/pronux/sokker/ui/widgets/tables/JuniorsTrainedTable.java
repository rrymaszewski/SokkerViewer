package pl.pronux.sokker.ui.widgets.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.JuniorsTrainedComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class JuniorsTrainedTable extends SVTable<Junior> implements IViewSort<Junior> {

	private JuniorsTrainedComparator comparator;
	private List<Junior> juniors;

	public JuniorsTrainedTable(Composite parent, int style) {
		super(parent, style);

		juniors = new ArrayList<Junior>();

		comparator = new JuniorsTrainedComparator();
		comparator.setColumn(JuniorsTrainedComparator.SURNAME);
		comparator.setDirection(JuniorsTrainedComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());
		this.setVisible(true);

		String[] titles = { Messages.getString("table.name"), 
						   Messages.getString("table.surname"), 
						   Messages.getString("table.formation"), Messages.getString("table.skill"), 
						   Messages.getString("table.sum"), 
						   Messages.getString("table.stamina"), 
						   Messages.getString("table.sum.mainskills"), 
						   Messages.getString("table.pace"), 
						   Messages.getString("table.technique"), 
						   Messages.getString("table.passing"), 
						   Messages.getString("table.keeper"), 
						   Messages.getString("table.defender"), 
						   Messages.getString("table.playmaker"), 
						   Messages.getString("table.scorer"), 
						   Messages.getString("table.age"), 
						   Messages.getString("table.week"), 
						   Messages.getString("junior.averageJumps"), 
						   Messages.getString("junior.table.money.all"), 
						   "" 
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
			if (j == titles.length - 1) {
				// potrzebne do dopelnienia tabel w Linuxie
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}
		}

		this.setSortColumn(this.getColumn(JuniorsTrainedComparator.SURNAME));
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
		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);

		Collections.sort(juniors, comparator);
		for (Junior junior : juniors) {
			TableItem item = new TableItem(this, SWT.NONE);

			Player player = junior.getPlayer();

			int c = 0;
			item.setData(Junior.class.getName(), junior);
			item.setText(c++, junior.getName());
			item.setText(c++, junior.getSurname());
			if (junior.getFormation() >= 0) {
				item.setText(c++, Messages.getString("junior.formation." + junior.getFormation()));				
			} else {
				item.setText(c++, "-");
			}
			item.setText(c++, String.valueOf(junior.getSkills()[junior.getSkills().length - 1].getSkill()));
			if (player != null) {
				item.setText(c++, String.valueOf(player.getSkills()[0].getSummarySkill() + player.getSkills()[0].getStamina()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getStamina()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getSummarySkill()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getPace()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getTechnique()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getPassing()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getKeeper()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getDefender()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getPlaymaker()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getScorer()));
				item.setText(c++, String.valueOf(player.getSkills()[0].getAge()));
			} else {
				c += 11;
			}

			item.setText(c++, String.valueOf(junior.getSkills()[0].getWeeks()));
			if (junior.getAveragePops() == 0) {
				item.setText(c++, "-"); 
			} else {
				if (junior.getPops() > 1) {
					item.setText(c++, SVNumberFormat.formatDouble(junior.getAveragePops()));
				} else {
					item.setForeground(c, ColorResources.getDarkGray());
					item.setText(c++, "~" + SVNumberFormat.formatDouble(junior.getAveragePops())); 
				}
			}
			item.setText(c++, junior.getAllMoneyToSpend().formatIntegerCurrencySymbol());

		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}

		// Turn drawing back on
		this.setRedraw(true);

	}

	public void sort(SVComparator<Junior> comparator) {
		if (juniors != null) {
			Collections.sort(juniors, comparator);
			fill(juniors);
		}

	}

	public SVComparator<Junior> getComparator() {
		return comparator;
	}
}
