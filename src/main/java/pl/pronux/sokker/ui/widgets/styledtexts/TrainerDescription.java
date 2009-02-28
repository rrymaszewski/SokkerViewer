package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class TrainerDescription extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		//super.checkSubclass();
	}
	
	public TrainerDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}

	public void setInfo(Coach coach) {
		String[][] values;
		this.setRedraw(false);
		this.setText("");
		
		values = new String[7][2];
		values[0][0] = Messages.getString("coach.id");
		values[1][0] = Messages.getString("coach.name");
		values[2][0] = Messages.getString("coach.surname");
		values[3][0] = Messages.getString("coach.general");
		values[4][0] = Messages.getString("coach.age");
		values[5][0] = Messages.getString("coach.country");
		values[6][0] = Messages.getString("coach.salary");

		values[0][1] = String.valueOf(coach.getId());
		values[1][1] = coach.getName();
		values[2][1] = coach.getSurname();
		values[3][1] = Messages.getString("skill.a" + coach.getGeneralskill()) + " [" + coach.getGeneralskill() + "]";
		values[4][1] = String.valueOf(coach.getAge());
		values[5][1] = Messages.getString("country." + coach.getCountryfrom() + ".name");
		values[6][1] = coach.getSalary().formatDoubleCurrencySymbol();

		for (int i = 0; i < values.length; i++) {
			this.addText(values[i]);
		}		
		this.setRedraw(true);
	}

	private void addText(Object[] values) {
		this.append(String.format("%-25s%-15s" + NEW_LINE, values));
	}
}
