package pl.pronux.sokker.interfaces;

public interface IProgressMonitor {
	public static int UNKNOWN = -1;
	public void beginTask(String task, int totalTime);
	public boolean isCanceled();
	public void setCanceled(boolean canceled);
	public void worked(int worked);
	public void setTaskName(String taskName);
	public void subTask(String subTaskName);
	public void done();
	public int getWorked();
	public int getTotalTime();
	public void setTotalTime(int totalTime);
	public boolean isInterrupted();
	public void interrupt();
}
