package pl.pronux.sokker.ui.plugins;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.comparators.PlayerComparator;
import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.downloader.HTMLDownloader;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.CountryXmlParser;
import pl.pronux.sokker.downloader.xml.parsers.PlayersXmlParser;
import pl.pronux.sokker.downloader.xml.parsers.TeamXmlParser;
import pl.pronux.sokker.enums.Language;
import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.model.PersonInterface;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.LangResources;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.utils.file.PropertiesChecker;
import pl.pronux.sokker.utils.security.Crypto;

public class ViewNT implements IPlugin {

	private final String SECRET_KEY_128 = "vsXFsdVfeGFTyMdpOVhY4A==";

	private final static String SYMMETRIC_KEY_TYPE = "Rijndael";

	private final static String SYMMETRIC_KEY_SEQUENCE = "Rijndael/CBC/NoPadding";

	private final static String NT_DATABASE_ADDRESS = "http://baza.sokkerviewer.net/";

	private final static String NT_DATABASE_ADDRESS_ADD = NT_DATABASE_ADDRESS + "sv_addplayer.php";

	private final static String NT_DATABASE_ADDRESS_HIDE = NT_DATABASE_ADDRESS + "sv_hideplayer.php";

	private final static String NT_DATABASE_ADDRESS_SHOW = NT_DATABASE_ADDRESS + "sv_showplayer.php";

	private final static String NT_DATABASE_ADDRESS_STATUS = NT_DATABASE_ADDRESS + "sv_statusplayer.php";

	private Table table;

	private PlayerComparator comparator;

	private ArrayList<Player> players;

	private Shell shell;

	private Composite composite;

	private Menu menuPopUp;

	private Menu menuClear;

	private CLabel status;

	private Button buttonGet;

	private Text loginText;

	private Text passwordText;

	private Label passwordLabel;

	private Label loginLabel;

	private TreeItem _treeItem;

	protected Club club;

	private Combo trainingCombo;

	private Group loginGroup;

	private Group pluginGroup;

	private Label trainingLabel;

	private Label languageLabel;

	private Combo languageCombo;

	// private Group notePlugin;
	//
	// private Text note;

	private Shell noteShell;

	private Label charLimit;

	private StyledText note;

	private Monitor _monitor;

	private SokkerViewerSettings settings;

	public ViewNT() {

	}

	public void clear() {
		players = new ArrayList<Player>();
		loginText.setText("");
		passwordText.setText("");
		status.setText("");
	}

	public void set() {
		// fillTable(table, players, comparator);

		loginText.setText(settings.getUsername());
		buttonGet.setEnabled(true);

		_treeItem.setImage(FlagsResources.getFlag(Cache.getClub().getCountry()));
	}

	public void init(Composite composite) {
		this.composite = composite;
		composite.setLayout(new FormLayout());
		addView();
		addPopupMenu();
		composite.layout();
		addNoteBox(composite.getShell());
	}

	private void addNoteBox(Shell shell) {

		noteShell = new Shell(shell);
		noteShell.setLayout(new FormLayout());

		noteShell.addListener(SWT.Close, new Listener() {

			public void handleEvent(Event event) {
				noteShell.setVisible(false);
				event.doit = false;
			}

		});

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		// formData.bottom = new FormAttachment(100, -40);
		// formData.right = new FormAttachment(100, -5);
		formData.width = 400;
		formData.height = 170;

		Group notePlugin = new Group(noteShell, SWT.NONE);
		notePlugin.setLayout(new FormLayout());
		notePlugin.setLayoutData(formData);
		notePlugin.setText(Messages.getString("viewnt.settings.note"));

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -30);
		formData.right = new FormAttachment(100, -5);

		note = new StyledText(notePlugin, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		note.setLayoutData(formData);
		note.setTextLimit(500);
		note.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event arg0) {
				charLimit.setText(String.format("%s: %d", Messages.getString("viewnt.message.limit.label"), (note.getTextLimit() - note.getText().length())));
			}
		});

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(note, 5);
		formData.right = new FormAttachment(100, -5);

		charLimit = new Label(notePlugin, SWT.NONE);
		charLimit.setLayoutData(formData);
		charLimit.setText(String.format("%s: %d", Messages.getString("viewnt.message.limit.label"), note.getTextLimit()));
		charLimit.pack();

		formData = new FormData();
		formData.width = 50;
		formData.top = new FormAttachment(notePlugin, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(50, -5);

		Button okButton = new Button(noteShell, SWT.NONE);
		okButton.setText(Messages.getString("button.ok"));
		okButton.setLayoutData(formData);
		okButton.pack();

		okButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (note.getText().length() > 500) {
					return;
				}
				if (noteShell.getData(Player.class.getName()) instanceof Player) {
					Player player = (Player) noteShell.getData(Player.class.getName());

					if (player != null) {
						int training_type = trainingCombo.getSelectionIndex();
						String parameters = "";

						String comment = note.getText();

						comment = comment.replaceAll(";;", "-");

						try {
							SecretKeySpec skey = Crypto.convertByteArrayToSymmetricKey(Crypto.decodeBase64(SECRET_KEY_128), SYMMETRIC_KEY_TYPE);

							parameters = Crypto.encodeBase64(Crypto
								.encryptSymmetric((player.getId() + ";;" + player.getCountryfrom() + ";;" + player.getName() + ";;" + player.getSurname()
												   + ";;" + club.getId() + ";;" + club.getClubName().get(0).getName() + ";;" + player.getSkills()[0].getAge()
												   + ";;" + player.getSkills()[0].getValue().toInt() + ";;" + player.getSkills()[0].getForm() + ";;"
												   + player.getSkills()[0].getStamina() + ";;" + player.getSkills()[0].getPace() + ";;"
												   + player.getSkills()[0].getTechnique() + ";;" + player.getSkills()[0].getPassing() + ";;"
												   + player.getSkills()[0].getKeeper() + ";;" + player.getSkills()[0].getDefender() + ";;"
												   + player.getSkills()[0].getPlaymaker() + ";;" + player.getSkills()[0].getScorer() + ";;" + training_type
												   + ";;" + comment).getBytes("UTF-8"), skey, SYMMETRIC_KEY_SEQUENCE));
							parameters = URLEncoder.encode(parameters, "UTF-8");
						} catch (InvalidKeyException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT", e2);
						} catch (NoSuchAlgorithmException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT NoSuchAlgorithmException", e2);
						} catch (NoSuchPaddingException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT NoSuchPaddingException", e2);
						} catch (IllegalBlockSizeException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IllegalBlockSizeException", e2);
						} catch (BadPaddingException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT BadPaddingException", e2);
						} catch (InvalidAlgorithmParameterException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT InvalidAlgorithmParameterException", e2);
						} catch (ShortBufferException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT ShortBufferException", e2);
						} catch (IOException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IOException", e2);
						} catch (BadArgumentException e2) {
							new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IOException", e2);
						}
						String params = "parser=" + parameters;
						// params = params.replaceAll("\\+", "%2B");

						String url = NT_DATABASE_ADDRESS_ADD;
						String referer = url;

						try {
							ProxySettings proxySettings = settings.getProxySettings();
							HTMLDownloader htmlDownloader;
							htmlDownloader = new HTMLDownloader(proxySettings);
							String value = htmlDownloader.postDataToPage(url, params, referer).replaceAll("[^0-9]", "");
							if (value.equals("1")) {
								status.setText(Messages.getString("viewnt.status.added") + " " + player.getName() + " " + player.getSurname());
								MessageBox msg = new MessageBox(noteShell, SWT.OK | SWT.ICON_INFORMATION);
								msg.setMessage(Messages.getString("viewnt.status.added") + " " + player.getName() + " " + player.getSurname());
								if (msg.open() == SWT.OK) {
									noteShell.setVisible(false);
								} else {
									noteShell.setVisible(false);
								}
							} else if (value.equals("2")) {
								status.setText(Messages.getString("viewnt.status.updated") + " " + player.getName() + " " + player.getSurname());
								MessageBox msg = new MessageBox(noteShell, SWT.OK | SWT.ICON_INFORMATION);
								msg.setMessage(Messages.getString("viewnt.status.updated") + " " + player.getName() + " " + player.getSurname());
								if (msg.open() == SWT.OK) {
									noteShell.setVisible(false);
								} else {
									noteShell.setVisible(false);
								}
							} else {
								status.setText(Messages.getString("viewnt.status.error") + " " + value);
								MessageBox msg = new MessageBox(noteShell, SWT.OK | SWT.ICON_ERROR);
								msg.setMessage(Messages.getString("viewnt.status.error") + " " + value);
								if (msg.open() == SWT.OK) {
									noteShell.setVisible(false);
								} else {
									noteShell.setVisible(false);
								}
							}
						} catch (IOException e1) {
							new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.file.ioexception"), e1);
						}
					}
				}
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(notePlugin, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.left = new FormAttachment(50, 5);

		Button cancelButton = new Button(noteShell, SWT.NONE);
		cancelButton.setText(Messages.getString("button.cancel"));
		cancelButton.setLayoutData(formData);
		cancelButton.pack();

		cancelButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				noteShell.setVisible(false);
			}

		});
		noteShell.pack();

		Rectangle shellRect = noteShell.getBounds();
		Rectangle displayRect = noteShell.getDisplay().getPrimaryMonitor().getBounds();
		int x = (displayRect.width - shellRect.width) / 2;
		int y = (displayRect.height - shellRect.height) / 2;
		noteShell.setLocation(x, y);

	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public void setTreeItem(TreeItem treeItem) {
		_treeItem = treeItem;
		_treeItem.setText(Messages.getString("tree.ViewNT"));

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if (item != null && item.equals(_treeItem)) {
					ViewerHandler.getViewer().setDefaultButton(buttonGet);
				}
			}
		});
	}

	public Composite getComposite() {
		return composite;
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoNT");
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public void translateWidgets(LangResources langResource) {

		languageLabel.setText(langResource.getString("confShell.langtype"));
		languageLabel.pack();

		// notePlugin.setText(langProperties.getProperty("viewnt.settings.note"));

		menuPopUp.getItem(0).setText(langResource.getString("popup.nt.export"));
		menuPopUp.getItem(2).setText(langResource.getString("viewnt.button.hide"));
		menuPopUp.getItem(3).setText(langResource.getString("viewnt.button.show"));
		menuPopUp.getItem(5).setText(langResource.getString("viewnt.button.info"));

		loginGroup.setText(langResource.getString("viewnt.settings.login"));
		passwordLabel.setText(langResource.getString("confShell.skpassword"));
		loginLabel.setText(langResource.getString("confShell.sklogin"));

		trainingLabel.setText(langResource.getString("training.type"));
		trainingLabel.pack();

		trainingCombo.removeAll();
		for (int i = 1; i < 9; i++) {
			trainingCombo.add(langResource.getString("training.type." + i));
		}
		trainingCombo.setText(trainingCombo.getItem(0));

		pluginGroup.setText(langResource.getString("viewnt.settings.plugin"));

		_treeItem.setText(langResource.getString("tree.ViewNT"));

		String[] columns = { "", langResource.getString("table.name"), langResource.getString("table.surname"), langResource.getString("table.value"),
							langResource.getString("table.salary"), langResource.getString("table.age"), langResource.getString("table.form"),
							langResource.getString("table.stamina"), langResource.getString("table.pace"), langResource.getString("table.technique"),
							langResource.getString("table.passing"), langResource.getString("table.keeper"), langResource.getString("table.defender"),
							langResource.getString("table.playmaker"), langResource.getString("table.scorer") };

		for (int j = 0; j < columns.length; j++) {
			table.getColumn(j).setText(columns[j]);
			table.getColumn(j).pack();
		}

		buttonGet.setText(langResource.getString("button.import"));
		buttonGet.pack();

	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	private void addPopupMenu() {
		// added popup menu
		menuPopUp = new Menu(composite.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("popup.nt.export"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TableItem[] items = table.getSelection();

				if (items.length == 1) {

					Player player = (Player) items[0].getData(Player.class.getName());
					player.setNt(true);

					noteShell.setText(player.getSurname() + " " + player.getName());
					noteShell.setData(Player.class.getName(), player);
					noteShell.open();

				}

			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("viewnt.button.hide"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TableItem[] items = table.getSelection();
				if (items.length == 1) {
					PersonInterface player = (PersonInterface) items[0].getData(Player.class.getName());
					hidePlayer(player);
				}
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("viewnt.button.show"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TableItem[] items = table.getSelection();
				if (items.length == 1) {
					PersonInterface player = (PersonInterface) items[0].getData(Player.class.getName());
					showPlayer(player);
				}
			}
		});

		menuItem = new MenuItem(menuPopUp, SWT.SEPARATOR);

		menuItem = new MenuItem(menuPopUp, SWT.PUSH);
		menuItem.setText(Messages.getString("viewnt.button.info"));
		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				TableItem[] items = table.getSelection();
				if (items.length == 1) {
					PersonInterface player = (PersonInterface) items[0].getData(Player.class.getName());
					infoPlayer(player);
				}
			}

		});
		menuClear = new Menu(composite.getShell(), SWT.POP_UP);
	}

	private void infoPlayer(PersonInterface player) {
		String info = "";
		String response = requestPlayer(player, NT_DATABASE_ADDRESS_STATUS);
		if (response.contains(";;")) {
			status.setText("");
			String[] table = response.split(";;");
			info += Messages.getString("viewnt.info.date.modification") + " " + table[0] + "\n";
			info += Messages.getString("viewnt.info.visible") + " ";
			if (table[1].replaceAll("[^0-9]", "").equals("1")) {
				info += Messages.getString("viewnt.info.visible.yes");
			} else {
				info += Messages.getString("viewnt.info.visible.no");
			}
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_INFORMATION);
			msg.setMessage(info);
			msg.open();
		} else {
			response = response.replaceAll("[^0-9]", "");
			if (response.equals("1")) {
				status.setText(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
				MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_WARNING);
				msg.setMessage(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
				msg.open();
			} else {
				status.setText(Messages.getString("viewnt.status.error") + " " + response);
				MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
				msg.setMessage(Messages.getString("viewnt.status.error") + " " + response);
				msg.open();
			}
		}

	}

	private void showPlayer(PersonInterface player) {
		String response = requestPlayer(player, NT_DATABASE_ADDRESS_SHOW).replaceAll("[^0-9]", "");
		if (response.equals("1")) {
			status.setText(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_WARNING);
			msg.setMessage(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
			msg.open();
		} else if (response.equals("2")) {
			status.setText(Messages.getString("viewnt.status.show") + " " + player.getName() + " " + player.getSurname());
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_INFORMATION);
			msg.setMessage(Messages.getString("viewnt.status.show") + " " + player.getName() + " " + player.getSurname());
			msg.open();
		} else {
			status.setText(Messages.getString("viewnt.status.error") + " " + response);
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
			msg.setMessage(Messages.getString("viewnt.status.error") + " " + response);
			msg.open();
		}
	}

	private String requestPlayer(PersonInterface player, String url) {
		String parameters = "";
		try {

			ProxySettings proxySettings = settings.getProxySettings();
			HTMLDownloader htmlDownloader;
			htmlDownloader = new HTMLDownloader(proxySettings);

			SecretKeySpec skey = Crypto.convertByteArrayToSymmetricKey(Crypto.decodeBase64(SECRET_KEY_128), SYMMETRIC_KEY_TYPE);

			parameters = Crypto.encodeBase64(Crypto.encryptSymmetric((player.getId() + ";;").getBytes("UTF-8"), skey, SYMMETRIC_KEY_SEQUENCE));
			parameters = URLEncoder.encode(parameters, "UTF-8");
			String params = "parser=" + parameters;
			String referer = url;

			String value = htmlDownloader.postDataToPage(url, params, referer);
			return value;
			// if (value.equals("1")) {
			// status.setText(langResource.getString("viewnt.status.hide") + " "
			// +
			// player.getName() + " " + player.getSurname());
			// } else {
			// status.setText(langResource.getString("viewnt.status.error") + "
			// " +
			// value);
			// }
		} catch (InvalidKeyException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT", e2);
		} catch (NoSuchAlgorithmException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT NoSuchAlgorithmException", e2);
		} catch (NoSuchPaddingException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT NoSuchPaddingException", e2);
		} catch (IllegalBlockSizeException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IllegalBlockSizeException", e2);
		} catch (BadPaddingException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT BadPaddingException", e2);
		} catch (InvalidAlgorithmParameterException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT InvalidAlgorithmParameterException", e2);
		} catch (ShortBufferException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT ShortBufferException", e2);
		} catch (IOException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IOException", e2);
		} catch (BadArgumentException e2) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewNT IOException", e2);
		}
		return "-1";

	}

	private void hidePlayer(PersonInterface player) {
		String response = requestPlayer(player, NT_DATABASE_ADDRESS_HIDE).replaceAll("[^0-9]", "");
		if (response.equals("1")) {
			status.setText(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_WARNING);
			msg.setMessage(Messages.getString("viewnt.status.exist.no") + " " + player.getName() + " " + player.getSurname());
			msg.open();
		} else if (response.equals("2")) {
			status.setText(Messages.getString("viewnt.status.hide") + " " + player.getName() + " " + player.getSurname());
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_INFORMATION);
			msg.setMessage(Messages.getString("viewnt.status.hide") + " " + player.getName() + " " + player.getSurname());
			msg.open();
		} else {
			status.setText(Messages.getString("viewnt.status.error") + " " + response);
			MessageBox msg = new MessageBox(composite.getShell(), SWT.OK | SWT.ICON_ERROR);
			msg.setMessage(Messages.getString("viewnt.status.error") + " " + response);
			msg.open();
		}
	}

	// private void addTableEditor(final Table table) {
	// final TableEditor editor = new TableEditor(table);
	// editor.horizontalAlignment = SWT.LEFT;
	// editor.grabHorizontal = true;
	// table.addListener(SWT.MouseDown, new Listener() {
	// public void handleEvent(Event event) {
	// Rectangle clientArea = table.getClientArea();
	// Point pt = new Point(event.x, event.y);
	// final TableItem item = table.getItem(pt);
	// if (item != null) {
	//
	// boolean visible = false;
	// for (int i = table.getColumnCount() - 1; i < table.getColumnCount() ;
	// i++)
	// {
	// Rectangle rect = item.getBounds(i);
	// if (rect.contains(pt)) {
	// // final int column = i;
	// final Text text = new Text(table, SWT.RIGHT);
	// text.setTextLimit(500);
	// text.setFont(SokkerBean.getFontTable());
	//
	// editor.setEditor(text, item, i);
	//
	// text.setText(item.getText(i));
	//
	// Listener textListener = new Listener() {
	// public void handleEvent(final Event e) {
	// switch (e.type) {
	// case SWT.FocusOut:
	// ((Player) item.getData(Player.class.getName())).setNote(text.getText());
	// fillTable(table, players, comparator);
	// text.dispose();
	// break;
	// case SWT.Traverse:
	// switch (e.detail) {
	// case SWT.TRAVERSE_RETURN:
	// ((Player) item.getData(Player.class.getName())).setNote(text.getText());
	// fillTable(table, players, comparator);
	//
	// // FALL THROUGH
	// case SWT.TRAVERSE_ESCAPE:
	// text.dispose();
	// e.doit = false;
	// break;
	// }
	// }
	// }
	// };
	// text.addListener(SWT.FocusOut, textListener);
	// text.addListener(SWT.Traverse, textListener);
	//
	// text.selectAll();
	// text.setFocus();
	// return;
	// }
	// if (!visible && rect.intersects(clientArea)) {
	// visible = true;
	// }
	// }
	// if (!visible)
	// return;
	// }
	// }
	// });

	// }

	public ViewNT(Display display) {
		shell = new Shell(display);
		_monitor = display.getPrimaryMonitor();
		shell.setSize(_monitor.getClientArea().width, _monitor.getClientArea().height);
		shell.setLayout(new FormLayout());

		Rectangle splashRect = shell.getBounds();
		Rectangle displayRect = _monitor.getClientArea();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		shell.setLocation(x, y);

		addFonts();
		players = new ArrayList<Player>();

		try {
			settings = new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).getSokkerViewerSettings();
			Messages.setDefault(new Locale("pl_PL"));

			if (settings.isCheckProperties()) {
				new PropertiesChecker().checkSokkerProperties();
				settings.setCheckProperties(false);
				new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
			}

			if (settings.getLangCode().isEmpty()) {
				settings.setLangCode(Language.en_EN.name());
				new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
			}
			String[] table = settings.getLangCode().split("_");
			Messages.setDefault(new Locale(table[0], table[1]));
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.file.ioexception"), e);
		} catch (IOException e) {
			new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.file.ioexception"), e);
		} catch (SVException e) {
			new BugReporter(composite.getShell()).openErrorMessage(e.getMessage(), e);
		}

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);

		composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(formData);
		composite.setLayout(new FormLayout());

		init(composite);
		addPopupMenu();

		Tree tree = new Tree(composite, SWT.NONE);
		_treeItem = new TreeItem(tree, SWT.NONE);
		tree.setVisible(false);

		languageCombo.setEnabled(true);
		buttonGet.setEnabled(true);
		shell.setDefaultButton(buttonGet);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ViewNT(new Display());
	}

	private void addView() {

		// FormData formData = new FormData();
		// formData.left = new FormAttachment(50, 0);
		// formData.top = new FormAttachment(0, 5);
		// formData.width = 0;
		// formData.height = 0;
		//
		// Label centerPoint = new Label(composite, SWT.NONE);
		// centerPoint.setLayoutData(formData);

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(0, 5);
		// formData.height = 150;
		// formData.width = 400;

		loginGroup = new Group(composite, SWT.NONE);
		loginGroup.setLayout(new FormLayout());
		loginGroup.setLayoutData(formData);
		loginGroup.setText(Messages.getString("viewnt.settings.login"));

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 5);
		// formData.height = 15;

		loginLabel = new Label(loginGroup, SWT.NONE);
		loginLabel.setLayoutData(formData);
		loginLabel.setText(Messages.getString("confShell.sklogin"));
		loginLabel.setFont(ConfigBean.getFontMain());
		loginLabel.pack();

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(loginLabel, 5);
		formData.right = new FormAttachment(100, -10);
		formData.height = 15;
		formData.width = 150;

		loginText = new Text(loginGroup, SWT.BORDER);
		loginText.setLayoutData(formData);
		loginText.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(loginText, 5);
		// formData.height = 15;

		passwordLabel = new Label(loginGroup, SWT.NONE);
		passwordLabel.setLayoutData(formData);
		passwordLabel.setText(Messages.getString("confShell.skpassword"));
		passwordLabel.setFont(ConfigBean.getFontMain());
		passwordLabel.pack();

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(passwordLabel, 5);
		formData.right = new FormAttachment(100, -10);
		formData.height = 15;
		formData.width = 150;

		passwordText = new Text(loginGroup, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(formData);
		// passwordText.setEchoChar('*');
		passwordText.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(passwordText, 0, SWT.CENTER);
		formData.top = new FormAttachment(passwordText, 5);

		buttonGet = new Button(loginGroup, SWT.NONE);
		buttonGet.setText(Messages.getString("button.import"));
		buttonGet.setLayoutData(formData);
		buttonGet.setFont(ConfigBean.getFontMain());
		buttonGet.pack();
		buttonGet.setEnabled(false);
		buttonGet.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				try {
					// String xml =
					// HTMLDownloader.postDataToPage("http://62.233.129.98/sokker/dataxml.php",
					// "ilogin=" + URLEncoder.encode(loginText.getText(),
					// "UTF-8") +
					// "&ipassword=" + URLEncoder.encode(passwordText.getText(),
					// "UTF-8"),
					// "http://62.233.129.98/sokker/data.php");

					// Parser parser = new Parser();
					// InputSource input = new InputSource(new
					// StringReader(xml));
					// parser.parseXmlSax(input, null);
					//
					// players = parser.alPlayers;
					// club = parser.club;

					XMLDownloader xmlDownloader = new XMLDownloader();
					xmlDownloader.login(loginText.getText(), passwordText.getText());
					if (xmlDownloader.getStatus().equals("OK")) {
					} else {
						throw new SVException(xmlDownloader.getErrorno());
					}
					String teamXML = xmlDownloader.getTeam(xmlDownloader.getTeamID());
					String playersXML = xmlDownloader.getPlayers(xmlDownloader.getTeamID());

					InputSource input;
					TeamXmlParser teamXMLparser = new TeamXmlParser();
					input = new InputSource(new StringReader(teamXML));
					teamXMLparser.parseXmlSax(input, null);

					PlayersXmlParser playersXMLparser = new PlayersXmlParser();
					input = new InputSource(new StringReader(playersXML));
					playersXMLparser.parseXmlSax(input, null);

					String countryXML = xmlDownloader.getCountry(String.valueOf(teamXMLparser.getClub().getCountry()));
					CountryXmlParser countryXMLParser = new CountryXmlParser();
					input = new InputSource(new StringReader(countryXML));
					countryXMLParser.parseXmlSax(input, null);

					if (Money.getCurrency() == 0) {
						Money.setCurrency(countryXMLParser.getCountry().getCurrencyRate());
						Money.setCurrencySymbol(countryXMLParser.getCountry().getCurrencyName());
					}

					club = teamXMLparser.getClub();
					players = playersXMLparser.getAlPlayers();

					fillTable(table, playersXMLparser.getAlPlayers(), comparator);
				} catch (IOException e) {
					new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.file.ioexception"), e);
				} catch (SAXException e) {
					new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("message.error.login"), e);
				} catch (SVException e) {
					new BugReporter(composite.getShell()).openErrorMessage(Messages.getString("login.error." + e.getMessage()), e);
				}

			}

		});

		formData = new FormData();
		formData.left = new FormAttachment(50, 0);
		formData.top = new FormAttachment(buttonGet, 0);
		formData.bottom = new FormAttachment(100, -5);
		formData.width = 0;
		formData.height = 0;

		Label centerPoint = new Label(loginGroup, SWT.NONE);
		centerPoint.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(100, -20);
		formData.bottom = new FormAttachment(100, 0);

		status = new CLabel(composite, SWT.BORDER);
		status.setLayoutData(formData);
		status.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(loginGroup, 20);
		formData.bottom = new FormAttachment(status, -5);

		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setLayoutData(formData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setFont(ConfigBean.getFontTable());

		String[] columns = { "", Messages.getString("table.name"), Messages.getString("table.surname"), Messages.getString("table.value"),
							Messages.getString("table.salary"), Messages.getString("table.age"), Messages.getString("table.form"),
							Messages.getString("table.stamina"), Messages.getString("table.pace"), Messages.getString("table.technique"),
							Messages.getString("table.passing"), Messages.getString("table.keeper"), Messages.getString("table.defender"),
							Messages.getString("table.playmaker"), Messages.getString("table.scorer"), "" };

		// addTableEditor(table);

		for (int j = 0; j < columns.length; j++) {
			TableColumn column = new TableColumn(table, SWT.NONE);

			if (j > 2 && j < columns.length - 1) {
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
				column.pack();
			}
		}

		table.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3) {
					// Rectangle clientArea = allCoachesTable.getClientArea();
					Point pt = new Point(event.x, event.y);
					TableItem item = table.getItem(pt);
					if (item != null) {
						// note.setEnabled(true);
						table.setMenu(menuPopUp);
						table.getMenu().setVisible(true);
					} else {
						table.setMenu(menuClear);
						// note.setEnabled(false);
					}
				}
				// else {
				// Point pt = new Point(event.x, event.y);
				// TableItem item = table.getItem(pt);
				//
				// if (item != null) {
				// // note.setEnabled(true);
				// // note.setData(Player.class.getName(), (Player)item.getData(Player.class.getName()));
				// // note.setData("item" , item);
				// // if(((Player)item.getData(Player.class.getName())).getNote() == null) {
				// // note.setText("");
				// // } else {
				// // note.setText(((Player)item.getData(Player.class.getName())).getNote());
				// // }
				//
				// } else {
				// // note.setEnabled(false);
				// }
				//
				// }
			}
		});

		comparator = new PlayerComparator();
		comparator.setColumn(PlayerComparator.VALUE);
		comparator.setDirection(PlayerComparator.DESCENDING);

		table.setSortColumn(table.getColumn(PlayerComparator.VALUE));
		table.setSortDirection(SWT.DOWN);

		players = new ArrayList<Player>();

		final TableColumn[] column = table.getColumns();

		for (int i = 1; i < column.length; i++) {

			column[i].addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					int column = table.indexOf((TableColumn) event.widget);

					if (column != comparator.getColumn()) {
						comparator.setColumn(column);
						comparator.setDirection(PlayerComparator.DESCENDING);

						table.setSortColumn(table.getColumn(column));
						table.setSortDirection(SWT.DOWN);
					} else {

						if (comparator.getDirection() == PlayerComparator.ASCENDING) {
							table.setSortDirection(SWT.DOWN);
							comparator.reverseDirection();
						} else {
							table.setSortDirection(SWT.UP);
							comparator.reverseDirection();
						}
					}

					fillTable(table, players, comparator);
				}
			});
		}

		formData = new FormData();
		formData.left = new FormAttachment(loginGroup, 15);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(table, -20);
		formData.width = 200;

		pluginGroup = new Group(composite, SWT.NONE);
		pluginGroup.setLayout(new FormLayout());
		pluginGroup.setLayoutData(formData);
		pluginGroup.setText(Messages.getString("viewnt.settings.plugin"));

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);

		trainingLabel = new Label(pluginGroup, SWT.NONE);
		trainingLabel.setLayoutData(formData);
		trainingLabel.setText(Messages.getString("training.type"));
		trainingLabel.setFont(ConfigBean.getFontMain());
		trainingLabel.pack();

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(trainingLabel, 5);
		formData.right = new FormAttachment(100, -5);
		formData.height = 20;

		trainingCombo = new Combo(pluginGroup, SWT.READ_ONLY);
		for (int i = 1; i < 9; i++) {
			trainingCombo.add(Messages.getString("training.type." + i));
		}
		trainingCombo.setText(trainingCombo.getItem(0));
		trainingCombo.setVisibleItemCount(8);
		trainingCombo.setLayoutData(formData);
		trainingCombo.setFont(ConfigBean.getFontMain());

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(trainingCombo, 5);
		formData.right = new FormAttachment(100, -5);

		languageLabel = new Label(pluginGroup, SWT.NONE);
		languageLabel.setText(Messages.getString("confShell.langtype"));
		languageLabel.setLayoutData(formData);
		languageLabel.setFont(ConfigBean.getFontMain());
		languageLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(languageLabel, 5);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.height = 20;

		Listener confShellLangComboListner = new Listener() {

			public void handleEvent(Event event) {
				String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
				String[] table = Language.getLanguageCode(text).split("_");
				// langResource = Messages.getLangResources(new Locale(table[0],
				// table[1]));
				Messages.setDefault(new Locale(table[0], table[1]));
				translateWidgets(Messages.getDefaultLangResources());
			}
		};

		languageCombo = new Combo(pluginGroup, SWT.BORDER | SWT.READ_ONLY);
		languageCombo.setItems(Language.languageNames());
		languageCombo.setText(Language.getLanguageName(settings.getLangCode()));
		languageCombo.setLayoutData(formData);
		languageCombo.setFont(ConfigBean.getFontMain());
		languageCombo.addListener(SWT.Selection, confShellLangComboListner);
		languageCombo.setEnabled(false);

		// formData = new FormData();
		// formData.left = new FormAttachment(pluginGroup, 15);
		// formData.top = new FormAttachment(0, 5);
		// formData.bottom = new FormAttachment(table, -20);
		// formData.right = new FormAttachment(100, -10);
		//
		// notePlugin = new Group(composite, SWT.NONE);
		// notePlugin.setLayout(new FormLayout());
		// notePlugin.setLayoutData(formData);
		// notePlugin.setText(langProperties.getProperty("viewnt.settings.note"));
		//
		// formData = new FormData();
		// formData.left = new FormAttachment(0, 5);
		// formData.top = new FormAttachment(0, 5);
		// formData.bottom = new FormAttachment(100, -5);
		// formData.right = new FormAttachment(100, -5);
		//
		// note = new Text(notePlugin, SWT.BORDER | SWT.WRAP | SWT.MULTI |
		// SWT.V_SCROLL);
		// note.setLayoutData(formData);
		// note.setTextLimit(500);
		// note.setEnabled(false);
		//
		// note.addListener(SWT.Modify, new Listener() {
		//
		// public void handleEvent(Event arg0) {
		// if(note.getData(Player.class.getName()) != null) {
		// Player player = (Player)note.getData(Player.class.getName());
		// player.setNote(note.getText());
		// if(note.getData("item") != null) {
		// ((TableItem)note.getData("item")).setText(table.getColumnCount()-1,
		// note.getText());
		// if(note.getText().length() > table.getColumn(table.getColumnCount() -
		// 1).getText().length()) {
		// table.getColumn(table.getColumnCount() - 1).pack();
		// }
		// }
		// }
		// }
		//
		// });

	}

	private void fillTable(Table table, ArrayList<Player> players, PlayerComparator comparator) {
		int maxSkill = 0;
		// Turn off drawing to avoid flicker
		table.setRedraw(false);

		// We remove all the table entries, sort our
		// rows, then add the entries
		table.remove(0, table.getItemCount() - 1);
		Collections.sort(players, comparator);
		for (Player player : players) {
			maxSkill = player.getSkills().length - 1;
			TableItem item = new TableItem(table, SWT.NONE);
			int c = 0;
			item.setData(Player.class.getName(), player);
			if (player.getNational() > 0) {
				item.setImage(c++, FlagsResources.getFlag(player.getCountryfrom()));
			} else {
				item.setImage(c++, FlagsResources.getFlagLight(player.getCountryfrom()));
			}
			// item.setImage(c++,
			// FlagsResources.getFlag(player.getCountryfrom()));
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
			// if (player.getNote() == null) {
			// item.setText(c++, "");
			// } else {
			// item.setText(c++, player.getNote());
			// }

			if (player.isNt()) {
				item.setBackground(ColorResources.getColor(233, 252, 224));
			}

		}
		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumn(i).pack();
		}

		// Turn drawing back on
		table.setRedraw(true);

		// note.setEnabled(false);
	}

	private void addFonts() {
		// ustawiam fonty
		ConfigBean.setFontCurrent(shell.getFont());
		Font fontCurrent = ConfigBean.getFontCurrent();

		if (System.getProperty("os.name").equals("Linux")) {
			ConfigBean.setFontMain(Fonts.getFont(shell.getDisplay(), fontCurrent.getFontData()[0].getName(), fontCurrent.getFontData()[0].height, SWT.NORMAL));

			ConfigBean.setFontDescription(Fonts.getFont(shell.getDisplay(), "Bitstream Vera Sans Mono, Luxi Mono,Nimbus Mono L",
														fontCurrent.getFontData()[0].height, SWT.NORMAL));

			ConfigBean.setFontTable(Fonts.getFont(shell.getDisplay(), fontCurrent.getFontData()[0].getName(), fontCurrent.getFontData()[0].height, SWT.NORMAL));
		} else {
			ConfigBean.setFontMain(Fonts.getFont(shell.getDisplay(), "Arial", fontCurrent.getFontData()[0].height, SWT.NORMAL));

			ConfigBean.setFontDescription(Fonts.getFont(shell.getDisplay(), "Courier New", fontCurrent.getFontData()[0].height + 1, SWT.NORMAL));

			ConfigBean.setFontTable(Fonts.getFont(shell.getDisplay(), "Arial", fontCurrent.getFontData()[0].height, SWT.NORMAL));
		}

	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub

	}

}
