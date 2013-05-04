package pl.pronux.sokker.ui.widgets.custom;

import pl.pronux.sokker.interfaces.ProgressMonitor;

public class Monitor implements ProgressMonitor {

	private boolean canceled;
	private boolean done;
	private boolean subTask;
	private String subTaskName;
	private String taskName;
	private int totalTime;
	private int worked;
	private boolean interrupted;
	private Throwable throwable;

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public Monitor() {
		done = false;
	}

	public void beginTask(String task, int totalTime) {
		setTotalTime(totalTime);
		setTaskName(task);
		setWorked(0);
	}

	public void done() {
		setDone(true);
	}

	public String getSubTaskName() {
		return subTaskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public int getWorked() {
		return worked;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public boolean isDone() {
		return done;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	private void setSubTaskName(String subTaskName) {
		this.subTaskName = subTaskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
		setSubTask(false);
		setSubTaskName(""); 
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public void setWorked(int worked) {
		this.worked = worked;
	}

	public void subTask(String subTaskName) {
		this.setSubTaskName(subTaskName);
		this.setSubTask(true);
	}

	public void worked(int worked) {
		setWorked(worked + this.worked);
	}

	public boolean isSubTask() {
		return subTask;
	}

	private void setSubTask(boolean subTask) {
		this.subTask = subTask;
	}

	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
	
	public void interrupt() {
		this.setInterrupted(true);
	}

}
