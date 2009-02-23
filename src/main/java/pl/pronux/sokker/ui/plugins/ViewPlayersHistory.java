package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
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
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.composites.DescriptionSingleComposite;
import pl.pronux.sokker.ui.widgets.composites.JuniorTrainedDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.JuniorTrainedTable;
import pl.pronux.sokker.ui.widgets.tables.PlayerTable;
import pl.pronux.sokker.ui.widgets.tables.PlayersHistoryTable;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.pdf.PDFexport;

public class ViewPlayersHistory implements IPlugin, ISort {

	private TreeItem _treeItem;

	private PlayersHistoryTable allPlayersTable;

	private Clipboard cb;

	private String cbData;

	private Composite vComposite;

	private Composite currentDesc;

	// private Player currentPlayer;

	private Table currentView;

	private Map<Integer, Composite> descMap;

	private DescriptionSingleComposite descriptionComposite;

	private FormData descriptionFormData;

	private ChartDateComposite graphComposite;

	private Listener graphList;

	private JuniorTrainedTable juniorTrainedTable;

	private Menu menuClear;

	private Menu menuPopUp;

	private Menu menuPopUpParentTree;

	private DescriptionDoubleComposite playerDesc;

	private HashMap<Integer, Player> playerMap;

	private ArrayList<Player> players;

	private PlayerTable playerView;

	private Composite previousDesc;

	private Map<Integer, TableItem> tableItemMap;

	private HashMap<Integer, TreeItem> treeItemMap;

	private JuniorTrainedDescriptionComposite universalJuniorComposite;

	private DescriptionDoubleComposite universalPlayerComposite;

	private FormData viewFormData;

	private Listener viewKeyListener;

	private Listener viewListener;

	private HashMap<Integer, Table> viewMap;

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
			vComposite.layout(new Control[] {
				viewMap.get(player.getId())
			});
			// composite.layout();
		}
		showView(player.getId());
	}

	public void clear() {
		descriptionComposite.clearAll();
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
					if (((Player) item.getData(Player.IDENTIFIER)).getTransferSell() != null) {
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

										if (text.getText().equals("")) {
											text.setText("0");
										}

										int temp = Integer.valueOf(item.getText(column).replaceAll("[^0-9]", "")).intValue();

										int value = Integer.valueOf(text.getText().replaceAll("[^0-9]", "")).intValue();

										if (temp != value) {
											item.setText(column, Money.convertMoneyFormatDoubleToInteger(Money.convertPricesToBase(value)));
											Player player = (Player) item.getData(Player.IDENTIFIER);
											player.setSoldPrice(Money.convertPricesToBase(value));
											setStatsPlayerInfo(player, (DescriptionDoubleComposite) descMap.get(player.getId()), 0);
											try {
												new PlayersManager().updatePlayersSoldPrice(player);
											} catch (SQLException e) {
												new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->FocusOut", e);
											}
											playerView.getColumn(playerView.getColumnCount() - 2).pack();
										}
										text.dispose();
										break;
									case SWT.Traverse:
										switch (event.detail) {
										case SWT.TRAVERSE_RETURN:
											if (text.getText().equals("")) {
												text.setText("0");
											}

											temp = Integer.valueOf(item.getText(column).replaceAll("[^0-9]", "")).intValue();

											value = Integer.valueOf(text.getText().replaceAll("[^0-9]", "")).intValue();

											if (temp != value) {
												item.setText(column, Money.convertMoneyFormatDoubleToInteger(Money.convertPricesToBase(value)));
												Player player = (Player) item.getData(Player.IDENTIFIER);
												player.setSoldPrice(Money.convertPricesToBase(value));
												setStatsPlayerInfo(player, (DescriptionDoubleComposite) descMap.get(player.getId()), 0);
												try {
													new PlayersManager().updatePlayersSoldPrice(player);
												} catch (SQLException e) {
													new BugReporter(vComposite.getDisplay()).openErrorMessage("ViewPlayerHistory->Traverse", e);
												}
												playerView.getColumn(playerView.getColumnCount() - 2).pack();
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
					if (!visible)
						return;
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

		descMap = new HashMap<Integer, Composite>();
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

		currentDesc = descriptionComposite;
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

	private void setPlayersData() {
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
		playerDesc = new DescriptionDoubleComposite(vComposite, SWT.BORDER);
		playerDesc.setLayoutData(descriptionFormData);
		playerDesc.setVisible(false);

		playerDesc.setLeftDescriptionStringFormat("%-25s%-15s\r\n");
		playerDesc.setLeftFirstColumnSize(25);
		playerDesc.setLeftSecondColumnSize(15);

		playerDesc.setRightDescriptionStringFormat("%-25s%-15s\r\n");
		playerDesc.setRightFirstColumnSize(25);
		playerDesc.setRightSecondColumnSize(15);

		playerDesc.setFont(ConfigBean.getFontDescription());

		setStatsPlayerInfo(player, playerDesc, 0);

		descMap.put(player.getId(), playerDesc);
	}

	private void addPlayerView(Player player) {

		playerView = new PlayerTable(vComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		playerView.setLayoutData(viewFormData);
		playerView.setVisible(false);

		playerView.setData(Player.IDENTIFIER, player);

		playerView.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 1) {
					_treeItem.getParent().setSelection(new TreeItem[] {
						_treeItem
					});
					showView(allPlayersTable);
					showDescription(descriptionComposite);
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
							((ChartDateComposite) currentDesc).setMarkers((Date) item.getData("date"), Calendar.THURSDAY, Integer.valueOf(item.getText(((ChartDateComposite) currentDesc).getColumn()).replaceAll("[^0-9-]", "")));
						} else if (currentDesc instanceof DescriptionDoubleComposite) {
							int index = item.getParent().indexOf(item);
							setStatsPlayerInfo((Player) item.getParent().getData(Player.IDENTIFIER), universalPlayerComposite, index);
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
				setPlayersData();
				setDescriptionComposite(players);
				fillTree(players);
				tableItemMap = allPlayersTable.fill(players);
			}
		});

		players = Cache.getPlayersHistory();
		setViewComposite(players);
		setDescriptionComposite(players);
		fillTree(players);
		addTableEditor(allPlayersTable);
		setPlayersData();

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null) {
					if (item.equals(_treeItem) && event.button == 3) {
						cbData = descriptionComposite.getText();
						_treeItem.getParent().setMenu(menuPopUpParentTree);
						_treeItem.getParent().getMenu().setVisible(true);
					}
				}
			}
		});

		_treeItem.getParent().addListener(SWT.MouseDown, viewListener);
		_treeItem.getParent().addListener(SWT.KeyUp, viewKeyListener);

	}

	private void addDescriptionComposite() {
		descriptionComposite = new DescriptionSingleComposite(vComposite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);

		// descriptionComposite.setDescriptionStringFormat(40, 15);
		descriptionComposite.setDescriptionStringFormat("%-40s%-35s\r\n");
		descriptionComposite.setFirstColumnSize(40);
		descriptionComposite.setSecondColumnSize(15);

		descriptionComposite.setFont(ConfigBean.getFontDescription());

		graphComposite = new ChartDateComposite(vComposite, SWT.BORDER);
		graphComposite.setLayoutData(descriptionFormData);
		graphComposite.setVisible(false);
		// graphComposite.getCanvas().addListener(SWT.MouseDown,historyBackListener);

		universalPlayerComposite = new DescriptionDoubleComposite(vComposite, SWT.BORDER);
		universalPlayerComposite.setLayoutData(descriptionFormData);
		universalPlayerComposite.setVisible(false);

		universalPlayerComposite.setLeftDescriptionStringFormat("%-25s%-15s\r\n");
		universalPlayerComposite.setLeftFirstColumnSize(25);
		universalPlayerComposite.setLeftSecondColumnSize(15);

		universalPlayerComposite.setRightDescriptionStringFormat("%-25s%-15s\r\n");
		universalPlayerComposite.setRightFirstColumnSize(25);
		universalPlayerComposite.setRightSecondColumnSize(15);

		universalPlayerComposite.setFont(ConfigBean.getFontDescription());

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
							universalJuniorComposite.setStatsJuniorInfo((Junior) item.getParent().getData(Junior.IDENTIFIER), index);
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
				cb.setContents(new Object[] {
					cbData
				}, new Transfer[] {
					textTransfer
				});
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.export.player"));
		menuItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (menuPopUp.getData("item") != null) {
					Item item = (Item) menuPopUp.getData("item");
					if (item.getData(Player.IDENTIFIER) != null) {
						Player player = (Player) item.getData(Player.IDENTIFIER);
						String[] extensions = {
							"*.sv_"
						};
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
					if (item.getData(Player.IDENTIFIER) != null) {
						Player player = (Player) item.getData(Player.IDENTIFIER);
						MessageBox messageBox = new MessageBox(vComposite.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
						messageBox.setMessage(Messages.getString("message.playerMove.text"));
						messageBox.setText(Messages.getString("message.playerMove.title"));

						if (messageBox.open() == SWT.YES) {
							// Player player = (Player)currentItem.getData("");
							try {
								PersonsManager.movePersonToTrash(player);
								tableItemMap = allPlayersTable.fill(players);

								_treeItem.getParent().setSelection(new TreeItem[] {
									_treeItem
								});

								setDescriptionComposite(players);
								showDescription(descriptionComposite);
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
					if (item.getData(Player.IDENTIFIER) != null) {
						Player currentPlayer = (Player) item.getData(Player.IDENTIFIER);
						String[] extensions = {
							"*.pdf"
						};
						FileDialog fileDialog = new FileDialog(vComposite.getShell(), SWT.SAVE);
						fileDialog.setText(Messages.getString("confShell.chooser.title"));
						fileDialog.setFilterExtensions(extensions);
						fileDialog.setFilterPath(settings.getBaseDirectory());
						fileDialog.setFileName(String.format("%s_%s",currentPlayer.getSurname(), currentPlayer.getName()));

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
				cb.setContents(new Object[] {
					cbData
				}, new Transfer[] {
					textTransfer
				});
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
						showDescription(descriptionComposite);

						// allJuniorsTable.setRedraw(false);

						SQLSession.connect();

						for (Iterator<Player> itr = players.iterator(); itr.hasNext();) {

							Player player = itr.next();
							itr.remove();

							PersonsManager.movePersonToTrash(player);

							treeItemMap.get(player.getId()).dispose();
							tableItemMap.get(player.getId()).dispose();
						}

						tableItemMap = allPlayersTable.fill(players);

						SQLSession.close();

						// allJuniorsTable.setRedraw(true);

						_treeItem.getParent().setSelection(new TreeItem[] {
							_treeItem
						});

						setDescriptionComposite(players);
						showDescription(descriptionComposite);
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
					showDescription((Player) event.item.getData(Player.IDENTIFIER));
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
						Player player = (Player) item.getData(Player.IDENTIFIER);

						// currentPlayer = player;
						setCbData(player);
						menuPopUp.setData("item", item);
						allPlayersTable.setMenu(menuPopUp);
						allPlayersTable.getMenu().setVisible(true);
					} else {
						allPlayersTable.setMenu(menuClear);
						showDescription(descriptionComposite);
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

		viewListener = new Listener() {
			public void handleEvent(Event event) {

				Point point = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(point);
				if (item != null) {
					if (item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {
						Player player = (Player) item.getData(Player.IDENTIFIER);

						if (event.button == 3) {
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
						showDescription(descriptionComposite);
						showView(allPlayersTable);

					} else if (item.getData("idJunior") != null) {
						if (item.getParentItem().getParentItem().equals(_treeItem)) {

							Integer id = (Integer) item.getData("idJunior");

							juniorTrainedTable.fill(Cache.getJuniorsTrainedMap().get(id));

							juniorTrainedComposite.setStatsJuniorInfo(Cache.getJuniorsTrainedMap().get(id));
							showDescription(juniorTrainedComposite);
							showView(juniorTrainedTable);
						}
					}
				}

			}
		};

		viewKeyListener = new Listener() {

			public void handleEvent(Event event) {
				TreeItem item = null;
				for (int i = 0; i < _treeItem.getParent().getSelection().length; i++) {
					item = _treeItem.getParent().getSelection()[i];
				}

				if (item != null) {
					if (item.getParentItem() != null && item.getParentItem().equals(_treeItem)) {

						showDescription((Player) item.getData(Player.IDENTIFIER));
						showView((Player) item.getData(Player.IDENTIFIER));
					} else if (item.equals(_treeItem)) {

						showDescription(descriptionComposite);
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
		};

	}

	private void fillTree(ArrayList<Player> player) {

		_treeItem.removeAll();
		PlayerHistoryComparator comparator = new PlayerHistoryComparator();
		comparator.setColumn(PlayerHistoryComparator.SURNAME);
		comparator.setDirection(PlayerHistoryComparator.ASCENDING);
		Collections.sort(player, comparator);

		for (int i = 0; i < player.size(); i++) {
			TreeItem item = new TreeItem(_treeItem, SWT.NONE);
			item.setData(Player.IDENTIFIER, player.get(i));
			item.setText(player.get(i).getSurname() + " " + player.get(i).getName());
			item.setImage(FlagsResources.getFlag(player.get(i).getCountryfrom()));

			treeItemMap.put(player.get(i).getId(), item);

			if (player.get(i).getJunior() != null) {
				item = new TreeItem(item, 0);
				item.setImage(ImageResources.getImageResources("junior.png"));
				item.setText(Messages.getString("tree.junior"));
				item.setData("idJunior", Integer.valueOf(player.get(i).getJunior().getId()));
			}
		}
	}

	private void setCbData(Player player) {
		// TODO do poprawy !!!!!!!!
		cbData = ((DescriptionDoubleComposite) descMap.get(player.getId())).getLeftText();
		cbData += ((DescriptionDoubleComposite) descMap.get(player.getId())).getRightText();
		if (player.getJunior() != null) {

			Junior junior = player.getJunior();
			cbData += "====================================\r\n";
			cbData += String.format("%-35s\r\n", new Object[] {
				Messages.getString("junior")
			});
			cbData += "====================================\r\n";
			cbData += String.format("%-20s%-15s\r\n", new Object[] {
					Messages.getString("junior.id"),
					junior.getId()
			});
			cbData += String.format("%-20s%-15s\r\n", new Object[] {
					Messages.getString("junior.skill"),
					Messages.getString("skill.a" + junior.getSkills()[junior.getSkills().length - 1].getSkill()) + "[" + junior.getSkills()[junior.getSkills().length - 1].getSkill() + "]"
			});
			cbData += String.format("%-20s%-15s\r\n", new Object[] {
					Messages.getString("junior.weeksAll"),
					junior.getSkills()[0].getWeeks()
			});
			cbData += String.format("%-20s%-15s\r\n", new Object[] {
					Messages.getString("junior.numberOfJumps"),
					junior.getPops()
			});
			cbData += String.format("%-20s%-15s\r\n", new Object[] {
					Messages.getString("junior.averageJumps"),
					junior.getAveragePops()
			});
		}
	}

	private void setDescriptionComposite(ArrayList<Player> player) {
		int maxSkill = 0;
		double teamValuePast = 0;
		double teamSalaryPast = 0;
		double averAgePast = 0;
		double teamValue = 0;
		double teamSalary = 0;
		double averAge = 0;
		String[][] values;

		descriptionComposite.setText("");
		for (int i = 0; i < player.size(); i++) {
			maxSkill = player.get(i).getSkills().length - 1;
			teamValue += player.get(i).getSkills()[maxSkill].getValue().getDoubleValue();
			teamSalary += player.get(i).getSkills()[maxSkill].getSalary().getDoubleValue();
			averAge += player.get(i).getSkills()[maxSkill].getAge();

			if (maxSkill > 1) {
				teamValuePast += player.get(i).getSkills()[maxSkill - 1].getValue().getDoubleValue();
				teamSalaryPast += player.get(i).getSkills()[maxSkill - 1].getSalary().getDoubleValue();
				averAgePast += player.get(i).getSkills()[maxSkill - 1].getAge();
			}
		}

		// aktualizujemy srednie wartosci
		values = new String[6][2];

		values[0][0] = Messages.getString("player.allValue");
		values[1][0] = Messages.getString("player.averageValue");
		values[2][0] = Messages.getString("player.allSalary");
		values[3][0] = Messages.getString("player.averageSalary");
		values[4][0] = Messages.getString("player.averageAge");
		values[5][0] = Messages.getString("player.allPlayers");

		values[0][1] = Money.formatDoubleCurrencySymbol(teamValue);

		if (player.size() > 0) {

			values[1][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamValue / player.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

			values[2][1] = Money.formatDoubleCurrencySymbol(teamSalary);

			values[3][1] = Money.formatDoubleCurrencySymbol(BigDecimal.valueOf(teamSalary / player.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

			values[4][1] = BigDecimal.valueOf(averAge / player.size()).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "   ";

			values[5][1] = String.valueOf(player.size()).toString() + "   ";

		} else {
			values[1][1] = Money.formatDouble(0);
			values[2][1] = Money.formatDoubleCurrencySymbol(teamSalary);
			values[3][1] = Money.formatDouble(0);
			values[4][1] = "0   ";
			values[5][1] = "0   ";
		}

		for (int i = 0; i < values.length; i++) {
			descriptionComposite.addText(values[i]);
		}

		descriptionComposite.setColor();
	}

	private void setShellMainTableAllPlayersData(ArrayList<Player> player) {

		tableItemMap = allPlayersTable.fill(players);

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
							_treeItem.getParent().setSelection(new TreeItem[] {
								(TreeItem) treeItemMap.get(((PersonInterface) item.getData(Player.IDENTIFIER)).getId())
							});
							showView((Player) item.getData(Player.IDENTIFIER));
							showDescription((Player) item.getData(Player.IDENTIFIER));
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

	}

	private void setStatsPlayerInfo(Player player, DescriptionDoubleComposite description, int index) {

		description.clearAll();

		int maxSkill = player.getSkills().length - index;

		int diffrents = 0;
		// int maxSkill = player.getSkills().length;
		int textSize = 0;

		String cards = "\u2588";
		String[][] values;

		values = new String[12][2];
		values[0][0] = Messages.getString("player.id");
		values[1][0] = Messages.getString("player.name");
		values[2][0] = Messages.getString("player.surname");
		values[3][0] = Messages.getString("player.country");
		values[4][0] = Messages.getString("player.value");
		values[5][0] = Messages.getString("player.salary");
		values[6][0] = Messages.getString("player.matches");
		values[7][0] = Messages.getString("player.goals");
		values[8][0] = Messages.getString("player.assists");
		values[9][0] = Messages.getString("player.cards");
		values[10][0] = Messages.getString("player.injurydays");
		values[11][0] = Messages.getString("player.soldPrice");

		textSize = 0;

		textSize = textSize + description.checkLeftFirstTextSize(values[0][0]);

		values[0][1] = String.valueOf(player.getId());

		textSize = textSize + description.checkLeftSecondTextSize(values[0][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[1][0]);

		values[1][1] = player.getName();

		textSize = textSize + description.checkLeftSecondTextSize(values[1][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[2][0]);

		values[2][1] = player.getSurname();

		textSize = textSize + description.checkLeftSecondTextSize(values[2][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[3][0]);

		values[3][1] = Messages.getString("country." + player.getCountryfrom() + ".name");

		textSize = textSize + description.checkLeftSecondTextSize(values[3][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[4][0]);

		values[4][1] = player.getSkills()[maxSkill - 1].getValue().formatDoubleCurrencySymbol();

		textSize = textSize + description.checkLeftSecondTextSize(values[4][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[5][0]);

		values[5][1] = player.getSkills()[maxSkill - 1].getSalary().formatDoubleCurrencySymbol();

		textSize = textSize + description.checkLeftSecondTextSize(values[5][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[6][0]);

		values[6][1] = String.valueOf(player.getSkills()[maxSkill - 1].getMatches());

		textSize = textSize + description.checkLeftSecondTextSize(values[6][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[7][0]);

		values[7][1] = String.valueOf(player.getSkills()[maxSkill - 1].getGoals());

		textSize = textSize + description.checkLeftSecondTextSize(values[7][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[8][0]);

		values[8][1] = String.valueOf(player.getSkills()[maxSkill - 1].getAssists());

		textSize = textSize + description.checkLeftSecondTextSize(values[8][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[9][0]);

		if (player.getSkills()[maxSkill - 1].getCards() == 2) {
			cards += " " + cards;
		} else if (player.getSkills()[maxSkill - 1].getCards() == 0) {
			cards = String.valueOf(player.getSkills()[maxSkill - 1].getCards());
		}

		values[9][1] = cards.toString();

		diffrents = player.getSkills()[maxSkill - 1].getCards();
		if (diffrents < 3 && diffrents > 0) {
			description.leftColorText(textSize, values[9][1].length(), ConfigBean.getColorYellowCard());
		} else if (diffrents >= 3) {
			description.leftColorText(textSize, values[9][1].length(), ConfigBean.getColorDecreaseDescription());
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[9][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[10][0]);

		double injury = player.getSkills()[maxSkill - 1].getInjurydays();
		if (injury > 7) {
			values[10][1] = String.format("%.0f %s [%.2f]", new BigDecimal(injury).setScale(0, BigDecimal.ROUND_UP), Messages.getString("injury.days"), injury);
		} else if (injury > 0 && injury < 7) {
			values[10][1] = String.format("%s [%.2f]", Messages.getString("injury.lastDays"), injury);
		} else {
			values[10][1] = String.format("[%.2f]",injury);
		}

		if (player.getSkills()[maxSkill - 1].getInjurydays() > 0) {
			description.leftColorText(textSize, values[10][1].length(), ConfigBean.getColorInjuryFg(), ConfigBean.getColorInjuryBg());
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[10][1]);

		textSize = textSize + description.checkLeftFirstTextSize(values[11][0]);

		if (player.getTransferSell() != null) {
			if (player.getTransferSell().getPrice().toInt() > 0) {
				values[11][1] = player.getTransferSell().getPrice().formatDoubleCurrencySymbol();
			} else {
				values[11][1] = Messages.getString("player.fired");
			}
		} else {
			if (player.getSoldPrice().toInt() > 0) {
				values[11][1] = player.getSoldPrice().formatDoubleCurrencySymbol();
			} else {
				values[11][1] = Messages.getString("player.fired");
			}
		}

		textSize = textSize + description.checkLeftSecondTextSize(values[11][1]);

		for (int i = 0; i < values.length; i++) {
			description.addLeftText(values[i]);

		}

		description.setLeftColor();

		values = new String[11][2];
		values[0][0] = Messages.getString("player.age");
		values[1][0] = Messages.getString("player.form");
		values[2][0] = Messages.getString("player.stamina");
		values[3][0] = Messages.getString("player.pace");
		values[4][0] = Messages.getString("player.technique");
		values[5][0] = Messages.getString("player.passing");
		values[6][0] = Messages.getString("player.keeper");
		values[7][0] = Messages.getString("player.defender");
		values[8][0] = Messages.getString("player.playmaker");
		values[9][0] = Messages.getString("player.scorer");
		values[10][0] = Messages.getString("player.general");

		values[0][1] = String.valueOf(player.getSkills()[maxSkill - 1].getAge());
		values[0][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getAge(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getAge() - player.getSkills()[0].getAge()));

		values[1][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getForm());
		values[1][1] += String.format(" [%d] ", player.getSkills()[maxSkill - 1].getForm());

		values[2][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getStamina());
		values[2][1] += String.format(" [%d %s]", player.getSkills()[maxSkill - 1].getStamina(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getStamina() - player.getSkills()[0].getStamina()));

		values[3][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getPace());
		values[3][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getPace(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPace() - player.getSkills()[0].getPace()));

		values[4][1] = Messages.getString("skill.b" + player.getSkills()[maxSkill - 1].getTechnique());
		values[4][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getTechnique(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getTechnique() - player.getSkills()[0].getTechnique()));

		values[5][1] = Messages.getString("skill.c" + player.getSkills()[maxSkill - 1].getPassing());
		values[5][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getPassing(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPassing() - player.getSkills()[0].getPassing()));

		values[6][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getKeeper());
		values[6][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getKeeper(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getKeeper() - player.getSkills()[0].getKeeper()));

		values[7][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getDefender());
		values[7][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getDefender(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getDefender() - player.getSkills()[0].getDefender()));

		values[8][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getPlaymaker());
		values[8][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getPlaymaker(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getPlaymaker() - player.getSkills()[0].getPlaymaker()));

		values[9][1] = Messages.getString("skill.a" + player.getSkills()[maxSkill - 1].getScorer());
		values[9][1] += String.format(" [%d %s] ", player.getSkills()[maxSkill - 1].getScorer(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getScorer() - player.getSkills()[0].getScorer()));

		values[10][1] = String.format("[%d %s] ", player.getSkills()[maxSkill - 1].getSummarySkill(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill - 1].getSummarySkill() - player.getSkills()[0].getSummarySkill()));

		for (int i = 0; i < values.length; i++) {
			description.addRightText(values[i]);
		}
		description.setRightColor();
	}

	private void setViewComposite(ArrayList<Player> player) {
		setShellMainTableAllPlayersData(player);
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
		// TODO Auto-generated method stub

	}

}
