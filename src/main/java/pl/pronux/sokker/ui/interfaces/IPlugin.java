package pl.pronux.sokker.ui.interfaces;



import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;

/**
 * @author rymek
 * @version 0.7.2.7
 *
 */
public interface IPlugin extends SV {
	final public static String IDENTIFIER = "IPlugin"; //$NON-NLS-1$
	/**
	 * method run before shell dispose
	 * in this method all resources like fonts, images, colors 
	 * should be disposed
	 */
	public void dispose();
	
	
	/**
	 * method run on start and on reload views
	 * it should be used to clear objects in view
	 * for example table.removeAll() etc. 
	 */
	public void clear();
	
	/**
	 * method run after get data into SokkerBean
	 * it should set all objects in view
	 */
	public void set();
	
	/**
	 * method run before get data into SokkerBean
	 * initialize sokkerViewer and show defaults view
	 * @param composite
	 */
	public void init(Composite composite);
	
	/**
	 * only for tests
	 */
	public String getInfo();

//	/**
//	 * Method is used to set default parameters for Composite
//	 * @param formData default widht, height and position for Composite it should be used like Composite.setLayoutData(formData)
//	 *
//	 */
//	public void setLayoutView(FormData formData);
	
	/**
	 * Method for set data for treeItem which is in mainTree
	 * @param treeItem this handler should be used to set ico of item, text of item etc.
	 */
	public void setTreeItem(TreeItem treeItem);
	
//	public void setSokkerBean(final SokkerBean sokkerBean);
	
	/**
	 * Method for get reference to Composite
	 * Body should contains <i>return this</i>;
	 * @return Composite
	 */
	public Composite getComposite();

	/**
	 * Method return string which is used to show current state in statusBar
	 * @return String
	 */
	public String getStatusInfo();

//	/**
//	 * @deprecated
//	 * Method need to be used to show/hide Composite
//	 * it should be used like : 
//	 * this.setVisible(false); 
//	 * @param visible
//	 */
//	public void setVisible(boolean visible);

	/**
	 * Methods sets default properties
	 * @param sokkerViewerSettings Properties configuration file
	 */
	public void setSettings(SokkerViewerSettings sokkerViewerSettings);
	
	/**
	 * Return TreeItem handler 
	 * @return TreeItem
	 */
	public TreeItem getTreeItem();
	
	/**
	 * Method not used yet
	 * @return Composite
	 */
	public IViewConfigure getConfigureComposite();
	
	/**
	 * insert bean with data
	 * @param svBean
	 */
	public void setSvBean(SvBean svBean);
	
	public void reload();
}
