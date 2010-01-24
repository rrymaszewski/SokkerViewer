package pl.pronux.sokker.ui.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.PlayerArchiveComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.PlayerArchive;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.groups.ArchiveInformationGroup;
import pl.pronux.sokker.ui.widgets.groups.ArchiveSearchGroup;
import pl.pronux.sokker.ui.widgets.shells.NoteShell;
import pl.pronux.sokker.ui.widgets.tables.PlayersArchiveTable;

public class ViewArchive implements IPlugin, ISort {

	private Composite composite;
	private TreeItem treeItem;
	private HashMap<Integer, PlayerArchive> hmPlayersArchive = new HashMap<Integer, PlayerArchive>();
	private ArchiveSearchGroup archiveSearchGroup;
	private Set<Entry<Integer, PlayerArchive>> setPlayersArchive = new HashSet<Entry<Integer, PlayerArchive>>();
	private HashMap<String, ArrayList<PlayerArchive>> hmPlayerNameArchive = new HashMap<String, ArrayList<PlayerArchive>>();
	private HashMap<String, ArrayList<PlayerArchive>> hmPlayerSurnameArchive = new HashMap<String, ArrayList<PlayerArchive>>();
	private HashMap<Integer, ArrayList<PlayerArchive>> hmPlayerYouthTeamIDArchive = new HashMap<Integer, ArrayList<PlayerArchive>>();
	private Set<PlayerArchive> stPlayerArchive = new HashSet<PlayerArchive>();
	private ArrayList<PlayerArchive> players = new ArrayList<PlayerArchive>();
	private PlayersArchiveTable playersArchiveTable;
	private Menu menuPopUp;
	private Menu menuClear;
	private ArchiveInformationGroup archiveInformationGroup;
	private HashMap<Integer, ArrayList<PlayerArchive>> hmPlayerCountryIDArchive = new HashMap<Integer, ArrayList<PlayerArchive>>();

	public void clear() {
	}

	public void dispose() {
	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoArchive"); //$NON-NLS-1$
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);

		MenuItem menuItem;

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.note.open")); //$NON-NLS-1$
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) { //$NON-NLS-1$
					Item item = (Item) menuPopUp.getData("item"); //$NON-NLS-1$
					if (item.getData(PlayerArchive.class.getName()) != null) { //$NON-NLS-1$
						openNote(item);
					}
				}
			}
		});

		// menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);
		//		
		// menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		// menuItem.setText(Messages.getString("popup.complete.archive.player"));
		// menuItem.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event event) {
		// if (menuPopUp.getData("item") != null) {
		// Item item = (Item) menuPopUp.getData("item");
		// if (item.getData(Player.IDENTIFIER) != null) {
		// openNote(item);
		// }
		// }
		// }
		// });

		menuClear = new Menu(composite.getShell(), SWT.POP_UP);
	}

	public void init(Composite composite) {
		this.composite = composite;
		this.composite.setLayout(new FormLayout());

		addPopupMenu();

		FormData formData = new FormData(300, 170);
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);

		archiveSearchGroup = new ArchiveSearchGroup(composite, SWT.NONE);
		archiveSearchGroup.setLayoutData(formData);

		archiveSearchGroup.getSearchButton().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				stPlayerArchive = new HashSet<PlayerArchive>();
				players = new ArrayList<PlayerArchive>();
				if (hmPlayerYouthTeamIDArchive.get(archiveSearchGroup.getPlayerYouthTeamId()) == null) {
					hmPlayerYouthTeamIDArchive.put(archiveSearchGroup.getPlayerYouthTeamId(), new ArrayList<PlayerArchive>());
				}
				if (hmPlayerNameArchive.get(archiveSearchGroup.getPlayerName().toLowerCase()) == null) {
					hmPlayerNameArchive.put(archiveSearchGroup.getPlayerName().toLowerCase(), new ArrayList<PlayerArchive>());
				}
				if (hmPlayerSurnameArchive.get(archiveSearchGroup.getPlayerSurname().toLowerCase()) == null) {
					hmPlayerSurnameArchive.put(archiveSearchGroup.getPlayerSurname().toLowerCase(), new ArrayList<PlayerArchive>());
				}

				if (hmPlayerCountryIDArchive.get(archiveSearchGroup.getPlayerCountryID()) == null) {
					hmPlayerCountryIDArchive.put(archiveSearchGroup.getPlayerCountryID(), new ArrayList<PlayerArchive>());
				}

				int search = 0;
				final int id = 1 << 1;
				final int youthTeamID = 1 << 2;
				final int name = 1 << 3;
				final int surname = 1 << 4;
				final int countryID = 1 << 5;

				if (archiveSearchGroup.getPlayerID() >= 0) {
					search = search | id;
				}
				if (archiveSearchGroup.getPlayerYouthTeamId() >= 0) {
					search = search | youthTeamID;
				}
				if (!archiveSearchGroup.getPlayerName().isEmpty()) {
					search = search | name;
				}
				if (!archiveSearchGroup.getPlayerSurname().isEmpty()) { 
					search = search | surname;
				}
//				if (archiveSearchGroup.getPlayerCountryID() > 0 && archiveSearchGroup.getPlayerCountryID() < FlagsResources.EMPTY_FLAG) {
				if(archiveSearchGroup.getPlayerCountryID() != FlagsResources.QUESTION_FLAG) {
					search = search | countryID;
				}

				if ((search & id) != 0) {
					if (hmPlayersArchive.get(archiveSearchGroup.getPlayerID().intValue()) != null) {
						stPlayerArchive.add(hmPlayersArchive.get(archiveSearchGroup.getPlayerID()));
					}
				}
				stPlayerArchive.addAll(hmPlayerCountryIDArchive.get(archiveSearchGroup.getPlayerCountryID()));
				stPlayerArchive.addAll(hmPlayerSurnameArchive.get(archiveSearchGroup.getPlayerSurname().toLowerCase()));
				stPlayerArchive.addAll(hmPlayerNameArchive.get(archiveSearchGroup.getPlayerName().toLowerCase()));
				stPlayerArchive.addAll(hmPlayerYouthTeamIDArchive.get(archiveSearchGroup.getPlayerYouthTeamId()));
				if(archiveSearchGroup.getPlayerCountryID() == FlagsResources.QUESTION_FLAG) {
					stPlayerArchive.addAll(hmPlayersArchive.values());
				}
				
				if ((search & id) != 0) {
					stPlayerArchive.clear();
					if (hmPlayersArchive.get(archiveSearchGroup.getPlayerID().intValue()) != null) {
						stPlayerArchive.add(hmPlayersArchive.get(archiveSearchGroup.getPlayerID()));
					}
				}
				if ((search & name) != 0) {
					stPlayerArchive.retainAll(hmPlayerNameArchive.get(archiveSearchGroup.getPlayerName().toLowerCase()));
				}
				if ((search & surname) != 0) {
					stPlayerArchive.retainAll(hmPlayerSurnameArchive.get(archiveSearchGroup.getPlayerSurname().toLowerCase()));
				}
				if ((search & youthTeamID) != 0) {
					stPlayerArchive.retainAll(hmPlayerYouthTeamIDArchive.get(archiveSearchGroup.getPlayerYouthTeamId()));
				}
				if ((search & countryID) != 0) {
					if(archiveSearchGroup.getPlayerCountryID() == FlagsResources.EMPTY_FLAG) {
						stPlayerArchive.retainAll(hmPlayerCountryIDArchive.get(0));
					} else {
						stPlayerArchive.retainAll(hmPlayerCountryIDArchive.get(archiveSearchGroup.getPlayerCountryID()));	
					}
				}

				players = new ArrayList<PlayerArchive>(stPlayerArchive);
				playersArchiveTable.fill(players);

			}

		});

		composite.getShell().setDefaultButton(archiveSearchGroup.getSearchButton());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(archiveSearchGroup, 5);
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);

		playersArchiveTable = new PlayersArchiveTable(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		playersArchiveTable.setLayoutData(formData);
		playersArchiveTable.setVisible(true);

//		playersArchiveTable.addListener(SWT.MouseDoubleClick, new Listener() {
//			public void handleEvent(Event event) {
//				Rectangle clientArea = playersArchiveTable.getClientArea();
//				Point pt = new Point(event.x, event.y);
//				int index = playersArchiveTable.getTopIndex();
//
//				while (index < playersArchiveTable.getItemCount()) {
//					boolean visible = false;
//					TableItem item = playersArchiveTable.getItem(index);
//					for (int i = 0; i < playersArchiveTable.getColumnCount(); i++) {
//						Rectangle rect = item.getBounds(i);
//						if (rect.contains(pt) && i == PlayerArchiveComparator.NOTE) {
//							openNote(item);
//						}
//						if (!visible && rect.intersects(clientArea)) {
//							visible = true;
//						}
//					}
//					if (!visible) {
//						return;
//					}
//					index++;
//				}
//			}
//		});

		// Disable native tooltip
//		playersArchiveTable.setToolTipText(""); //$NON-NLS-1$
//
//		// Implement a "fake" tooltip
//		final Listener labelListener = new Listener() {
//			public void handleEvent(Event event) {
//				Label label = (Label) event.widget;
//				Shell shell = label.getShell();
//				switch (event.type) {
//				case SWT.MouseDown:
//					Event e = new Event();
//					e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
//					// Assuming table is single select, set the selection as
//					// if
//					// the mouse down event went through to the table
//					playersArchiveTable.setSelection(new TableItem[] {
//						(TableItem) e.item
//					});
//					playersArchiveTable.notifyListeners(SWT.Selection, e);
//					break;
//				// fall through
//				case SWT.MouseExit:
//					shell.dispose();
//					break;
//				}
//			}
//		};
//
//		Listener tableListener = new Listener() {
//
//			Label label = null;
//
//			Shell shell = ViewerHandler.getViewer();
//
//			Shell tip = null;
//
//			public void handleEvent(Event event) {
//				switch (event.type) {
//				case SWT.Dispose:
//				case SWT.KeyDown:
//				case SWT.MouseMove: {
//					if (tip == null) {
//						break;
//					}
//					tip.dispose();
//					tip = null;
//					label = null;
//					break;
//				}
//				case SWT.MouseHover: {
//					Rectangle clientArea = playersArchiveTable.getClientArea();
//					Point pt = new Point(event.x, event.y);
//					TableItem item = playersArchiveTable.getItem(pt);
//					if (item != null) {
//						boolean visible = false;
//						for (int i = 0; i < playersArchiveTable.getColumnCount(); i++) {
//							Rectangle rect = item.getBounds(i);
//							if (rect.contains(pt)) {
//								if (!visible) {
//									return;
//								}
//								if (i == PlayerArchiveComparator.NOTE) {
//									if (tip != null && !tip.isDisposed()) {
//										tip.dispose();
//									}
//
//									int minSizeX = 200;
//									int minSizeY = 80;
//
//									int maxSizeX = 400;
//									int maxSizeY = 200;
//
//									PlayerArchive player = (PlayerArchive) item.getData(PlayerArchive.IDENTIFIER); //$NON-NLS-1$
//									if (player.getNote() != null && !player.getNote().equals("")) { //$NON-NLS-1$
//										tip = new Shell(shell, SWT.ON_TOP | SWT.TOOL);
//										tip.setLayout(new FillLayout());
//										label = new Label(tip, SWT.NONE);
//										label.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
//										label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
//										label.setData("_TABLEITEM", item); //$NON-NLS-1$
//										label.setText(player.getNote());
//										label.addListener(SWT.MouseExit, labelListener);
//										label.addListener(SWT.MouseDown, labelListener);
//
//										Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//
//										if (size.x < minSizeX) {
//											size.x = minSizeX;
//										}
//										if (size.y < minSizeY) {
//											size.y = minSizeY;
//										}
//
//										if (size.x > maxSizeX) {
//											size.x = maxSizeX;
//										}
//
//										if (size.y > maxSizeY) {
//											size.y = maxSizeY;
//										}
//										
//										Point ptDisplay = playersArchiveTable.toDisplay(event.x - size.x, event.y - size.y);
//										tip.setBounds(ptDisplay.x, ptDisplay.y, size.x, size.y);
//										tip.setVisible(true);
//
//									}
//								}
//							}
//							if (!visible && rect.intersects(clientArea)) {
//								visible = true;
//							}
//						}
//
//					}
//				}
//					break;
//				}
//			}
//		};
//		playersArchiveTable.addListener(SWT.Dispose, tableListener);
//		playersArchiveTable.addListener(SWT.KeyDown, tableListener);
//		playersArchiveTable.addListener(SWT.MouseMove, tableListener);
//		playersArchiveTable.addListener(SWT.MouseHover, tableListener);

		playersArchiveTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = playersArchiveTable.getItem(pt);
				if (item != null && item.getData(PlayerArchive.class.getName()) != null) { //$NON-NLS-1$
					PlayerArchive playerArchive = (PlayerArchive) item.getData(PlayerArchive.class.getName()); //$NON-NLS-1$
					archiveInformationGroup.fill(playerArchive);
				}

				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					if (item != null) {
						menuPopUp.setData("item", item); //$NON-NLS-1$
						playersArchiveTable.setMenu(menuPopUp);
						playersArchiveTable.getMenu().setVisible(true);
					} else {
						playersArchiveTable.setMenu(menuClear);
					}
				}
			}
		});

		formData = new FormData();
		formData.left = new FormAttachment(archiveSearchGroup, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(playersArchiveTable, -5);
		formData.width = 250;
		formData.height = 170;

		archiveInformationGroup = new ArchiveInformationGroup(composite, SWT.NONE);
		archiveInformationGroup.setLayoutData(formData);

	}

	private void openNote(Item item) {
		PlayerArchive playerArchive = (PlayerArchive) item.getData(PlayerArchive.class.getName()); //$NON-NLS-1$
		final NoteShell noteShell = new NoteShell(composite.getShell(), SWT.PRIMARY_MODAL | SWT.CLOSE);
		noteShell.setPerson(playerArchive);
		noteShell.open();

		if (item instanceof TreeItem) {
			playersArchiveTable.fill(players);
		} else if (item instanceof TableItem) {
			if (playerArchive.getNote() != null) {
				if (playerArchive.getNote().isEmpty()) {
					((TableItem) item).setImage(PlayerArchiveComparator.NOTE, null);
				} else {
					((TableItem) item).setImage(PlayerArchiveComparator.NOTE, ImageResources.getImageResources("note.png")); //$NON-NLS-1$
				}
			}
		}
	}

	public void reload() {
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void setTreeItem(final TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewArchive")); //$NON-NLS-1$
		this.treeItem.setImage(ImageResources.getImageResources("archive.png")); //$NON-NLS-1$
		treeItem.getParent().addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = treeItem.getParent().getItem(pt);
				if (item != null && item.equals(treeItem)) {
					ViewerHandler.getViewer().setDefaultButton(archiveSearchGroup.getSearchButton());
				}
			}
		});
	}

	public void set() {
		hmPlayersArchive = Cache.getPlayersArchiveMap();

		hmPlayerNameArchive = new HashMap<String, ArrayList<PlayerArchive>>();
		hmPlayerSurnameArchive = new HashMap<String, ArrayList<PlayerArchive>>();
		hmPlayerYouthTeamIDArchive = new HashMap<Integer, ArrayList<PlayerArchive>>();
		hmPlayerCountryIDArchive = new HashMap<Integer, ArrayList<PlayerArchive>>();
		setPlayersArchive = hmPlayersArchive.entrySet();
		for (Entry<Integer, PlayerArchive> element : setPlayersArchive) {

			if (hmPlayerNameArchive.get(element.getValue().getName().toLowerCase()) == null) {
				hmPlayerNameArchive.put(element.getValue().getName().toLowerCase(), new ArrayList<PlayerArchive>());
			}
			hmPlayerNameArchive.get(element.getValue().getName().toLowerCase()).add(element.getValue());

			if (hmPlayerSurnameArchive.get(element.getValue().getSurname().toLowerCase()) == null) {
				hmPlayerSurnameArchive.put(element.getValue().getSurname().toLowerCase(), new ArrayList<PlayerArchive>());
			}
			hmPlayerSurnameArchive.get(element.getValue().getSurname().toLowerCase()).add(element.getValue());

			if (hmPlayerYouthTeamIDArchive.get(element.getValue().getYouthTeamID()) == null) {
				hmPlayerYouthTeamIDArchive.put(element.getValue().getYouthTeamID(), new ArrayList<PlayerArchive>());
			}
			hmPlayerYouthTeamIDArchive.get(element.getValue().getYouthTeamID()).add(element.getValue());

			if (hmPlayerCountryIDArchive.get(element.getValue().getCountryID()) == null) {
				hmPlayerCountryIDArchive.put(element.getValue().getCountryID(), new ArrayList<PlayerArchive>());
			}
			hmPlayerCountryIDArchive.get(element.getValue().getCountryID()).add(element.getValue());
		}

	}

}
