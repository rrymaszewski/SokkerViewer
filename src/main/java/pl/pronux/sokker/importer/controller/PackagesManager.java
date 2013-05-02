package pl.pronux.sokker.importer.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;

public abstract class PackagesManager implements IRunnableWithProgress {

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
	}
	
	public abstract List<IXMLpack> getPackages();

	public abstract void setPackages(List<IXMLpack> packages) ;

}
