package pl.pronux.sokker.ui.widgets.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.custom.Monitor;
import pl.pronux.sokker.ui.widgets.custom.ProgressBarCustom;

public class ProgressBarDialog extends Shell {
	private ProgressBarCustom progressBar;
	private Button cancelButton;
	private Button closeButton;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}
	public ProgressBarDialog(Shell shell, int style) {
		super(shell, style);
		
		init(this);
		this.setSize(400, 150);
		org.eclipse.swt.widgets.Monitor _monitor = shell.getDisplay().getPrimaryMonitor();
		Rectangle splashRect = this.getBounds();
		Rectangle displayRect = _monitor.getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		this.setLocation(x, y);
	}
	
	private void init(Shell parent) {
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0,5);
		formData.top = new FormAttachment(0,5);
		formData.right = new FormAttachment(100,-5);
		
		progressBar = new ProgressBarCustom(parent, SWT.NONE);
		progressBar.setLayoutData(formData);
		
		formData = new FormData();
		formData.right = new FormAttachment(100,-5);
		formData.bottom = new FormAttachment(100,-5);
		
		cancelButton = new Button(this, SWT.PUSH);
		cancelButton.setLayoutData(formData);
		cancelButton.setText(Messages.getString("button.cancel")); //$NON-NLS-1$
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				progressBar.cancel();
			}
		});
		
		formData = new FormData();
		formData.right = new FormAttachment(cancelButton,-10);
		formData.bottom = new FormAttachment(100,-5);
		
		closeButton = new Button(this, SWT.PUSH);
		closeButton.setLayoutData(formData);
		closeButton.setText(Messages.getString("button.close")); //$NON-NLS-1$
		closeButton.setEnabled(false);
		closeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event arg0) {
				ProgressBarDialog.this.close();
			}
		});
		
		this.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				progressBar.cancel();
			}
		});

		this.setLocation(this.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - this.getBounds().width / 2, this.getDisplay().getPrimaryMonitor().getClientArea().height / 2 - this.getBounds().height / 2);
	}
	
	public Monitor getProgressMonitor() {
		return progressBar.getProgressMonitor();
	}
	
	public void run(boolean fork, boolean cancellable, final boolean autoclose, final IRunnableWithProgress runnable) throws InterruptedException, InvocationTargetException {
		
		if(autoclose) {
			closeButton.setVisible(false);
		}
		
		if(!cancellable) {
			cancelButton.setVisible(false);
		}
		
		this.progressBar.run(fork, cancellable, runnable);
		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				final Monitor monitor = progressBar.getProgressMonitor();
				while ((!monitor.isDone() && !monitor.isCanceled() && !monitor.isInterrupted()) || progressBar.getRunningThreads() != 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				
				if ((monitor.isDone() || monitor.isCanceled()) && !monitor.isInterrupted()) {
					ProgressBarDialog.this.getDisplay().asyncExec(new Runnable() {
						public void run() {
							if(autoclose) {
								ProgressBarDialog.this.close();
							} else {
								closeButton.setEnabled(true);
								cancelButton.setEnabled(false);
							}
						}
					});
				}

				if (monitor.isInterrupted()) {
					ProgressBarDialog.this.getDisplay().syncExec(new Runnable() {
						public void run() {
							closeButton.setEnabled(true);
							cancelButton.setEnabled(false);
						}
					});
				}
				
			}
		});

		monitorThread.setDaemon(true);
		monitorThread.start();
		this.open();
	}
	
}
