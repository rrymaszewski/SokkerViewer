package pl.pronux.sokker.ui.configure;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.widgets.shells.AddTemplateShell;

public class ViewTemplates implements IViewConfigure {

	private Composite composite;
	private TreeItem treeItem;

	public void applyChanges() {
		// TODO Auto-generated method stub

	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Composite getComposite() {
		return composite;
	}

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void init(final Composite composite) {
		this.composite = composite;

		this.composite.setLayout (new FormLayout());

		Text previewText = new Text(composite, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI);
		FormData formData = new FormData ();
		formData.left = new FormAttachment(0,5);
		formData.bottom = new FormAttachment(100,-5);
		formData.right = new FormAttachment(100, -5);
		formData.height = 150;
		previewText.setLayoutData(formData);
		
		Button addButton = new Button (composite, SWT.PUSH);
		addButton.setText (Messages.getString("add"));
		formData = new FormData ();
		formData.left = new FormAttachment(100, -130);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0,5);
		
		addButton.setLayoutData (formData);
		addButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				new AddTemplateShell(composite.getShell(), SWT.DIALOG_TRIM).open();
			}
			
		});

		Button deleteButton = new Button (composite, SWT.PUSH);
		formData = new FormData ();
		formData.left = new FormAttachment(100, -130);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(addButton,5);
		deleteButton.setText (Messages.getString("delete"));
		deleteButton.setLayoutData (formData);
		
		Button exportButton = new Button (composite, SWT.PUSH);
		formData = new FormData ();
		formData.left = new FormAttachment(100, -130);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(deleteButton,25);
		exportButton.setText (Messages.getString("export"));
		exportButton.setLayoutData (formData);
		
		Button importButton = new Button (composite, SWT.PUSH);
		formData = new FormData ();
		formData.left = new FormAttachment(100, -130);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(exportButton,5);
		importButton.setText (Messages.getString("import"));
		importButton.setLayoutData (formData);

		Table table = new Table (composite, SWT.PUSH | SWT.CHECK | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		formData = new FormData ();
		formData.left = new FormAttachment(0,5);
		formData.top = new FormAttachment(0,5);
		formData.right = new FormAttachment(addButton, -5);
		formData.bottom = new FormAttachment(previewText, -5);
		table.setLayoutData (formData);

		composite.layout();
	}

	public void restoreDefaultChanges() {
		// TODO Auto-generated method stub

	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
		// TODO Auto-generated method stub

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("configurator.templates"));

	}

	public void set() {
		// TODO Auto-generated method stub

	}

}
