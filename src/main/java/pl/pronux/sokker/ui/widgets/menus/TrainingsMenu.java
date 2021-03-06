package pl.pronux.sokker.ui.widgets.menus;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.widgets.shells.TrainingEditShell;
import pl.pronux.sokker.ui.widgets.shells.TrainingReportShell;

public class TrainingsMenu extends Menu {

	private TrainingsMenu menu;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public TrainingsMenu(final Decorations parent, int style) {
		super(parent, style);
		menu = this;
		MenuItem item;

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("trainings.menu.item.report")); 
		item.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (menu.getData(Training.class.getName()) != null && menu.getData(Training.class.getName()) instanceof Training) {
					Training training = (Training) menu.getData(Training.class.getName()); 
					new TrainingReportShell(parent.getShell(), SWT.CLOSE | SWT.PRIMARY_MODAL, training).open();
				}
			}
		});

		item = new MenuItem(this, SWT.PUSH);
		item.setText(Messages.getString("trainings.menu.item.edit")); 
		item.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (menu.getData(Training.class.getName()) != null && menu.getData(Training.class.getName()) instanceof Training) {
					Training training = (Training) menu.getData(Training.class.getName());
					ArrayList<Coach> coaches = new ArrayList<Coach>();
					coaches.addAll(Cache.getCoaches());
					coaches.addAll(Cache.getCoachesFired());
					coaches.addAll(Cache.getCoachesTrash());

					TrainingEditShell trainingView = new TrainingEditShell(menu.getShell(), SWT.PRIMARY_MODAL, coaches, training);
					trainingView.open();
				}
			}
		});

	}
}
