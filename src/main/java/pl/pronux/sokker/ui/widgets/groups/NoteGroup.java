package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class NoteGroup extends Group {

	private Text text;

	public NoteGroup(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setText(Messages.getString("note")); 
		setLayout(new FillLayout());
		text = new Text(this, SWT.LEFT | SWT.MULTI | SWT.WRAP);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());
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
