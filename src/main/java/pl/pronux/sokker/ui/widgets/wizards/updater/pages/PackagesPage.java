package pl.pronux.sokker.ui.widgets.wizards.updater.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;
import pl.pronux.sokker.updater.model.PackagesCollection;

public class PackagesPage extends Page {
	public static final String PAGE_NAME = "PACKAGES_PAGE"; //$NON-NLS-1$
	private Tree treePackages;
	private Text infoText;
	private PackagesCollection packagesCollection;

	public PackagesPage(Wizard parent) {
		super(parent, Messages.getString("updater.page.packages"), PAGE_NAME); //$NON-NLS-1$
	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		GridData data = new GridData();
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		treePackages = new Tree(container, SWT.SINGLE | SWT.BORDER);
		treePackages.setBackground(container.getBackground());
		treePackages.setLinesVisible(true);
		treePackages.setHeaderVisible(false);
		treePackages.setLayoutData(data);

		new TreeColumn(treePackages, SWT.NONE).pack();

		infoText = new Text(container, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		infoText.setBackground(container.getBackground());
		infoText.setLayoutData(data);
	}

	@Override
	public void onEnterPage() {
		packagesCollection = ((XMLPage) getWizard().getPage(XMLPage.PAGE_NAME)).getPackagesCollection();

		treePackages.removeAll();
		for (int i = 0; i < packagesCollection.getPackages().size(); i++) {
			new TreeItem(treePackages, SWT.NONE).setText(packagesCollection.getPackages().get(i).getName());
		}

		for (int i = 0; i < treePackages.getColumnCount(); i++) {
			treePackages.getColumn(i).pack();
		}
		
		infoText.setText(packagesCollection.getInfo());

		super.onEnterPage();
	}
}
