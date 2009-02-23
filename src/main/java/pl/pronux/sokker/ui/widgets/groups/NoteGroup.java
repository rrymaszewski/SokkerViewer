package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class NoteGroup extends Group {

	private Text text;

	public NoteGroup(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setText(Messages.getString("note")); //$NON-NLS-1$
		setLayout(new FillLayout());
		text = new Text(this, SWT.LEFT | SWT.MULTI | SWT.WRAP);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(ColorResources.getBlueDescription());
	}

	public void setNote(String note) {
		this.text.setText(note);
	}
	public String getNote() {
		return text.getText();
	}
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

}
