package pl.pronux.sokker.ui.plugins;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Report;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.tables.ReportsTable;

public class ViewOffice implements IPlugin {

	private Composite composite;

	private TreeItem treeItem;

	private ReportsTable table;

	private List<Report> reports;

	public void clear() {
	}

	public void dispose() {
	}

	public Composite getComposite() {
		return this.composite;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoOffice");
	}

	public TreeItem getTreeItem() {
		return this.treeItem;
	}

	public void init(Composite composite) {
		this.composite = composite;
		this.composite.setLayout(new FillLayout());
		table = new ReportsTable(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		this.composite.layout();
	}

	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
	}

	public void setSvBean(SvBean svBean) {

	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
		this.treeItem.setText(Messages.getString("tree.ViewOffice"));
		this.treeItem.setImage(ImageResources.getImageResources("office.png"));
	}

	public void set() {
		reports = Cache.getReports();
		table.fill(reports);
	}

	public void reload() {
	}

}
