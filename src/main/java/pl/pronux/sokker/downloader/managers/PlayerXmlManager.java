package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.data.sql.SQLSession;
import pl.pronux.sokker.data.sql.dao.PlayersArchiveDao;
import pl.pronux.sokker.data.sql.dao.TransfersDao;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.PlayerXmlParser;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.PlayerArchive;

public class PlayerXmlManager extends XmlManager<Player> {

	public void completePlayersArchive(int limit) throws SQLException {
		String value = "-3"; 
		PlayersArchiveDao playersArchiveDao = new PlayersArchiveDao(SQLSession.getConnection());
		TransfersDao transfersDao = new TransfersDao(SQLSession.getConnection());
		Set<Integer> playersToDownload = new HashSet<Integer>();

		List<Integer> uncompletedTransfers = transfersDao.getUncompletedTransfers();
		playersToDownload.addAll(uncompletedTransfers);
		
		if(limit - uncompletedTransfers.size() > 0) {
			limit -= uncompletedTransfers.size();
			List<Integer> alPlayersToDownload = playersArchiveDao.getIdPlayersNotInArchive(limit);
			playersToDownload.addAll(alPlayersToDownload);
		}
		
		if (downloader.getStatus().equals("OK")) { 
			value = "0"; 
		} else {
			value = downloader.getErrorno();
		}

		if (value.equals("0")) { 
			for (Integer playerId : playersToDownload) {
				try {
					String xml = downloader.getPlayer(String.valueOf(playerId));
					PlayerXmlParser parser = new PlayerXmlParser();
					InputSource input = new InputSource(new StringReader(xml));
					try {
						parser.parseXmlSax(input, null);
					} catch (SAXException sex) {
						continue;
					}
					Player player = parser.getPlayer();
					if (player != null) {
						playersArchiveDao.addPlayer(new PlayerArchive(player));
						write(xml, playerId);
					} else {
						playersArchiveDao.addNotExists(playerId);
					}
				} catch (IOException ioex) {
					// value = "-1";
					// FIXME: added expeption catch
				}
			}
		}
	}

	public PlayerXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public PlayerXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("player", destination, downloader, currentDay); 
	}

	@Override
	public void download() throws IOException {
//		setContent(downloader.getPlayers(downloader.getTeamID()));
	}

	public void importToSQL(Player player) throws SQLException {
//		new PlayersManager().addPlayers(alPlayers, training);
	}

	public List<Player> parseXML() throws SAXException {
//		return parseXML(getContent());
		return null;
	}

	@Override
	public void importToSQL() throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public boolean write(String content, Integer playerID) throws IOException {
		return super.write(content, String.valueOf(playerID));
	}

	@Override
	public List<Player> parseXML(String xml) throws SAXException {
		// TODO Auto-generated method stub
		return null;
	}
}
