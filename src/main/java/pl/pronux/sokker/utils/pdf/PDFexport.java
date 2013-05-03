package pl.pronux.sokker.utils.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import pl.pronux.sokker.handlers.SettingsHandler;
import pl.pronux.sokker.interfaces.SV;
import pl.pronux.sokker.model.Coach;
import pl.pronux.sokker.model.Date;
import pl.pronux.sokker.model.Junior;
import pl.pronux.sokker.model.Player;
import pl.pronux.sokker.model.SVNumberFormat;
import pl.pronux.sokker.model.SokkerDate;
import pl.pronux.sokker.resources.Messages;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFexport {
	private static int diffrents;

	private static PdfWriter writer;

	private static BaseFont helv;

	private static int width;

	private static int height;

	public static void export(String file, Player player) {
		PdfContentByte cb;
		width = 800;
		height = 750;
		int maxSkill = player.getSkills().length - 1;
		String cards = "\u2588"; 
		// step 1: creation of a document-object
		Document document = new Document(new com.lowagie.text.Rectangle(width,height));
		try {

			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();
			// step 3: we open the document

			// document.setMargins(2,2,2,2);
			// step 4: we add a paragraph to the document
			BaseFont bfCourier = BaseFont.createFont(SettingsHandler.getSokkerViewerSettings().getBaseDirectory() + File.separator + "fonts" + File.separator + "cour.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);  
			Font font = new Font(bfCourier, 12);

			Font fontBold = new Font(bfCourier, 14);
			fontBold.setStyle(Font.BOLD);

			Font fontMini = new Font(bfCourier, 8);

			Font red = new Font(bfCourier, 12, Font.NORMAL, new Color(0xFF, 0x00, 0x00));
			Font yellow = new Font(bfCourier, 12, Font.NORMAL, new Color(0xFF, 0xFF, 0x00));

			PdfPTable table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

			PdfPCell cell = new PdfPCell(new Paragraph(Messages.getString("player.information"), fontBold)); 
			cell.setColspan(2);
			cell.setBorder(0);
			table.addCell(cell);

			table.addCell(new Phrase(Messages.getString("player.id"), font)); 
			table.addCell(new Phrase(String.valueOf(player.getId()), font));

			table.addCell(new Phrase(Messages.getString("player.name"), font)); 
			table.addCell(new Phrase(player.getName(), font));

			table.addCell(new Phrase(Messages.getString("player.surname"), font)); 
			table.addCell(new Phrase(player.getSurname(), font));

			table.addCell(new Phrase(Messages.getString("player.country"), font)); 
			table.addCell(new Phrase(Messages.getString("country." + player.getCountryfrom() + ".name"), font));  

			table.addCell(new Phrase(Messages.getString("player.age"), font)); 
			table.addCell(new Phrase(String.format("%d [%d %s]", player.getSkills()[maxSkill].getAge(), player.getSkills()[maxSkill].getAge(),  SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getAge() - player.getSkills()[0].getAge())), font)); 

			table.addCell(new Phrase(Messages.getString("player.value"), font)); 
			table.addCell(new Phrase(player.getSkills()[maxSkill].getValue().formatDoubleCurrencySymbol(), font));

			table.addCell(new Phrase(Messages.getString("player.salary"), font)); 
			table.addCell(new Phrase(player.getSkills()[maxSkill].getSalary().formatDoubleCurrencySymbol(), font));

			table.addCell(new Phrase(Messages.getString("player.matches"), font)); 
			table.addCell(new Phrase(String.valueOf(player.getSkills()[maxSkill].getMatches()), font));

			table.addCell(new Phrase(Messages.getString("player.goals"), font)); 
			table.addCell(new Phrase(String.valueOf(player.getSkills()[maxSkill].getGoals()), font));

			table.addCell(new Phrase(Messages.getString("player.assists"), font)); 
			table.addCell(new Phrase(String.valueOf(player.getSkills()[maxSkill].getAssists()), font));

			table.addCell(new Phrase(Messages.getString("player.cards"), font)); 
			if (player.getSkills()[maxSkill].getCards() == 2) {
				cards += " " + cards; 
			} else if (player.getSkills()[maxSkill].getCards() == 0) {
				cards = ""; 
			}

			diffrents = player.getSkills()[maxSkill].getCards();
			if (diffrents > 0 && diffrents < 3) {
				table.addCell(new Phrase(cards, yellow));
			} else if (diffrents >= 3) {
				table.addCell(new Phrase(cards, red));
			} else {
				table.addCell(cards);
			}

			table.addCell(new Phrase(Messages.getString("player.injurydays"), font)); 
			double injury = player.getSkills()[maxSkill].getInjurydays();
			if (injury > 7) {
				table.addCell(new Phrase(String.format("%d %s [%d]", new BigDecimal(injury).setScale(0, BigDecimal.ROUND_UP), Messages.getString("injury.days"), injury), red));  
			} else if (injury > 0 && injury < 7) {
				table.addCell(new Phrase(String.format("%s [%d]", Messages.getString("injury.lastDays"), injury), red));  
			} else {
				table.addCell(new Phrase(String.format("[%f]", injury), font)); 
			}

			table.addCell(new Phrase(Messages.getString("player.position"), font)); 
			table.addCell(new Phrase(Messages.getString("assistant.position." + player.getPosition()), font)); 

			document.add(table);

			document.add(new Paragraph(" ")); 

			table = new PdfPTable(2);
			table.getDefaultCell().setBorder(0);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

			cell = new PdfPCell(new Paragraph(Messages.getString("player.skills"), fontBold)); 
			cell.setColspan(2);
			cell.setBorder(0);
			table.addCell(cell);

			table.addCell(new Phrase(Messages.getString("player.form"), font)); 
			table.addCell(new Phrase(String.format("%s [%d] ", Messages.getString("skill.b" + player.getSkills()[maxSkill].getForm()), player.getSkills()[maxSkill].getForm()), font));  

			table.addCell(new Phrase(Messages.getString("player.stamina"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.b" + player.getSkills()[maxSkill].getStamina()), player.getSkills()[maxSkill].getStamina(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getStamina() - player.getSkills()[0].getStamina())), font));  

			table.addCell(new Phrase(Messages.getString("player.pace"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.b" + player.getSkills()[maxSkill].getPace()), player.getSkills()[maxSkill].getPace(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getPace() - player.getSkills()[0].getPace())), font));  

			table.addCell(new Phrase(Messages.getString("player.technique"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.b" + player.getSkills()[maxSkill].getTechnique()), player.getSkills()[maxSkill].getTechnique(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getTechnique() - player.getSkills()[0].getTechnique())), font));  

			table.addCell(new Phrase(Messages.getString("player.passing"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.b" + player.getSkills()[maxSkill].getPassing()), player.getSkills()[maxSkill].getPassing(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getPassing() - player.getSkills()[0].getPassing())), font));  

			table.addCell(new Phrase(Messages.getString("player.keeper"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.a" + player.getSkills()[maxSkill].getKeeper()), player.getSkills()[maxSkill].getKeeper(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getKeeper() - player.getSkills()[0].getKeeper())), font));  

			table.addCell(new Phrase(Messages.getString("player.defender"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.a" + player.getSkills()[maxSkill].getDefender()), player.getSkills()[maxSkill].getDefender(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getDefender() - player.getSkills()[0].getDefender())), font));  

			table.addCell(new Phrase(Messages.getString("player.playmaker"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.a" + player.getSkills()[maxSkill].getPlaymaker()), player.getSkills()[maxSkill].getPlaymaker(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getPlaymaker() - player.getSkills()[0].getPlaymaker())), font));  

			table.addCell(new Phrase(Messages.getString("player.scorer"), font)); 
			table.addCell(new Phrase(String.format("%s [%d %s] ", Messages.getString("skill.a" + player.getSkills()[maxSkill].getScorer()), player.getSkills()[maxSkill].getScorer(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getScorer() - player.getSkills()[0].getScorer())), font));  

			table.addCell(new Phrase(Messages.getString("player.general"), font)); 
			table.addCell(new Phrase(String.format("[%d %s] ", player.getSkills()[maxSkill].getSummarySkill(), SVNumberFormat.formatIntegerWithSignZero(player.getSkills()[maxSkill].getSummarySkill() - player.getSkills()[0].getSummarySkill())), font)); 
			document.add(table);

			helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, false);
			String text = "Document generated by SokkerViewer " + SV.SK_VERSION; 
			float textBase = document.bottom() - 20;
			float textCenter = document.getPageSize().width() / 2;

			// step 4: we grab the ContentByte and do some stuff with it
			cb = writer.getDirectContent();

			// we tell the ContentByte we're ready to draw text
			cb.beginText();

			cb.setFontAndSize(helv, 10);
			// we show some text starting on some absolute position with a given
			// alignment
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, textCenter, textBase, 0);
			// we tell the contentByte, we've finished drawing text
			cb.endText();

			document.newPage();

  		DefaultFontMapper mapper = new DefaultFontMapper();

  		mapper.insertDirectory(SettingsHandler.getSokkerViewerSettings().getBaseDirectory() + File.separator + "fonts" + File.separator); 
  		DefaultFontMapper.BaseFontParameters pp;
  		if (SettingsHandler.IS_LINUX) {
  			pp = mapper.getBaseFontParameters("Free Monospaced"); 
  		} else {
  			pp = mapper.getBaseFontParameters("Courier New"); 
  		}

  		if (pp != null) {
  			pp.encoding = BaseFont.IDENTITY_H;
  		}

      cb = writer.getDirectContent();
      PdfTemplate tp = cb.createTemplate(width, height);
      Graphics2D g2 = tp.createGraphics(width, height, mapper);

			float[] widths = {
					0.15f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f,
					0.085f };

			table = new PdfPTable(11);
			table.setWidthPercentage(90);
			table.setWidths(widths);
			// table.getDefaultCell().setBorder(1);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

			cell = new PdfPCell(new Paragraph(Messages.getString("player.history"), fontBold)); 
			cell.setColspan(11);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			table.addCell(new Phrase(Messages.getString("table.date"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.form"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.stamina"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.pace"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.technique"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.passing"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.keeper"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.defender"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.playmaker"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.scorer"), fontMini)); 
			table.addCell(new Phrase(Messages.getString("table.generallSkill"), fontMini)); 

			for (int i = player.getSkills().length - 1; i >= 0; i--) {
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getDate().toDateString()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getForm()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getStamina()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getPace()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getTechnique()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getPassing()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getKeeper()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getDefender()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getPlaymaker()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getScorer()), fontMini));
				table.addCell(new Phrase(String.valueOf(player.getSkills()[i].getSummarySkill()), fontMini));
			}

			document.add(table);

			if (player.getJunior() != null) {
				Junior junior = player.getJunior();
				maxSkill = junior.getSkills().length - 1;
				document.newPage();
				table = new PdfPTable(2);
				table.getDefaultCell().setBorder(0);

				table.addCell(new Phrase(Messages.getString("junior.id"), fontMini)); 
				table.addCell(new Phrase(String.valueOf(junior.getId()), fontMini));
				table.addCell(new Phrase(Messages.getString("junior.skill"), fontMini)); 
				table.addCell(new Phrase(String.format("%s [%d] ", Messages.getString("skill.a" + junior.getSkills()[maxSkill].getSkill()), junior.getSkills()[maxSkill].getSkill()), fontMini));  
				table.addCell(new Phrase(Messages.getString("junior.weeksAll"), fontMini)); 
				table.addCell(new Phrase(String.valueOf(junior.getSkills()[0].getWeeks()), fontMini));
				table.addCell(new Phrase(Messages.getString("junior.numberOfJumps"), fontMini)); 
				table.addCell(new Phrase(String.valueOf(junior.getPops()), fontMini));
				table.addCell(new Phrase(Messages.getString("junior.averageJumps"), fontMini)); 
				table.addCell(new Phrase(String.valueOf(new BigDecimal(junior.getAveragePops()).setScale(2, BigDecimal.ROUND_HALF_UP)), fontMini));

				document.add(table);

				table = new PdfPTable(4);
				table.getDefaultCell().setBorder(0);

				table.addCell(new Phrase(Messages.getString("table.date"), fontMini)); 
				table.addCell(new Phrase(Messages.getString("table.skill"), fontMini)); 
				table.addCell(new Phrase(Messages.getString("table.week"), fontMini)); 
				table.addCell(new Phrase(Messages.getString("table.coach"), fontMini)); 
				document.add(table);

				table = new PdfPTable(4);
				for (int i = junior.getSkills().length - 1; i >= 0; i--) {
					table.addCell(new Phrase(junior.getSkills()[i].getDate().toDateString(), fontMini));
					table.addCell(new Phrase(String.valueOf(junior.getSkills()[i].getSkill()), fontMini));
					table.addCell(new Phrase(String.valueOf(junior.getSkills()[i].getWeeks()), fontMini));

					if (junior.getSkills()[i].getTraining() != null) {
						Coach coach = junior.getSkills()[i].getTraining().getJuniorCoach();
						if (coach != null) {
							int value = coach.getGeneralskill();
							table.addCell(new Phrase(String.valueOf(value), fontMini));
						} else {
							table.addCell(new Phrase(String.valueOf(" - "), fontMini)); 
						}
					} else {
						table.addCell(new Phrase(String.valueOf(" - "), fontMini)); 
					}
				}

				document.add(table);
				Integer[] intTable = new Integer[0];
				Date[] dateTable = new Date[0];
				ArrayList<Integer> intList = new ArrayList<Integer>();
				ArrayList<Date> dateList = new ArrayList<Date>();

				for (int i = 0; i < junior.getSkills().length; i++) {
					intList.add(junior.getSkills()[i].getSkill());
					dateList.add(junior.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
				}

				intTable = intList.toArray(new Integer[intList.size()]);
				dateTable = dateList.toArray(new Date[dateList.size()]);

				convertToPdf(getXYChart(Messages.getString("junior.skill"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 4, g2, cb, tp); 

			}


			Integer[] intTable = new Integer[0];
			Date[] dateTable = new Date[0];
			ArrayList<Integer> intList = new ArrayList<Integer>();
			ArrayList<Date> dateList = new ArrayList<Date>();


      document.newPage();

      cb = writer.getDirectContent();
      tp = cb.createTemplate(width, height);
      g2 = tp.createGraphics(width, height, mapper);


			intTable = new Integer[0];
			dateTable = new Date[0];
			intList = new ArrayList<Integer>();
			dateList = new ArrayList<Date>();

			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(player.getSkills()[i].getValue().calculatePrices());
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}

			intTable = intList.toArray(new Integer[intList.size()]);
			dateTable = dateList.toArray(new Date[dateList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.value"), intTable, dateTable, Calendar.THURSDAY, true, -1), document.getPageSize().width(), 0, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getForm()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.form"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 1, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getStamina()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.stamina"), intTable, dateTable, Calendar.THURSDAY, true, 12), document.getPageSize().width(), 2, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getPace()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);


      document.newPage();

      cb = writer.getDirectContent();
      tp = cb.createTemplate(width, height);
      g2 = tp.createGraphics(width, height, mapper);

			convertToPdf(getXYChart(Messages.getString("player.pace"), intTable, dateTable, Calendar.THURSDAY, true,18), document.getPageSize().width(), 0, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getTechnique()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.technique"), intTable, dateTable, Calendar.THURSDAY, true,18), document.getPageSize().width(), 1, g2, cb, tp); 


			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getPassing()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.passing"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 2, g2, cb, tp); 


			document.newPage();

      cb = writer.getDirectContent();
      tp = cb.createTemplate(width, height);
      g2 = tp.createGraphics(width, height, mapper);


			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getKeeper()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.keeper"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 0, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getDefender()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.defender"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 1, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getPlaymaker()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.playmaker"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 2, g2, cb, tp); 


			document.newPage();

      cb = writer.getDirectContent();
      tp = cb.createTemplate(width, height);
      g2 = tp.createGraphics(width, height, mapper);


			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getScorer()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.scorer"), intTable, dateTable, Calendar.THURSDAY, true, 18), document.getPageSize().width(), 0, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getAge()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.age"), intTable, dateTable, Calendar.THURSDAY, true, -1), document.getPageSize().width(), 1, g2, cb, tp); 

			intList.clear();
			dateList.clear();
			for (int i = 0; i < player.getSkills().length; i++) {
				intList.add(Integer.valueOf(player.getSkills()[i].getSummarySkill()));
				dateList.add(player.getSkills()[i].getDate().getTrainingDate(SokkerDate.THURSDAY));
			}
			dateTable = dateList.toArray(new Date[dateList.size()]);
			intTable = intList.toArray(new Integer[intList.size()]);

			convertToPdf(getXYChart(Messages.getString("player.general"), intTable, dateTable, Calendar.THURSDAY, true, -1), document.getPageSize().width(), 2, g2, cb, tp); 


			g2.dispose();
			document.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}


		// step 5: we close the document

	}

	/**
	 * Gets an example XY chart
	 *
	 * @return an XY chart
	 * @throws UnsupportedEncodingException
	 */
	public static JFreeChart getXYChart(String title, final Integer[] tempIntTable, final Date[] tempDateTable, final int trainingDay, final boolean zero, int maxValue) throws UnsupportedEncodingException {

		final TimeSeries series = new TimeSeries("Weeks Periods", Day.class); 

		GregorianCalendar date = new GregorianCalendar();

		for (int i = tempIntTable.length - 1; i >= 0; i--) {

			date.setTimeInMillis(tempDateTable[i].getMillis());
			if (trainingDay == Calendar.MONDAY) {
				date.setTimeInMillis(tempDateTable[i].getMillis() - tempDateTable[i].mondayFirstDayOfWeek() * 86400000);
			} else if (trainingDay == Calendar.THURSDAY) {
				date.setTimeInMillis(tempDateTable[i].getMillis() - tempDateTable[i].thursdayFirstDayOfWeek() * 86400000);
			} else if (trainingDay == Calendar.SATURDAY) {
				date.setTimeInMillis(tempDateTable[i].getMillis() - tempDateTable[i].saturdayFirstDayOfWeek() * 86400000);
			}

			series.add(new Day(date.getTime()), tempIntTable[i]);
		}

		final TimeSeriesCollection dataset = new TimeSeriesCollection(series);
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, false);   

		java.awt.Font font;
		if (SettingsHandler.IS_LINUX) {
			font = new java.awt.Font("Free Monospaced", java.awt.Font.PLAIN, 14); 
		} else {
			font = new java.awt.Font("Courier New", java.awt.Font.TRUETYPE_FONT, 14); 
		}

		chart.setTitle(new TextTitle(title, font));
		// // set the layout of our frame to a GridLayout so the chart will
		// // automatically fill the entire area

		// ChartPanel cp = new ChartPanel(chart);

		final XYPlot plot = chart.getXYPlot();
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, true);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		if (zero) {
			rangeAxis.setAutoRangeIncludesZero(true);
		} else {
			rangeAxis.setAutoRangeIncludesZero(false);
		}

    if(maxValue != -1) {
    	rangeAxis.setRange(-1,maxValue);
    	rangeAxis.setAutoTickUnitSelection(false);
    	font = rangeAxis.getLabelFont();
    	rangeAxis.setTickLabelFont(font.deriveFont(font.getStyle(),font.getSize() - 4));
    }

		return chart;
	}

	/**
	 * Converts a JFreeChart to PDF syntax.
	 *
	 * @param filename
	 *          the name of the PDF file
	 * @param chart
	 *          the JFreeChart
	 * @param width
	 *          the width of the resulting PDF
	 * @param height
	 *          the height of the resulting PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void convertToPdf(JFreeChart chart, float width, int chartNumber, Graphics2D g2, PdfContentByte cb, PdfTemplate tp ) throws DocumentException, IOException {

		int height = 250;

    Double rectangle2D = new Rectangle2D.Double(0, chartNumber * height, width, height);
    chart.draw(g2, rectangle2D);
    cb.addTemplate(tp, 0, 0);


	}

}
