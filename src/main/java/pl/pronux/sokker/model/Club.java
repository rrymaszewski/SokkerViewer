package pl.pronux.sokker.model;

import java.util.List;
import java.util.Set;

public class Club {

	public static final double CREDIT_INTEREST = 0.045;

	public static final int FANCLUB_TICKET = 500;

	public static final double SPONSORS_BONUS_RATE = 1.5;

	private Training training;

	private User user;

	private Arena arena;

	private List<ClubSupporters> clubSupporters;

	private List<ClubBudget> clubBudget;

	private List<ClubName> clubName;

	private int country;

	private Money creditInterest;

	private Date dateCreated;

	private Money fanclubSupport;

	private int id;

	private String imagePath;

	private int juniorsMax;

	private List<Rank> rank;

	private Region region;

	private int regionId;

	private Money sponsorsBonus;

	private Set<Integer> visitedCountries;

	private Set<Integer> invitedCountries;

	private List<Player> players;

	private List<Junior> juniors;

	private List<Coach> coaches;

	private List<Match> matches;

	public Set<Integer> getVisitedCountries() {
		return visitedCountries;
	}

	public void setVisitedCountries(Set<Integer> visitedCountries) {
		this.visitedCountries = visitedCountries;
	}

	public Arena getArena() {
		return arena;
	}

	public List<ClubSupporters> getClubSupporters() {
		return clubSupporters;
	}

	public List<ClubBudget> getClubBudget() {
		return clubBudget;
	}

	public List<ClubName> getClubName() {
		return clubName;
	}

	public int getCountry() {
		return country;
	}

	public Money getCreditInterest() {
		if (this.creditInterest == null) {
			if (this.getClubBudget().get(getClubBudget().size() - 1).getMoney().toInt() < 0) {
				setCreditInterest(new Money(this.getClubBudget().get(getClubBudget().size() - 1).getMoney().toInt() * CREDIT_INTEREST));
			} else {
				setCreditInterest(new Money(0));
			}
			return getCreditInterest();
		} else {
			return this.creditInterest;
		}
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Money getFanclubSupport() {
		if (this.fanclubSupport == null) {
			setFanclubSupport(new Money(this.getClubSupporters().get(this.getClubSupporters().size() - 1).getFanclubcount() * FANCLUB_TICKET));
			return getFanclubSupport();
		} else {
			return this.fanclubSupport;
		}
	}

	public int getId() {
		return id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public int getJuniorsMax() {
		return juniorsMax;
	}

	public List<Rank> getRank() {
		return rank;
	}

	public Region getRegion() {
		return region;
	}

	public int getRegionId() {
		return regionId;
	}

	public Money getSponsorsBonus() {
		if (this.sponsorsBonus == null) {
			setSponsorsBonus(new Money(this.getClubBudget().get(getClubBudget().size() - 1).getMoney().toInt() * SPONSORS_BONUS_RATE));
			return getSponsorsBonus();
		} else {
			return this.sponsorsBonus;
		}
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public void setClubSupporters(List<ClubSupporters> supporters) {
		clubSupporters = supporters;
	}

	public void setClubBudget(List<ClubBudget> budget) {
		clubBudget = budget;
	}

	public void setClubName(List<ClubName> clubName) {
		this.clubName = clubName;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public void setCreditInterest(Money creditInterest) {
		this.creditInterest = creditInterest;
	}

	public void setDateCreated(Date creationDate) {
		this.dateCreated = creationDate;
	}

	public void setFanclubSupport(Money fanclubSupport) {
		this.fanclubSupport = fanclubSupport;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setJuniorsMax(int juniorsMax) {
		this.juniorsMax = juniorsMax;
	}

	public void setRank(List<Rank> rank) {
		this.rank = rank;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public void setSponsorsBonus(Money sponsorsBonus) {
		this.sponsorsBonus = sponsorsBonus;
	}

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setInvitedCountries(Set<Integer> invitedCountries) {
		this.invitedCountries = invitedCountries;
	}

	public Set<Integer> getInvitedCountries() {
		return invitedCountries;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Junior> getJuniors() {
		return juniors;
	}

	public void setJuniors(List<Junior> juniors) {
		this.juniors = juniors;
	}

	public List<Coach> getCoaches() {
		return coaches;
	}

	public void setCoaches(List<Coach> coaches) {
		this.coaches = coaches;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

}
