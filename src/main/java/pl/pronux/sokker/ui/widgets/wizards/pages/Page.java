package pl.pronux.sokker.ui.widgets.wizards.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.ui.widgets.wizards.Wizard;

public class Page extends Composite {

	private String title;
	private String pageName;
	private Composite container;
	private Wizard wizard;

	public Page(Wizard parent, String title, String pageName) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());
		createControl(this);
		this.setTitle(title);
		this.setPageName(pageName);
		this.setWizard(parent);
	}
	
	public Wizard getWizard() {
		return wizard;
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}
	
	protected void createControl(Composite parent) {
	}

	protected Composite getContainer() {
		return container;
	}

	protected void setContainer(Composite container) {
		this.container = container;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public void onEnterPage() {
	}
	
	public void onCancelPage() {
	}
	
	public void onNextPage() {
	}
	
	public void onFinishPage() {
	}
}
