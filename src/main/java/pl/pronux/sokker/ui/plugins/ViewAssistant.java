package pl.pronux.sokker.ui.plugins;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.actions.AssistantManager;
import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.ISort;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.CursorResources;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.composites.DescriptionDoubleComposite;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.ui.widgets.tables.AssitantPlayersTable;

public class ViewAssistant implements IPlugin, ISort {

	private class Configure implements IViewConfigure {

		private Composite composite;

		private Table confTable;

		private Button defaultButton;

		private boolean init;

		private TreeItem treeItem;

		private boolean changed;

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

						boolean visible = false;
						for (int i = 1; i < table.getColumnCount() - 1; i++) {
							Rectangle rect = item.getBounds(i);
							if (rect.contains(pt)) {
								final int column = i;
								final Text text = new Text(table, SWT.NONE);
								text.setTextLimit(3);
								text.setFont(ConfigBean.getFontTable());

								editor.setEditor(text, item, i);

								text.setText(item.getText(i));

								Listener textListener = new Listener() {
									public void handleEvent(final Event e) {
										switch (e.type) {
										case SWT.FocusOut:
											String temp = item.getText(column);
											if(text.getText().equals("")) {
												text.setText("0");
											}
											item.setText(column, text.getText());
											if (checkSumItem(item) > 100) {
												item.setText(column, temp);
												checkSumItem(item);
											}
											if (!item.getText(column).equals(temp)) {
												changed = true;
											}
											text.dispose();
											break;
										case SWT.Traverse:
											switch (e.detail) {
											case SWT.TRAVERSE_RETURN:

												temp = item.getText(column);
												if(text.getText().equals("")) {
													text.setText("0");
												}
												item.setText(column, text.getText());
												if (checkSumItem(item) > 100) {
													item.setText(column, temp);
													checkSumItem(item);
												}
												if (!item.getText(column).equals(temp)) {
													changed = true;
												}
													break;
												// FALL THROUGH
											case SWT.TRAVERSE_ESCAPE:
												text.dispose();
												e.doit = false;
													break;
											}
											break;
										case SWT.Verify:
											String string = e.text;
											char[] chars = new char[string.length()];
											string.getChars(0, chars.length, chars, 0);
											for (int j = 0; j < chars.length; j++) {
												if (!('0' <= chars[j] && chars[j] <= '9')) {
													e.doit = false;
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
						if (!visible) {
							return;
						}
					}

				}
			});

		}

		public void applyChanges() {
			if (init && changed) {
				try {
					ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));
					Cache.setAssistant(getConfigurationTableData(confTable));
					PlayersManager.updateAssistantData(Cache.getAssistant());
					PlayersManager.calculatePositionForAllPlayer(players, Cache.getAssistant());
					new PlayersManager().updatePlayersPositions(players);
					playersTable.fill(players);
//					composite.getShell().notifyListeners(IEvents.REFRESH_PLAYERS_DESCRIPTION, new Event());
					ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));
				} catch (SQLException e) {
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewAssistant", e);
				}

				changed = false;
			}
		}

		private void checkSum(Table table) {
			for (int j = 0; j < table.getItemCount(); j++) {
				checkSumItem(table.getItem(j));
			}
		}

		private int checkSumItem(TableItem item) {
			int sum;
			Table table;

			table = item.getParent();
			sum = 0;
			for (int i = 1; i < table.getColumnCount() - 1; i++) {
				sum += Integer.valueOf(item.getText(i)).intValue();
			}
			if (sum <= 100) {
				item.setText(table.getColumnCount() - 1, String.valueOf(sum));
			} else {
				item.setText(table.getColumnCount() - 1, Messages.getString("table.error"));
			}

			return sum;
		}

		public void clear() {

		}

		public void dispose() {

		}

		private void fillConfigurationTable(Table table, int[][] data) {
			// Turn off drawing to avoid flicker
			table.setRedraw(false);

			// We remove all the table entries, sort our
			// rows, then add the entries
			table.removeAll();
			for (int i = 0; i < data.length; i++) {
				TableItem item = new TableItem(table, SWT.NONE);

				int c = 0;
				int j = 0;
				item.setText(c++, Messages.getString("assistant.position." + data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
				item.setText(c++, String.valueOf(data[i][j++]));
			}

			checkSum(table);
			// Turn drawing back on
			for (int i = 0; i < table.getColumnCount() - 1; i++) {
				table.getColumn(i).pack();
			}
			table.setRedraw(true);
		}

		public Composite getComposite() {
			return this.composite;
		}

		private int[][] getConfigurationTableData(Table table) {
			int[][] data = new int[table.getItemCount()][table.getColumnCount() - 1];

			for (int i = 0; i < data.length; i++) {
				int c = 0;
				int j = 1;
				data[i][c++] = i + 1;
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
				data[i][c++] = Integer.valueOf(table.getItem(i).getText(j++)).intValue();
			}
			return data;
		}

		public TreeItem getTreeItem() {
			return this.treeItem;
		}

		public void init(final Composite composite) {
			this.composite = composite;
			this.composite.setLayout(new FormLayout());

			FormData formData = new FormData(0,0);
			formData.top = new FormAttachment(50, 0);
			formData.left = new FormAttachment(50, 0);

			Label centerPoint = new Label(composite, SWT.NONE);
			centerPoint.setLayoutData(formData);

			formData = new FormData();
			formData.left = new FormAttachment(centerPoint, 0, SWT.CENTER);
			formData.top = new FormAttachment(0, 20);
			// formData.bottom = new FormAttachment(100,-20);
			// formData.right = new FormAttachment(100, -20);

			confTable = new Table(this.composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
			confTable.setLinesVisible(true);
			confTable.setHeaderVisible(true);
			// configuration.setLayoutData(formData);
			confTable.setLayoutData(formData);
			confTable.setFont(ConfigBean.getFontTable());

			String[] titles = {
					Messages.getString("table.position"),
					Messages.getString("table.form"),
					Messages.getString("table.stamina"),
					Messages.getString("table.pace"),
					Messages.getString("table.technique"),
					Messages.getString("table.passing"),
					Messages.getString("table.keeper"),
					Messages.getString("table.defender"),
					Messages.getString("table.playmaker"),
					Messages.getString("table.scorer"),
					Messages.getString("assistant.sum")
			};

			for (int i = 0; i < titles.length; i++) {
				TableColumn column = new TableColumn(confTable, SWT.NONE);
				column.setText(titles[i]);
				column.setResizable(false);
				column.setResizable(false);
				column.pack();
			}

			for (int i = 1; i < 12; i++) {
				TableItem item = new TableItem(confTable, SWT.NONE);
				item.setText(0, Messages.getString("assistant.position." + i));
				confTable.getColumn(0).pack();
			}
			confTable.pack();



			formData = new FormData();
			formData.left = new FormAttachment(confTable, 0, SWT.CENTER);
			formData.top = new FormAttachment(confTable, 20);

			defaultButton = new Button(this.composite, SWT.NONE);
			defaultButton.setLayoutData(formData);
			defaultButton.setEnabled(false);
			defaultButton.setFont(ConfigBean.getFontTable());
			defaultButton.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					MessageBox msg = new MessageBox(confTable.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					msg.setText(Messages.getString("message.WARNING"));
					msg.setMessage(Messages.getString("message.setDefaults"));

					if (msg.open() == SWT.YES) {
						ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));
						int[][] data = new int[confTable.getItemCount()][];
						for (int i = 0; i < confTable.getItemCount(); i++) {
							String[] temp = SettingsHandler.getDefaultProperties().getProperty("assistant.default." + (i + 1)).split(";");
							data[i] = new int[temp.length + 1];
							data[i][0] = i + 1;
							for (int j = 0; j < temp.length; j++) {
								data[i][j + 1] = Integer.valueOf(temp[j]).intValue();
							}
						}

						try {
							Cache.setAssistant(data);
							PlayersManager.updateAssistantData(data);
							PlayersManager.calculatePositionForAllPlayer(players, data);
							new PlayersManager().updatePlayersPositions(players);
							fillConfigurationTable(confTable, data);

							playersTable.fill(players);

//							composite.getShell().notifyListeners(IEvents.REFRESH_PLAYERS_DESCRIPTION, new Event());
							ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));
						} catch (SQLException e) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewAssistant", e);
						}
					}
				}
			});
			this.treeItem.setText(Messages.getString("tree.ViewAssistant"));
			this.defaultButton.setText(Messages.getString("button.default.system"));
			this.defaultButton.pack();


		}

		public void restoreDefaultChanges() {
			if (init && changed) {
				try {

					ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_WAIT));

					SQLSession.connect();
					int[][] data = AssistantManager.getAssistantData();

					Cache.setAssistant(data);
					PlayersManager.calculatePositionForAllPlayer(players, data);
					new PlayersManager().updatePlayersPositions(players);
					fillConfigurationTable(confTable, data);

					playersTable.fill(players);

//					this.composite.getShell().notifyListeners(IEvents.REFRESH_PLAYERS_DESCRIPTION, new Event());
					ViewerHandler.getViewer().setCursor(CursorResources.getCursor(SWT.CURSOR_ARROW));

					changed = false;
				} catch (SQLException e) {
					try {
						SQLSession.close();
					} catch (SQLException e1) {
					}
					new BugReporter(composite.getDisplay()).openErrorMessage("ViewAssitant", e);
				}
			}
		}

		public void setSettings(SokkerViewerSettings sokkerViewerSettings) {

		}

		public void setTreeItem(TreeItem treeItem) {
			this.treeItem = treeItem;

		}

		public void set() {

			init = true;

			defaultButton.setEnabled(true);
			// confTable.setMenu(menuClear);
			fillConfigurationTable(confTable, data);
			addTableEditor(confTable);
			confTableListener = new Listener() {

				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.MouseDown:
						if (event.button == 3) {
							// // Rectangle clientArea = allCoachesTable.getClientArea();
							// Point pt = new Point(event.x, event.y);
							// TableItem item = confTable.getItem(pt);
							// if (item != null) {
							// confTable.setMenu(menuPopUp);
							// confTable.getMenu().setVisible(true);
							// } else {
							// confTable.setMenu(menuClear);
							// }
						}
						break;
					}
				}
			};
			confTable.addListener(SWT.MouseDown, confTableListener);
			confTable.addListener(SWT.Selection, confTableListener);

		}

	}

	private Composite composite;

	private Listener confTableListener;

	private int[][] data;

	private DescriptionDoubleComposite descriptionComposite;

	private FormData descriptionFormData;

	private ArrayList<Player> players;

	private AssitantPlayersTable playersTable;

	private Listener playersTableListener;

	private FormData sashFormData;

	private Sash sashHorizontal;

	private TreeItem treeItem;

	private FormData viewFormData;

	private void setPlayersView() {

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);

		sashHorizontal = new Sash(composite, SWT.HORIZONTAL | SWT.NONE);

		sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 200);
		sashFormData.right = new FormAttachment(100, 0);
		sashFormData.left = new FormAttachment(0, 0);

		sashHorizontal.setLayoutData(sashFormData);
		sashHorizontal.setVisible(true);

		sashHorizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((FormData) sashHorizontal.getLayoutData()).top = new FormAttachment(0, event.y);
				sashHorizontal.getParent().layout();
			}
		});

		viewFormData = new FormData();
		viewFormData.top = new FormAttachment(sashHorizontal, 0);
		viewFormData.right = new FormAttachment(100, 0);
		viewFormData.left = new FormAttachment(0, 0);
		viewFormData.bottom = new FormAttachment(100, 0);

		descriptionFormData = new FormData();
		descriptionFormData.top = new FormAttachment(0, 0);
		descriptionFormData.right = new FormAttachment(100, 0);
		descriptionFormData.left = new FormAttachment(0, 0);
		descriptionFormData.bottom = new FormAttachment(sashHorizontal, 0);

		descriptionComposite = new DescriptionDoubleComposite(composite, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);
		descriptionComposite.setFont(ConfigBean.getFontDescription());

		descriptionComposite.setLeftDescriptionStringFormat("%-20s%-15s\r\n");
		descriptionComposite.setLeftFirstColumnSize(20);
		descriptionComposite.setLeftSecondColumnSize(15);

		descriptionComposite.setRightDescriptionStringFormat("%-20s%-15s\r\n");
		descriptionComposite.setRightFirstColumnSize(20);
		descriptionComposite.setRightSecondColumnSize(15);

		playersTable = new AssitantPlayersTable(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		playersTable.setLayoutData(viewFormData);
	}

	private void addPopupMenu() {
		// added popup menu
		// menuPopUp = new Menu(confTable.getShell(), SWT.POP_UP);
		// MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		// menuItem.setText(langProperties.getProperty("popup.setColor"));
		// menuItem.addListener(SWT.Selection, new Listener() {
		// public void handleEvent(Event e) {
		// ColorDialog crDialog = new ColorDialog(confTable.getShell());
		// crDialog.setRGB(currentItem.getBackground(0).getRGB());
		// RGB rgb = crDialog.open();
		// if (rgb != null) {
		// currentItem.setBackground(0, new Color(confTable.getDisplay(), rgb));
		// okButton.setEnabled(true);
		// }
		// }
		// });

//		menuClear = new Menu(composite.getShell(), SWT.POP_UP);

	}

	public void clear() {

	}

	private void comparePlayers(DescriptionDoubleComposite description, Player p1, Player p2) {
		description.clearAll();
		String[][] valuesLeft = new String[13][2];
		valuesLeft[0][0] = Messages.getString("player.name");
		valuesLeft[1][0] = Messages.getString("player.surname");
		valuesLeft[2][0] = Messages.getString("player.form");
		valuesLeft[3][0] = Messages.getString("player.stamina");
		valuesLeft[4][0] = Messages.getString("player.pace");
		valuesLeft[5][0] = Messages.getString("player.technique");
		valuesLeft[6][0] = Messages.getString("player.passing");
		valuesLeft[7][0] = Messages.getString("player.keeper");
		valuesLeft[8][0] = Messages.getString("player.defender");
		valuesLeft[9][0] = Messages.getString("player.playmaker");
		valuesLeft[10][0] = Messages.getString("player.scorer");
		valuesLeft[11][0] = Messages.getString("player.general");
		valuesLeft[12][0] = Messages.getString("player.position");

		String[][] valuesRight = new String[13][2];
		valuesRight[0][0] = Messages.getString("player.name");
		valuesRight[1][0] = Messages.getString("player.surname");
		valuesRight[2][0] = Messages.getString("player.form");
		valuesRight[3][0] = Messages.getString("player.stamina");
		valuesRight[4][0] = Messages.getString("player.pace");
		valuesRight[5][0] = Messages.getString("player.technique");
		valuesRight[6][0] = Messages.getString("player.passing");
		valuesRight[7][0] = Messages.getString("player.keeper");
		valuesRight[8][0] = Messages.getString("player.defender");
		valuesRight[9][0] = Messages.getString("player.playmaker");
		valuesRight[10][0] = Messages.getString("player.scorer");
		valuesRight[11][0] = Messages.getString("player.general");
		valuesRight[12][0] = Messages.getString("player.position");

		int textLeftSize = 0;
		int textRightSize = 0;
		int maxSkill1 = p1.getSkills().length - 1;
		int maxSkill2 = p2.getSkills().length - 1;
		int c = 0;
		int j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = p1.getName();
		valuesRight[c][j] = p2.getName();

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = p1.getSurname();
		valuesRight[c][j] = p2.getSurname();

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getForm());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getForm() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getForm() - p2.getSkills()[maxSkill2].getForm()) + ")";

		valuesRight[c][j] = Messages.getString("skill.b" + p2.getSkills()[maxSkill2].getForm());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getForm() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getForm() - p1.getSkills()[maxSkill1].getForm()) + ")";

		if (p1.getSkills()[maxSkill1].getForm() > p2.getSkills()[maxSkill2].getForm()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getForm() < p2.getSkills()[maxSkill2].getForm()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getStamina());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getStamina() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getStamina() - p2.getSkills()[maxSkill2].getStamina()) + ")";

		valuesRight[c][j] = Messages.getString("skill.b" + p2.getSkills()[maxSkill2].getStamina());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getStamina() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getStamina() - p1.getSkills()[maxSkill1].getStamina()) + ")";

		if (p1.getSkills()[maxSkill1].getStamina() > p2.getSkills()[maxSkill2].getStamina()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getStamina() < p2.getSkills()[maxSkill2].getStamina()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getPace());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPace() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getPace() - p2.getSkills()[maxSkill2].getPace()) + ")";

		valuesRight[c][j] = Messages.getString("skill.b" + p2.getSkills()[maxSkill2].getPace());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getPace() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getPace() - p1.getSkills()[maxSkill1].getPace()) + ")";

		if (p1.getSkills()[maxSkill1].getPace() > p2.getSkills()[maxSkill2].getPace()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getPace() < p2.getSkills()[maxSkill2].getPace()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getTechnique());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getTechnique() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getTechnique() - p2.getSkills()[maxSkill2].getTechnique()) + ")";

		valuesRight[c][j] = Messages.getString("skill.b" + p2.getSkills()[maxSkill2].getTechnique());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getTechnique() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getTechnique() - p1.getSkills()[maxSkill1].getTechnique()) + ")";

		if (p1.getSkills()[maxSkill1].getTechnique() > p2.getSkills()[maxSkill2].getTechnique()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getTechnique() < p2.getSkills()[maxSkill2].getTechnique()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.c" + p1.getSkills()[maxSkill1].getPassing());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPassing() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getPassing() - p2.getSkills()[maxSkill2].getPassing()) + ")";

		valuesRight[c][j] = Messages.getString("skill.c" + p2.getSkills()[maxSkill2].getPassing());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getPassing() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getPassing() - p1.getSkills()[maxSkill1].getPassing()) + ")";

		if (p1.getSkills()[maxSkill1].getPassing() > p2.getSkills()[maxSkill2].getPassing()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getPassing() < p2.getSkills()[maxSkill2].getPassing()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getKeeper());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getKeeper() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getKeeper() - p2.getSkills()[maxSkill2].getKeeper()) + ")";

		valuesRight[c][j] = Messages.getString("skill.a" + p2.getSkills()[maxSkill2].getKeeper());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getKeeper() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getKeeper() - p1.getSkills()[maxSkill1].getKeeper()) + ")";

		if (p1.getSkills()[maxSkill1].getKeeper() > p2.getSkills()[maxSkill2].getKeeper()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getKeeper() < p2.getSkills()[maxSkill2].getKeeper()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getDefender());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getDefender() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getDefender() - p2.getSkills()[maxSkill2].getDefender()) + ")";

		valuesRight[c][j] = Messages.getString("skill.a" + p2.getSkills()[maxSkill2].getDefender());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getDefender() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getDefender() - p1.getSkills()[maxSkill1].getDefender()) + ")";

		if (p1.getSkills()[maxSkill1].getDefender() > p2.getSkills()[maxSkill2].getDefender()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getDefender() < p2.getSkills()[maxSkill2].getDefender()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getPlaymaker());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPlaymaker() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getPlaymaker() - p2.getSkills()[maxSkill2].getPlaymaker()) + ")";

		valuesRight[c][j] = Messages.getString("skill.a" + p2.getSkills()[maxSkill2].getPlaymaker());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getPlaymaker() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getPlaymaker() - p1.getSkills()[maxSkill1].getPlaymaker()) + ")";

		if (p1.getSkills()[maxSkill1].getPlaymaker() > p2.getSkills()[maxSkill2].getPlaymaker()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getPlaymaker() < p2.getSkills()[maxSkill2].getPlaymaker()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getScorer());
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getScorer() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getScorer() - p2.getSkills()[maxSkill2].getScorer()) + ")";

		valuesRight[c][j] = Messages.getString("skill.a" + p2.getSkills()[maxSkill2].getScorer());
		valuesRight[c][j] += " [" + p2.getSkills()[maxSkill2].getScorer() + "] " + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getScorer() - p1.getSkills()[maxSkill1].getScorer()) + ")";

		if (p1.getSkills()[maxSkill1].getScorer() > p2.getSkills()[maxSkill2].getScorer()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getScorer() < p2.getSkills()[maxSkill2].getScorer()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = "[" + p1.getSkills()[maxSkill1].getSummarySkill() + "]" + "(" + SVNumberFormat.formatIntegerWithSignZero(p1.getSkills()[maxSkill1].getSummarySkill() - p2.getSkills()[maxSkill2].getSummarySkill()) + ")";

		valuesRight[c][j] = "[" + p2.getSkills()[maxSkill2].getSummarySkill() + "]" + "(" + SVNumberFormat.formatIntegerWithSignZero(p2.getSkills()[maxSkill2].getSummarySkill() - p1.getSkills()[maxSkill1].getSummarySkill()) + ")";

		if (p1.getSkills()[maxSkill1].getSummarySkill() > p2.getSkills()[maxSkill2].getSummarySkill()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorIncreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorDecreaseDescription());
		} else if (p1.getSkills()[maxSkill1].getSummarySkill() < p2.getSkills()[maxSkill2].getSummarySkill()) {
			description.leftColorText(textLeftSize, valuesLeft[c][j].length(), ConfigBean.getColorDecreaseDescription());
			description.rightColorText(textRightSize, valuesRight[c][j].length(), ConfigBean.getColorIncreaseDescription());
		}

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightFirstTextSize(valuesRight[c][j++]);

		valuesLeft[c][j] = Messages.getString("assistant.position." + p1.getPosition());

		valuesRight[c][j] = Messages.getString("assistant.position." + p2.getPosition());

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c][j]);
		textRightSize = textRightSize + description.checkRightSecondTextSize(valuesRight[c++][j]);

		for (int i = 0; i < valuesLeft.length; i++) {
			description.addLeftText(valuesLeft[i]);
		}
		for (int i = 0; i < valuesRight.length; i++) {
			description.addRightText(valuesRight[i]);
		}

		description.setLeftColor();
		description.setRightColor();
	}

	public void dispose() {

	}

	public Composite getComposite() {
		return composite;
	}

	public IViewConfigure getConfigureComposite() {
		return new Configure();
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoAssistant"); //$NON-NLS-1$;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());

		setPlayersView();
		addPopupMenu();

		composite.layout(true);
	}

//	private void resetPlayersPosition(ArrayList<Player> players2) {
//		for (Iterator itr = players.iterator(); itr.hasNext();) {
//			Player player = (Player) itr.next();
//			player.setPositionTable(new double[] {
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0,
//					0.0
//			});
//		}
//	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// this.confProperties = confProperties;
	}

	public void setLayoutView(FormData formData) {
		this.composite.setLayoutData(formData);
	}

	private void setPlayerInfo(DescriptionDoubleComposite description, Player p1) {
		description.clearAll();
		String[][] valuesLeft = new String[13][2];
		valuesLeft[0][0] = Messages.getString("player.name");
		valuesLeft[1][0] = Messages.getString("player.surname");
		valuesLeft[2][0] = Messages.getString("player.form");
		valuesLeft[3][0] = Messages.getString("player.stamina");
		valuesLeft[4][0] = Messages.getString("player.pace");
		valuesLeft[5][0] = Messages.getString("player.technique");
		valuesLeft[6][0] = Messages.getString("player.passing");
		valuesLeft[7][0] = Messages.getString("player.keeper");
		valuesLeft[8][0] = Messages.getString("player.defender");
		valuesLeft[9][0] = Messages.getString("player.playmaker");
		valuesLeft[10][0] = Messages.getString("player.scorer");
		valuesLeft[11][0] = Messages.getString("player.general");
		valuesLeft[12][0] = Messages.getString("player.position");

		int textLeftSize = 0;
		int maxSkill1 = p1.getSkills().length - 1;
		int c = 0;
		int j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = p1.getName();

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = p1.getSurname();

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getForm()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getForm() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getStamina()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getStamina() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getPace()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPace() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.b" + p1.getSkills()[maxSkill1].getTechnique()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getTechnique() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.c" + p1.getSkills()[maxSkill1].getPassing()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPassing() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;
		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getKeeper()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getKeeper() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getDefender()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getDefender() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getPlaymaker()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getPlaymaker() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("skill.a" + p1.getSkills()[maxSkill1].getScorer()).toString();
		valuesLeft[c][j] += " [" + p1.getSkills()[maxSkill1].getScorer() + "] ";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = "[" + p1.getSkills()[maxSkill1].getSummarySkill() + "]";

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		j = 0;

		textLeftSize = textLeftSize + description.checkLeftFirstTextSize(valuesLeft[c][j++]);

		valuesLeft[c][j] = Messages.getString("assistant.position." + p1.getPosition());

		textLeftSize = textLeftSize + description.checkLeftSecondTextSize(valuesLeft[c++][j]);

		for (int i = 0; i < valuesLeft.length; i++) {
			description.addLeftText(valuesLeft[i]);
		}
	}

	public void setSvBean(SvBean svBean) {

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewAssistant"));
		this.treeItem.setImage(ImageResources.getImageResources("briefcase.png"));
	}

	public void set() {
		players = Cache.getPlayers();

//		resetPlayersPosition(players);

		data = Cache.getAssistant();

//		Utils.calculatePositionForAllPlayer(players, data);
		
		playersTable.fill(players);

		playersTableListener = new Listener() {

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown:
					if (event.button == 3) {
						// // Rectangle clientArea = allCoachesTable.getClientArea();
						// Point pt = new Point(event.x, event.y);
						// TableItem item = confTable.getItem(pt);
						// if (item != null) {
						// confTable.setMenu(menuPopUp);
						// confTable.getMenu().setVisible(true);
						// } else {
						// confTable.setMenu(menuClear);
						// }
					}
					break;
				case SWT.Selection:
					if (playersTable.getSelectionCount() == 1) {
						TableItem[] items = playersTable.getSelection();
						if (items.length == 1) {
							if (items[0] != null) {
								setPlayerInfo(descriptionComposite, (Player) items[0].getData("person"));
							}
						}
					}
					if (playersTable.getSelectionCount() == 2) {

						TableItem[] items = playersTable.getSelection();
						if (items.length == 2) {
							if (items[0] != null && items[1] != null) {
								comparePlayers(descriptionComposite, (Player) items[0].getData("person"), (Player) items[1].getData("person"));
							}
						}
					} else if (playersTable.getSelectionCount() > 2 || playersTable.getSelectionCount() < 1) {
						descriptionComposite.clearAll();
					}
					break;
				}
			}
		};
		playersTable.addListener(SWT.Selection, playersTableListener);
	}

	public void reload() {
		// TODO Auto-generated method stub
	}

}
