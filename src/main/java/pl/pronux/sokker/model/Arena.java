package pl.pronux.sokker.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Arena {

	public Arena() {
		this.stands = new ArrayList<Stand>();
		this.arenaNames = new ArrayList<ClubArenaName>();
	}

	private List<ClubArenaName> arenaNames;

	private List<Stand> stands;

	public List<Stand> getStands() {
		return stands;
	}

	public void setStands(List<Stand> stands) {
		this.stands = stands;
		this.setStandsMap(null);
	}

	public List<ClubArenaName> getArenaNames() {
		return arenaNames;
	}

	public void setArenaNames(List<ClubArenaName> arenaNames) {
		this.arenaNames = arenaNames;
	}

	private Map<Integer, Stand> standsMap;

	public Map<Integer, Stand> getStandsMap() {
		if (standsMap == null) {
			standsMap = new HashMap<Integer, Stand>();
			for (Stand stand : this.getStands()) {
				standsMap.put(stand.getLocation(), stand);
			}
		}
		return standsMap;
	}

	public void setStandsMap(Map<Integer, Stand> standsMap) {
		this.standsMap = standsMap;
	}

	public int getCapacity() {
		int capacity = 0;
		Collection<Stand> stands = this.getStands();
		for (Stand stand : stands) {
			capacity += stand.getCapacity();
		}
		return capacity;
	}

	public int getIncome() {
		int income = 0;
		int standIncome = 0;
		for (Stand stand : this.getStands()) {
			standIncome = stand.getIncome();
			income += standIncome;
		}
		return income;
	}

	public int getCost() {
		int cost = 0;
		for (Stand stand : this.getStands()) {
			cost += stand.getCost();
		}
		return cost;
	}

	public ArenaPlaceCapacity getArenaPlaceCapacity() {
		ArenaPlaceCapacity arenaPlaceCapacity = new ArenaPlaceCapacity();
		for (Stand stand : this.getStands()) {
			switch (stand.getType()) {
				case Stand.TYPE_STANDING:
					arenaPlaceCapacity.setStanding(arenaPlaceCapacity.getStanding() + stand.getCapacity());
					break;
				case Stand.TYPE_TERRACES:
					if (stand.getIsRoof() == Stand.ROOF_FALSE) {
						arenaPlaceCapacity.setTerraces(arenaPlaceCapacity.getTerraces() + stand.getCapacity());
					} else {
						arenaPlaceCapacity.setTerracesWithRoof(arenaPlaceCapacity.getTerracesWithRoof() + stand.getCapacity());
					}
					break;
				case Stand.TYPE_BENCHES:
					if (stand.getIsRoof() == Stand.ROOF_FALSE) {
						arenaPlaceCapacity.setBenches(arenaPlaceCapacity.getBenches() + stand.getCapacity());
					} else {
						arenaPlaceCapacity.setBenchesWithRoof(arenaPlaceCapacity.getBenchesWithRoof() + stand.getCapacity());
					}
					break;
				case Stand.TYPE_SEATS:
					if (stand.getIsRoof() == Stand.ROOF_FALSE) {
						arenaPlaceCapacity.setSeats(arenaPlaceCapacity.getSeats() + stand.getCapacity());
					} else {
						arenaPlaceCapacity.setSeatsWithRoof(arenaPlaceCapacity.getSeatsWithRoof() + stand.getCapacity());
					}
					break;
			}
		}
		return arenaPlaceCapacity;
	}
	
	public ArenaProject getCostOfRebuild(Arena newArena) {
		ArenaProject arenaProject = new ArenaProject();
		int fasteningCost = 0;
		int dissasemblyFasteningCost = 0;
		int dissasemblyBenchesCost = 0;
		int dissasemblySeatsCost = 0;
		int dissasemblyRoofCost = 0;
		int dissasemblyTerracesCost = 0;
		int benchesCost = 0;
		int seatsCost = 0;
		int roofCost = 0;
		int allCosts = 0;
		int projectCost = 0; // 100000
		int incdec = 0;
		int terracesCost = 0;

		Map<Integer, Stand> map = this.getStandsMap();
		Map<Integer, Stand> tempMap = newArena.getStandsMap();

		Set<Integer> keys = map.keySet();

		for (Integer key : keys) {

			if (map.get(key).getCapacity() != tempMap.get(key).getCapacity() || map.get(key).getType() != tempMap.get(key).getType() || map.get(key).getIsRoof() != tempMap.get(key).getIsRoof()) {

				// koszt projektu
				projectCost += Stand.COST_PROJECT;

				if (map.get(key).getType() == Stand.TYPE_STANDING) {
					incdec = tempMap.get(key).getCapacity();
					terracesCost += map.get(key).getCostOfTerracesRebuild(tempMap.get(key).getCapacity());
				} else if (tempMap.get(key).getType() == Stand.TYPE_STANDING) {
					incdec = tempMap.get(key).getCapacity();
					dissasemblyTerracesCost += map.get(key).getCostOfTerracesRebuild(0);
				} else {
					incdec = tempMap.get(key).getCapacity() - map.get(key).getCapacity();
					// rebuild terraces
					if (incdec > 0) {
						terracesCost += map.get(key).getCostOfTerracesRebuild(tempMap.get(key).getCapacity());
					} else if (incdec < 0) {
						dissasemblyTerracesCost += map.get(key).getCostOfTerracesRebuild(tempMap.get(key).getCapacity());
					}
				}

				// rebuild roof
				if (tempMap.get(key).getIsRoof() == Stand.ROOF_TRUE && map.get(key).getIsRoof() == Stand.ROOF_FALSE) {
					roofCost += tempMap.get(key).getCapacity() * Stand.COST_ROOF;
				} else if (tempMap.get(key).getIsRoof() == Stand.ROOF_FALSE && map.get(key).getIsRoof() == Stand.ROOF_TRUE) {
					dissasemblyRoofCost += map.get(key).getCapacity() * Stand.COST_DISSASEMBLY_ROOF;
				} else if (tempMap.get(key).getIsRoof() == Stand.ROOF_TRUE && map.get(key).getIsRoof() == Stand.ROOF_TRUE) {
					if (incdec > 0) {
						roofCost += incdec * Stand.COST_ROOF;
					} else if (incdec < 0) {
						roofCost += incdec * Stand.COST_DISSASEMBLY_ROOF;
					}
				}

				// rebuild stadion if the type is same
				if (map.get(key).getType() == tempMap.get(key).getType()) {

					// if (mainArena[i].getType() == 2 &&
					// mainArena[i].getIsRoof() == 0) {
					if (map.get(key).getType() == Stand.TYPE_BENCHES) {
						if (incdec > 0) {
							fasteningCost += incdec * Stand.COST_FASTENING;
							benchesCost += incdec * Stand.COST_BENCHES;
						} else if (incdec < 0) {
							dissasemblyBenchesCost += -1 * incdec * Stand.COST_DISSASEMBLY_BENCHES;
							dissasemblyFasteningCost += -1 * incdec * Stand.COST_DISSASEMBLY_FASTENING;
						}
					}

					// if (mainArena[i].getType() == 3 &&
					// mainArena[i].getIsRoof() == 0) {
					if (map.get(key).getType() == Stand.TYPE_SEATS) {
						if (incdec > 0) {
							fasteningCost += incdec * Stand.COST_FASTENING;
							seatsCost += incdec * Stand.COST_SEATS;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * Stand.COST_DISSASEMBLY_FASTENING;
							dissasemblySeatsCost += -1 * incdec * Stand.COST_DISSASEMBLY_SEATS;
						}
					}
				}

				// rebuild stadion if the type is diffrent
				if (map.get(key).getType() != tempMap.get(key).getType()) {

					if (map.get(key).getType() == Stand.TYPE_BENCHES && tempMap.get(key).getType() == Stand.TYPE_SEATS) {
						dissasemblyBenchesCost += map.get(key).getCapacity() * Stand.COST_DISSASEMBLY_BENCHES;
						seatsCost += Stand.COST_SEATS * tempMap.get(key).getCapacity();
						if (incdec > 0) {
							fasteningCost += incdec * Stand.COST_FASTENING;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * Stand.COST_DISSASEMBLY_FASTENING;
						}
					}

					if (map.get(key).getType() == Stand.TYPE_SEATS && tempMap.get(key).getType() == Stand.TYPE_BENCHES) {
						dissasemblySeatsCost += map.get(key).getCapacity() * Stand.COST_DISSASEMBLY_SEATS;
						benchesCost += Stand.COST_BENCHES * tempMap.get(key).getCapacity();
						if (incdec > 0) {
							fasteningCost += incdec * Stand.COST_FASTENING;
						} else if (incdec < 0) {
							dissasemblyFasteningCost += -1 * incdec * Stand.COST_DISSASEMBLY_FASTENING;
						}
					}

					if ((map.get(key).getType() == Stand.TYPE_TERRACES || map.get(key).getType() == Stand.TYPE_STANDING) && tempMap.get(key).getType() == Stand.TYPE_BENCHES) {
						benchesCost += Stand.COST_BENCHES * tempMap.get(key).getCapacity();
						fasteningCost += tempMap.get(key).getCapacity() * Stand.COST_FASTENING;
					}

					if ((map.get(key).getType() == Stand.TYPE_TERRACES || map.get(key).getType() == Stand.TYPE_STANDING) && tempMap.get(key).getType() == Stand.TYPE_SEATS) {
						seatsCost += Stand.COST_SEATS * tempMap.get(key).getCapacity();
						fasteningCost += tempMap.get(key).getCapacity() * Stand.COST_FASTENING;
					}

					if (map.get(key).getType() == Stand.TYPE_SEATS && (tempMap.get(key).getType() == Stand.TYPE_TERRACES || tempMap.get(key).getType() == Stand.TYPE_STANDING)) {
						dissasemblySeatsCost += Stand.COST_DISSASEMBLY_SEATS * map.get(key).getCapacity();
						dissasemblyFasteningCost += map.get(key).getCapacity() * Stand.COST_DISSASEMBLY_FASTENING;
					}

					if (map.get(key).getType() == Stand.TYPE_BENCHES && (tempMap.get(key).getType() == Stand.TYPE_TERRACES || tempMap.get(key).getType() == Stand.TYPE_STANDING)) {
						dissasemblyBenchesCost += Stand.COST_DISSASEMBLY_BENCHES * map.get(key).getCapacity();
						dissasemblyFasteningCost += map.get(key).getCapacity() * Stand.COST_DISSASEMBLY_FASTENING;
					}

				}

			}
		}

		allCosts = terracesCost + dissasemblyTerracesCost + projectCost + roofCost + dissasemblyRoofCost + fasteningCost + dissasemblyFasteningCost + benchesCost + dissasemblyBenchesCost + seatsCost + dissasemblySeatsCost;

		arenaProject.setAllCosts(allCosts);
		arenaProject.setBenchesCost(benchesCost);
		arenaProject.setDissasemblyBenchesCost(dissasemblyBenchesCost);
		arenaProject.setDissasemblyFasteningCost(dissasemblyFasteningCost);
		arenaProject.setDissasemblyRoofCost(dissasemblyRoofCost);
		arenaProject.setDissasemblySeatsCost(dissasemblySeatsCost);
		arenaProject.setDissasemblyTerracesCost(dissasemblyTerracesCost);
		arenaProject.setFasteningCost(fasteningCost);
		arenaProject.setProjectCost(projectCost);
		arenaProject.setRoofCost(roofCost);
		arenaProject.setSeatsCost(seatsCost);
		arenaProject.setTerracesCost(terracesCost);
		
		return arenaProject;
	}
}
