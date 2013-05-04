package pl.pronux.sokker.interfaces;

public interface ProgressMonitor {
	int UNKNOWN = -1;
	void beginTask(String task, int totalTime);
	boolean isCanceled();
	void setCanceled(boolean canceled);
	void worked(int worked);
	void setTaskName(String taskName);
	void subTask(String subTaskName);
	void done();
	int getWorked();
	int getTotalTime();
	void setTotalTime(int totalTime);
	boolean isInterrupted();
	void interrupt();
}
