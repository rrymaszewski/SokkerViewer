package pl.pronux.sokker.interfaces;

import java.lang.reflect.InvocationTargetException;

public interface RunnableWithProgress {

	void run(ProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

	void onFinish();
}
