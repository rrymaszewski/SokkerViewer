package pl.pronux.sokker.model;

public class Coach extends Person {

	/**
	 *
	 */
	private static final long serialVersionUID = 6754250135645356480L;

	public static final int STATUS_IN_CLUB = 0;

	public static final int STATUS_SACKED = 1;
	
	public static final int STATUS_TRASH = 11;
	
	public static final int STATUS_DELETED = 21;

	public static final int JOB_NONE = 0;

	public static final int JOB_HEAD = 1;

	public static final int JOB_ASSISTANT = 2;

	public static final int JOB_JUNIORS = 3;
	
	private byte signed;

	private int job;

	private int countryfrom;

	private byte age;

	private Money salary;

	private byte generalskill;

	private byte stamina;

	private byte pace;

	private byte technique;

	private byte passing;

	private byte keepers;

	private byte defenders;

	private byte playmakers;

	private byte scorers;

	private int summarySkill;

	public int getSummarySkill() {
		return summarySkill;
	}

	public void setSummarySkill() {
		this.summarySkill = stamina + pace + technique + passing + keepers + defenders + playmakers + scorers;
	}

	public int[] getStatsTable() {
		int[] table = {
				salary.toInt(),
				age,
				generalskill,
				stamina,
				pace,
				technique,
				passing,
				keepers,
				defenders,
				playmakers,
				scorers
		};
		return table;
	}

	public byte getAge() {
		return age;
	}

	public void setAge(byte age) {
		this.age = age;
	}

	public int getCountryfrom() {
		return countryfrom;
	}

	public void setCountryfrom(int countryfrom) {
		this.countryfrom = countryfrom;
	}

	public byte getDefenders() {
		return defenders;
	}

	public void setDefenders(byte defenders) {
		this.defenders = defenders;
	}

	public byte getGeneralskill() {
		return generalskill;
	}

	public void setGeneralskill(byte generalskill) {
		this.generalskill = generalskill;
	}

	public Integer getJob() {
		return job;
	}

	public void setJob(Integer integer) {
		this.job = integer;
	}

	public byte getKeepers() {
		return keepers;
	}

	public void setKeepers(byte keepers) {
		this.keepers = keepers;
	}

	public byte getPace() {
		return pace;
	}

	public void setPace(byte pace) {
		this.pace = pace;
	}

	public byte getPassing() {
		return passing;
	}

	public void setPassing(byte passing) {
		this.passing = passing;
	}

	public byte getPlaymakers() {
		return playmakers;
	}

	public void setPlaymakers(byte playmakers) {
		this.playmakers = playmakers;
	}

	public Money getSalary() {
		return salary;
	}

	public void setSalary(Money salary) {
		this.salary = salary;
	}

	public byte getScorers() {
		return scorers;
	}

	public void setScorers(byte scorers) {
		this.scorers = scorers;
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public byte getStamina() {
		return stamina;
	}

	public void setStamina(byte stamina) {
		this.stamina = stamina;
	}

	public byte getTechnique() {
		return technique;
	}

	public void setTechnique(byte technique) {
		this.technique = technique;
	}
}
