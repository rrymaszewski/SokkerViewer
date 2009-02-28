package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.tables.TrainingConfigurationTable;

public class TrainingConfigurationGroup extends Group {

	private TrainingConfigurationTable table;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public TrainingConfigurationGroup(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontMain());
		this.setLayout(new FillLayout());
		table = new TrainingConfigurationTable(this, SWT.FULL_SELECTION);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.table.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	public void fill(Integer[][] settings) {
		table.fill(settings);
	}
	
	public Integer[][] getValues() {
		return table.getValues();
	}

	public void setType(int type) {
		table.setType(type);
	}
}
