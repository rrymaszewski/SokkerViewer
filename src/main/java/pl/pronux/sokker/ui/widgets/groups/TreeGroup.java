package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.tree.SVTree;

public class TreeGroup extends Group {

	private SVTree tree;

	public TreeGroup(Composite composite, int style) {
		super(composite, style);
		this.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(0, 25);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.HORIZONTAL);
		toolBar.setLayoutData(formData);

		ToolItem collapse = new ToolItem(toolBar, SWT.NONE);
		collapse.setImage(ImageResources.getImageResources("tree_collapse.png")); 
		collapse.setSelection(false);

		collapse.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				tree.collapseAll();
			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(toolBar, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		Label separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(separator, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		tree = new SVTree(this, SWT.NONE);
		tree.setLayoutData(formData);
		tree.setFont(ConfigBean.getFontTable());

	}

	public SVTree getTree() {
		return tree;
	}
	
	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
}
