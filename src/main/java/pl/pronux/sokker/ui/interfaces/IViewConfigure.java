package pl.pronux.sokker.ui.interfaces;



import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.model.SokkerViewerSettings;

/**
 * @author rymek
 * @version 0.8.3.0
 *
 */
public interface IViewConfigure  {
	
	String IDENTIFIER = "IViewConfigure"; 
	/**
	 * method run before shell dispose
	 * in this method all resources like fonts, images, colors 
	 * should be disposed
	 */
	void dispose();
	
	
	/**
	 * method run on start and on reload views
	 * it should be used to clear objects in view
	 * for example table.removeAll() etc. 
	 */
	void clear();
	
	/**
	 * method run after get data into SokkerBean
	 * it should set all objects in view
	 */
	void set();
	
	/**
	 * method run before get data into SokkerBean
	 * initialize sokkerViewer and show defaults view
	 * @param composite
	 */
	void init(Composite composite);
	
	/**
	 * Method for set data for treeItem which is in mainTree
	 * @param treeItem this handler should be used to set ico of item, text of item etc.
	 */
	void setTreeItem(TreeItem treeItem);
	
//	public void setSokkerBean(final SokkerBean sokkerBean);
	
	/**
	 * Method for get reference to Composite
	 * Body should contains <i>return this</i>;
	 * @return Composite
	 */
	Composite getComposite();

	/**
	 * Methods sets default properties
	 * @param sokkerViewerSettings Properties configuration file
	 */
	void setSettings(SokkerViewerSettings sokkerViewerSettings);
	
	/**
	 * Return TreeItem handler 
	 * @return TreeItem
	 */
	TreeItem getTreeItem();
	
	/**
	 * Apply temporary changes
	 * 
	 */
	void applyChanges();

	
	/**
	 * Restore changes
	 */
	void restoreDefaultChanges();
		
}
