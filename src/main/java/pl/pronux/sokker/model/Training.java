package pl.pronux.sokker.model;

import java.util.ArrayList;

public class Training {
	final public static int FORMATION_GK = 0;
	final public static int FORMATION_DEF = 1;
	final public static int FORMATION_MID = 2;
	final public static int FORMATION_ATT = 3;
	final public static int FORMATION_ALL = 4;

	final public static int TYPE_UNKNOWN = 0;
	final public static int TYPE_STAMINA = 1;
	final public static int TYPE_KEEPER = 2;
	final public static int TYPE_PLAYMAKING = 3;
	final public static int TYPE_PASSING = 4;
	final public static int TYPE_TECHNIQUE = 5;
	final public static int TYPE_DEFENDING = 6;
	final public static int TYPE_STRIKER = 7;
	final public static int TYPE_PACE = 8;

	final public static int NO_TRAINING = 1 << 1;
	final public static int NEW_TRAINING = 1 << 2;
	final public static int UPDATE_TRAINING = 1 << 3;
	final public static int UPDATE_PLAYERS = 1 << 4;
	
	final public static String IDENTIFIER = "TRAINING"; //$NON-NLS-1$
	
	private Date date;

	private int formation;

	private int id;

	private String note;

	private int type;

	private Coach headCoach;

	private Coach juniorCoach;

	private ArrayList<Coach> alAssistants =  new ArrayList<Coach>();

	private ArrayList<Player> alPlayers;

	private ArrayList<Junior> alJuniors;

	private boolean reported;
	
	private int status;

	public ArrayList<Coach> getAlAssistants() {
		return alAssistants;
	}

	public void setAssistants(ArrayList<Coach> alAssistants) {
		this.alAssistants = alAssistants;
	}

	public Coach getHeadCoach() {
		return headCoach;
	}

	public void setHeadCoach(Coach headCoach) {
		this.headCoach = headCoach;
	}

	public Coach getJuniorCoach() {
		return juniorCoach;
	}

	public void setJuniorCoach(Coach juniorCoach) {
		this.juniorCoach = juniorCoach;
	}

	public Date getDate() {
		return date;
	}

	public int getFormation() {
		return formation;
	}

	public int getId() {
		return id;
	}

	public String getNote() {
		return note;
	}

	public int getType() {
		return type;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setFormation(int formation) {
		this.formation = formation;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isReported() {
		return reported;
	}

	public void setReported(boolean reported) {
		this.reported = reported;
	}

	public ArrayList<Junior> getAlJuniors() {
		return alJuniors;
	}

	public void setJuniors(ArrayList<Junior> alJuniors) {
		this.alJuniors = alJuniors;
	}

	public ArrayList<Player> getAlPlayers() {
		return alPlayers;
	}

	public void setPlayers(ArrayList<Player> alPlayers) {
		this.alPlayers = alPlayers;
	}

//	public Object clone() {
//		Object o = null;
//			try {
//				o = super.clone();
//			} catch (CloneNotSupportedException e) {
//				System.err.println("There is no posibillity to clone training object");
//			}
//			return o;
//	}

	public void copy(Training training) {
		this.setAssistants(training.getAlAssistants());
		this.setJuniors(training.getAlJuniors());
		this.setPlayers(training.getAlPlayers());
		this.setDate(training.getDate());
		this.setFormation(training.getFormation());
		this.setHeadCoach(training.getHeadCoach());
		this.setId(training.getId());
		this.setJuniorCoach(training.getJuniorCoach());
		this.setNote(training.getNote());
		this.setReported(training.isReported());
		this.setType(training.getType());
	}

	public Training clone() {
		Training training = new Training();
		training.setAssistants(this.getAlAssistants());
		training.setAssistants(new ArrayList<Coach>());
		for(Coach assistant : this.alAssistants) {
			training.getAlAssistants().add(assistant);
		}
		training.setJuniors(this.getAlJuniors());
		training.setPlayers(this.getAlPlayers());
		training.setDate(this.getDate());
		training.setFormation(this.getFormation());
		training.setHeadCoach(this.getHeadCoach());
		training.setId(this.getId());
		training.setJuniorCoach(this.getJuniorCoach());
		training.setNote(this.getNote());
		training.setReported(this.isReported());
		training.setType(this.getType());
		return training;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
