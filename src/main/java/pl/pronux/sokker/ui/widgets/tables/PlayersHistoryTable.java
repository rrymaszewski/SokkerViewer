package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.PlayerHistoryComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.FlagsResources;

public class PlayersHistoryTable extends SVTable<Player> {

	private PlayerHistoryComparator comparator;
	private List<Player> players;

	public PlayersHistoryTable(Composite parent, int style) {
		super(parent, style);

		this.setLinesVisible(true);

		this.setHeaderVisible(true);

		this.setFont(ConfigBean.getFontTable());
		comparator = new PlayerHistoryComparator();
		comparator.setColumn(PlayerHistoryComparator.SURNAME);
		comparator.setDirection(PlayerHistoryComparator.ASCENDING);

		// tworzymy kolumny dla trenerow

		String[] titles = {
				"", //$NON-NLS-1$
				Messages.getString("table.name"), //$NON-NLS-1$
				Messages.getString("table.surname"), //$NON-NLS-1$
				Messages.getString("table.value"), //$NON-NLS-1$
				Messages.getString("table.salary"), //$NON-NLS-1$
				Messages.getString("table.age"), //$NON-NLS-1$
				Messages.getString("table.form"), //$NON-NLS-1$
				Messages.getString("table.stamina"), //$NON-NLS-1$
				Messages.getString("table.pace"), //$NON-NLS-1$
				Messages.getString("table.technique"), //$NON-NLS-1$
				Messages.getString("table.passing"), //$NON-NLS-1$
				Messages.getString("table.keeper"), //$NON-NLS-1$
				Messages.getString("table.defender"), //$NON-NLS-1$
				Messages.getString("table.playmaker"), //$NON-NLS-1$
				Messages.getString("table.scorer"), //$NON-NLS-1$
				Messages.getString("table.sold"), //$NON-NLS-1$
				"" //$NON-NLS-1$
		};

		for (int j = 0; j < titles.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);

			if (j > 2) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

			column.setText(titles[j]);
			column.setResizable(false);
			column.setMoveable(false);
			if (titles[j].isEmpty()) {
				// column.setWidth(70);
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				// column.setWidth(40);
				column.pack();
			}
		}

		this.setSortColumn(this.getColumn(PlayerHistoryComparator.SURNAME));
		this.setSortDirection(SWT.UP);

		final TableColumn[] columns = this.getColumns();

		for (int i = 0; i < columns.length - 1; i++) {
			columns[i].addSelectionListener(new SortTableListener<Player>(this, comparator));
		}

		this.addLabelsListener();
	}

	public Map<Integer, TableItem> fill(List<Player> players) {
		Map<Integer, TableItem> tableItemMap = new HashMap<Integer, TableItem>();

		this.players = players;
		int maxSkill = 0;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the this entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		Collections.sort(players, comparator);
		for (Player player : players) {
			maxSkill = player.getSkills().length - 1;
			TableItem item = new TableItem(this, SWT.NONE);

			tableItemMap.put(player.getId(), item);

			int c = 0;
			item.setData(Player.class.getName(), player);
			item.setImage(c++, FlagsResources.getFlag(player.getCountryfrom()));
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, player.getSkills()[maxSkill].getValue().formatIntegerCurrency());
			item.setText(c++, player.getSkills()[maxSkill].getSalary().formatIntegerCurrency());
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getAge()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getForm()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getStamina()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPace()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getTechnique()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPassing()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getKeeper()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getDefender()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getPlaymaker()));
			item.setText(c++, String.valueOf(player.getSkills()[maxSkill].getScorer()));
			if (player.getTransferSell() != null) {
				item.setText(c++, player.getTransferSell().getPrice().formatIntegerCurrency());
			} else {
				item.setText(c++, player.getSoldPrice().formatIntegerCurrency());
			}

		}

		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}
		// Turn drawing back on
		this.setRedraw(true);

		return tableItemMap;
	}

	@Override
	public void sort(SVComparator<Player> comparator) {
		if (players != null) {
			Collections.sort(players, comparator);
			fill(players);
		}
	}

	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column >= PlayerHistoryComparator.FORM && column <= PlayerHistoryComparator.SCORER) {
			Player player = (Player) item.getData(Player.class.getName());
			int maxSkill = player.getSkills().length - 1;
			int[] skills = player.getSkills()[maxSkill].getStatsTable();
			label.setText(Messages.getString("skill.a" + skills[column - 3]));
			label.pack();
		}
		super.setLabel(label, column, item);
	}
}
