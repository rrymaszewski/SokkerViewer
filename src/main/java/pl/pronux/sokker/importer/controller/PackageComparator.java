package pl.pronux.sokker.importer.controller;

import java.util.Comparator;

import pl.pronux.sokker.importer.model.IXMLpack;

public class PackageComparator implements Comparator<IXMLpack> {
	
	public int compare(IXMLpack pack1, IXMLpack pack2) {
		int rc = 0;
		rc = pack1.getDate().compareTo(pack2.getDate());
		return rc;
	}


}