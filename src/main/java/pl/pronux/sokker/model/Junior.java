package pl.pronux.sokker.model;

import java.math.BigDecimal;

import pl.pronux.sokker.interfaces.DateConst;

public class Junior extends Person {

	public static final int MINIMUM_AGE = 16;
	public static final Money JUNIOR_COST = new Money(4000);

	private static double minimumPop = 3.0;

	private static final long serialVersionUID = 1279340105076757174L;

	public static final int STATUS_IN_SCHOOL = 0;

	public static final int STATUS_SACKED = 2;

	public static final int STATUS_TRAINED = 1;

	public static final int STATUS_DELETED_SACKED = STATUS_SACKED + 20;

	public static final int STATUS_DELETED_TRAINED = STATUS_TRAINED + 20;

	public static final int STATUS_TRASH_SACKED = STATUS_SACKED + 10;

	public static final int STATUS_TRASH_TRAINED = STATUS_TRAINED + 10;

	private Money allMoneyToSpend;

	private Double averagePops;

	private Date endDate;

	private int estimatedAge = -1;

	private int pops;

	private int lastPop;

	private Money moneySpent;

	private Player player;

	private Integer estimatedSkill;

	private Money restMoneyToSpend;

	private JuniorSkills[] skills;

	private Double trainersAverage;

	private int formation = -1;

	public Money getAllMoneyToSpend() {
		if (this.allMoneyToSpend == null) {
			this.setAllMoneyToSpend(new Money(
					(this.getSkills()[0].getWeeks() + 1) * JUNIOR_COST.toInt()));
			return getAllMoneyToSpend();
		} else {
			return allMoneyToSpend;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#getAverageJumps()
	 */
	public double getAveragePops() {
		if (this.averagePops == null) {
			int jump = 0;
			int lastJump = 0;
			double firstJump = 0;

			lastJump = skills[0].getWeeks();

			for (int j = 0; j < skills.length - 1; j++) {
				if (skills[j].getSkill() < skills[j + 1].getSkill()) {
					if (jump == 0) {
						firstJump = skills[j + 1].getWeeks();
					}
					jump++;
					lastJump = skills[j + 1].getWeeks();
				}
			}

			setLastPop(lastJump);
			setPops(jump);

			if (jump > 1) {
				if (lastJump + 1 - skills[skills.length - 1].getWeeks() > ((firstJump - lastJump) / (jump - 1))) {
					setAveragePops((firstJump + 1 - skills[skills.length - 1]
							.getWeeks()) / jump);
				} else {
					setAveragePops((firstJump - lastJump) / --jump);
				}
			} else {
				if (jump == 1) {
					Double weeksDiff1 = Double.valueOf(lastJump
							- skills[skills.length - 1].getWeeks() + 1);
					Double weeksDiff2 = Double.valueOf(skills[0].getWeeks()
							- lastJump);
					if (weeksDiff1 >= getMinimumPop() || weeksDiff2 > getMinimumPop()) {
						if (weeksDiff1 > weeksDiff2) {
							setAveragePops(weeksDiff1);
						} else {
							setAveragePops(weeksDiff2);
						}
					} else {
						setAveragePops(getMinimumPop());
					}
				} else {
					int weeksDiff = skills[0].getWeeks()
							- skills[skills.length - 1].getWeeks();
					if (weeksDiff >= getMinimumPop()) {
						setAveragePops(Double.valueOf(weeksDiff + 1));
					} else {
						setAveragePops(getMinimumPop());
					}
				}
			}
			return getAveragePops();
		} else {
			return averagePops;
		}
	}

	public Date getEndDate() {
		if (endDate == null) {
			endDate = new Date(getSkills()[0].getDate()
					.getTrainingDate(SokkerDate.THURSDAY).getMillis()
					+ (getSkills()[0].getWeeks() * DateConst.WEEK));
			return endDate;
		} else {
			return endDate;
		}
	}

	public int getEstimatedAge() {
		if (this.estimatedAge != -1) {
			return this.estimatedAge;
		}
		if (skills.length > 0) {
			int estimatedAge = new SokkerDate(0, skills[0].getDate()
					.getSokkerDate().getWeek()
					+ skills[0].getWeeks()).getSeason()
					- skills[0].getDate().getSokkerDate().getSeason();
			setEstimatedAge(estimatedAge);
			return estimatedAge;
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#getJumps()
	 */
	public int getPops() {
		getAveragePops();
		return pops;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#getLastJump()
	 */
	public int getLastPop() {
		getAveragePops();
		return lastPop;
	}

	public Money getMoneySpent() {
		if (this.moneySpent == null) {
			this.setMoneySpent(new Money(
					(this.getSkills()[0].getWeeks()
							- this.getSkills()[this.getSkills().length - 1]
									.getWeeks() + 1)
							* JUNIOR_COST.toInt()));
			return getMoneySpent();
		} else {
			return moneySpent;
		}
	}

	public Player getPlayer() {
		return player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#getPropablySkill()
	 */
	public int getEstimatedSkill() {
		if (estimatedSkill == null) {
			if (getAveragePops() != 0) {
				int limit = BigDecimal
						.valueOf(
								getMinimumPop()
										* BigDecimal
												.valueOf(
														getSkills()[0]
																.getWeeks()
																/ getMinimumPop())
												.setScale(0,
														BigDecimal.ROUND_DOWN)
												.intValue())
						.setScale(0, BigDecimal.ROUND_UP).intValue();
				if (getPops() == 0
						&& getSkills()[getSkills().length - 1].getWeeks() > limit) {
					estimatedSkill = BigDecimal
							.valueOf(
									getSkills()[getSkills().length - 1]
											.getWeeks() / getMinimumPop())
							.setScale(0, BigDecimal.ROUND_UP).intValue();
					estimatedSkill += getSkills()[getSkills().length - 1]
							.getSkill();
				} else {
					estimatedSkill = BigDecimal
							.valueOf(getLastPop() / getAveragePops())
							.setScale(1, BigDecimal.ROUND_HALF_DOWN).intValue();
					estimatedSkill += getSkills()[getSkills().length - 1]
							.getSkill();
				}

				if (estimatedSkill > 17) {
					estimatedSkill = 17;
				}
				setEstimatedSkill(estimatedSkill);
				return getEstimatedSkill();
			} else {
				this.setEstimatedSkill(0);
				return getEstimatedSkill();
			}
		} else {
			return estimatedSkill;
		}
	}

	public Money getRestMoneyToSpend() {
		if (this.restMoneyToSpend == null) {
			this.setRestMoneyToSpend(new Money(this.getSkills()[this
					.getSkills().length - 1].getWeeks() * JUNIOR_COST.toInt()));
			return getRestMoneyToSpend();
		} else {
			return restMoneyToSpend;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#getSkills()
	 */
	public JuniorSkills[] getSkills() {
		return skills;
	}

	public Double getTrainersAverage() {
		if (trainersAverage == null) {
			int counter = 0;
			Double sum = 0.0;
			for (int i = 0; i < this.getSkills().length; i++) {
				Coach trainer = this.getSkills()[i].getTrainer();
				if (trainer != null) {
					counter++;
					sum += trainer.getGeneralskill();
				}
			}

			if (counter > 0) {
				this.setTrainersAverage(sum / counter);
			} else {
				this.setTrainersAverage(0.0);
			}

			return getTrainersAverage();
		} else {
			return trainersAverage;
		}
	}

	public int getWeeksWithoutJump() {
		return getLastPop() - skills[skills.length - 1].getWeeks();
	}

	public void setAllMoneyToSpend(Money allMoneyToSpend) {
		this.allMoneyToSpend = allMoneyToSpend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#setAverageJumps(double)
	 */
	public void setAveragePops(Double averagePops) {
		this.averagePops = averagePops;
	}

	public void setEstimatedAge(int estimatedAge) {
		this.estimatedAge = estimatedAge;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#setJumps(int)
	 */
	public void setPops(int pops) {
		this.pops = pops;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#setLastJump(int)
	 */
	public void setLastPop(int lastPop) {
		this.lastPop = lastPop;
	}

	public void setMoneySpent(Money moneySpent) {
		this.moneySpent = moneySpent;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.pronux.sokker.JuniorInterface#setPropablySkill()
	 */
	public void setEstimatedSkill(Integer estimatedSkill) {
		this.estimatedSkill = estimatedSkill;
	}

	public void setRestMoneyToSpend(Money restMoneyToSpend) {
		this.restMoneyToSpend = restMoneyToSpend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pl.pronux.sokker.JuniorInterface#setSkills(pl.pronux.sokker.JuniorSkills
	 * [])
	 */
	public void setSkills(JuniorSkills[] skills) {
		this.skills = skills;
	}

	public void setTrainersAverage(double juniorTrainersAverage) {
		this.trainersAverage = juniorTrainersAverage;
	}

	public void reload() {
		setAveragePops(null);
		setEstimatedSkill(null);
	}

	public void setFormation(int formation) {
		this.formation = formation;
	}

	public int getFormation() {
		return formation;
	}

	public static double getMinimumPop() {
		return minimumPop;
	}

	public static void setMinimumPop(double minimumPop) {
		Junior.minimumPop = minimumPop;
	}

}
