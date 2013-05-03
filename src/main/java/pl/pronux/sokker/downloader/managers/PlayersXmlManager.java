package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.PlayersManager;
import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.PlayersDao;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.PlayerXmlParser;
import pl.pronux.sokker.downloader.xml.parsers.PlayersXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.Training;

public class PlayersXmlManager extends XmlManager<Player> {

	private PlayersManager playersManager = PlayersManager.getInstance();
	
	private Map<String, String> teamsMap = new HashMap<String, String>();
	
	private List<Player> players;
	
	public String completeUncompletedPlayers() throws SQLException {
		String value = "-3"; 
		List<Integer> playersId = new ArrayList<Integer>();

		try {
			PlayersDao playersDao = new PlayersDao(SQLSession.getConnection());
			playersId = playersDao.getUncompletePlayers();
			if (downloader.getStatus().equals("OK")) { 
				value = "0"; 
			} else {
				value = downloader.getErrorno();
			}
			// download xmls
			if (value.equals("0")) { 
				for (int i = 0; i < playersId.size(); i++) {
					try {
						String xml = downloader.getPlayer(String.valueOf(playersId.get(i)));
						PlayerXmlParser parser = new PlayerXmlParser();
						InputSource input = new InputSource(new StringReader(xml));
						try {
							parser.parseXmlSax(input, null);
						} catch (SAXException sex) {
							continue;
						}
						Player player = parser.getPlayer();
						if (player != null) {
							playersDao.updateUncompletedPlayer(player);
						} else {
							playersDao.updateNotExists(playersId.get(i));
						}
					} catch (IOException ioex) {
					}
				}
			}
		} catch (final SQLException e) {
			throw e;
		}
		return value;
	}


	public PlayersXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}
	
	public PlayersXmlManager(String content, Date currentDay, int teamId) {
		super(content, currentDay, teamId);
	}

	public PlayersXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("players", destination, downloader, currentDay); 
	}

	@Override
	public void download() throws IOException {
		setContent(downloader.getPlayers(downloader.getTeamId()));
	}

	public void download(String teamId) throws IOException {
		teamsMap.put(teamId, downloader.getPlayers(teamId));
	}
	
	public void importToSQL(Training training) throws SQLException {
		playersManager.addPlayers(players, training);
	}

	public List<Player> parseXML() throws SAXException {
		this.players = parseXML(getContent());
		return this.players;
	}

	public List<Player> parseXML(int teamId) throws SAXException {
		return parseXML(teamsMap.get(String.valueOf(teamId)));
	}
	
	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Player> parseXML(String xml) throws SAXException {
		PlayersXmlParser playersXmlParser = new PlayersXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			playersXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			playersXmlParser.parseXmlSax(input, null);
		}
		return playersXmlParser.getPlayers();
	}

	@Override
	public boolean write() throws IOException {
		return write(getContent(), downloader.getTeamId());
	}

}
