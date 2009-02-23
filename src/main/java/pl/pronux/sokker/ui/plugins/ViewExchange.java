package pl.pronux.sokker.ui.plugins;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import pl.pronux.sokker.data.cache.Cache;
import pl.pronux.sokker.model.Country;
import pl.pronux.sokker.model.Exchange;
import pl.pronux.sokker.model.SokkerViewerSettings;
import pl.pronux.sokker.model.SvBean;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.handlers.ViewerHandler;
import pl.pronux.sokker.ui.interfaces.IPlugin;
import pl.pronux.sokker.ui.interfaces.IViewConfigure;
import pl.pronux.sokker.ui.listeners.SelectAllListener;
import pl.pronux.sokker.ui.listeners.SelectNoneListener;
import pl.pronux.sokker.ui.listeners.VerifyMoneyListener;
import pl.pronux.sokker.ui.resources.ImageResources;
import pl.pronux.sokker.ui.widgets.tables.ExchangeTable;

public class ViewExchange implements IPlugin {

	private TreeItem _treeItem;
	private ArrayList<Exchange> alExchange;
	private Composite composite;
	private Button countButton;
	private Exchange exchange;

	private ExchangeTable exchangeTable;
	private Label valueLabel;
	private Label currencyLabel;
	private HashMap<String, Double> currencyHM;
	private Combo currencyCombo;
	private List<Country> countries;
	private Listener verifyCurrencyList;

//	public ViewExchange(Composite parent, int style) {
//		super(parent, style);
//		thisComposite = this;
//		langProperties = SokkerBean.getLangProperties();
//		confProperties = SokkerBean.getConfProperties();
//		this.setLayout(new FormLayout());
//	}

	public void clear() {
	}

	public Composite getComposite() {
		return composite;
	}

	public String getInfo() {
		return this.getClass().getSimpleName();
	}

	public String getStatusInfo() {
		return Messages.getString("progressBar.info.setInfoExchange"); //$NON-NLS-1$
	}

	public void init(Composite composite) {
		this.composite = composite;
		
		countries = new ArrayList<Country>();
		composite.setLayout(new FormLayout());

		addViewComposite();

		composite.layout(true);
	}


	public void setSettings(SokkerViewerSettings sokkerViewerSettings) {
//		this.confProperties = confProperties;
	}

	public void setTreeItem(TreeItem treeItem) {

		this._treeItem = treeItem;

		_treeItem.setText(Messages.getString("tree.ViewExchange")); //$NON-NLS-1$

		_treeItem.setImage(ImageResources.getImageResources("calc.png")); //$NON-NLS-1$

		_treeItem.getParent().addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TreeItem item = _treeItem.getParent().getItem(pt);
				if(item != null) {
					if(item.equals(_treeItem)) {
						ViewerHandler.getViewer().setDefaultButton(countButton);
					}
				}
			}
		});
	}

	public void set() {
		int id;
		String text;
		currencyHM = new HashMap<String,Double>();
		alExchange = new ArrayList<Exchange>();
		countries = Cache.getCountries();

		for(Country country : countries) {
			id = country.getCountryID();

			text = String.format("%s (%s)", Messages.getString("country." + id + ".name"), country.getCurrencyName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			currencyCombo.add(text);
			currencyHM.put(text, country.getCurrencyRate());
		}
		currencyCombo.setText(currencyCombo.getItem(0));

		for(Country country : countries) {
			exchange = new Exchange();
			id = country.getCountryID();
			exchange.setId(id);
			exchange.setName(Messages.getString("country." + id + ".name")); //$NON-NLS-1$ //$NON-NLS-2$
			exchange.setOriginalName(country.getName());
			exchange.setCurrency(country.getCurrencyName());
			exchange.setExchange(country.getCurrencyRate());
			exchange.setValue(0);
			alExchange.add(exchange);
		}
		countButton.addListener(SWT.Selection, verifyCurrencyList);
		
		exchangeTable.fill(alExchange);
	}

	private void addViewComposite() {
		int fieldHigh = 15;
		int fieldWidth = 200;

		FormData formData = new FormData();
		formData.top = new FormAttachment(0,5);
		formData.left = new FormAttachment(0,5);

		valueLabel = new Label(composite, SWT.NONE);
		valueLabel.setLayoutData(formData);
		valueLabel.setText(Messages.getString("exchange.valueLabel")); //$NON-NLS-1$
		valueLabel.setFont(ConfigBean.getFontMain());
		valueLabel.pack();

		formData = new FormData(fieldWidth, fieldHigh);
		formData.top = new FormAttachment(valueLabel,5);
		formData.left = new FormAttachment(0,5);

		final Text valueText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		valueText.setLayoutData(formData);
		valueText.addListener(SWT.MouseDown, new SelectAllListener());
		valueText.addListener(SWT.FocusIn , new SelectAllListener());
		valueText.addListener(SWT.FocusOut, new SelectNoneListener());
		valueText.addListener(SWT.Verify, new VerifyMoneyListener());
		valueText.setTextLimit(20);
		valueText.setText("0"); //$NON-NLS-1$
		valueText.setFont(ConfigBean.getFontMain());

		formData = new FormData(fieldWidth, fieldHigh);
		formData.top = new FormAttachment(valueText,5);
		formData.left = new FormAttachment(0,5);

		currencyLabel = new Label(composite, SWT.NONE);
		currencyLabel.setLayoutData(formData);
		currencyLabel.setText(Messages.getString("exchange.currencyLabel")); //$NON-NLS-1$
		currencyLabel.setFont(ConfigBean.getFontMain());


		formData = new FormData(fieldWidth, fieldHigh);
		formData.top = new FormAttachment(currencyLabel,5);
		formData.left = new FormAttachment(0,5);

		currencyCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		currencyCombo.setFont(ConfigBean.getFontMain());

		currencyCombo.setLayoutData(formData);

		verifyCurrencyList = new Listener() {
			public void handleEvent(Event e) {
				String string = valueText.getText();
				String currency = currencyCombo.getText();
				if(!string.matches("[0-9]+(.[0-9]{2}){0,1}")) { //$NON-NLS-1$
					MessageBox messageBox = new MessageBox(composite.getShell(), SWT.OK | SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
					messageBox.setText(Messages.getString("message.error")); //$NON-NLS-1$
					messageBox.setMessage(Messages.getString("message.badValueFormat")); //$NON-NLS-1$
					messageBox.open();
					e.doit = false;
					return;
				}
				BigDecimal summary = new BigDecimal(0);
				double value = Double.valueOf(string).doubleValue();
				double tempSummary = 0;
				tempSummary = value * Double.valueOf(currencyHM.get(currency));



					for(int i = 0; i < exchangeTable.getItemCount() ; i++) {
						summary = new BigDecimal(tempSummary / alExchange.get(i).getExchange()).setScale(2, RoundingMode.HALF_UP);

						alExchange.get(i).setValue(summary.doubleValue());
						exchangeTable.getItem(i).setText(5,String.valueOf(summary));
					}

				exchangeTable.getColumn(5).pack();
			}

		};

		formData = new FormData();
		formData.top = new FormAttachment(currencyCombo,10);
		formData.left = new FormAttachment(0,5);

		countButton = new Button(composite, SWT.NONE);
		countButton.setLayoutData(formData);
		countButton.setText(Messages.getString("button.count")); //$NON-NLS-1$
		countButton.setFont(ConfigBean.getFontMain());
		countButton.pack();

		formData = new FormData();
		formData.top = new FormAttachment(countButton,10);
		formData.left = new FormAttachment(0,0);
		formData.right = new FormAttachment(100,0);
		formData.bottom = new FormAttachment(100,0);

		exchangeTable = new ExchangeTable(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		exchangeTable.setLayoutData(formData);

		composite.layout(true);
	}

	public TreeItem getTreeItem() {
		return _treeItem;
	}

	public IViewConfigure getConfigureComposite() {
		return null;
	}

	public void dispose() {

	}

	public void setSvBean(SvBean svBean) {

	}

	public void reload() {
		// TODO Auto-generated method stub
	}


}
