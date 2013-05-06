package pl.pronux.sokker.downloader.managers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.actions.ConfigurationManager;
import pl.pronux.sokker.actions.TrainersManager;
import pl.pronux.sokker.downloader.xml.XMLDownloader;
import pl.pronux.sokker.downloader.xml.parsers.TrainerXmlParser;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Training;

public class TrainersXmlManager extends XmlManager<Coach> {

	private List<Coach> trainers;

	private TrainersManager trainersManager = TrainersManager.getInstance();
	
	private ConfigurationManager configurationManager = ConfigurationManager.getInstance();

	public TrainersXmlManager(String name, String destination, XMLDownloader downloader, Date currentDay) {
		super(name, destination, downloader, currentDay);
	}

	public TrainersXmlManager(String destination, XMLDownloader downloader, Date currentDay) {
		super("trainers", destination, downloader, currentDay); 
	}

	public TrainersXmlManager(String content, Date currentDay, int teamId) {
		super(content, currentDay, teamId);
	}

	@Override
	public void download() throws IOException {
		setContent(getDownloader().getTrainers());
	}

	public List<Coach> getTrainers() {
		return trainers;
	}

	public List<Coach> parseXML() throws SAXException {
		return parseXML(getContent());
	}

	public void repairCoaches() throws SQLException {
		trainersManager.repairCoaches(this.trainers);
		configurationManager.updateDbRepairCoaches(false);
	}

	@Override
	public void importToSQL() throws SQLException {
		trainersManager.importTrainers(trainers);
	}

	@Override
	public List<Coach> parseXML(String xml) throws SAXException {
		TrainerXmlParser trainerXmlParser = new TrainerXmlParser();
		InputSource input = new InputSource(new StringReader(xml));
		try {
			trainerXmlParser.parseXmlSax(input, null);
		} catch (SAXException ex) {
			input = new InputSource(new StringReader(filterCharacters(xml)));
			trainerXmlParser.parseXmlSax(input, null);
		}
		this.trainers = trainerXmlParser.getCoaches();
		return trainers;
	}

	public void importCoachesAtTraining(Training training) throws SQLException {
		if (training != null ) {
			if((training.getStatus() & Training.NEW_TRAINING) != 0) {
				trainersManager.importTrainersAtTraining(trainers, training);	
			} else if((training.getStatus() & Training.UPDATE_TRAINING) != 0) {
				trainersManager.updateTrainersAtTraining(trainers, training);
			}
			
		}
	}

}
