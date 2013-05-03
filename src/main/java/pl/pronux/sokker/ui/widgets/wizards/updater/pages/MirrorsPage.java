package pl.pronux.sokker.ui.widgets.wizards.updater.pages;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.resources.FlagsResources;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class MirrorsPage extends Page {
	public static final String PAGE_NAME = "MIRRORS_PAGE"; 
	private Table mirrorTable;
	private String mirror;

	public String getMirror() {
		return mirror;
	}

	public MirrorsPage(Wizard parent) {
		super(parent, Messages.getString("updater.page.mirrorspage"), PAGE_NAME); 

	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);

		mirrorTable = new Table(container, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		mirrorTable.setLayoutData(formData);
		// mirrorTable.setBackground(composite.getBackground());
		mirrorTable.setLinesVisible(true);
		mirrorTable.setHeaderVisible(false);

		new TableColumn(mirrorTable, SWT.NONE).pack();
		new TableColumn(mirrorTable, SWT.NONE).pack();

		String root = "sv/updates/"; 
		ArrayList<String[]> mirrors = new ArrayList<String[]>();

		mirrors.add(new String[] {
				"icpnet.pl", 
				"http://www.rymek.user.icpnet.pl/" + root, 
				"1" 
		});

		mirrors.add(new String[] {
				"sokkerviewer.net", 
				"http://sokkerviewer.net/" + root, 
				"1" 
		});

		mirrors.add(new String[] {
				"sv.sokker.us", 
				"http://sv.sokker.us/" + root, 
				"4" 
		});
		TableItem item;
		for (int i = 0; i < mirrors.size(); i++) {
			item = new TableItem(mirrorTable, SWT.NONE);
			item.setText(0, mirrors.get(i)[0]);
			item.setText(1, mirrors.get(i)[1]);
			item.setImage(FlagsResources.getFlag(Integer.valueOf(mirrors.get(i)[2])));
		}

		for (int i = 0; i < mirrorTable.getColumnCount(); i++) {
			mirrorTable.getColumn(i).pack();
			mirrorTable.getColumn(i).setWidth(mirrorTable.getColumn(i).getWidth() + 20);
		}
	}

	@Override
	public void onNextPage() {
		if (mirrorTable.getItemCount() > 0) {
			if (mirrorTable.getSelection().length == 1) {
				setMirror(mirrorTable.getSelection()[0].getText(1));
			} else {
				setMirror(mirrorTable.getItem(0).getText(1));
			}
		}
	}

	private void setMirror(String mirror) {
		this.mirror = mirror;
	}

}
