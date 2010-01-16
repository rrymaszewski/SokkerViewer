package pl.pronux.sokker.ui.widgets.shells;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.actions.SettingsManager;
import pl.pronux.sokker.enums.Language;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.events.UpdateEvent;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IEvents;
import pl.pronux.sokker.ui.listeners.SelectAllListener;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.CursorResources;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;

public class LoginShell extends Shell {

	private SettingsManager settingsManager = SettingsManager.instance();

	private Display display;

	private Text skLoginText;

	private Label skPasswordLabel;

	private Text skPasswordText;

	private Button checkDlButton;

	private Button savePasswordButton;

	private Label langTypeLabel;

	private Combo langTypeCombo;

	private Button cancelButton;

	private Button okButton;

	private Label skLoginLabel;

	private Label changeLoginLabel;

	private Button proxyButton;

	private SokkerViewerSettings settings;

	private ProxySettings proxySettings;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public LoginShell(Shell parent, int style) {
		super(parent, style);

		this.display = parent.getDisplay();
		// this.setLayout(new FormLayout());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginLeft = 20;
		layout.marginRight = 20;
		layout.marginTop = 5;
		layout.marginBottom = 10;
		layout.verticalSpacing = 5;
		this.setLayout(layout);

		settings = SettingsHandler.getSokkerViewerSettings();
		proxySettings = settings.getProxySettings();

		this.setFont(ConfigBean.getFontMain());
		this.setText(Messages.getString("confShell.title")); //$NON-NLS-1$

		GridData defaultGridData = new GridData();
		defaultGridData.horizontalSpan = 2;
		defaultGridData.grabExcessHorizontalSpace = true;
		defaultGridData.horizontalAlignment = GridData.FILL;
		defaultGridData.widthHint = 150;
		defaultGridData.heightHint = 20;

		skLoginLabel = new Label(this, SWT.NONE);
		skLoginLabel.setText(Messages.getString("confShell.sklogin")); //$NON-NLS-1$
		skLoginLabel.setLayoutData(defaultGridData);

		skLoginText = new Text(this, SWT.BORDER);
		skLoginText.setText(settings.getUsername());

		skLoginText.addListener(SWT.MouseDown, new SelectAllListener());
		skLoginText.addListener(SWT.FocusIn, new SelectAllListener());
		skLoginText.addListener(SWT.FocusOut, new SelectAllListener());
		skLoginText.setLayoutData(defaultGridData);

		skPasswordLabel = new Label(this, SWT.NONE);
		skPasswordLabel.setText(Messages.getString("confShell.skpassword")); //$NON-NLS-1$
		skPasswordLabel.setLayoutData(defaultGridData);

		skPasswordText = new Text(this, SWT.BORDER | SWT.PASSWORD);
		skPasswordText.setText(settings.getPassword());
		// confShellSkPasswordText.setEchoChathis
		skPasswordText.addListener(SWT.MouseDown, new SelectAllListener());
		skPasswordText.addListener(SWT.FocusIn, new SelectAllListener());
		skPasswordText.addListener(SWT.FocusOut, new SelectAllListener());
		skPasswordText.setLayoutData(defaultGridData);

		checkDlButton = new Button(this, SWT.CHECK);
		checkDlButton.setText(Messages.getString("confShell.dlcheck")); //$NON-NLS-1$
		checkDlButton.setLayoutData(defaultGridData);
		checkDlButton.setSelection(settings.isUpdate());

		savePasswordButton = new Button(this, SWT.CHECK);
		savePasswordButton.setText(Messages.getString("confShell.savepass")); //$NON-NLS-1$
		savePasswordButton.setLayoutData(defaultGridData);
		savePasswordButton.setSelection(settings.isSavePassword());

		if (proxySettings.getHostname() != null && !proxySettings.getHostname().matches(" *")) { //$NON-NLS-1$
			proxyButton = new Button(this, SWT.CHECK);
			proxyButton.setText(Messages.getString("confShell.proxy")); //$NON-NLS-1$
			proxyButton.setLayoutData(defaultGridData);
			proxyButton.setSelection(proxySettings.isEnabled());
		}

		langTypeLabel = new Label(this, SWT.VERTICAL | SWT.SHADOW_NONE);
		langTypeLabel.setText(Messages.getString("confShell.langtype")); //$NON-NLS-1$
		langTypeLabel.setLayoutData(defaultGridData);

		Listener confShellLangComboListner = new Listener() {

			public void handleEvent(Event event) {
				try {
					String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
					String langCode = Language.getLanguageCode(text);

					if (langCode != null) {
						settings.setLangCode(langCode);
						settingsManager.updateSettings(settings);
						String[] table = langCode.split("_"); //$NON-NLS-1$
						Messages.setDefault(new Locale(table[0], table[1]));
						setShellConfWidgetsTranslation();
						MessageDialog.openInformationMessage(LoginShell.this.getShell(), Messages.getString("message.information.restart")); //$NON-NLS-1$
					}
				} catch (FileNotFoundException e1) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e1);
				} catch (IOException e1) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e1);
				} catch (SVException e1) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e1);
				}
			}
		};

		langTypeCombo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		langTypeCombo.setItems(Language.languageNames());
		String language = Language.getLanguageName(settings.getLangCode());
		if (language != null) {
			langTypeCombo.setText(language);
		}
		langTypeCombo.setLayoutData(defaultGridData);
		langTypeCombo.addListener(SWT.Selection, confShellLangComboListner);

		Listener confShellOkListner = new Listener() {

			public void handleEvent(Event event) {
				if (skLoginText.getText().isEmpty() || skPasswordText.getText().isEmpty()) {
					MessageBox msg = new MessageBox(LoginShell.this, SWT.OK | SWT.ICON_ERROR);
					msg.setText(Messages.getString("message.confShell.title")); //$NON-NLS-1$
					msg.setMessage(Messages.getString("message.confShell.text.nologin")); //$NON-NLS-1$
					msg.open();
					return;
				}

				settings.setUsername(skLoginText.getText());
				settings.setUpdate(checkDlButton.getSelection());

				settings.setSavePassword(savePasswordButton.getSelection());
				settings.setPassword(skPasswordText.getText());

				if (proxyButton != null) {
					proxySettings.setEnabled(proxyButton.getSelection());
					if (proxyButton.getSelection()) {
						System.setProperty("proxySet", "true"); //$NON-NLS-1$ //$NON-NLS-2$
						System.setProperty("http.proxyHost", proxySettings.getHostname()); //$NON-NLS-1$
						System.setProperty("http.proxyPort", String.valueOf(proxySettings.getPort())); //$NON-NLS-1$
						System.setProperty("http.proxyUser", proxySettings.getUsername()); //$NON-NLS-1$
						System.setProperty("http.proxyPassword", proxySettings.getPassword()); //$NON-NLS-1$
					} else {
						System.setProperty("proxySet", "false"); //$NON-NLS-1$ //$NON-NLS-2$
						System.setProperty("http.proxyHost", ""); //$NON-NLS-1$ //$NON-NLS-2$
						System.setProperty("http.proxyPort", ""); //$NON-NLS-1$ //$NON-NLS-2$
						System.setProperty("http.proxyUser", ""); //$NON-NLS-1$ //$NON-NLS-2$
						System.setProperty("http.proxyPassword", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}

				try {
					settingsManager.updateSettings(settings);
				} catch (FileNotFoundException e) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e);
				} catch (IOException e) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e);
				} catch (SVException e) {
					new BugReporter(LoginShell.this.getDisplay()).openErrorMessage("Login", e);
				}

				setShellConfWidgetsTranslation();
				LoginShell.this.getParent().update();
				ViewerHandler.getViewer().notifyListeners(IEvents.LOAD_DATA, new UpdateEvent(checkDlButton.getSelection()));
				LoginShell.this.close();
			}
		};

		Listener confShellCancelListner = new Listener() {

			public void handleEvent(Event event) {
				LoginShell.this.close();
			}
		};

		GridData buttonGridData = new GridData();
		buttonGridData.widthHint = 60;
		buttonGridData.grabExcessHorizontalSpace = true;
		buttonGridData.horizontalAlignment = GridData.END;

		okButton = new Button(this, SWT.CENTER);
		okButton.setText(Messages.getString("button.ok")); //$NON-NLS-1$
		okButton.addListener(SWT.Selection, confShellOkListner);
		okButton.setFont(ConfigBean.getFontMain());
		okButton.setLayoutData(buttonGridData);

		buttonGridData = new GridData();
		buttonGridData.widthHint = 60;
		buttonGridData.grabExcessHorizontalSpace = true;
		buttonGridData.horizontalAlignment = GridData.BEGINNING;

		cancelButton = new Button(this, SWT.CENTER);
		cancelButton.setText(Messages.getString("button.cancel")); //$NON-NLS-1$
		cancelButton.addListener(SWT.Selection, confShellCancelListner);
		cancelButton.setFont(ConfigBean.getFontMain());
		cancelButton.setLayoutData(buttonGridData);

		changeLoginLabel = new Label(this, SWT.CENTER);
		changeLoginLabel.setLayoutData(defaultGridData);
		changeLoginLabel.setText(Messages.getString("confShell.login.change")); //$NON-NLS-1$
		changeLoginLabel.setForeground(ColorResources.getBlue());
		changeLoginLabel.setCursor(CursorResources.getCursor(SWT.CURSOR_HAND));

		changeLoginLabel.addListener(SWT.MouseEnter, new Listener() {

			public void handleEvent(Event arg0) {
				changeLoginLabel.setForeground(ColorResources.getRed());
			}

		});

		changeLoginLabel.addListener(SWT.MouseExit, new Listener() {

			public void handleEvent(Event arg0) {
				changeLoginLabel.setForeground(ColorResources.getBlue());
			}

		});

		changeLoginLabel.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event arg0) {
				ChangeLoginShell shellLogin = new ChangeLoginShell(LoginShell.this, SWT.CLOSE | SWT.PRIMARY_MODAL);
				shellLogin.open();
				String login = shellLogin.getLogin();
				if (login != null) {
					if (!login.isEmpty()) {
						skLoginText.setText(login);
						skPasswordText.setFocus();
					}
				}
			}
		});

		this.setDefaultButton(okButton);
		this.pack();
		this.setLocation(display.getPrimaryMonitor().getClientArea().width / 2 - this.getBounds().width / 2, display.getPrimaryMonitor().getClientArea().height
																											 / 2 - this.getBounds().height / 2);
	}

	final private void setShellConfWidgetsTranslation() {
		skLoginLabel.setText(Messages.getString("confShell.sklogin")); //$NON-NLS-1$
		skPasswordLabel.setText(Messages.getString("confShell.skpassword")); //$NON-NLS-1$
		langTypeLabel.setText(Messages.getString("confShell.langtype")); //$NON-NLS-1$
		savePasswordButton.setText(Messages.getString("confShell.savepass")); //$NON-NLS-1$
		checkDlButton.setText(Messages.getString("confShell.dlcheck")); //$NON-NLS-1$
		cancelButton.setText(Messages.getString("button.cancel")); //$NON-NLS-1$
		okButton.setText(Messages.getString("button.ok")); //$NON-NLS-1$
		changeLoginLabel.setText(Messages.getString("confShell.login.change")); //$NON-NLS-1$
	}
}
