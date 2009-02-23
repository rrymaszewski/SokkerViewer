package pl.pronux.sokker.ui.widgets.tabs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.ui.beans.ConfigBean;

public class SVTabFolder extends CTabFolder {
	private Map<String, CTabItem> itemsMap = new HashMap<String, CTabItem>();
	private Map<String, Composite> compositesMap = new HashMap<String, Composite>(); 
	
	public SVTabFolder(Composite parent, int style) {
		super(parent, style);
		this.setFont(ConfigBean.getFontMain());
	}

	@Override
	protected void checkSubclass() {
//		super.checkSubclass();
	}
	
	protected void addItem(String name, Composite composite) {
		CTabItem item = new CTabItem(this, SWT.NONE);
		item.setControl(composite);
		item.setText(name);
		compositesMap.put(name, composite);
		itemsMap.put(name, item);
	}
	
	protected void setItem(String name, Composite composite) {
		CTabItem item = itemsMap.get(name);
		if(item != null) {
			item.setControl(composite);
		}
	}
	
	protected CTabItem getItem(String name) {
		return itemsMap.get(name);
	}
	
	protected Composite getComposite(String name) {
		return compositesMap.get(name);
	}
	
	protected void setText(String name, String text) {
		CTabItem item = itemsMap.get(name);
		if(item != null) {
			item.setText(text);
		}
		
	}
	
}
