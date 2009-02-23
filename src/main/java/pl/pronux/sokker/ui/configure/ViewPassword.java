package pl.pronux.sokker.ui.configure;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.utils.security.Password;

public class ViewPassword implements IViewConfigure {

	private TreeItem treeItem;
	private Composite composite;
	private Button generatePasswordButton;
	private CLabel generatePasswordLabel;
	private ProgressBar passwordProgressBar;
	private Text passwordText;
	private SokkerViewerSettings settings;

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
		// formData.height = 25;

		generatePasswordLabel = new CLabel(composite, SWT.NONE);
		generatePasswordLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(generatePasswordLabel, 10);
		formData.left = new FormAttachment(0, 10);
		formData.width = 200;
		formData.height = (int)composite.getFont().getFontData()[0].height + 4;

		passwordText = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(formData);
		// passwordText.setEchoChar('*');
		passwordText.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event arg0) {
				Text item = (Text) arg0.widget;
				String text = item.getText();
				Double strength = new Password(text).getStrength();
				passwordProgressBar.setSelection(strength.intValue());
				if (passwordProgressBar.getSelection() < passwordProgressBar.getMaximum() / 2) {
					passwordProgressBar.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
				} else {
					passwordProgressBar.setBackground(composite.getBackground());
				}
			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(generatePasswordLabel, 10);
		formData.left = new FormAttachment(passwordText, 10);
		formData.width = 200;

		passwordProgressBar = new ProgressBar(composite, SWT.SMOOTH);
		passwordProgressBar.setLayoutData(formData);
		passwordProgressBar.setMinimum(0);
		passwordProgressBar.setMaximum(50);
		passwordProgressBar.setSelection(0);

		formData = new FormData();
		formData.top = new FormAttachment(passwordProgressBar, 10);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		// formData.height = 25;

		generatePasswordLabel = new CLabel(composite, SWT.NONE);
		generatePasswordLabel.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(generatePasswordLabel, 5);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100,-10);

		final Table passwordTable = new Table(composite, SWT.SINGLE | SWT.BORDER);
		passwordTable.setLinesVisible(true);
		passwordTable.setHeaderVisible(false);
		passwordTable.setLayoutData(formData);
		passwordTable.setBackground(composite.getBackground());

		for (int i = 0; i < 5; i++) {
			TableColumn column = new TableColumn(passwordTable, SWT.NONE);
			column.setText("         "); //$NON-NLS-1$
		}


		for (int i = 0; i < 10; i++) {
			TableItem item = new TableItem(passwordTable, SWT.NONE);
			item.setText("         "); //$NON-NLS-1$
		}

		for(int i = 0; i < passwordTable.getColumnCount(); i++) {
			passwordTable.getColumn(i).pack();
		}

		formData = new FormData();
		formData.top = new FormAttachment(passwordTable, 10);
		formData.left = new FormAttachment(passwordTable, 0, SWT.CENTER);

		generatePasswordButton = new Button(composite, SWT.NONE | SWT.FLAT);
		generatePasswordButton.setLayoutData(formData);
		generatePasswordButton.pack();
		generatePasswordButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				fillPasswordTable(passwordTable);
			}

		});

		generatePasswordButton.setText(Messages.getString("configure.password.button.generate")); //$NON-NLS-1$
		generatePasswordLabel.setText(Messages.getString("configure.password.label.generate")); //$NON-NLS-1$
		generatePasswordLabel.setText(Messages.getString("configure.password.label.check")); //$NON-NLS-1$
		treeItem.setText(Messages.getString("configure.password")); //$NON-NLS-1$

		composite.layout(true);
	}

	private void fillPasswordTable(Table table) {
		table.clearAll();
		for(int i = 0 ; i < table.getItemCount(); i++) {
			for(int j = 0 ; j < table.getColumnCount(); j++) {
				table.getItem(i).setText(j, new Password().getPassword());
			}
		}

		for(int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}

	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		this.settings = sokkerViewerSettings;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		treeItem.setText(Messages.getString("configure.password")); //$NON-NLS-1$

	}

	public void set() {
		passwordText.setText(settings.getPassword());
	}

	public void applyChanges() {

	}

	public void restoreDefaultChanges() {

	}

}
