package pl.pronux.sokker.ui.widgets.tables;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.PlayerArchiveComparator;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SVComparator;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.listeners.SortTableListener;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.interfaces.IViewSort;

public class PlayersArchiveTable extends SVTable<PlayerArchive> implements IViewSort<PlayerArchive> {
	private PlayerArchiveComparator comparator;
	private List<PlayerArchive> players;

	public PlayersArchiveTable(Composite parent, int style) {
		super(parent, style);

		comparator = new PlayerArchiveComparator();
		comparator.setColumn(PlayerArchiveComparator.SURNAME);
		comparator.setDirection(PlayerArchiveComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] columns = { "", 
				Messages.getString("table.id"), 
				Messages.getString("table.name"), 
				Messages.getString("table.surname"), 
				Messages.getString("table.youthTeamId"), 
				Messages.getString("table.note"), 
				"" 
		};

		for (int j = 0; j < columns.length; j++) {
			TableColumn column = new TableColumn(this, SWT.NONE);

			if (j > 5 && j < columns.length - 1) {
				column.setAlignment(SWT.RIGHT);
			} else {
				column.setAlignment(SWT.LEFT);
			}

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
				column.addSelectionListener(new SortTableListener<PlayerArchive>(this, comparator));
			}
		}

		this.setSortColumn(this.getColumn(comparator.getColumn()));
		this.setSortDirection(comparator.getDirection());

		this.addLabelsListener();
		this.addNoteListener(PlayerArchive.class.getName(), PlayerArchiveComparator.NOTE);
	}

	public void fill(List<PlayerArchive> players) {
		this.players = players;
		// Turn off drawing to avoid flicker
		this.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		this.remove(0, this.getItemCount() - 1);
		Collections.sort(players, comparator);
		for (PlayerArchive player : players) {
			TableItem item = new TableItem(this, SWT.NONE);
			int c = 0;
			item.setData(PlayerArchive.class.getName(), player); 
			if (player.getCountryId() > 0) {
				item.setImage(c++, FlagsResources.getFlag(player.getCountryId()));
			} else {
				item.setImage(c++, FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
			}

			item.setText(c++, String.valueOf(player.getId()));
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, String.valueOf(player.getYouthTeamId()));
			if (player.getNote().isEmpty()) {
				c++;
			} else {
				item.setImage(c++, ImageResources.getImageResources("note.png")); 
			}

		}
		for (int i = 0; i < this.getColumnCount() - 1; i++) {
			this.getColumn(i).pack();
		}

		// Turn drawing back on
		this.setRedraw(true);
	}

	public void sort(SVComparator<PlayerArchive> comparator) {
		if (players != null) {
			Collections.sort(players, comparator);
			fill(players);
		}
	}

	public SVComparator<PlayerArchive> getComparator() {
		return comparator;
	}

	@Override
	public void setLabel(Label label, int column, TableItem item) {
		if (column == PlayerArchiveComparator.NOTE) {
			PlayerArchive player = (PlayerArchive) item.getData(PlayerArchive.class.getName());
			if (player.getNote() != null && !player.getNote().isEmpty()) {
				label.setText(player.getNote());
				int minSizeX = 200;
				int minSizeY = 80;
				int maxSizeX = 400;
				int maxSizeY = 200;

				Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				if (size.x < minSizeX) {
					size.x = minSizeX;
				}
				if (size.y < minSizeY) {
					size.y = minSizeY;
				}
				if (size.x > maxSizeX) {
					size.x = maxSizeX;
				}
				if (size.y > maxSizeY) {
					size.y = maxSizeY;
				}

				label.setSize(size);
			}
		}
		super.setLabel(label, column, item);
	}
}
