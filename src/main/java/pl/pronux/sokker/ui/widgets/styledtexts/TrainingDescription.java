package pl.pronux.sokker.ui.widgets.styledtexts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Training;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.Colors;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;

public class TrainingDescription extends StyledText implements IDescription {

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public TrainingDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}
	
	private String getSkill(int skill) {
		return String.format("%4s%s",String.format("[%d]", skill), Messages.getString("skill.a" + skill)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void setInfo(Training training) {
		String text;
		
		this.setRedraw(false);
		this.setText("");
		
		text = String.format("%s:", Messages.getString("training.main")); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);
		
		String formation = Messages.getString("formation." + training.getFormation()); //$NON-NLS-1$
		text = String.format("%s: %s", Messages.getString("formation"), formation); //$NON-NLS-1$ //$NON-NLS-2$ 
		this.addText(text);
		this.addStyle(this.getText().length() - formation.length(), formation.length(), ColorResources.getColor(48, 121, 182), this.getBackground(), SWT.NORMAL);
		this.addText(NEW_LINE);
		
		String type = Messages.getString("training.type." + training.getType()); //$NON-NLS-1$
		text = String.format("%s: %s" , Messages.getString("training.type"), type); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		this.addStyle(this.getText().length() - type.length(), type.length(), ColorResources.getColor(48, 121, 182), this.getBackground(), SWT.NORMAL);
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);

		if(training.getHeadCoach() != null) {
			String skill = getSkill(training.getHeadCoach().getGeneralskill());
			text = String.format("%-25s %s", Messages.getString("coach.job.head"), skill); //$NON-NLS-1$ //$NON-NLS-2$
			this.addText(text);
			this.addStyle(this.getText().length() - skill.length(), 4, Colors.getGray(), this.getBackground(), SWT.NORMAL);
			this.addStyle(this.getText().length() - skill.length() + 4, skill.length() - 4, Colors.getTrainerGeneralSkill(), this.getBackground(), SWT.BOLD);
		} else {
			text = String.format("%-25s %s", Messages.getString("coach.job.head"), "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.addText(text);
			this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
		}

		this.addText(NEW_LINE);
		
		if(training.getAssistants().size() > 3) {
			for(int i=0; i < 3; i++) {
				text = String.format("%-25s %s", Messages.getString("coach.job.assistant"), "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.addText(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
				this.addText(NEW_LINE);
			}
		} else {
			List<Coach> assistants = training.getAssistants();
			for(Coach trainer : assistants) {
				String skill = getSkill(trainer.getGeneralskill());
				text = String.format("%-25s %s", Messages.getString("coach.job.assistant"), skill); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.addText(text);
				this.addStyle(this.getText().length() - skill.length(), 4, Colors.getGray(), this.getBackground(), SWT.NORMAL);
				this.addStyle(this.getText().length() - skill.length() + 4, skill.length() - 4, Colors.getTrainerGeneralSkill(), this.getBackground(), SWT.NORMAL);
				this.addText(NEW_LINE);
			}
			
			for(int i = assistants.size(); i < 3; i++ ) {
				text = String.format("%-25s %s", Messages.getString("coach.job.assistant"), "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				this.addText(text);
				this.addStyle(this.getText().length() - text.length(), text.length(), Colors.getGray(), this.getBackground(), SWT.NORMAL);
				this.addText(NEW_LINE);
			}
		}
		
		this.addText(NEW_LINE);
		
		text = String.format("%s:", Messages.getString("training.general")); //$NON-NLS-1$ //$NON-NLS-2$
		this.addText(text);
		
		this.addText(NEW_LINE);
		this.addText(NEW_LINE);
		
		if(training.getHeadCoach() != null) {
			Coach trainer = training.getHeadCoach();
			addSkill(trainer.getStamina(), Messages.getString("coach.stamina"));
			addSkill(trainer.getKeepers(), Messages.getString("coach.keeper"));
			this.addText(NEW_LINE);
			addSkill(trainer.getPace(), Messages.getString("coach.pace"));
			addSkill(trainer.getDefenders(), Messages.getString("coach.defender"));
			this.addText(NEW_LINE);
			addSkill(trainer.getTechnique(), Messages.getString("coach.technique"));
			addSkill(trainer.getPlaymakers(), Messages.getString("coach.playmaker"));
			this.addText(NEW_LINE);
			addSkill(trainer.getPassing(), Messages.getString("coach.passing"));
			addSkill(trainer.getScorers(), Messages.getString("coach.scorer"));
			this.addText(NEW_LINE);
		}
		
//		text = String.format("%-25s%-15s" + NEW_LINE, Messages.getString("training.date"), training.getDate().getTrainingDate(SokkerDate.THURSDAY).toDateString()); //$NON-NLS-1$ //$NON-NLS-2$
//		this.addText(text);
//
//		text = String.format("%-25s%-15s" + NEW_LINE, Messages.getString("training.date.insert"), training.getDate().toDateString()); //$NON-NLS-1$ //$NON-NLS-2$
//		this.addText(text);	
//
//		text = String.format("%-25s%-15d" + NEW_LINE, Messages.getString("training.week"), training.getDate().getTrainingDate(SokkerDate.THURSDAY).getSokkerDate().getSeasonWeek()); //$NON-NLS-1$ //$NON-NLS-2$
//		this.addText(text);
//		
//
//		
//		String headCoachStats = getHeadCoachStats(training);
//		int rightColumn = 15;
//		text = String.format("%-25s%-" + rightColumn + "s" + NEW_LINE, Messages.getString("coach.job.head"), headCoachStats); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		this.addText(text);
//
//		if (training.getHeadCoach() != null) {
//			if(headCoachStats.length() > rightColumn) {
//				addStyle(getText().length() - NEW_LINE.length() - headCoachStats.length() + getIndexOfTrainedSkill(training), getLengthOfTrainedSkill(training), ColorResources.getBlack(), ColorResources.getWhite(), SWT.NONE);
//			} else {
//				addStyle(getText().length() - NEW_LINE.length() - rightColumn + getIndexOfTrainedSkill(training), getLengthOfTrainedSkill(training), ColorResources.getBlack(), ColorResources.getWhite(), SWT.NONE);				
//			}
//		}
//
//		text = String.format("%-25s%-15s" + NEW_LINE, Messages.getString("training.assistants"), getAssistantsStats(training)); //$NON-NLS-1$ //$NON-NLS-2$
//		this.addText(text);
//		
//		if (training.getJuniorCoach() != null) {
//			text = String.format("%-25s%-15s" + NEW_LINE, Messages.getString("coach.job.juniors"), Messages.getString("skill.a" + training.getJuniorCoach().getGeneralskill()) + " [" + training.getJuniorCoach().getGeneralskill() + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
//		} else {
//			text = "-"; //$NON-NLS-1$
//		}
//		this.addText(text);
		
		this.setRedraw(true);
	}
	
	private void addSkill(int skill, String skillName) {
		String skillValue = getSkill(skill);
		String skillFormatted = String.format("%s %s", skillValue, skillName.toLowerCase());
		String text = String.format("%-30s", skillFormatted);
		this.addText(text);
		this.addStyle(this.getText().length() - text.length(), 4, Colors.getGray(), this.getBackground(), SWT.NORMAL);
		this.addStyle(this.getText().length() - text.length() + 4, skillValue.length() - 4, Colors.getTrainerSkill(), this.getBackground(), SWT.NORMAL);

	}
	private void addText(String text) {
		this.append(text);
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

//	private String getHeadCoachStats(Training training) {
//		if (training.getHeadCoach() != null) {
//			Coach coach = training.getHeadCoach();
//			return coach.getGeneralskill() + " " + coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " "
//					+ coach.getDefenders() + " " + coach.getPlaymakers() + " " + coach.getScorers();
//		} else {
//			return "-";
//		}
//	}

//	private int getLengthOfTrainedSkill(Training training) {
//		if (training.getHeadCoach() != null) {
//			Coach coach = training.getHeadCoach();
//			switch (training.getType()) {
//			case Training.TYPE_STAMINA:
//				return String.valueOf(coach.getStamina()).length();
//			case Training.TYPE_KEEPER:
//				return String.valueOf(coach.getKeepers()).length();
//			case Training.TYPE_PLAYMAKING:
//				return String.valueOf(coach.getPlaymakers()).length();
//			case Training.TYPE_PASSING:
//				return String.valueOf(coach.getPassing()).length();
//			case Training.TYPE_TECHNIQUE:
//				return String.valueOf(coach.getTechnique()).length();
//			case Training.TYPE_DEFENDING:
//				return String.valueOf(coach.getDefenders()).length();
//			case Training.TYPE_STRIKER:
//				return String.valueOf(coach.getScorers()).length();
//			case Training.TYPE_PACE:
//				return String.valueOf(coach.getPace()).length();
//			}
//		}
//		return 0;
//	}

//	private int getIndexOfTrainedSkill(Training training) {
//		if (training.getHeadCoach() != null) {
//			Coach coach = training.getHeadCoach();
//			String position = String.valueOf(coach.getGeneralskill()) + " ";
//			switch (training.getType()) {
//			case Training.TYPE_STAMINA:
//				return position.length();
//			case Training.TYPE_KEEPER:
//				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " ";
//				return position.length();
//			case Training.TYPE_PLAYMAKING:
//				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " " + coach.getDefenders() + " ";
//				return position.length();
//			case Training.TYPE_PASSING:
//				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " ";
//				return position.length();
//			case Training.TYPE_TECHNIQUE:
//				position += coach.getStamina() + " " + coach.getPace() + " ";
//				return position.length();
//			case Training.TYPE_DEFENDING:
//				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " ";
//				return position.length();
//			case Training.TYPE_STRIKER:
//				position += coach.getStamina() + " " + coach.getPace() + " " + coach.getTechnique() + " " + coach.getPassing() + " " + coach.getKeepers() + " " + coach.getDefenders() + " "
//						+ coach.getPlaymakers() + " ";
//				return position.length();
//			case Training.TYPE_PACE:
//				position += coach.getStamina() + " ";
//				return position.length();
//			}
//		}
//		return 0;
//	}

//	private String getAssistantsStats(Training training) {
//		String assistants = "";
//		if (training.getAssistants().size() > 0) {
//			for (Coach coach : training.getAssistants()) {
//				assistants += coach.getGeneralskill() + " ";
//			}
//			return assistants;
//		} else {
//			return "-";
//		}
//	}
}
