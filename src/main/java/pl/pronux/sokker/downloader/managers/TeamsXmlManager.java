package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.TeamManager;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.TeamsDao;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.TeamXmlParser;
import pl.pronux.sokker.model.Club;
import pl.pronux.sokker.model.Date;

public class TeamsXmlManager extends XmlManager<Club> {

	private TeamManager teamManager = TeamManager.getInstance();
	
	private Map<String, String> teamsStringMap = new HashMap<String, String>();

	private List<Club> teams = new ArrayList<Club>();

	private Club club;

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public TeamsXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public TeamsXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("team", destination, downloader, currentDay); 
	}
	
	public TeamsXmlManager(String content, Date currentDay, int teamId) {
		super(content, currentDay, teamId);
		teamsStringMap.put(String.valueOf(teamId), content);
		this.setTeamId(teamId);
	}

	@Override
	public void download() throws IOException {
		String xml = getDownloader().getTeam(getDownloader().getTeamId());
		setContent(xml);
		teamsStringMap.put(getDownloader().getTeamId(), xml);
	}

	public void download(String teamId) throws IOException {
		teamsStringMap.put(teamId, getDownloader().getTeam(teamId));
	}

	@Override
	public void importToSQL() throws SQLException {
		int teamId = 0;
		if(getDownloader().getTeamId() != null && getDownloader().getTeamId().matches("[0-9]+")) { 
			teamId = Integer.valueOf(getDownloader().getTeamId());
		}
		for(Club team : teams) {
			if(team.getId() == teamId) {
				teamManager.importTeam(team, getCurrentDay());		
			} else {
				teamManager.importForeignClub(team, getCurrentDay());	
			}
		}
	}

	public Club parseXML(int teamId) throws SAXException {
		List<Club> list = parseXML(teamsStringMap.get(String.valueOf(teamId)));
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return null;	
		}
	}
	
	public List<Club> parseXML() throws SAXException {
//		parseXML(getContent());
		Set<String> teamsStringSet = teamsStringMap.keySet();

		for (String key : teamsStringSet) {
			parseXML(teamsStringMap.get(key));
		}
		
		for (Club team : teams) {
			if(team.getId() == Integer.valueOf(getDownloader().getTeamId())) {
				this.setClub(team);
			}
		}
		return teams;
	}

	public void completeClubs() throws SQLException, IOException, SAXException {
		String xml;
		
		TeamsDao clubsDao = new TeamsDao(SQLSession.getConnection());
		List<Integer> notImportedTeams = clubsDao.getNotImportedClubsId();
		
		for ( Integer integer : notImportedTeams) {
			xml = getDownloader().getTeam(integer.toString());
			teamsStringMap.put(integer.toString(), xml);
			parseXML(xml);
			write(xml, integer.toString());
		}
		
		importToSQL();
		
	}

	@Override
	public List<Club> parseXML(String xml) throws SAXException {
		TeamXmlParser teamXmlParser = new TeamXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			teamXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			teamXmlParser.parseXmlSax(input, null);
		}
		teams.add(teamXmlParser.getClub());
		return teams;
	}

	@Override
	public boolean write() throws IOException {
		write(getContent(), getDownloader().getTeamId());

		Set<String> teamsStringSet = teamsStringMap.keySet();

		for (String key : teamsStringSet) {
			if (!write(teamsStringMap.get(key), key)) {
				// return false;
			}
		}
		return true;
	}

}
