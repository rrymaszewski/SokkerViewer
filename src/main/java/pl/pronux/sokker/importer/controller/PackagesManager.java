package pl.pronux.sokker.importer.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import pl.pronux.sokker.importer.model.IXMLpack;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;

public abstract class PackagesManager implements IRunnableWithProgress {

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
	}
	
	public abstract ArrayList<IXMLpack> getPackages();

	public abstract void setPackages(ArrayList<IXMLpack> packages) ;

}
