package pl.pronux.sokker.importer.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.interfaces.ProgressMonitor;
import pl.pronux.sokker.interfaces.RunnableWithProgress;

public abstract class PackagesManager implements RunnableWithProgress {

	public void run(ProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
	}
	
	public abstract List<IXMLpack> getPackages();

	public abstract void setPackages(List<IXMLpack> packages) ;

}
