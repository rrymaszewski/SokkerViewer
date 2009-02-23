package pl.pronux.sokker.importer.model;

import pl.pronux.sokker.model.Date;

public interface IXMLpack {

	public boolean isComplete();

	public void setComplete(boolean complete);

	public Date getDate();

	public void setDate(Date date);
	
	public boolean isImported();
	
	public void setImported(boolean imported);

}