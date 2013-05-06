package pl.pronux.sokker.downloader.managers;

import java.io.File;
import java.io.IOException;

import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.utils.file.OperationOnFile;

public class XmlManagerUtils {

	private String name;
	private String destination;
	private Date currentDay;

	public XmlManagerUtils(String name, String destination, Date currentDay) {
		this.name = name;
		this.destination = destination;
		this.setCurrentDay(currentDay);
	}
	
	public XmlManagerUtils(Date currentDay) {
		this.setCurrentDay(currentDay);
	}

	public static String filterCharacters(String xml) {
		char[] array = xml.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (xml.charAt(i) < 0x9 || (xml.charAt(i) > 0xa && xml.charAt(i) < 0xf)) {
			} else {
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}

	public boolean write(String content) throws IOException {
		return write(content, null);
	}
	
	public boolean write(String content, String teamId) throws IOException {
		String name = this.name;
		if(teamId != null) {
			name += "_" + teamId; 
		}
		String file = destination + File.separator + addTail(name, getCurrentDay());
		if(content != null) {
			OperationOnFile.writeToFileUTF(file, content);
			return true;
		} else {
			return false;
		}
	}

	private String addTail(String name, Date currentDay) {
		return String.format("%s_%d_%d_%d.xml", name, currentDay.getSokkerDate().getWeek(), currentDay.getSokkerDate().getDay(),currentDay.getMillis()); 
	}

	public Date getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(Date currentDay) {
		this.currentDay = currentDay;
	}
}
