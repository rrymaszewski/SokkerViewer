package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.bean.TrainingSummary;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class TrainingDescriptionAdds extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public TrainingDescriptionAdds(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}

	public void setInfo(Training training) {
		this.setRedraw(false);
		this.setText("");
		String text;
		text = String.format("%-25s%-15s", Messages.getString("training.date"), training.getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString()); //$NON-NLS-1$ //$NON-NLS-2$
		this.append(text);
		this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
		this.append(NEW_LINE);

		text = String.format("%-25s%-15s", Messages.getString("training.date.insert"), training.getDate().toDateString()); //$NON-NLS-1$ //$NON-NLS-2$
		this.append(text);
		this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
		this.append(NEW_LINE);

		text = String.format("%-25s%-15d", Messages.getString("training.week"), training.getDate().getTrainingDate(SokkerDate.THURSDAY).getSokkerDate().getSeasonWeek()); //$NON-NLS-1$ //$NON-NLS-2$
		this.append(text);
		this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
		this.append(NEW_LINE);

		this.append(NEW_LINE);

		if (training.getJuniorCoach() != null) {
			String skill = getSkill(training.getJuniorCoach().getGeneralskill());
			text = String.format("%-25s %s", Messages.getString("coach.job.juniors"), skill); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
			this.addStyle(this.getText().length() - skill.length(), 4, Colors.getGray(), this.getBackground(), SWT.NORMAL);
			this.addStyle(this.getText().length() - skill.length() + 4, skill.length() - 4, Colors.getTrainerGeneralSkill(), this.getBackground(), SWT.BOLD);
		} else {
			text = String.format("%-25s %s", Messages.getString("coach.job.juniors"), "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.append(text);
			this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
		}
		this.append(NEW_LINE);
		this.append(NEW_LINE);
		this.append(NEW_LINE);
		this.append(NEW_LINE);
		this.append(NEW_LINE);
		this.append(NEW_LINE);

		this.append(String.format("%s:", Messages.getString("summary")));
		this.append(NEW_LINE);
		this.append(NEW_LINE);

		TrainingSummary trainedSkillSummary = training.getTrainingSummary();
		
		if (trainedSkillSummary.getStaminaPops() > 0 || trainedSkillSummary.getStaminaFalls() > 0) {
			text = String.format("%-25s", Messages.getString("training.skill.stamina")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
			if (trainedSkillSummary.getStaminaPops() > 0) {
				text = String.format("+%d", trainedSkillSummary.getStaminaPops());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkGreen(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
			this.append("/");
			if (trainedSkillSummary.getStaminaFalls() > 0) {
				text = String.format("-%d", trainedSkillSummary.getStaminaFalls());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getRed(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
		} else {
			text = String.format("%-25s -/-", Messages.getString("training.skill.stamina")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
		}
		this.append(NEW_LINE);
		
		if (trainedSkillSummary.getTrainedSkillsPops() > 0 || trainedSkillSummary.getTrainedSkillsFalls() > 0) {
			text = String.format("%-25s", Messages.getString("training.skill.trained")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
			if (trainedSkillSummary.getTrainedSkillsPops() > 0) {
				text = String.format("+%d", trainedSkillSummary.getTrainedSkillsPops());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkGreen(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
			this.append("/");
			if (trainedSkillSummary.getTrainedSkillsFalls() > 0) {
				text = String.format("-%d", trainedSkillSummary.getTrainedSkillsFalls());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getRed(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
		} else {
			text = String.format("%-25s -/-", Messages.getString("training.skill.trained")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
		}
		this.append(NEW_LINE);

		if (trainedSkillSummary.getOthersSkillsPops() > 0 || trainedSkillSummary.getOthersSkillsFalls() > 0) {
			text = String.format("%-25s", Messages.getString("training.skill.others")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
			if (trainedSkillSummary.getOthersSkillsPops() > 0) {
				text = String.format("+%d", trainedSkillSummary.getOthersSkillsPops());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkGreen(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
			this.append("/");
			if (trainedSkillSummary.getOthersSkillsFalls() > 0) {
				text = String.format("-%d", trainedSkillSummary.getOthersSkillsFalls());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getRed(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
		} else {
			text = String.format("%-25s -/-", Messages.getString("training.skill.others")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
		}
		this.append(NEW_LINE);

		if (trainedSkillSummary.getJuniorsPops() > 0 || trainedSkillSummary.getJuniorsFalls() > 0) {
			text = String.format("%-25s", Messages.getString("training.skill.juniors")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
			if (trainedSkillSummary.getJuniorsPops() > 0) {
				text = String.format("+%d", trainedSkillSummary.getJuniorsPops());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getDarkGreen(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
			this.append("/");
			if (trainedSkillSummary.getJuniorsFalls() > 0) {
				text = String.format("-%d", trainedSkillSummary.getJuniorsFalls());
				this.append(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), ColorResources.getRed(), this.getBackground(), SWT.NORMAL);
			} else {
				this.append("-");
			}
		} else {
			text = String.format("%-25s -/-", Messages.getString("training.skill.juniors")); //$NON-NLS-1$ //$NON-NLS-2$
			this.append(text);
		}
		
		this.append(NEW_LINE);

		this.setRedraw(true);
	}

	private String getSkill(int skill) {
		return String.format("%4s%s", String.format("[%d]", skill), Messages.getString("skill.a" + skill)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void addStyle(int start, int length, Color foreground, Color background, int style) {
		StyleRange range = new StyleRange();
		range.foreground = foreground;
		range.background = background;
		range.start = start;
		range.fontStyle = style;
		range.length = length;
		this.setStyleRange(range);
	}

}
