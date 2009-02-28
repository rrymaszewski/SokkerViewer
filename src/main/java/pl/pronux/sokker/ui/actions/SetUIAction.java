package pl.pronux.sokker.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.interfaces.IProgressMonitor;
import pl.pronux.sokker.interfaces.IRunnableWithProgress;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;

public class SetUIAction implements IRunnableWithProgress {

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		ArrayList<IPlugin> pluginsList = ViewerHandler.getViewer().getPluginsList();
		monitor.beginTask(Messages.getString("SetUIAction.info"), pluginsList.size()); //$NON-NLS-1$

		for (IPlugin plugin : pluginsList) {
			if (monitor.isCanceled() || monitor.isInterrupted()) {
				return;
			}
			monitor.subTask(plugin.getStatusInfo());
			ViewerHandler.getViewer().setPlugin(plugin);
			monitor.worked(1);
		}
		if (monitor.isCanceled() || monitor.isInterrupted()) {
			return;
		}
		ViewerHandler.getViewer().setView();
		
		ViewerHandler.getViewer().setLastUpdateDate(Messages.getString("statusBar.lastUpdateLabel.text") + " " + Cache.getDate().toDateTimeString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void onFinish() {
	}

}
