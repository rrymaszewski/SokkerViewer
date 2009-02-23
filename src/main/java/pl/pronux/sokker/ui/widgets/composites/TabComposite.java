package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;

import pl.pronux.sokker.ui.beans.ConfigBean;

public class TabComposite extends Composite {

	private Sash sashHorizontal;
	private FormData sashFormData;
	private FormData viewFormData;
	private FormData descriptionFormData;
	private DescriptionDoubleComposite descriptionComposite;
	private ChartDateComposite graphComposite;
	private Table viewTable;

	public TabComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		setVisible(false);
		
		sashHorizontal = new Sash(this, SWT.HORIZONTAL | SWT.NONE);

		sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 200);
		sashFormData.right = new FormAttachment(100, 0);
		sashFormData.left = new FormAttachment(0, 0);

		sashHorizontal.setLayoutData(sashFormData);
		sashHorizontal.setVisible(true);
		
		sashHorizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((FormData) sashHorizontal.getLayoutData()).top = new FormAttachment(0,
						event.y);
				sashHorizontal.getParent().layout();
			}
		});

		viewFormData = new FormData();
		viewFormData.top = new FormAttachment(sashHorizontal, 0);
		viewFormData.right = new FormAttachment(100, 0);
		viewFormData.left = new FormAttachment(0, 0);
		viewFormData.bottom = new FormAttachment(100, 0);
		
		descriptionFormData = new FormData();
		descriptionFormData.top = new FormAttachment(0, 0);
		descriptionFormData.right = new FormAttachment(100, 0);
		descriptionFormData.left = new FormAttachment(0, 0);
		descriptionFormData.bottom = new FormAttachment(sashHorizontal, 0);
		
		descriptionComposite = new DescriptionDoubleComposite(this, SWT.BORDER);
		descriptionComposite.setLayoutData(descriptionFormData);
		descriptionComposite.setVisible(true);
		descriptionComposite.setFont(ConfigBean.getFontDescription());

		descriptionComposite.setLeftDescriptionStringFormat("%-20s%-15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setLeftFirstColumnSize(20);
		descriptionComposite.setLeftSecondColumnSize(15);

		descriptionComposite.setRightDescriptionStringFormat("%-20s%-15s\r\n"); //$NON-NLS-1$
		descriptionComposite.setRightFirstColumnSize(20);
		descriptionComposite.setRightSecondColumnSize(15);
		
		graphComposite = new ChartDateComposite(this, SWT.BORDER);
		graphComposite.setLayoutData(descriptionFormData);
		graphComposite.setVisible(false);

	}

	public FormData getViewFormData() {
		return viewFormData;
	}
	
	public DescriptionDoubleComposite getDescriptionComposite() {
		return descriptionComposite;
	}

	public void setDescriptionComposite(
			DescriptionDoubleComposite descriptionComposite) {
		this.descriptionComposite = descriptionComposite;
	}

	public Table getViewTable() {
		return viewTable;
	}

	public void setViewTable(Table viewTable) {
		this.viewTable = viewTable;
	}

	public ChartDateComposite getGraphComposite() {
		return graphComposite;
	}

	public void setGraphComposite(ChartDateComposite graphComposite) {
		this.graphComposite = graphComposite;
	}

	public void clearAll() {
		this.getViewTable().removeAll();
		
	}
	
}
