package pl.pronux.sokker.ui.plugins;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.PersonsManager;
import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.bean.SvBean;
import pl.pronux.sokker.comparators.PlayerHistoryComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.PersonInterface;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerHistoryTable;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.CursorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.ChartDateComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.JuniorTrainedDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.PlayerHistoryDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.PlayersHistoryDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.styledtexts.PlayerHistoryDescription;
import pl.pronux.sokker.ui.widgets.tables.JuniorTrainedTable;
import pl.pronux.sokker.ui.widgets.tables.PlayerTable;
import pl.pronux.sokker.ui.widgets.tables.PlayersHistoryTable;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.pdf.PDFexport;

public class ViewPlayersHistory implements IPlugin, ISort {

	private PersonsManager personsManager = PersonsManager.instance();

	private PlayersManager playersManager = PlayersManager.instance();

	private TreeItem _treeItem;

	private PlayersHistoryTable allPlayersTable;

	private Clipboard cb;

	private String cbData;

	private Composite vComposite;

	private Composite currentDesc;

	// private Player currentPlayer;

	private Table currentView;

	private Map<Integer, PlayerHistoryDescriptionComposite> descMap;

	private PlayersHistoryDescriptionComposite playersHistoryDescription;

	private FormData descriptionFormData;

	private ChartDateComposite graphComposite;

	private Listener graphList;

	private JuniorTrainedTable juniorTrainedTable;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private PlayerHistoryDescriptionComposite playerDesc;

	private Map<Integer, Player> playerMap;

	private List<Player> players;

	private PlayerTable playerView;

	private Composite previousDesc;

	private Map<Integer, TableItem> tableItemMap;

	private Map<Integer, TreeItem> treeItemMap;

	private JuniorTrainedDescriptionComposite universalJuniorComposite;

	private PlayerHistoryDescriptionComposite universalPlayerComposite;

	private FormData viewFormData;

	private Map<Integer, Table> viewMap;

	private SokkerViewerSettings settings;

	private JuniorTrainedDescriptionComposite juniorTrainedComposite;

	private void showDescription(Player player) {
		if (viewMap.get(player.getId()) == null) {
			addPlayerView(player);
			vComposite.layout();
		}
		showDescription(player.getId());
	}

	private void showView(Player player) {
		if (viewMap.get(player.getId()) == null) {
			addPlayerView(player);
			vComposite.layout(new Control[] { viewMap.get(player.getId()) });
			// composite.layout();
		}
		showView(player.getId());
	}

	public void clear() {
		playersHistoryDescription.clearAll();
		allPlayersTable.removeAll();
		_treeItem.removeAll();
		players.clear();
		// List the entries using entrySet()
		descMap.clear();
		viewMap.clear();
		treeItemMap.clear();
	}

	public Composite getComposite() {
		return vComposite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoPlayerHistory");
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	private void addTableEditor(final Table table) {
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		table.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				final TableItem item = table.getItem(pt);
				if (item != null) {
					if (((Player) item.getData(Player.class.getName())).getTransferSell() != null) {
						return;
					}
					boolean visible = false;
					for (int i = table.getColumnCount() - 2; i < table.getColumnCount() - 1; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							final int column = i;
							final Text text = new Text(table, SWT.RIGHT);
							text.setTextLimit(10);
							text.setFont(ConfigBean.getFontTable());

							editor.setEditor(text, item, i);

							text.setText(item.getText(i));

							Listener textListener = new Listener() {

								public void handleEvent(final Event event) {
									switch (event.type) {
									case SWT.FocusOut:

										if (text.getText().isEmpty()) {
											text.setText("0");
										}

										int temp = Integer.valueOf(item.getText(column).replaceAll("[^0-9]", "")).intValue();

										int value = Integer.valueOf(text.getText().replaceAll("[^0-9]", "")).intValue();

										if (temp != value) {
											item.setText(column, Money.convertMoneyFormatDoubleToInteger(Money.convertPricesToBase(value)));
											Player player = (Player) item.getData(Player.class.getName());
											player.setSoldPrice(Money.convertPricesToBase(value));
											descMap.get(player.getId()).setStatsPlayerInfo(player, 0);
											try {
												playersManager.updatePlayersSoldPrice(player);
											} catch (SQLException e) {
												new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->FocusOut", e);
											}
											table.getColumn(table.getColumnCount() - 2).pack();
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											if (text.getText().isEmpty()) {
												text.setText("0");
											}

											temp = Integer.valueOf(item.getText(column).replaceAll("[^0-9]", "")).intValue();

											value = Integer.valueOf(text.getText().replaceAll("[^0-9]", "")).intValue();

											if (temp != value) {
												item.setText(column, Money.convertMoneyFormatDoubleToInteger(Money.convertPricesToBase(value)));
												Player player = (Player) item.getData(Player.class.getName());
												player.setSoldPrice(Money.convertPricesToBase(value));
												descMap.get(player.getId()).setStatsPlayerInfo(player, 0);
												try {
													playersManager.updatePlayersSoldPrice(player);
												} catch (SQLException e) {
													new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->Traverse", e);
												}
												table.getColumn(table.getColumnCount() - 2).pack();
											}
											break;
										// FALL THROUGH
										case SWT.TRAVERSE_ESCAPE:
											text.dispose();
											event.doit = false;
											break;
										}
										break;
									case SWT.Verify:
										String string = event.text;
										char[] chars = new char[string.length()];
										string.getChars(0, chars.length, chars, 0);
										for (int j = 0; j < chars.length; j++) {
											if (!('0' <= chars[j] && chars[j] <= '9')) {
												event.doit = false;
												return;
											}
										}
										break;
									}
								}
							};
							text.addListener(SWT.FocusOut, textListener);
							text.addListener(SWT.Traverse, textListener);
							text.addListener(SWT.Verify, textListener);

							text.selectAll();
							text.setFocus();
							return;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible) return;
				}
			}
		});

	}

	public void init(Composite composite) {
		vComposite = new ViewComposite(composite.getParent(), composite.getStyle());
		this.vComposite.setLayoutData(composite.getLayoutData());

		composite.dispose();

		viewFormData = ((ViewComposite) this.vComposite).getViewFormData();
		descriptionFormData = ((ViewComposite) this.vComposite).getDescriptionFormData();

		cb = ViewerHandler.getClipboard();

		players = new ArrayList<Player>();
		descMap = new HashMap<Integer, PlayerHistoryDescriptionComposite>();
		viewMap = new HashMap<Integer, Table>();
		treeItemMap = new HashMap<Integer, TreeItem>();
		playerMap = new HashMap<Integer, Player>();
		tableItemMap = new HashMap<Integer, TableItem>();

		addViewListener();

		addViewComposite();
		addDescriptionComposite();
		addJuniorTrainedView();
		addJuniorTrainedDescription();
		addPopupMenu();
		addPopupMenuParentTree();

		currentDesc = playersHistoryDescription;
		currentView = allPlayersTable;
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;

	}

	public void setTreeItem(TreeItem treeItem) {
		this._treeItem = treeItem;
		_treeItem.setText(Messages.getString("tree.ViewPlayersHistory"));
		_treeItem.setImage(ImageResources.getImageResources("sold_player.png"));
	}

	private void setPlayersData(List<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			playerMap.put(players.get(i).getId(), players.get(i));
		}
		// this loop could be comment for faster downloading
		for (int i = 0; i < players.size(); i++) {
			addPlayerDescription(players.get(i));
		}
		vComposite.layout(true);
	}

	private void addPlayerDescription(Player player) {
		playerDesc = new PlayerHistoryDescriptionComposite(vComposite, SWT.BORDER);
		playerDesc.setLayoutData(descriptionFormData);
		playerDesc.setStatsPlayerInfo(player, 0);
		descMap.put(player.getId(), playerDesc);
	}

	private void addPlayerView(Player player) {

		playerView = new PlayerTable(vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		playerView.setLayoutData(viewFormData);
		playerView.setVisible(false);

		playerView.setData(Player.class.getName(), player);

		playerView.addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });
					showView(allPlayersTable);
					showDescription(playersHistoryDescription);
				}
			}
		});

		playerView.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				TableItem item = currentView.getItem(point);
				if (item != null) {
					if (event.button == 1) {
						if (currentDesc instanceof ChartDateComposite) {
							((ChartDateComposite) currentDesc).setMarkers((Date) item.getData("date"), Calendar.THURSDAY, Integer.valueOf(item
								.getText(((ChartDateComposite) currentDesc).getColumn())
								.replaceAll("[^0-9-]", "")));
						} else if (currentDesc instanceof PlayerHistoryDescription) {
							int index = item.getParent().indexOf(item);
							universalPlayerComposite.setStatsPlayerInfo((Player) item.getParent().getData(Player.class.getName()), index);
							showDescription(universalPlayerComposite);
						}
					} else if (event.button == 3) {
						showDescription(previousDesc);
					}
				}
			}
		});

		playerView.fill(player);
		viewMap.put(player.getId(), playerView);

		for (int j = 1; j < playerView.getColumnCount() - 1; j++) {
			playerView.getColumn(j).addListener(SWT.Selection, graphList);
		}
	}

	public void set() {

		ViewerHandler.getViewer().addListener(IEvents.REFRESH_PLAYERS_HISTORY, new Listener() {

			public void handleEvent(Event arg0) {
				setPlayersData(players);
				playersHistoryDescription.setDescription(players);
				fillTree(players);
				tableItemMap = allPlayersTable.fill(players);
			}
		});

		players = Cache.getPlayersHistory();
		tableItemMap = allPlayersTable.fill(players);
		playersHistoryDescription.setDescription(players);
		fillTree(players);
		addTableEditor(allPlayersTable);
		setPlayersData(players);

		allPlayersTable.addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				Rectangle clientArea = allPlayersTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = allPlayersTable.getTopIndex();

				while (index < allPlayersTable.getItemCount()) {
					boolean visible = false;
					TableItem item = allPlayersTable.getItem(index);
					for (int i = 0; i < allPlayersTable.getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							_treeItem.getParent().setSelection(
															   new TreeItem[] { (TreeItem) treeItemMap.get(((PersonInterface) item.getData(Player.class
																   .getName())).getId()) });
							showView((Player) item.getData(Player.class.getName()));
							showDescription((Player) item.getData(Player.class.getName()));
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible) return;
					index++;
				}
			}
		});

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(_treeItem) && event.button == 3) {
						cbData = playersHistoryDescription.getText();
						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}
		});
		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				switch (event.type) {
				case SWT.KeyUp:
					for (int i = 0; i < _treeItem.getParent().getSelection().length; i++) {
						item = _treeItem.getParent().getSelection()[i];
					}
					break;
				case SWT.MouseDown:
					Point point = new Point(event.x, event.y);
					item = _treeItem.getParent().getItem(point);
					break;
				}

				if (item != null) {
					if (item != null) {
						if (item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {
							Player player = (Player) item.getData(Player.class.getName());

							if (event.type == SWT.MouseDown && event.button == 3) {
								// currentPlayer = player;
								setCbData(player);
								menuPopUp.setData("item", item);
								_treeItem.getParent().setMenu(menuPopUp);
								// _treeItem.getParent().getMenu()._setVisible(true);
								_treeItem.getParent().getMenu().setVisible(true);
							}

							showDescription(player);
							showView(player);

						} else if (item.equals(_treeItem)) {
							showDescription(playersHistoryDescription);
							showView(allPlayersTable);

						} else if (item.getData("idJunior") != null) {
							if (item.getParentItem().getParentItem().equals(_treeItem)) {
								juniorTrainedTable.removeAll();
								Integer id = (Integer) item.getData("idJunior");
								juniorTrainedTable.fill(Cache.getJuniorsTrainedMap().get(id));
								juniorTrainedComposite.setStatsJuniorInfo(Cache.getJuniorsTrainedMap().get(id));
								showDescription(juniorTrainedComposite);
								showView(juniorTrainedTable);
							}
						}
					}
				}
			}

		};

		_treeItem.getParent().addListener(SWT.MouseDown, listener);
		_treeItem.getParent().addListener(SWT.KeyUp, listener);

	}

	private void addDescriptionComposite() {
		playersHistoryDescription = new PlayersHistoryDescriptionComposite(vComposite, SWT.BORDER);
		playersHistoryDescription.setLayoutData(descriptionFormData);
		playersHistoryDescription.setVisible(true);

		graphComposite = new ChartDateComposite(vComposite, SWT.BORDER);
		graphComposite.setLayoutData(descriptionFormData);
		graphComposite.setVisible(false);
		// graphComposite.getCanvas().addListener(SWT.MouseDown,historyBackListener);

		universalPlayerComposite = new PlayerHistoryDescriptionComposite(vComposite, SWT.BORDER);
		universalPlayerComposite.setLayoutData(descriptionFormData);
		universalPlayerComposite.setVisible(false);

		universalJuniorComposite = new JuniorTrainedDescriptionComposite(vComposite, SWT.BORDER);
		universalJuniorComposite.setLayoutData(descriptionFormData);
		universalJuniorComposite.setVisible(false);

	}

	private void addJuniorTrainedDescription() {
		juniorTrainedComposite = new JuniorTrainedDescriptionComposite(vComposite, SWT.BORDER);
		juniorTrainedComposite.setLayoutData(descriptionFormData);
		juniorTrainedComposite.setVisible(false);
	}

	private void addJuniorTrainedView() {
		juniorTrainedTable = new JuniorTrainedTable(vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		juniorTrainedTable.setLayoutData(viewFormData);
		juniorTrainedTable.setVisible(false);

		juniorTrainedTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 1) {
					Point point = new Point(event.x, event.y);
					TableItem item = currentView.getItem(point);
					if (item != null) {
						if (currentDesc instanceof ChartDateComposite) {
							((ChartDateComposite) currentDesc).setMarkers((Date) item.getData("date"), Calendar.THURSDAY, -1);
						} else if (currentDesc instanceof DescriptionSingleComposite) {
							int index = item.getParent().indexOf(item);
							universalJuniorComposite.setStatsJuniorInfo((Junior) item.getParent().getData(Junior.class.getName()), index);
							showDescription(universalJuniorComposite);
						}
					}
				} else if (event.button == 3) {
					showDescription(previousDesc);
				}
			}
		});

		for (int j = 1; j < juniorTrainedTable.getColumnCount() - 2; j++) {
			juniorTrainedTable.getColumn(j).addListener(SWT.Selection, graphList);
		}
	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(vComposite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { cbData }, new Transfer[] { textTransfer });
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.export.player"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) {
					Item item = (Item) menuPopUp.getData("item");
					if (item.getData(Player.class.getName()) != null) {
						Player player = (Player) item.getData(Player.class.getName());
						String[] extensions = { "*.sv_" };
						FileDialog fileDialog = new FileDialog(vComposite.getShell(), SWT.SAVE);
						fileDialog.setText(Messages.getString("confShell.chooser.title"));
						fileDialog.setFilterExtensions(extensions);
						fileDialog.setFilterPath(settings.getBaseDirectory());
						fileDialog.setFileName(String.format("%s_%s", player.getSurname(), player.getName()));

						String propsInputFile = fileDialog.open();
						if (propsInputFile != null) {
							try {
								OperationOnFile.serializePlayer(player, propsInputFile);
							} catch (Exception e) {
								new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->serializePlayer", e);
							}
						}
					}
				}

			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.move"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) {
					Item item = (Item) menuPopUp.getData("item");
					if (item.getData(Player.class.getName()) != null) {
						Player player = (Player) item.getData(Player.class.getName());
						MessageBox messageBox = new MessageBox(vComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
						messageBox.setMessage(Messages.getString("message.playerMove.text"));
						messageBox.setText(Messages.getString("message.playerMove.title"));

						if (messageBox.open() == SWT.YES) {
							// Player player = (Player)currentItem.getData("");
							try {
								personsManager.movePersonToTrash(player);
								tableItemMap = allPlayersTable.fill(players);

								_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });
								playersHistoryDescription.setDescription(players);
								showDescription(playersHistoryDescription);
								showView(allPlayersTable);

								treeItemMap.get(player.getId()).dispose();
								// tableItemMap.get(player.getId()).dispose();
								viewMap.get(player.getId()).dispose();
								descMap.get(player.getId()).dispose();

								ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_PLAYERS, new Event());
							} catch (SQLException e) {
								new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->moveToTrash", e);
							}

						}
					}
				}
			}
		});

		// menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		// menuItem.setText(langProperties.getProperty("popup.set.soldPrice"));
		// menuItem.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event e) {
		// }
		// });
		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.generateToPDF"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) {
					Item item = (Item) menuPopUp.getData("item");
					if (item.getData(Player.class.getName()) != null) {
						Player currentPlayer = (Player) item.getData(Player.class.getName());
						String[] extensions = { "*.pdf" };
						FileDialog fileDialog = new FileDialog(vComposite.getShell(), SWT.SAVE);
						fileDialog.setText(Messages.getString("confShell.chooser.title"));
						fileDialog.setFilterExtensions(extensions);
						fileDialog.setFilterPath(settings.getBaseDirectory());
						fileDialog.setFileName(String.format("%s_%s", currentPlayer.getSurname(), currentPlayer.getName()));

						String propsInputFile = fileDialog.open();
						if (propsInputFile != null) {
							PDFexport.export(propsInputFile, currentPlayer);
						}
					}
				}

			}
		});

		menuClear = new Menu(vComposite.getShell(), SWT.POP_UP);

	}

	private void addPopupMenuParentTree() {
		// added popup menu
		menuPopUpParentTree = new Menu(vComposite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.clipboard"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents(new Object[] { cbData }, new Transfer[] { textTransfer });
			}
		});

		menuItem = new MenuItem(menuPopUpParentTree, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.moveAll"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				try {
					MessageBox messageBox = new MessageBox(vComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					messageBox.setMessage(Messages.getString("message.playerMoveAll.text"));
					messageBox.setText(Messages.getString("message.playerMoveAll.title"));

					if (messageBox.open() == SWT.YES) {

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));

						showView(allPlayersTable);
						showDescription(playersHistoryDescription);

						// allJuniorsTable.setRedraw(false);

						SQLSession.connect();

						for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {

							Player player = itr.next();
							itr.remove();

							personsManager.movePersonToTrash(player);

							treeItemMap.get(player.getId()).dispose();
							tableItemMap.get(player.getId()).dispose();
						}

						tableItemMap = allPlayersTable.fill(players);

						SQLSession.close();

						// allJuniorsTable.setRedraw(true);

						_treeItem.getParent().setSelection(new TreeItem[] { _treeItem });

						playersHistoryDescription.setDescription(players);
						showDescription(playersHistoryDescription);
						showView(allPlayersTable);

						ViewerHandler.getViewer().notifyListeners(IEvents.REFRESH_TRASH_PLAYERS, new Event());

						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));
					}

				} catch (SQLException e) {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
					new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayersHistory->moveAllToTrash", e);
				}
			}
		});

	}

	private void addViewComposite() {
		allPlayersTable = new PlayersHistoryTable(vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		allPlayersTable.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				if (event != null) {
					showDescription((Player) event.item.getData(Player.class.getName()));
				}
			}

		});

		allPlayersTable.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = allPlayersTable.getItem(pt);
					if (item != null) {
						Player player = (Player) item.getData(Player.class.getName());

						// currentPlayer = player;
						setCbData(player);
						menuPopUp.setData("item", item);
						allPlayersTable.setMenu(menuPopUp);
						allPlayersTable.getMenu().setVisible(true);
					} else {
						allPlayersTable.setMenu(menuClear);
						showDescription(playersHistoryDescription);
					}
				}
			}
		});

		allPlayersTable.setLayoutData(viewFormData);
		allPlayersTable.setVisible(true);
	}

	private void addViewListener() {
		graphList = new Listener() {

			public void handleEvent(Event event) {

				TableColumn tempColumn = (TableColumn) event.widget;
				Table tempTable = (tempColumn).getParent();

				int[] tempIntTable = new int[tempTable.getItemCount()];
				String[] tempDateTable = new String[tempTable.getItemCount()];

				// sprawdzamy czy tabela zawiera elementy
				int k = tempTable.indexOf(tempColumn);
				for (int x = 0; x < tempTable.getItemCount(); x++) {
					tempIntTable[x] = Integer.valueOf(tempTable.getItem(x).getText(k).replaceAll("[^0-9]", "")).intValue();
					tempDateTable[x] = tempTable.getItem(x).getText(0);
				}

				graphComposite.setColumn(k);

				switch (k) {
				case PlayerHistoryTable.VALUE:
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, false, -1);
					break;
				case PlayerHistoryTable.SALARY:
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, -1);
					break;
				case PlayerHistoryTable.AGE:
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, -1);
					break;
				case PlayerHistoryTable.STAMINA:
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, 12);
					break;
				default:
					graphComposite.fillGraph(tempIntTable, tempDateTable, Calendar.THURSDAY, true, 18);
					break;
				}

				showDescription(graphComposite);
			}
		};

	}

	private void fillTree(List<Player> players) {
		_treeItem.removeAll();
		PlayerHistoryComparator comparator = new PlayerHistoryComparator();
		comparator.setColumn(PlayerHistoryComparator.SURNAME);
		comparator.setDirection(PlayerHistoryComparator.ASCENDING);
		Collections.sort(players, comparator);

		for (int i = 0; i < players.size(); i++) {
			TreeItem item = new TreeItem(_treeItem, SWT.NONE);
			item.setData(Player.class.getName(), players.get(i));
			item.setText(players.get(i).getSurname() + " " + players.get(i).getName());
			item.setImage(FlagsResources.getFlag(players.get(i).getCountryfrom()));

			treeItemMap.put(players.get(i).getId(), item);

			if (players.get(i).getJunior() != null) {
				item = new TreeItem(item, 0);
				item.setImage(ImageResources.getImageResources("junior.png"));
				item.setText(Messages.getString("tree.junior"));
				item.setData("idJunior", Integer.valueOf(players.get(i).getJunior().getId()));
			}
		}
	}

	private void setCbData(Player player) {
		// TODO do poprawy !!!!!!!!
		cbData = ((PlayerHistoryDescriptionComposite) descMap.get(player.getId())).getText();
		// cbData += ((DescriptionDoubleComposite)
		// descMap.get(player.getId())).getRightText();
		if (player.getJunior() != null) {

			Junior junior = player.getJunior();
			cbData += "====================================\r\n";
			cbData += String.format("%-35s\r\n", new Object[] { Messages.getString("junior") });
			cbData += "====================================\r\n";
			cbData += String.format("%-20s%-15s\r\n", new Object[] { Messages.getString("junior.id"), junior.getId() });
			cbData += String.format("%-20s%-15s\r\n",
									new Object[] {
												  Messages.getString("junior.skill"),
												  Messages.getString("skill.a" + junior.getSkills()[junior.getSkills().length - 1].getSkill()) + "["
																  + junior.getSkills()[junior.getSkills().length - 1].getSkill() + "]" });
			cbData += String.format("%-20s%-15s\r\n", new Object[] { Messages.getString("junior.weeksAll"), junior.getSkills()[0].getWeeks() });
			cbData += String.format("%-20s%-15s\r\n", new Object[] { Messages.getString("junior.numberOfJumps"), junior.getPops() });
			cbData += String.format("%-20s%-15s\r\n", new Object[] { Messages.getString("junior.averageJumps"), junior.getAveragePops() });
		}
	}

	private void showDescription(Composite composite) {
		if (!(currentDesc instanceof ChartDateComposite)) {
			previousDesc = currentDesc;
		}
		currentDesc.setVisible(false);
		currentDesc = composite;
		currentDesc.setVisible(true);
	}

	private void showDescription(int id) {
		currentDesc.setVisible(false);
		currentDesc = (Composite) descMap.get(id);
		currentDesc.setVisible(true);

		previousDesc = currentDesc;
	}

	private void showView(int id) {
		currentView.setVisible(false);
		currentView = viewMap.get(id);
		currentView.setVisible(true);
	}

	private void showView(Table table) {
		currentView.setVisible(false);
		currentView = table;
		currentView.setVisible(true);
	}

	public void dispose() {
	}

	public void setSvBean(SvBean svBean) {
	}

	public void reload() {
	}

}
