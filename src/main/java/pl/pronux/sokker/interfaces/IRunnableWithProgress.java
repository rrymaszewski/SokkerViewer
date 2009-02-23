package pl.pronux.sokker.interfaces;

import java.lang.reflect.InvocationTargetException;



public interface IRunnableWithProgress {
	
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

}
