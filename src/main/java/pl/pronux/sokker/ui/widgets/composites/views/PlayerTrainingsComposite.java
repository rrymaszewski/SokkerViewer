package pl.pronux.sokker.ui.widgets.composites.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.widgets.composites.TrainingDescriptionComposite;
import pl.pronux.sokker.ui.widgets.composites.ViewComposite;
import pl.pronux.sokker.ui.widgets.tables.PlayerTrainingsTable;

public class PlayerTrainingsComposite extends ViewComposite {

	private TrainingDescriptionComposite trainingDescription;
	private PlayerTrainingsTable trainingTable;

	public PlayerTrainingsComposite(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		
		trainingDescription = new TrainingDescriptionComposite(this, SWT.BORDER);
		trainingDescription.setLayoutData(getDescriptionFormData());
		trainingDescription.setVisible(true);
		
		trainingTable = new PlayerTrainingsTable(this, SWT.BORDER);
		FormData tableFormData = getViewFormData();
		tableFormData.bottom = new FormAttachment(100, -35);
		trainingTable.setLayoutData(tableFormData);
		
		FormData toolbarFormData = new FormData();
		toolbarFormData.left = new FormAttachment(0,0);
		toolbarFormData.top = new FormAttachment(trainingTable, 5);
		toolbarFormData.bottom = new FormAttachment(100, 0);
		toolbarFormData.right = new FormAttachment(100,0);
		
		ToolBar toolbar = new ToolBar(this, SWT.HORIZONTAL | SWT.FLAT);
		toolbar.setLayoutData(toolbarFormData);
		toolbar.setFont(ConfigBean.getFontMain());
		
		ToolItem item = new ToolItem(toolbar, SWT.RADIO);
		item.setText(Messages.getString("training.main"));
		item.setSelection(true);
		
		item = new ToolItem(toolbar, SWT.RADIO);
		item.setText(Messages.getString("training.general"));
		
	}

	public void fill(Player player) {
		if(player.getSkills()[0].getTraining() != null) {
			trainingDescription.setInfo(player.getSkills()[player.getSkills().length-1].getTraining());	
		}
	}
}
