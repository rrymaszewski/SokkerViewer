package pl.pronux.sokker.ui.configure;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.DatabaseSettingsDao;
import pl.pronux.sokker.model.DatabaseSettings;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ViewDatabase implements IViewConfigure {

	private TreeItem treeItem;

	private Composite composite;

	private SokkerViewerSettings settings;

	private Label confShellDbTypeLabel;

	private Combo confShellDbTypeCombo;

	private Label confShellDbServerLabel;

	private Text confShellDbServerText;

	private Label confShellDbNameLabel;

	private Text confShellDbNameText;

	private Label confShellDbLoginLabel;

	private Text confShellDbLoginText;

	private Label confShellDbPasswordLabel;

	private Text confShellDbPasswordText;

	private DatabaseSettings databaseSettings;

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
		
		databaseSettings = settings.getDatabaseSettings();
		composite.setLayout(new FormLayout());
		FormData formData;
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbTypeLabel = new Label(composite, SWT.VERTICAL | SWT.SHADOW_NONE);

		confShellDbTypeLabel.setLayoutData(formData);

		Listener confShellComboListner = new Listener() {

			public void handleEvent(Event event) {
				String text = ((Combo) event.widget).getItem(((Combo) event.widget).getSelectionIndex());
				if (text.equalsIgnoreCase("hsqldb")) { //$NON-NLS-1$
					confShellDbServerText.setEnabled(false);
					confShellDbNameText.setEnabled(false);
					confShellDbLoginText.setEnabled(false);
					confShellDbPasswordText.setEnabled(false);
				} else if (text.equalsIgnoreCase("postgresql")) { //$NON-NLS-1$
					confShellDbServerText.setEnabled(true);
					confShellDbNameText.setEnabled(true);
					confShellDbLoginText.setEnabled(true);
					confShellDbPasswordText.setEnabled(true);
				}
			}
		};

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbTypeLabel, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		confShellDbTypeCombo.setLayoutData(formData);
		confShellDbTypeCombo.addListener(SWT.Selection, confShellComboListner);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbTypeCombo, 15);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbServerLabel = new Label(composite, SWT.VERTICAL | SWT.SHADOW_NONE);
		confShellDbServerLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbServerLabel, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbServerText = new Text(composite, SWT.BORDER);
		confShellDbServerText.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbServerText, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbNameLabel = new Label(composite, SWT.NONE);
		confShellDbNameLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbNameLabel, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbNameText = new Text(composite, SWT.BORDER);
		confShellDbNameText.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbNameText, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbLoginLabel = new Label(composite, SWT.NONE);
		confShellDbLoginLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbLoginLabel, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbLoginText = new Text(composite, SWT.BORDER);
		confShellDbLoginText.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbLoginText, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbPasswordLabel = new Label(composite, SWT.NONE);
		confShellDbPasswordLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(confShellDbPasswordLabel, 5);
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.height = 15;

		confShellDbPasswordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		confShellDbPasswordText.setLayoutData(formData);

		confShellDbPasswordText.setText(databaseSettings.getPassword());
		confShellDbServerText.setText(databaseSettings.getServer());
		confShellDbLoginText.setText(databaseSettings.getUsername());
		confShellDbTypeCombo.setItems(databaseSettings.getTypes().toArray(new String[databaseSettings.getTypes().size()]));
		confShellDbTypeCombo.setText(databaseSettings.getType());
		confShellDbNameText.setText(databaseSettings.getName());

		if (confShellDbTypeCombo.getText().equalsIgnoreCase(DatabaseSettings.HSQLDB)) {
			confShellDbServerText.setEnabled(false);
			confShellDbNameText.setEnabled(false);
			confShellDbLoginText.setEnabled(false);
			confShellDbPasswordText.setEnabled(false);
		} else if (confShellDbTypeCombo.getText().equalsIgnoreCase(DatabaseSettings.POSTGRESQL)) {
			confShellDbServerText.setEnabled(true);
			confShellDbNameText.setEnabled(true);
			confShellDbLoginText.setEnabled(true);
			confShellDbPasswordText.setEnabled(true);
		}

		confShellDbTypeLabel.setText(Messages.getString("confShell.dbtype")); //$NON-NLS-1$
		treeItem.setText(Messages.getString("configure.database")); //$NON-NLS-1$
		confShellDbServerLabel.setText(Messages.getString("confShell.dbserver")); //$NON-NLS-1$
		confShellDbNameLabel.setText(Messages.getString("confShell.dbname")); //$NON-NLS-1$
		confShellDbLoginLabel.setText(Messages.getString("confShell.dblogin")); //$NON-NLS-1$
		confShellDbPasswordLabel.setText(Messages.getString("confShell.dbpassword")); //$NON-NLS-1$
		composite.layout(true);
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		treeItem.setText(Messages.getString("configure.database")); //$NON-NLS-1$

	}

	public void set() {

	}

	public void applyChanges() {
		databaseSettings.setServer(confShellDbServerText.getText());
		databaseSettings.setName(confShellDbNameText.getText());
		databaseSettings.setUsername(confShellDbLoginText.getText());
		databaseSettings.setPassword(confShellDbPasswordText.getText());
		databaseSettings.setType(confShellDbTypeCombo.getText());
		try {
			new DatabaseSettingsDao(PropertiesDatabase.getSession()).updateDatabaseSettings(databaseSettings);
		} catch (FileNotFoundException e) {
			new BugReporter(composite.getShell()).openErrorMessage("Configure Database", e); //$NON-NLS-1
		} catch (IOException e) {
			new BugReporter(composite.getShell()).openErrorMessage("Configure Database", e); //$NON-NLS-1
		}
	}

	public void restoreDefaultChanges() {
		set();
	}

}
