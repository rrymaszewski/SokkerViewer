package pl.pronux.sokker.ui.widgets.tables;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.comparators.PlayerArchiveComparator;
import pl.pronux.sokker.enums.OperatingSystem;
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
	private ArrayList<PlayerArchive> players;

	public PlayersArchiveTable(Composite parent, int style) {
		super(parent, style);

		comparator = new PlayerArchiveComparator();
		comparator.setColumn(PlayerArchiveComparator.SURNAME);
		comparator.setDirection(PlayerArchiveComparator.ASCENDING);

		this.setLinesVisible(true);
		this.setHeaderVisible(true);
		this.setFont(ConfigBean.getFontTable());

		String[] columns = { "", //$NON-NLS-1$
				Messages.getString("table.id"), //$NON-NLS-1$
				Messages.getString("table.name"), //$NON-NLS-1$
				Messages.getString("table.surname"), //$NON-NLS-1$
				Messages.getString("table.youthTeamId"), //$NON-NLS-1$
				Messages.getString("table.note"), //$NON-NLS-1$
				"" //$NON-NLS-1$
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
				if (SettingsHandler.OS_TYPE == OperatingSystem.LINUX) {
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
		this.addNoteListener(PlayerArchive.IDENTIFIER, PlayerArchiveComparator.NOTE);
	}

	public void fill(ArrayList<PlayerArchive> players) {
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
			item.setData(PlayerArchive.IDENTIFIER, player); //$NON-NLS-1$
			if (player.getCountryID() > 0) {
				item.setImage(c++, FlagsResources.getFlag(player.getCountryID()));
			} else {
				item.setImage(c++, FlagsResources.getFlag(FlagsResources.EMPTY_FLAG));
			}

			item.setText(c++, String.valueOf(player.getId()));
			item.setText(c++, player.getName());
			item.setText(c++, player.getSurname());
			item.setText(c++, String.valueOf(player.getYouthTeamID()));
			if (player.getNote().isEmpty()) {
				c++;
			} else {
				item.setImage(c++, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
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
			PlayerArchive player = (PlayerArchive) item.getData(PlayerArchive.IDENTIFIER);
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
