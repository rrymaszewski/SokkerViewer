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

	private TeamManager teamManager = TeamManager.instance();
	
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
		super("team", destination, downloader, currentDay); //$NON-NLS-1$
	}
	
	public TeamsXmlManager(String content, Date currentDay, int teamID) {
		super(content, currentDay, teamID);
		teamsStringMap.put(String.valueOf(teamID), content);
		this.teamID = teamID;
	}

	@Override
	public void download() throws IOException {
		String xml = downloader.getTeam(downloader.getTeamID());
		setContent(xml);
		teamsStringMap.put(downloader.getTeamID(), xml);
	}

	public void download(String teamID) throws IOException {
		teamsStringMap.put(teamID, downloader.getTeam(teamID));
	}

	@Override
	public void importToSQL() throws SQLException {
		int teamID = 0;
		if(downloader.getTeamID() != null && downloader.getTeamID().matches("[0-9]+")) { //$NON-NLS-1$
			teamID = Integer.valueOf(downloader.getTeamID());
		}
		for(Club team : teams) {
			if(team.getId() == teamID) {
				teamManager.importTeam(team, currentDay);		
			} else {
				teamManager.importForeignClub(team, currentDay);	
			}
		}
	}

	public Club parseXML(int teamID) throws SAXException {
		List<Club> list = parseXML(teamsStringMap.get(String.valueOf(teamID)));
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
			if(team.getId() == Integer.valueOf(downloader.getTeamID())) {
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
			xml = downloader.getTeam(integer.toString());
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
		write(getContent(), downloader.getTeamID());

		Set<String> teamsStringSet = teamsStringMap.keySet();

		for (String key : teamsStringSet) {
			if (!write(teamsStringMap.get(key), key)) {
				// return false;
			}
		}
		return true;
	}

}
