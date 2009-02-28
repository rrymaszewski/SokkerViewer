package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class TrainerDescriptionAdds extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		//super.checkSubclass();
	}
	
	public TrainerDescriptionAdds(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}
	
	public void setInfo(Coach coach) {
		String[][] values;
		
		this.setRedraw(false);
		this.setText("");

		values = new String[10][2];
		values[0][0] = Messages.getString("coach.training");
		values[1][0] = Messages.getString("coach.stamina");
		values[2][0] = Messages.getString("coach.pace");
		values[3][0] = Messages.getString("coach.technique");
		values[4][0] = Messages.getString("coach.passing");
		values[5][0] = Messages.getString("coach.keeper");
		values[6][0] = Messages.getString("coach.defender");
		values[7][0] = Messages.getString("coach.playmaker");
		values[8][0] = Messages.getString("coach.scorer");
		values[9][0] = Messages.getString("coach.general");
		values[0][1] = "";
		values[1][1] = Messages.getString("skill.a" + coach.getStamina()) + " [" + coach.getStamina() + "]";
		values[2][1] = Messages.getString("skill.a" + coach.getPace()) + " [" + coach.getPace() + "]";
		values[3][1] = Messages.getString("skill.a" + coach.getTechnique()) + " [" + coach.getTechnique() + "]";
		values[4][1] = Messages.getString("skill.a" + coach.getPassing()) + " [" + coach.getPassing() + "]";
		values[5][1] = Messages.getString("skill.a" + coach.getKeepers()) + " [" + coach.getKeepers() + "]";
		values[6][1] = Messages.getString("skill.a" + coach.getDefenders()) + " [" + coach.getDefenders() + "]";
		values[7][1] = Messages.getString("skill.a" + coach.getPlaymakers()) + " [" + coach.getPlaymakers() + "]";
		values[8][1] = Messages.getString("skill.a" + coach.getScorers()) + " [" + coach.getScorers() + "]";
		values[9][1] = "[" + coach.getSummarySkill() + "]";

		for (int i = 0; i < values.length; i++) {
			this.addText(values[i]);
		}

		this.setRedraw(true);
	}
	
	private void addText(Object[] values) {
		this.append(String.format("%-25s%-15s" + NEW_LINE, values));
	}

}
