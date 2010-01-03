package pl.pronux.sokker.ui.widgets.custom;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;

import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.ui.handlers.DisplayHandler;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;

public class ProgressBarCustom extends Composite {

	private ProgressBar progressBar;
	private CLabel taskLabel;
	private CLabel subtaskLabel;
	private Monitor monitor;
	private boolean cancellable;
	private boolean fork;
	private int running = 0;
	private IRunnableWithProgress runnable;

	public ProgressBarCustom(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FormLayout());

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);

		taskLabel = new CLabel(this, SWT.NONE);
		taskLabel.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(taskLabel, 10);
		formData.height = 20;

		if ((style & SWT.INDETERMINATE) != 0) {
			progressBar = new ProgressBar(this, SWT.INDETERMINATE);
		} else {
			progressBar = new ProgressBar(this, SWT.NONE);
		}

		progressBar.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(progressBar, 10);
		subtaskLabel = new CLabel(this, SWT.NONE);
		subtaskLabel.setLayoutData(formData);

		this.layout();

		this.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event arg0) {
				if (ProgressBarCustom.this.runnable != null) {
					ProgressBarCustom.this.runnable.onFinish();	
				}
			}
		});
	}

	private synchronized void addThread() {
		running++;
	}

	private synchronized void removeThread() {
		running--;
	}

	public synchronized int getRunningThreads() {
		return running;
	}

	public void run(boolean fork, boolean cancellable, final IRunnableWithProgress runnable) throws InterruptedException, InvocationTargetException {
		monitor = new Monitor();
		this.cancellable = cancellable;
		this.fork = fork;
		this.runnable = runnable;
		Thread monitorThread = new Thread() {
			@Override
			public void run() {
				addThread();
				try {
					while (!monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) {
						DisplayHandler.getDisplay().asyncExec(new Runnable() {
							public void run() {
								if (!ProgressBarCustom.this.isDisposed()) {
									progressBar.setMaximum(monitor.getTotalTime());
									progressBar.setSelection(monitor.getWorked());
									taskLabel.setText(monitor.getTaskName());
									if (monitor.isSubTask()) {
										subtaskLabel.setText(monitor.getSubTaskName());
									} else {
										subtaskLabel.setText(""); //$NON-NLS-1$
									}
								}
							}
						});
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
					}
					if (!progressBar.isDisposed()) {
						DisplayHandler.getDisplay().syncExec(new Runnable() {
							public void run() {
								if (monitor.isDone() && !monitor.isCanceled()) {
									progressBar.setMaximum(monitor.getTotalTime());
									progressBar.setSelection(monitor.getWorked());
									taskLabel.setText(monitor.getTaskName());
									if (monitor.isSubTask()) {
										subtaskLabel.setText(monitor.getSubTaskName());
									} else {
										subtaskLabel.setText(""); //$NON-NLS-1$
									}
								}
							}
						});
					}
					if (!progressBar.isDisposed()) {
						DisplayHandler.getDisplay().syncExec(new Runnable() {
							public void run() {
								if ((monitor.isCanceled() || monitor.isInterrupted())) {
									progressBar.setMaximum(1);
									progressBar.setSelection(1);
									taskLabel.setText(""); //$NON-NLS-1$
									subtaskLabel.setText(""); //$NON-NLS-1$
									monitor.done();
								}
							}
						});
					}
				} finally {
					removeThread();
				}
			}
		};
		monitorThread.setDaemon(true);
		monitorThread.start();
		Thread workThread = new Thread() {
			@Override
			public void run() {
				addThread();

				try {
					runnable.run(monitor);
				} catch (final InvocationTargetException e) {
					monitor.setThrowable(e);
					monitor.setInterrupted(true);
					ProgressBarCustom.this.getDisplay().syncExec(new Runnable() {
						public void run() {
							if (e.getCause() != null) {
								Throwable throwable = e.getCause();
								new BugReporter(ProgressBarCustom.this.getShell()).openErrorMessage(throwable.getMessage(), throwable);
							} else {
								new BugReporter(ProgressBarCustom.this.getShell()).openErrorMessage("ProgressBarCustom", e); //$NON-NLS-1$
							}
						}
					});
				} catch (InterruptedException e) {
					monitor.setThrowable(e);
					monitor.setInterrupted(true);
				} finally {
					removeThread();
				}
			}
		};

		workThread.setDaemon(true);
		workThread.start();
	}

	public void cancel() {
		if (isCancellable()) {
			this.monitor.setCanceled(true);
		}
	}

	public boolean isCancellable() {
		return cancellable;
	}

	public void setCancellable(boolean cancellable) {
		this.cancellable = cancellable;
	}

	public boolean isFork() {
		return fork;
	}

	public void setFork(boolean fork) {
		this.fork = fork;
	}

	public Monitor getProgressMonitor() {
		return monitor;
	}

}
