package pl.pronux.sokker.ui.configure;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.properties.PropertiesDatabase;
import pl.pronux.sokker.data.properties.dao.SokkerViewerSettingsDao;
import pl.pronux.sokker.exceptions.SVException;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class ViewBackup implements IViewConfigure {

	private TreeItem treeItem;
	private Composite composite;
//	private Group proxyGroup;
	private SokkerViewerSettings settings;
	private Group backupGroup;
	private CLabel backupLabel;
	private Text backupPathText;
	private Button backupPathButton;
	protected String tempPropsFile;

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

	public void init(final Composite composite) {
		this.composite = composite;

		composite.setLayout(new FormLayout());
		FormData formData;

		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.height = 100;

		backupGroup = new Group(composite, SWT.NONE);
		backupGroup.setLayoutData(formData);
		backupGroup.setLayout(new FormLayout());


		formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		backupLabel = new CLabel(backupGroup, SWT.NONE);
		backupLabel.setLayoutData(formData);


		formData = new FormData();
		formData.top = new FormAttachment(backupLabel, 5);
		formData.right = new FormAttachment(100, -10);

		backupPathButton = new Button(backupGroup, SWT.NONE | SWT.FLAT);
		backupPathButton.setText("..."); //$NON-NLS-1$
		backupPathButton.setLayoutData(formData);
		backupPathButton.pack();

		backupPathButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {

				DirectoryDialog dirDialog = new DirectoryDialog(composite.getShell(), SWT.OPEN);
				dirDialog.setText(Messages.getString("confShell.chooser.title")); //$NON-NLS-1$
				dirDialog.setFilterPath(settings.getBackupDirectory());

				tempPropsFile = dirDialog.open();
				if(tempPropsFile != null) {
					backupPathText.setText(tempPropsFile);
				}
			}
		});


		formData = new FormData();
//		formData.top = new FormAttachment(backupPathText, 0, SWT.CENTER);
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(backupLabel, 5);
		formData.right = new FormAttachment(backupPathButton, -10);
		// formData.height = 25;

		backupPathText = new Text(backupGroup, SWT.BORDER | SWT.READ_ONLY);
		backupPathText.setLayoutData(formData);

		backupGroup.setText(Messages.getString("configure.backup.group")); //$NON-NLS-1$
		backupLabel.setText(Messages.getString("configure.backup.label")); //$NON-NLS-1$
		treeItem.setText(Messages.getString("configure.backup")); //$NON-NLS-1$
		setWidgets();
		composite.layout(true);
	}

	private void setWidgets() {
		backupPathText.setText(settings.getBackupDirectory());
	}

	public void setComposite(Composite composite) {
		this.composite = composite;

	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}


	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		treeItem.setText(Messages.getString("configure.backup")); //$NON-NLS-1$

	}

	public void set() {
	}

	public void applyChanges() {
		File backupDir = new File(settings.getBackupDirectory());

		if (tempPropsFile != null && !tempPropsFile.equals( settings.getBackupDirectory() )) {
			if (OperationOnFile.getDirList(new File(tempPropsFile)).size() == 0) {
				if (new File(tempPropsFile).canWrite()) {

					if (backupDir.exists()) {
						OperationOnFile.moveDirectory(backupDir, tempPropsFile);
					}

					try {
						backupPathText.setText(tempPropsFile);
						settings.setBackupDirectory(tempPropsFile);
						new SokkerViewerSettingsDao(PropertiesDatabase.getSession()).updateSokkerViewerSettings(settings);
						MessageDialog.openInformationMessage(composite.getShell(), Messages.getString("message.db.move.text")); //$NON-NLS-1$
					} catch (IOException e) {
						MessageDialog.openErrorMessage(composite.getShell(), e.getLocalizedMessage());
					} catch (SVException e) {
						MessageDialog.openErrorMessage(composite.getShell(), e.getLocalizedMessage());
					}
				} else {
					MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("message.db.backup.path.permissions.error.text")); //$NON-NLS-1$
				}
			} else {
				MessageDialog.openErrorMessage(composite.getShell(), Messages.getString("message.db.backup.dir.empty.error.text")); //$NON-NLS-1$
			}
		}
	}

	public void restoreDefaultChanges() {
		setWidgets();
	}

}
