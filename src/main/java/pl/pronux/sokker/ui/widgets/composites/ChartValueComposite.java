package pl.pronux.sokker.ui.widgets.composites;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import pl.pronux.sokker.model.Date;

public class ChartValueComposite extends Composite {
	private Canvas canvas;

	private JFreeChart chart;

	private Frame chartFrame;

	private int column;

	private Composite comp;

	private ChartPanel cp;

	PaintListener paintListener;

	public ChartValueComposite(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new FormLayout());

		FormData canvasFormFill = new FormData();
		canvasFormFill.top = new FormAttachment(0, 0);
		canvasFormFill.left = new FormAttachment(0, 0);
		canvasFormFill.right = new FormAttachment(100, 0);
		canvasFormFill.bottom = new FormAttachment(100, 0);
		// canvas = new Canvas(this, SWT.NO_REDRAW_RESIZE);
		// canvas.setVisible(false);

		// canvas.setLayoutData(canvasFormFill);

		comp = new Composite(this, SWT.EMBEDDED);
		comp.setLayoutData(canvasFormFill);

		chartFrame = SWT_AWT.new_Frame(comp);
		chartFrame.setLayout(new GridLayout());

	}

	public Canvas getCanvas() {
		return canvas;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public void fillGraph(List<Number> values1, List<Number> values2, List<Date> dates, final int trainingDay, final boolean zero, final double maxValue, boolean shapes, String title, String legend1, String legend2) {
		if (values1.size() <= 0 || values2.size() <= 0) {
			return;
		}

		final XYSeries series1 = new XYSeries(legend1);
		final XYSeries series2 = new XYSeries(legend2);

		// GregorianCalendar date;
		for (int i = values1.size() - 1; i >= 0; i--) {
			series1.add(dates.get(i).getSokkerDate().getWeek(), (Number) values1.get(i));
			series2.add(dates.get(i).getSokkerDate().getWeek(), (Number) values2.get(i));
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		setSeries(dataset, zero, maxValue, shapes, title);
	}

	public void fillGraph(List<Number> values, List<Date> dates, final int trainingDay, final boolean zero, final double maxValue, boolean shapes, String title) {
		if (values.size() <= 0) {
			return;
		}

		final XYSeries series = new XYSeries(""); //$NON-NLS-1$

		// GregorianCalendar date;
		for (int i = values.size() - 1; i >= 0; i--) {
			series.add(dates.get(i).getSokkerDate().getWeek(), (Number) values.get(i));
		}

		setSeries(series, zero, maxValue, shapes, title);
	}
	
	public void fillGraph(List<Number> values, final String[] tempDateTable, final int trainingDay, final boolean zero, final int maxValue, String title) {
		List<Date> dates = new ArrayList<Date>();

		for (int i = tempDateTable.length - 1; i >= 0; i--) {
			dates.add(new Date(tempDateTable[i].replaceAll("\\(.*\\)", ""))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		fillGraph(values, dates, trainingDay, zero, maxValue, true, title);
	}

	public void fillGraph(final double[] tempDoubleTable, final String[] tempDateTable, final int trainingDay, final boolean zero, final double maxValue) {
		List<Number> values = new ArrayList<Number>();
		List<Date> dates = new ArrayList<Date>();

		for (int i = tempDoubleTable.length - 1; i >= 0; i--) {
			dates.add(new Date(tempDateTable[i].replaceAll("\\(.*\\)", ""))); //$NON-NLS-1$ //$NON-NLS-2$
			values.add(tempDoubleTable[i]);
		}
		fillGraph(values, dates, trainingDay, zero, maxValue, true, ""); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param tempIntTable
	 * @param tempDateTable
	 * @param trainingDay
	 *          0 - saturday ... 6 - friday
	 */
	public void fillGraph(final int[] tempIntTable, final String[] tempDateTable, final int trainingDay, final boolean zero, final int maxValue) {
		List<Number> values = new ArrayList<Number>();
		List<Date> dates = new ArrayList<Date>();

		for (int i = tempIntTable.length - 1; i >= 0; i--) {
			dates.add(new Date(tempDateTable[i].replaceAll("\\(.*\\)", ""))); //$NON-NLS-1$ //$NON-NLS-2$
			values.add(tempIntTable[i]);
		}
		fillGraph(values, dates, trainingDay, zero, maxValue, true, ""); //$NON-NLS-1$
	}
	
	private void setSeries(final XYDataset dataset, final boolean zero, final double maxValue, boolean shapes, String title) {
		chartFrame.removeAll();
		
		chart = ChartFactory.createXYLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, true, true, false); //$NON-NLS-1$ //$NON-NLS-2$
		
		final XYPlot plot = chart.getXYPlot();
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for(int i = 0 ; i < dataset.getSeriesCount(); i++) {
			renderer.setSeriesLinesVisible(i, true);
			renderer.setSeriesShapesVisible(i, shapes);
		}
		plot.setRenderer(renderer);
		
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		if (zero) {
			rangeAxis.setAutoRangeIncludesZero(true);
		} else {
			rangeAxis.setAutoRangeIncludesZero(false);
		}

		if (maxValue != -1) {
			rangeAxis.setRange(-1, maxValue);
			rangeAxis.setAutoTickUnitSelection(false);
			java.awt.Font font = rangeAxis.getLabelFont();
			rangeAxis.setTickLabelFont(font.deriveFont(font.getStyle(), font.getSize() - 4));
		}
		
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		domainAxis.setAutoRangeMinimumSize(1.0);
		
		plot.setDomainAxis(domainAxis);
		
		drawChart(chart);

	}

	private void setSeries(final XYSeries series, final boolean zero, final double maxValue, boolean shapes, String title) {
		chartFrame.removeAll();
		final XYSeriesCollection dataset = new XYSeriesCollection(series);
		chart = ChartFactory.createXYLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, false, true, false); //$NON-NLS-1$ //$NON-NLS-2$
		
		final XYPlot plot = chart.getXYPlot();
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, shapes);
		plot.setRenderer(renderer);
		
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		if (zero) {
			rangeAxis.setAutoRangeIncludesZero(true);
		} else {
			rangeAxis.setAutoRangeIncludesZero(false);
		}

		if (maxValue != -1) {
			rangeAxis.setRange(-1, maxValue);
			rangeAxis.setAutoTickUnitSelection(false);
			java.awt.Font font = rangeAxis.getLabelFont();
			rangeAxis.setTickLabelFont(font.deriveFont(font.getStyle(), font.getSize() - 4));
		}
		
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		domainAxis.setAutoRangeMinimumSize(1.0);
		
		plot.setDomainAxis(domainAxis);
		
		drawChart(chart);

	}

	
	private void drawChart(JFreeChart chart) {
		cp = new ChartPanel(chart);
		chartFrame.add(cp);
		chartFrame.doLayout();
	}
	
	public void setMarkers(Date date, int day, Object value) {
		if (value instanceof Double) {
			setMarkers(date, day, (Double) value);
		} else if (value instanceof Integer) {
			setMarkers(date, day, (Integer) value);
		} else {
			return;
		}
	}

	public void setMarkers(Date date, int day, Integer range) {
		setMarkers(date, day, range.doubleValue());
	}

	public void setMarkers(Date date, int day, int range) {
		setMarkers(date, day, Double.valueOf(range));
	}

	public void setMarkers(Date date, int day, Double range) {
		int value = 0;
		chart.getXYPlot().clearDomainMarkers();

		value = date.getSokkerDate().getWeek();
		
		ValueMarker vm = new ValueMarker(value);
		vm.setPaint(new java.awt.Color(0, 0, 255));

		chart.getXYPlot().addDomainMarker(vm);

		chart.getXYPlot().clearRangeMarkers();

		vm = new ValueMarker(range);
		vm.setPaint(new java.awt.Color(0, 0, 255));

		chart.getXYPlot().addRangeMarker(vm);
	}
}
