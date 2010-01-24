package pl.pronux.sokker.ui.configure;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.Fonts;

public class Configurator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Configurator(new Shell(new Display()), SWT.CLOSE | SWT.MAX | SWT.RESIZE);
	}

	private Button cancelButton;

	private IViewConfigure currentComposite;

	private Label footerLine;

	private CLabel header;

	private Label headerLine;

	private Button okButton;

	private Sash sash;

	private Label sashLine;

	private Shell shell;

	private Tree tree;

	private Button restoreDefaultButton;

	private Button applyButton;

	private ArrayList<IViewConfigure> alView;

	private ScrolledComposite sc1;
	private CLabel info;

	private SokkerViewerSettings settings;

	public Configurator(final Shell parent, int style) {
		settings = SettingsHandler.getSokkerViewerSettings();

		shell = new Shell(parent, style);
		Configurator.this.setVisible(false);

		shell.setLayout(new FormLayout());

		if (parent.getImage() != null) {
			shell.setImage(parent.getImage());
		}

		shell.setSize(2 * (shell.getDisplay().getPrimaryMonitor().getClientArea().width / 3), shell.getDisplay().getPrimaryMonitor().getClientArea().height - 50);

		Rectangle shellRect = shell.getBounds();
		Rectangle displayRect = shell.getDisplay().getPrimaryMonitor().getClientArea();
		int x = (displayRect.width - shellRect.width) / 2;
		int y = (displayRect.height - shellRect.height) / 2;
		shell.setLocation(x, y);

		// shell.addDisposeListener(new DisposeListener() {
		// public void widgetDisposed(DisposeEvent arg0) {
		// parent.setEnabled(true);
		// }
		// });

		shell.addListener(SWT.Close, new Listener() {

			public void handleEvent(Event event) {
				Configurator.this.setVisible(false);
				event.doit = false;
			}
		});

		addFooter(shell);
		addSash(shell);
		addTree(shell);
		addHeader(shell);

		initView(shell);

		okButton.setText(Messages.getString("button.ok")); //$NON-NLS-1$
		cancelButton.setText(Messages.getString("button.cancel")); //$NON-NLS-1$
		applyButton.setText(Messages.getString("button.apply")); //$NON-NLS-1$
		restoreDefaultButton.setText(Messages.getString("button.restore")); //$NON-NLS-1$
		// restoreDefaultButton.pack();
		// applyButton.pack();
		// cancelButton.pack();
		// okButton.pack();
		info.setText(Messages.getString("message.information.restart")); //$NON-NLS-1$

		if (alView.get(0).getComposite() != null) {
			header.setText(alView.get(0).getTreeItem().getText());
			currentComposite.getComposite().layout();
		}
		// shell.open();

		// while (!shell.isDisposed()) {
		// if (!shell.getDisplay().readAndDispatch())
		// shell.getDisplay().sleep();
		// }

	}

	private void addFooter(final Shell shell) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(100, -40);
		formData.left = new FormAttachment(0, 1);
		formData.right = new FormAttachment(100, -1);
		// formData.height = 0;

		footerLine = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		footerLine.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(footerLine, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);

		Composite composite = new Composite(shell, SWT.NONE);
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
		cancelButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				for (int i = 0; i < alView.size(); i++) {
					alView.get(i).restoreDefaultChanges();
				}
				shell.close();
			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(centerPoint, 0, SWT.CENTER);
		formData.right = new FormAttachment(cancelButton, -10);
		formData.width = 100;

		okButton = new Button(composite, SWT.NONE);
		okButton.setLayoutData(formData);
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				for (int i = 0; i < alView.size(); i++) {
					alView.get(i).applyChanges();
				}
				shell.close();
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(0, 1);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(okButton, -20);
		formData.bottom = new FormAttachment(100, -1);

		info = new CLabel(composite, SWT.WRAP);
		info.setLayoutData(formData);

		formData = new FormData();
		formData.bottom = new FormAttachment(footerLine, -5);
		formData.right = new FormAttachment(100, -10);
		formData.width = 150;

		restoreDefaultButton = new Button(shell, SWT.NONE);
		restoreDefaultButton.setLayoutData(formData);
		restoreDefaultButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				currentComposite.restoreDefaultChanges();
			}
		});

		formData = new FormData();
		formData.bottom = new FormAttachment(footerLine, -5);
		formData.right = new FormAttachment(restoreDefaultButton, -10);
		formData.width = 150;

		applyButton = new Button(shell, SWT.NONE);
		applyButton.setLayoutData(formData);
		applyButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				currentComposite.applyChanges();
			}

		});

	}

	private void addHeader(Shell shell) {
		FormData formData = new FormData();
		formData.left = new FormAttachment(sashLine, 10);
		formData.top = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.height = 30;

		header = new CLabel(shell, SWT.NONE);
		header.setLayoutData(formData);
		header.setAlignment(SWT.CENTER);
		header.setBackground(ColorResources.getColor(248, 247, 243));
		header.setFont(Fonts.getFont(shell.getDisplay(), "Arial", 13, SWT.BOLD)); //$NON-NLS-1$

		formData = new FormData();
		formData.top = new FormAttachment(header, 10);
		formData.left = new FormAttachment(sashLine, 1);
		formData.right = new FormAttachment(100, -1);

		headerLine = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		headerLine.setLayoutData(formData);
	}

	public void addItem(IViewConfigure view) {

	}

	private void addSash(Shell shell) {

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(footerLine, 0);
		formData.left = new FormAttachment(0, 150);

		sash = new Sash(shell, SWT.VERTICAL);
		sash.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		sash.setLayoutData(formData);
		sash.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((FormData) sash.getLayoutData()).left = new FormAttachment(0, event.x);
				sash.getParent().layout();
			}
		});

		formData = new FormData();
		formData.top = new FormAttachment(0, 1);
		formData.bottom = new FormAttachment(footerLine, 1);
		formData.left = new FormAttachment(sash, 0);

		sashLine = new Label(shell, SWT.SEPARATOR | SWT.VERTICAL);
		sashLine.setLayoutData(formData);
	}

	private void addTree(Shell shell) {

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(footerLine, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(sashLine, 0);

		tree = new Tree(shell, SWT.SINGLE);
		tree.setLayoutData(formData);
		tree.setFont(ConfigBean.getFontMain());

		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				TreeItem item = tree.getItem(point);

				if (item != null) {
					while (item.getParentItem() != null) {
						item = item.getParentItem();
					}
					showView(((IViewConfigure) item.getData(IViewConfigure.IDENTIFIER))); //$NON-NLS-1$
					header.setText(item.getText());
				}
			}
		});
	}

	private void initView(Shell shell) {

		FormData formData = new FormData();
		formData.left = new FormAttachment(sashLine, 0);
		formData.top = new FormAttachment(headerLine, 0);
		formData.bottom = new FormAttachment(applyButton, -5);
		formData.right = new FormAttachment(100, 0);

		sc1 = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
		sc1.setLayoutData(formData);
		sc1.setExpandHorizontal(true);
		sc1.setExpandVertical(true);
		sc1.setLayout(new FormLayout());

		alView = new ArrayList<IViewConfigure>();
		alView.add(new ViewGeneral());
		alView.add(new ViewPlugins());
		alView.add(new ViewLookAndFeel());
		alView.add(new ViewProxy());
		alView.add(new ViewPassword());
		alView.add(new ViewBackup());
//		alView.add(new ViewTemplates());

		ArrayList<IPlugin> alPlugins = ViewerHandler.getViewer().getPluginsList();

		for (int i = 0; i < alPlugins.size(); i++) {
			if (alPlugins.get(i).getConfigureComposite() != null) {
				alView.add(alPlugins.get(i).getConfigureComposite());
			}
		}

		FormData cformData = new FormData();
		cformData.top = new FormAttachment(0, 0);
		cformData.left = new FormAttachment(0, 0);
		cformData.right = new FormAttachment(100, 0);
		cformData.bottom = new FormAttachment(100, 0);
		for (int i = 0; i < alView.size(); i++) {
			IViewConfigure view = alView.get(i);

			view.setSettings(settings);

			Composite composite = new Composite(sc1, SWT.NONE);
			sc1.setContent(composite);

			view.setTreeItem(new TreeItem(tree, SWT.NONE));

			view.init(composite);

			view.getComposite().setLayoutData(cformData);

			view.getTreeItem().setData(IViewConfigure.IDENTIFIER, view); //$NON-NLS-1$

			view.getComposite().setVisible(false);
		}

		if (alView.get(0).getComposite() != null) {
			currentComposite = alView.get(0);
			showView(currentComposite);
			header.setText(alView.get(0).getTreeItem().getText());
		}
	}

	public void setView() {
		for (int i = 0; i < alView.size(); i++) {
			alView.get(i).set();
		}
	}

	public void setVisible(boolean visible) {
		if (visible) {
			shell.getParent().setEnabled(false);
		} else {
			shell.getParent().setEnabled(true);
		}
		shell.setVisible(visible);
	}

	private void showView(IViewConfigure composite) {
		currentComposite.getComposite().setVisible(false);
		currentComposite = composite;
		currentComposite.getComposite().setVisible(true);
		sc1.setContent(currentComposite.getComposite());
		Rectangle r = sc1.getClientArea();
		sc1.setMinSize(sc1.getContent().computeSize(r.width - 30, SWT.DEFAULT));
	}

}
