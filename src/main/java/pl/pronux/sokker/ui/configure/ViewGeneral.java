package pl.pronux.sokker.ui.configure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewGeneral implements IViewConfigure {

	private TreeItem treeItem;

	private Composite composite;

	// private Group proxyGroup;
	private SokkerViewerSettings settings;

	private Button startupButton;

	private Button downloadButton;

	private Label confShellLangTypeLabel;

	private Combo confShellLangTypeCombo;

	private Button closeButton;

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

		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);

		startupButton = new Button(composite, SWT.CHECK);
		startupButton.setLayoutData(formData);

		startupButton.pack();

		formData = new FormData();
		formData.top = new FormAttachment(startupButton, 10);
		formData.left = new FormAttachment(0, 10);

		downloadButton = new Button(composite, SWT.CHECK);
		downloadButton.setLayoutData(formData);

		downloadButton.pack();

		formData = new FormData();
		formData.top = new FormAttachment(downloadButton, 10);
		formData.left = new FormAttachment(0, 10);

		closeButton = new Button(composite, SWT.CHECK);
		closeButton.setLayoutData(formData);

		closeButton.pack();

		formData = new FormData();
		formData.top = new FormAttachment(closeButton, 5);
		formData.left = new FormAttachment(0, 20);
		// formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellLangTypeLabel = new Label(composite, SWT.VERTICAL | SWT.SHADOW_NONE);
		confShellLangTypeLabel.setText(Messages.getString("confShell.langtype")); //$NON-NLS-1$
		confShellLangTypeLabel.setLayoutData(formData);
		confShellLangTypeLabel.pack();

		formData = new FormData();
		formData.top = new FormAttachment(confShellLangTypeLabel, 0, SWT.CENTER);
		formData.left = new FormAttachment(confShellLangTypeLabel, 20);
		formData.right = new FormAttachment(100, -20);
		// formData.height = 15;

		confShellLangTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		confShellLangTypeCombo.setItems(settings.getLanguages().toArray(new String[settings.getLanguages().size()]));
		confShellLangTypeCombo.setLayoutData(formData);
		confShellLangTypeCombo.pack();

		setWidgets();
		startupButton.setText(Messages.getString("configure.startup")); //$NON-NLS-1$
		downloadButton.setText(Messages.getString("configure.startup.download")); //$NON-NLS-1$
		closeButton.setText(Messages.getString("configure.info.close")); //$NON-NLS-1$
		treeItem.setText(Messages.getString("configure.general")); //$NON-NLS-1$
		confShellLangTypeLabel.setText(Messages.getString("confShell.langtype")); //$NON-NLS-1$
		confShellLangTypeLabel.pack();
		composite.layout(true);
	}

	private void setWidgets() {
		downloadButton.setSelection(settings.isUpdate());
		startupButton.setSelection(settings.isStartup());
		closeButton.setSelection(settings.isInfoClose());

		String language = settings.getLanguage(settings.getLangCode());
		if (language != null) {
			confShellLangTypeCombo.setText(language);
			confShellLangTypeCombo.pack();
		}
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
		settings.setStartup(startupButton.getSelection());
		settings.setInfoClose(closeButton.getSelection());
		settings.setUpdate(downloadButton.getSelection());

		String text = confShellLangTypeCombo.getItem(confShellLangTypeCombo.getSelectionIndex());
		String langCode = settings.getLangCode(text);

		if (langCode != null) {
			if (!langCode.equals(settings.getLangCode())) {
				settings.setLangCode(langCode);
				Messages.setDefault(new Locale(settings.getLangCode()));
			}
		}
		try {
			new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
		} catch (FileNotFoundException e1) {
			new BugReporter(composite.getDisplay()).openErrorMessage("Configurator", e1);
		} catch (IOException e1) {
			new BugReporter(composite.getDisplay()).openErrorMessage("Configurator", e1);
		} catch (SVException e1) {
			new BugReporter(composite.getDisplay()).openErrorMessage("Configurator", e1);
		}
	}

	public void restoreDefaultChanges() {
		setWidgets();

	}

}
