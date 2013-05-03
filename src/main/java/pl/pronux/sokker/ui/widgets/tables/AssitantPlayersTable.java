package pl.pronux.sokker.ui.widgets.tables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.PlayerAssistantComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class AssitantPlayersTable extends SVTable<Player> implements IViewSort<Player> {

	private PlayerAssistantComparator comparator;
	
	private List<Player> players = new ArrayList<Player>();

	public AssitantPlayersTable(Composite parent, int style) {
		super(parent, style);
		
		this.setHeaderVisible(true);
		this.setLinesVisible(true);
		this.setFont(ConfigBean.getFontTable());

		comparator = new PlayerAssistantComparator();
		comparator.setColumn(PlayerAssistantComparator.SURNAME);
		comparator.setDirection(PlayerAssistantComparator.ASCENDING);

		
		String[] title = {
				Messages.getString("table.name"), 
				Messages.getString("table.surname"), 
				// langProperties.getProperty("table.form"),
				// langProperties.getProperty("table.stamina"),
				// langProperties.getProperty("table.pace"),
				// langProperties.getProperty("table.technique"),
				// langProperties.getProperty("table.passing"),
				// langProperties.getProperty("table.keeper"),
				// langProperties.getProperty("table.defender"),
				// langProperties.getProperty("table.playmaker"),
				// langProperties.getProperty("table.scorer"),
				Messages.getString("assistant.position.short.1"), 
				Messages.getString("assistant.position.short.2"), 
				Messages.getString("assistant.position.short.3"), 
				Messages.getString("assistant.position.short.4"), 
				Messages.getString("assistant.position.short.5"), 
				Messages.getString("assistant.position.short.6"), 
				Messages.getString("assistant.position.short.7"), 
				Messages.getString("assistant.position.short.8"), 
				Messages.getString("assistant.position.short.9"), 
				Messages.getString("assistant.position.short.10"), 
				Messages.getString("assistant.position.short.11"), 
				Messages.getString("table.position.best"), 
				"" 
		};
		
		for (int i = 0; i < title.length; i++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			if (i < 2 || i == title.length - 2) {
				column.setAlignment(SWT.LEFT);
			} else {
				column.setAlignment(SWT.RIGHT);
			}
			column.setText(title[i]);
			column.setMoveable(false);
			column.setResizable(false);

			if (title[i].isEmpty()) {
				if (SettingsHandler.IS_LINUX) {
					column.pack();
				}
			} else {
				column.pack();
			}

		}
		
		this.setSortColumn(this.getColumn(PlayerAssistantComparator.SURNAME));
		this.setSortDirection(SWT.UP);

		final TableColumn[] columns = this.getColumns();
		
		for (int i = 0; i < columns.length - 1; i++) {
			columns[i].addSelectionListener(new SortTableListener<Player>(this, comparator));
		}
	}
	
	public void fill(List<Player> players) {
		// int maxSkill = 0;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);
		this.players = players;
		// We remove all the this entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);

		Collections.sort(players, comparator);
		for (Player player : players) {
			// maxSkill = player.getSkills().length - 1;
			TableItem item = new TableItem(this, SWT.NONE);

			if (player.getPosition() == Player.POSITION_GK) {
				item.setBackground(ColorResources.getColor(221, 255, 255));
			} else if (player.getPosition() == Player.POSITION_DEF) {
				item.setBackground(ColorResources.getColor(255, 230, 214));
			} else if (player.getPosition() == Player.POSITION_WINGBACK) {
				item.setBackground(ColorResources.getColor(255, 230, 214));
			} else if (player.getPosition() == Player.POSITION_DEF_OFF) {
				item.setBackground(ColorResources.getColor(255, 230, 214));
			} else if (player.getPosition() == Player.POSITION_MID) {
				item.setBackground(ColorResources.getColor(255, 255, 208));
			} else if (player.getPosition() == Player.POSITION_DEF_MID) {
				item.setBackground(ColorResources.getColor(255, 255, 208));
			} else if (player.getPosition() == Player.POSITION_WINGER) {
				item.setBackground(ColorResources.getColor(255, 255, 208));
			} else if (player.getPosition() == Player.POSITION_OFF_MID) {
				item.setBackground(ColorResources.getColor(255, 255, 208));
			} else if (player.getPosition() == Player.POSITION_ATT) {
				item.setBackground(ColorResources.getColor(226, 255, 208));
			} else if (player.getPosition() == Player.POSITION_DEF_ATT) {
				item.setBackground(ColorResources.getColor(226, 255, 208));
			} 
			
			int c = 0;
			int j = 0;
			item.setData("person", player); 
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getForm()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getStamina()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getPace()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getTechnique()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getPassing()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getKeeper()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getDefender()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getPlaymaker()).toString());
			// item.setText(c++,
			// String.valueOf(player.getSkills()[maxSkill].getScorer()).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			item.setText(c++, BigDecimal.valueOf(player.getPositionTable()[j++]).setScale(2).toString());
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));
			// item.setText(c++, String.valueOf(player.getPositionTable()[j++]));

			item.setText(c++, Messages.getString("assistant.position." + player.getPosition())); 

			item.setFont(player.getPosition() + 1, Fonts.getBoldFont(item.getDisplay(), item.getFont().getFontData()));
			// item.setForeground(player.getPosition() + 1,
			// composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		}
		// Turn drawing back on
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}
		this.setRedraw(true);

	}
	
	public void sort(SVComparator<Player> comparator) {
		if(players != null) {
			Collections.sort(players, comparator);
			fill(players);
		}
		
	}

	public SVComparator<Player> getComparator() {
		return comparator;
	}

}
