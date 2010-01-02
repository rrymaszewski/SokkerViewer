package pl.pronux.sokker.actions;

import java.sql.SQLException;
import java.util.List;

import pl.pronux.sokker.data.sql.SQLQuery;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.CountriesDao;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Region;

public class CountriesManager {

	private static final CountriesManager _instance = new CountriesManager();
	
	private CountriesManager() {
	}
	
	public static CountriesManager instance() {
		return _instance;
	}
	
	public void importCountries(List<Country> countries) throws SQLException {
		CountriesDao countriesDao = new CountriesDao(SQLSession.getConnection());
		for (Country country : countries) {
			if (!countriesDao.existsCountry(country.getCountryID())) {
				countriesDao.addCountry(country);
			} else {
				countriesDao.updateCountry(country);
			}
		}
	}
	
	public void importRegion(Region region) throws SQLException {
		CountriesDao countriesDao = new CountriesDao(SQLSession.getConnection());
		if (!countriesDao.existsRegion(region.getRegionID())) {
			countriesDao.addRegion(region);
		} else {
			countriesDao.updateRegion(region);
		}
	}

	public List<Country> getCountries() throws SQLException {
		List<Country> alCountries;
	
		boolean newConnection = SQLQuery.connect();
		CountriesDao countriesDao = new CountriesDao(SQLSession.getConnection());
		alCountries = countriesDao.getCountries();
		SQLQuery.close(newConnection);
		return alCountries;
	}
}
