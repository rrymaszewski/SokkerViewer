package pl.pronux.sokker.ui.widgets.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;

public class Wizard extends Shell {

	private CLabel header;
	private Label headerLine;
	private Label footerLine;
	private Button cancelButton;
	private Button nextButton;
	private CLabel info;
	private ScrolledComposite sc1;
	private Page currentPage;
	private Button previousButton;
	private List<Page> pages = new ArrayList<Page>();
	private Map<String, Page> pagesMap = new HashMap<String, Page>();
	private FormData pageFormData;
	private Button finishButton;

	public Wizard(Display display) {
		super(display);
		addContainer();
	}

	public Wizard(Shell parent) {
		super(parent);
		addContainer();
	}

	public Wizard(Shell parent, int decorator) {
		super(parent, decorator);
		addContainer();
	}

	private void addButtons() {
		FormData formData = new FormData();
		formData.top = new FormAttachment(100, -40);
		formData.left = new FormAttachment(0, 1);
		formData.right = new FormAttachment(100, -1);
		// formData.height = 0;

		footerLine = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		footerLine.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(footerLine, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(formData);
		composite.setLayout(new FormLayout());

		formData = new FormData(0, 0);
		formData.top = new FormAttachment(50, 0);
		formData.right = new FormAttachment(100, 0);

		Label centerPoint = new Label(composite, SWT.NONE);
		centerPoint.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(centerPoint, 0, SWT.CENTER);
		formData.right = new FormAttachment(100, -10);
		formData.width = 100;

		cancelButton = new Button(composite, SWT.NONE);
		cancelButton.setLayoutData(formData);
		cancelButton.setText(Messages.getString("button.cancel")); 
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if(currentPage != null) {
					currentPage.onCancelPage();					
				}
				Wizard.this.close();
			}
		});
		
		formData = new FormData();
		formData.top = new FormAttachment(centerPoint, 0, SWT.CENTER);
		formData.right = new FormAttachment(cancelButton, -10);
		formData.width = 100;

		finishButton = new Button(composite, SWT.NONE);
		finishButton.setLayoutData(formData);
		finishButton.setText(Messages.getString("button.finish")); 
		finishButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				if(currentPage != null) {
					currentPage.onFinishPage();
				}
				Wizard.this.close();
			}
		});
		finishButton.setEnabled(false);

		formData = new FormData();
		formData.top = new FormAttachment(centerPoint, 0, SWT.CENTER);
		formData.right = new FormAttachment(finishButton, -10);
		formData.width = 100;

		nextButton = new Button(composite, SWT.NONE);
		nextButton.setLayoutData(formData);
		nextButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int index = pages.indexOf(currentPage);
				if(index >= 0) {
					currentPage.onNextPage();
					previousButton.setEnabled(true);
					if(index == pages.size() - 2) {
						nextButton.setEnabled(false);
					}
					
					if (pages.size() > index + 1) {
						Page page = pages.get(index + 1);
						showPage(page);
						page.onEnterPage();
					}
				}
				sc1.setContent(currentPage);
				Rectangle r = sc1.getClientArea();
				sc1.setMinSize(sc1.getContent().computeSize(r.width - 30, SWT.DEFAULT));
			}
		});
		nextButton.setText(Messages.getString("button.next")); 

		formData = new FormData();
		formData.top = new FormAttachment(centerPoint, 0, SWT.CENTER);
		formData.right = new FormAttachment(nextButton, -10);
		formData.width = 100;

		previousButton = new Button(composite, SWT.NONE);
		previousButton.setLayoutData(formData);
		previousButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int index = pages.indexOf(currentPage);
				
				if (index == 1) {
					previousButton.setEnabled(false);
				}
				if (index == pages.size() - 1) {
					nextButton.setEnabled(true);
				}
				
				if (index > 0) {
					showPage(pages.get(index - 1));
				}
				sc1.setContent(currentPage);
				Rectangle r = sc1.getClientArea();
				sc1.setMinSize(sc1.getContent().computeSize(r.width - 30, SWT.DEFAULT));
			}
		});

		previousButton.setText(Messages.getString("button.previous")); 

		formData = new FormData();
		formData.top = new FormAttachment(0, 1);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(previousButton, -20);
		formData.bottom = new FormAttachment(100, -1);

		info = new CLabel(composite, SWT.WRAP);
		info.setLayoutData(formData);

	}
	
	public Button getCancelButton() {
		return cancelButton;
	}

	public Button getNextButton() {
		return nextButton;
	}
	
	public Button getFinishButton() {
		return finishButton;
	}

	public Button getPreviousButton() {
		return previousButton;
	}

	private void addContainer() {
		this.setLayout(new FormLayout());
		addHeader();
		addButtons();
		addPage();
		this.layout();
		// this.setSize((this.getDisplay().getPrimaryMonitor().getClientArea().width
		// / 2), 2 *
		// (this.getDisplay().getPrimaryMonitor().getClientArea().height /3) );
		this.setSize(650, 450);
		Rectangle thisRect = this.getBounds();
		Rectangle displayRect = this.getDisplay().getPrimaryMonitor().getClientArea();
		int x = (displayRect.width - thisRect.width) / 2;
		int y = (displayRect.height - thisRect.height) / 2;
		this.setLocation(x, y);
	}

	private void addHeader() {
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.top = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.height = 30;

		header = new CLabel(this, SWT.NONE);
		header.setLayoutData(formData);
		header.setAlignment(SWT.CENTER);
		header.setBackground(ColorResources.getColor(248, 247, 243));
		header.setFont(Fonts.getFont(this.getDisplay(), "Arial", 13, SWT.BOLD)); 

		formData = new FormData();
		formData.top = new FormAttachment(header, 10);
		formData.left = new FormAttachment(0, 1);
		formData.right = new FormAttachment(100, -1);

		headerLine = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		headerLine.setLayoutData(formData);
	}

	private void addPage() {
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 25);
		formData.top = new FormAttachment(headerLine, 25);
		formData.bottom = new FormAttachment(footerLine, -15);
		formData.right = new FormAttachment(100, -25);

		sc1 = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
		sc1.setLayoutData(formData);
		sc1.setExpandHorizontal(true);
		sc1.setExpandVertical(true);
		sc1.setLayout(new FormLayout());

		pageFormData = new FormData();
		pageFormData.top = new FormAttachment(0, 0);
		pageFormData.left = new FormAttachment(0, 0);
		pageFormData.right = new FormAttachment(100, 0);
		pageFormData.bottom = new FormAttachment(100, 0);
		// if (alView.get(0).getComposite() != null) {
		// currentComposite = alView.get(0);
		// showView(currentComposite);
		// header.setText(alView.get(0).getTreeItem().getText());
		// }
	}

	protected void addPage(Page page) {
		pages.add(page);
		pagesMap.put(page.getPageName(), page);
		page.setParent(sc1);
		page.setLayoutData(pageFormData);
		page.setVisible(false);
	}

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public Page getPage(String pageName) {
		return pagesMap.get(pageName);
	}

	@Override
	public void open() {
		if (pages.size() > 0) {
			showPage(pages.get(0));
			if (pages.size() > 1) {
				nextButton.setEnabled(true);
			}
			pages.get(0).onEnterPage();
		}

		previousButton.setEnabled(false);
		super.open();
		while (!this.isDisposed()) {
			if (!this.getDisplay().readAndDispatch()) {
				this.getDisplay().sleep();
			}
		}
	}

	private void showPage(Page page) {
		if (currentPage != null) {
			currentPage.setVisible(false);
		}
		currentPage = page;
		currentPage.setVisible(true);
		header.setText(page.getTitle());
		sc1.setContent(currentPage);
		Rectangle r = sc1.getClientArea();
		sc1.setMinSize(sc1.getContent().computeSize(r.width - 30, SWT.DEFAULT));
	}
}
