package pl.pronux.sokker.importer.model;

import pl.pronux.sokker.model.Date;

public interface IXMLpack {

	boolean isComplete();

	void setComplete(boolean complete);

	Date getDate();

	void setDate(Date date);
	
	boolean isImported();
	
	void setImported(boolean imported);

}