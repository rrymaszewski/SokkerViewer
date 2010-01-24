package pl.pronux.sokker.ui.widgets.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;

public class ViewComposite extends Composite {

	private FormData descriptionFormData;

	private FormData sashFormData;

	private Sash sashHorizontal;

	private FormData viewFormData;
	
	public ViewComposite(Composite parent, int style) {

		super(parent, style);

		setLayout(new FormLayout());

		sashHorizontal = new Sash(this, SWT.HORIZONTAL);

		sashFormData = new FormData();
		sashFormData.top = new FormAttachment(0, 230);
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

	}

	public void setLayoutView(FormData formData) {
		setLayoutData(formData);
	}

	public Composite getComposite() {
		return this;
	}
	private Control currentDesc;

	private Control currentView;

	protected void showView(Control control) {
		if (currentView == null) {
			currentView = control;
		}
		currentView.setVisible(false);
		currentView = control;
		currentView.setVisible(true);
	}

	protected void showDescription(Control control) {
		if (currentDesc == null) {
			currentDesc = control;
		}
		currentDesc.setVisible(false);
		currentDesc = control;
		currentDesc.setVisible(true);
	}
	public Control getCurrentDesc() {
		return currentDesc;
	}

	public Control getCurrentView() {
		return currentView;
	}

	public FormData getDescriptionFormData() {
		return descriptionFormData;
	}

	public void setDescriptionFormData(FormData descriptionFormData) {
		this.descriptionFormData = descriptionFormData;
	}

	public FormData getSashFormData() {
		return sashFormData;
	}

	public void setSashFormData(FormData sashFormData) {
		this.sashFormData = sashFormData;
	}

	public Sash getSashHorizontal() {
		return sashHorizontal;
	}

	public void setSashHorizontal(Sash sashHorizontal) {
		this.sashHorizontal = sashHorizontal;
	}

	public FormData getViewFormData() {
		return viewFormData;
	}

	public void setViewFormData(FormData viewFormData) {
		this.viewFormData = viewFormData;
	}
}