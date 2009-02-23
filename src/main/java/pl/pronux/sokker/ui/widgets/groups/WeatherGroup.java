package pl.pronux.sokker.ui.widgets.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import pl.pronux.sokker.model.Match;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;
import pl.pronux.sokker.ui.resources.ColorResources;
import pl.pronux.sokker.ui.resources.ImageResources;

public class WeatherGroup extends Group {

	private FormData formData;

	private Label weatherIcoLabel;

	private Label temperatureIcoLabel;

	private Label weatherLabel;

	@Override
	protected void checkSubclass() {
		// super.checkSubclass();
	}

	public WeatherGroup(Composite parent, int style) {
		super(parent, style & ~SWT.BORDER);

		this.setLayout(new FormLayout());
		this.setFont(ConfigBean.getFontMain());

		this.setText(Messages.getString("weather")); //$NON-NLS-1$

		this.setForeground(ColorResources.getBlueDescription());

		formData = new FormData(45, 45);
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);

		weatherIcoLabel = new Label(this, SWT.CENTER);
		weatherIcoLabel.setLayoutData(formData);

		formData = new FormData(45, 45);
		formData.left = new FormAttachment(weatherIcoLabel, 5);
		formData.top = new FormAttachment(0, 5);

		temperatureIcoLabel = new Label(this, SWT.NONE);
		temperatureIcoLabel.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(weatherIcoLabel, 5);
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -5);

		weatherLabel = new Label(this, SWT.WRAP);
		weatherLabel.setLayoutData(formData);
		weatherLabel.setFont(ConfigBean.getFontMain());
	}

	public void setWeatherInfo(int weather) {
		if (weather < 0 || weather > 7) {
			weatherLabel.setText(""); //$NON-NLS-1$
			weatherIcoLabel.setImage(null);
			temperatureIcoLabel.setImage(null);
			return;
		}
		weatherLabel.setText(Messages.getString("weather." + weather)); //$NON-NLS-1$
		// weatherLabel.pack();

		switch (weather) {
		case 0:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather0.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp0.png")); //$NON-NLS-1$
			break;
		case 1:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather1.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp0.png")); //$NON-NLS-1$
			break;
		case 2:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather2.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp0.png")); //$NON-NLS-1$
			break;
		case 3:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather3.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp0.png")); //$NON-NLS-1$
			break;
		case 4:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather3.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp1.png")); //$NON-NLS-1$
			break;
		case 5:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather4.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp1.png")); //$NON-NLS-1$
			break;
		case 6:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather5.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp1.png")); //$NON-NLS-1$
			break;
		case 7:
			weatherIcoLabel.setImage(ImageResources.getImageResources("weather5.png")); //$NON-NLS-1$
			temperatureIcoLabel.setImage(ImageResources.getImageResources("temp2.png")); //$NON-NLS-1$
			break;
		default:
			break;
		}

		// composite.layout();
	}

	public void setWeatherInfo(Match match) {
		if (match.getIsFinished() == Match.NOT_FINISHED) {
			setWeatherInfo(-1);
		} else {
			setWeatherInfo(match.getWeather());
		}

	}

}
