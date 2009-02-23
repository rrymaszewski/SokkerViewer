package pl.pronux.tools.pages;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class FilesPage extends Page {

	public static String PAGE_NAME = "FILES_PAGE";
	private Table table;

	private Set<String> files = new HashSet<String>();

	public Set<String> getFiles() {
		return files;
	}

	public void setFiles(Set<String> files) {
		this.files = files;
	}

	public FilesPage(Wizard parent) {
		super(parent, "Files page", PAGE_NAME);
	}

	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 20);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -100);
		formData.bottom = new FormAttachment(100, -10);

		table = new Table(container, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		table.setBackground(container.getBackground());
		table.setLayoutData(formData);

		new TableColumn(table, SWT.LEFT);
		table.getColumn(0).pack();

		formData = new FormData();
		formData.top = new FormAttachment(0, 20);
		formData.left = new FormAttachment(table, 5);
		formData.height = 25;
		formData.right = new FormAttachment(100, -5);

		Button button = new Button(container, SWT.PUSH);
		button.setText("add");
		button.setLayoutData(formData);

		button.addListener(SWT.Selection, new Listener() {

			private String[] filenames;
			private String directory;

			public void handleEvent(Event arg0) {
				String[] extensions = {
					"*.xml;*.jar;*.zip;*.exe"
				};
				String defaultPath = "";
				FileDialog fileDialog = new FileDialog(container.getShell(), SWT.OPEN | SWT.MULTI);
				if (new File(defaultPath).exists()) {
					fileDialog.setFilterPath(defaultPath);
				}
				fileDialog.setText("Open File");
				fileDialog.setFilterExtensions(extensions);

				fileDialog.open();
				filenames = fileDialog.getFileNames();
				directory = fileDialog.getFilterPath() + File.separator;

				if (filenames != null) {
					if (filenames.length > 0) {
						for (int i = 0; i < filenames.length; i++) {
							new TableItem(table, SWT.NONE).setText(directory + filenames[i]);
							files.add(directory + filenames[i]);
						}
						table.getColumn(0).pack();
					}
				}
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(button, 20);
		formData.left = new FormAttachment(table, 5);
		formData.height = 25;
		formData.right = new FormAttachment(100, -5);

		button = new Button(container, SWT.PUSH);
		button.setText("remove");
		button.setLayoutData(formData);

		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < table.getSelectionIndices().length; i++) {
					files.remove(table.getItem(table.getSelectionIndices()[i]).getText());
				}
				table.remove(table.getSelectionIndices());
			}
		});

		setContainer(container);
	}
}
