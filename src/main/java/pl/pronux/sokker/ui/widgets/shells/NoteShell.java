package pl.pronux.sokker.ui.widgets.shells;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.actions.PersonsManager;
import pl.pronux.sokker.model.Person;
import pl.pronux.sokker.resources.Messages;

public class NoteShell extends Shell {

	private StyledText note;

	private Label charLimit;

	private Button okButton;

	private Button cancelButton;

	private Group notePlugin;

	private Person person;

	private Listener okListener;

	private NoteShell shell;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public NoteShell(final Shell parent, int style) {
		super(parent, style);
		this.shell = this;

		this.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		// formData.bottom = new FormAttachment(100, -40);
		// formData.right = new FormAttachment(100, -5);
		formData.width = 400;
		formData.height = 170;

		notePlugin = new Group(this, SWT.NONE);
		notePlugin.setLayout(new FormLayout());
		notePlugin.setLayoutData(formData);
		notePlugin.setText(Messages.getString("table.note")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -30);
		formData.right = new FormAttachment(100, -5);

		note = new StyledText(notePlugin, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		note.setLayoutData(formData);
		note.setTextLimit(500);
		note.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event arg0) {
				charLimit.setText(Messages.getString("viewnt.message.limit.label") + ": " + (note.getTextLimit() - note.getText().length())); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(note, 5);
		formData.right = new FormAttachment(100, -5);

		charLimit = new Label(notePlugin, SWT.NONE);
		charLimit.setLayoutData(formData);
		charLimit.setText(Messages.getString("viewnt.message.limit.label") + note.getTextLimit()); //$NON-NLS-1$
		charLimit.pack();

		formData = new FormData();
		formData.width = 50;
		formData.top = new FormAttachment(notePlugin, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(50, -5);

		okListener = new Listener() {
			public void handleEvent(Event event) {

				if (note.getText().length() > 500) {
					return;
				}

				if (person != null) {
					person.setNote(note.getText());
					try {
						PersonsManager.updatePersonNote(person);
					} catch (SQLException e) {
						new BugReporter(NoteShell.this.getDisplay()).openErrorMessage("NoteShell -> update person", e);
					}

					shell.close();
				}
			}
		};

		okButton = new Button(this, SWT.NONE);
		okButton.setText(Messages.getString("button.ok")); //$NON-NLS-1$
		okButton.setLayoutData(formData);
		okButton.pack();
		okButton.addListener(SWT.Selection, okListener);

		formData = new FormData();
		formData.top = new FormAttachment(notePlugin, 5);
		formData.bottom = new FormAttachment(100, -5);
		formData.left = new FormAttachment(50, 5);

		cancelButton = new Button(this, SWT.NONE);

		cancelButton.setLayoutData(formData);
		cancelButton.pack();
		cancelButton.setText(Messages.getString("button.cancel")); //$NON-NLS-1$
		cancelButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				shell.close();
			}

		});
		this.pack();

		Rectangle shellRect = this.getBounds();
		Rectangle displayRect = this.getDisplay().getPrimaryMonitor().getBounds();
		int x = (displayRect.width - shellRect.width) / 2;
		int y = (displayRect.height - shellRect.height) / 2;
		this.setLocation(x, y);
	}

	public void setOkListener(Listener listener) {
		okButton.addListener(SWT.Selection, listener);
	}

	public void setPerson(Person person) {
		this.person = person;
		note.setText(person.getNote());
		this.setText(person.getSurname() + " " + person.getName()); //$NON-NLS-1$
	}

	@Override
	public void open() {
		super.open();
		while (!this.isDisposed()) {
			if (!this.getDisplay().readAndDispatch()) {
				this.getDisplay().sleep();
			}
		}
	}
}
