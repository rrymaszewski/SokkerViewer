package pl.pronux.sokker.ui.configure;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.ProxyDao;
import pl.pronux.sokker.model.ProxySettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewProxy implements IViewConfigure {

	private TreeItem treeItem;

	private Composite composite;

	// private Group proxyGroup;
	private Button proxyTurnButton;

	private CLabel proxyHostLabel;

	private Text proxyHostText;

	private CLabel proxyPortLabel;

	private Text proxyPortText;

	private CLabel proxyUserLabel;

	private Text proxyUserText;

	private CLabel proxyPasswordLabel;

	private Text proxyPasswordText;

	private SokkerViewerSettings settings;

	// private Button proxyOkButton;

	public void clear() {

	}

	public void dispose() {

	}

	public Composite getComposite() {
		return composite;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;

		composite.setLayout(new FormLayout());

		FormData formData;
		// formData = new FormData();
		// formData.top = new FormAttachment(0, 10);
		// formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);
		// // formData.height = 100;
		//
		// proxyGroup = new Group(composite, SWT.NONE);
		// proxyGroup.setLayoutData(formData);
		// proxyGroup.setLayout(new FormLayout());
		// proxyGroup.setText(langProperties.getProperty("configure.proxy.group"));

		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);

		proxyTurnButton = new Button(composite, SWT.CHECK);
		proxyTurnButton.setLayoutData(formData);
		proxyTurnButton.pack();
		proxyTurnButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				if (proxyTurnButton.getSelection()) {
					proxyHostText.setEnabled(true);
					proxyPortText.setEnabled(true);
					proxyUserText.setEnabled(true);
					proxyPasswordText.setEnabled(true);
				} else {
					proxyHostText.setEnabled(false);
					proxyPortText.setEnabled(false);
					proxyUserText.setEnabled(false);
					proxyPasswordText.setEnabled(false);
				}
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(proxyTurnButton, 5);
		formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		proxyHostLabel = new CLabel(composite, SWT.NONE);
		proxyHostLabel.setLayoutData(formData);
		proxyHostLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(proxyHostLabel, 5);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, 0);
		// formData.height = 25;

		proxyHostText = new Text(composite, SWT.BORDER);
		proxyHostText.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(proxyHostText, 5);
		formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		proxyPortLabel = new CLabel(composite, SWT.NONE);
		proxyPortLabel.setLayoutData(formData);
		proxyPortLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(proxyPortLabel, 5);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, 0);
		// formData.height = 25;

		proxyPortText = new Text(composite, SWT.BORDER);
		proxyPortText.setLayoutData(formData);
		proxyPortText.setTextLimit(5);
		proxyPortText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')) {
						e.doit = false;
						return;
					}
				}
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(proxyPortText, 5);
		formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		proxyUserLabel = new CLabel(composite, SWT.NONE);
		proxyUserLabel.setLayoutData(formData);
		proxyUserLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(proxyUserLabel, 5);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, 0);
		// formData.height = 25;

		proxyUserText = new Text(composite, SWT.BORDER);
		proxyUserText.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(proxyUserText, 5);
		formData.left = new FormAttachment(0, 10);
		// formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		proxyPasswordLabel = new CLabel(composite, SWT.NONE);
		proxyPasswordLabel.setLayoutData(formData);
		proxyPasswordLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(proxyPasswordLabel, 5);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(60, 0);
		// formData.height = 25;

		proxyPasswordText = new Text(composite, SWT.BORDER);
		proxyPasswordText.setLayoutData(formData);
		proxyPasswordText.setEchoChar('*');

		formData = new FormData();
		formData.top = new FormAttachment(proxyPasswordText, 5);
		formData.left = new FormAttachment(proxyPasswordText, 0, SWT.CENTER);
		formData.bottom = new FormAttachment(100, -10);

		setWidgets();

		treeItem.setText(Messages.getString("configure.proxy")); //$NON-NLS-1$
		proxyTurnButton.setText(Messages.getString("configure.proxy.turn.button")); //$NON-NLS-1$
		proxyPasswordLabel.setText(Messages.getString("configure.proxy.password.label")); //$NON-NLS-1$
		proxyPasswordLabel.setText(Messages.getString("configure.proxy.password.label")); //$NON-NLS-1$
		proxyUserLabel.setText(Messages.getString("configure.proxy.user.label")); //$NON-NLS-1$
		proxyPortLabel.setText(Messages.getString("configure.proxy.port.label")); //$NON-NLS-1$
		proxyHostLabel.setText(Messages.getString("configure.proxy.host.label")); //$NON-NLS-1$
		proxyTurnButton.setText(Messages.getString("configure.proxy.turn.button")); //$NON-NLS-1$

		composite.layout(true);
	}

	private void setWidgets() {
		ProxySettings proxySettings = settings.getProxySettings();
		proxyTurnButton.setSelection(proxySettings.isEnabled());

		proxyHostText.setText(proxySettings.getHostname());
		proxyPortText.setText(String.valueOf(proxySettings.getPort()));

		if (proxyTurnButton.getSelection()) {
			proxyHostText.setEnabled(true);
			proxyPortText.setEnabled(true);
			proxyUserText.setEnabled(true);
			proxyPasswordText.setEnabled(true);
		} else {
			proxyHostText.setEnabled(false);
			proxyPortText.setEnabled(false);
			proxyUserText.setEnabled(false);
			proxyPasswordText.setEnabled(false);
		}
		proxyUserText.setText(proxySettings.getUsername());
		proxyPasswordText.setText(proxySettings.getPassword());
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void set() {
		setWidgets();
	}

	public void applyChanges() {
		ProxySettings proxySettings = settings.getProxySettings();
		proxySettings.setEnabled(proxyTurnButton.getSelection());
		proxySettings.setHostname(proxyHostText.getText());
		try {
			proxySettings.setPort(Integer.valueOf(proxyPortText.getText()));
		} catch (Exception e) {
			proxySettings.setPort(0);
		}
		proxySettings.setUsername(proxyUserText.getText());
		proxySettings.setPassword(proxyPasswordText.getText());

		if (proxySettings.isEnabled()) {
			System.setProperty("proxySet", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			System.setProperty("http.proxyHost", proxyHostText.getText()); //$NON-NLS-1$
			System.setProperty("http.proxyPort", proxyPortText.getText()); //$NON-NLS-1$
			System.setProperty("http.proxyUser", proxyUserText.getText()); //$NON-NLS-1$
			System.setProperty("http.proxyPassword", proxySettings.getPassword()); //$NON-NLS-1$
		}

		try {
			new ProxyDao(PropertiesDatabase.getSession()).updateProxySettings(proxySettings);
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite Proxy", e);
		} catch (IOException e) {
			new BugReporter(composite.getDisplay()).openErrorMessage("ViewComposite Proxy", e);
		}
	}

	public void restoreDefaultChanges() {
		setWidgets();
	}

}
