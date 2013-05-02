package pl.pronux.sokker.interfaces;

import java.lang.reflect.InvocationTargetException;

public interface IRunnableWithProgress {

	void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

	void onFinish();
}
