package pl.pronux.sokker.ui.widgets.items;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.ui.beans.ConfigBean;

public class DayExpandItem extends ExpandItem {

	private Table table;

	public DayExpandItem(ExpandBar bar, int styles) {
		super(bar, styles);
		init(bar);
	}

	public DayExpandItem(ExpandBar bar, int styles, int index) {
		super(bar, styles, index);
		init(bar);
	}

	private void init(ExpandBar bar) {
		Composite composite = new Composite(bar, SWT.NONE);
		composite.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		table = new Table(composite, SWT.FULL_SELECTION);
		table.setBackground(composite.getBackground());
		table.setLayoutData(formData);
		table.setLinesVisible(false);
		table.setHeaderVisible(false);
		table.setFont(ConfigBean.getFontTable());
		table.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		new TableColumn(table, SWT.LEFT);

		this.setControl(composite);
		this.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
		this.setExpanded(true);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public void fill(List<String> values) {
		table.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		table.removeAll();
		for (String value : values) {
			new TableItem(table, SWT.NONE).setText("- " + value);
		}
		table.getColumn(0).pack();
	}

	public void append(String value) {
		new TableItem(table, SWT.NONE).setText(value);
	}

	public Table getTable() {
		return table;
	}

	public void pack() {
		this.table.pack();
		this.setHeight(table.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 5);
	}

	public void mark() {
		table.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
		this.setExpanded(true);
	}

}
