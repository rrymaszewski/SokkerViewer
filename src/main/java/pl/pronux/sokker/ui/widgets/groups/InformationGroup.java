package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class InformationGroup extends Group {

	private CLabel label;

	public InformationGroup(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setText(Messages.getString("InformationGroup.information")); 
		setLayout(new FillLayout());
		label = new CLabel(this, SWT.NONE);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());
		label.setBackground(this.getBackground());
	}

	public void setNote(String note) {
		this.label.setText(note);
	}
	public String getNote() {
		return label.getText();
	}
	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

}
