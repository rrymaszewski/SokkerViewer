package pl.pronux.sokker.ui.widgets.groups;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class ReportsGroup extends Group {
	private StyledText styledText;

	public ReportsGroup(Composite arg0, int arg1) {
		super(arg0, arg1);
		this.setText(Messages.getString("ReportsGroup.reports")); //$NON-NLS-1$
		setLayout(new FillLayout());
		styledText = new StyledText(this, SWT.H_SCROLL | SWT.READ_ONLY);
		this.setFont(ConfigBean.getFontMain());
		this.setForeground(Colors.getBlueDescription());
		styledText.setBackground(this.getBackground());
		styledText.setFont(ConfigBean.getFontMain());
	}

	public void fill(List<Report> reports) {
		styledText.setText(""); //$NON-NLS-1$
		int i = 0;
		while ( i < 11 && i < reports.size() ) {
			Report report = reports.get(i);
			int start = styledText.getText().length();
			addText(String.format("%s %s\r\n", report.getDate().toDateTimeString(), report.getMessage())); //$NON-NLS-1$
			int length = styledText.getText().length() - start;
			
			if(report.getStatus() == Report.COST) {
				if (!report.isChecked()) {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getRed(), styledText.getBackground(), SWT.BOLD));
				} else {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getRed(), styledText.getBackground(), SWT.NORMAL));	
				}
			} else if (report.getStatus() == Report.INCOME) {
				if (!report.isChecked()) {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getDarkGreen(), styledText.getBackground(), SWT.BOLD));
				} else {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getDarkGreen(), styledText.getBackground(), SWT.NORMAL));
				}
			} else if (report.getStatus() == Report.INFO) {
				if (!report.isChecked()) {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getColor(59, 91, 253), styledText.getBackground(), SWT.BOLD));
				} else {
					styledText.setStyleRange(new StyleRange(start + 19, length - 19, ColorResources.getColor(59, 91, 253), styledText.getBackground(), SWT.NORMAL));
				}
			}
			i++;
		}
	}

	private void addText(String text) {
		styledText.append(text);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

}
