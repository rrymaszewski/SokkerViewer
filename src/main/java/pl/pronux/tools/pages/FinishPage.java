package pl.pronux.tools.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.wizards.Wizard;
import pl.pronux.sokker.ui.widgets.wizards.pages.Page;
import pl.pronux.sokker.updater.model.Package;
import pl.pronux.sokker.updater.xml.UpdateXMLParser;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.security.Crypto;

public class FinishPage extends Page {
	private static final String SERVER_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKMjwf/S2GfaPBEf+FY4Z4rduwIOgHtMn0fIqVlPaMPy8D/8I9yi7hQ8KTwcs8NWVu/loRN7nva37DPkSfqTbjASOw7gflxCJxndo8zm1gNzUBufRSy21nsMm8KodCpM/bGu41nguy1gebVZAbwJ9PRoNSwrXQyWXLRmJKWZW6eFAgMBAAECgYAjQ1ltyM+KMzwEn5p4WJzZAXCo0yWmgTt0ssUYTvfgUyTnT8MYsAcyTIbFPZcFxCXqmQImSfRkdAEUfmYXScLpO/UfaPzq/as5L+sgdi2f2coXaeJNpPLhvqvX5kWpCQK5iC8PTI5LQtUCbzflJATA+d1TrUWiC36/xMdfBT7riQJBAOq5vnPuzHvTtU7s5vJGVoDn+kGYqvJ458e8VWhQDC+8D1L57986D7wmYddNB/c9LVi0m1wpolJmGbnr50IZ0k8CQQCx7QgHI1Bn2CqQTI8d1bhObXUugzRBDIfInvlEFq9cQfxf4HXCXr9J5CC4yBsLtUQIDSIzi8K5DUdxuM0l9ZfrAkApGiVzf5lnBYd2Lp2yRwbWw8havDUTCIDtxNjfz0STB0dXrFSIrk4bEE2Juf9vF9Nc+SNLXC8EZsSSffCc7pCZAkEAgWT9GpvPBMZLJgfXaP4dfEPUHZBjdw8SzI74flD1FOYUEPHfEgzvlmAmf9te9+PKZwSOI+h3IzcKByU7ZGi4IwJBANlm8P0KU0Ra9wQnmm9zs7PhY9OCuo5MeDEv+sOolGZtViR1C/WjeacBymWWNYHar6qEZ6c5XZD+kDi+aOs4XFY=";
	// private final static String SERVER_PUBLIC_KEY =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjI8H/0thn2jwRH/hWOGeK3bsCDoB7TJ9HyKlZT2jD8vA//CPcou4UPCk8HLPDVlbv5aETe572t+wz5En6k24wEjsO4H5cQicZ3aPM5tYDc1Abn0UsttZ7DJvCqHQqTP2xruNZ4LstYHm1WQG8CfT0aDUsK10Mlly0ZiSlmVunhQIDAQAB";

	public static final String PAGE_NAME = "FINISH_PAGE";
	// private PublicKey pubk;
	private PrivateKey prvk;

	public FinishPage(Wizard parent) {
		super(parent, "Finish page", PAGE_NAME);
		try {
			// pubk =
			// Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(SERVER_PUBLIC_KEY),
			// "RSA");
			prvk = Crypto.convertByteArrayToPrivateKey(Crypto.decodeBase64(SERVER_PRIVATE_KEY), "RSA");
		} catch (NoSuchAlgorithmException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		} catch (InvalidKeySpecException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		} catch (BadArgumentException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		}

	}

	@Override
	protected void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 30;
		container.setLayout(gridLayout);
	}

	@Override
	public void onEnterPage() {
		getWizard().getPreviousButton().setEnabled(false);
		getWizard().getCancelButton().setEnabled(false);
		getWizard().getNextButton().setEnabled(false);

		OperationOnFile.createDirectory(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "windows");
		OperationOnFile.createDirectory(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "linux");
		OperationOnFile.createDirectory(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "mac");
		try {
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "windows" + File.separator + "packages.xml",
					buildXML("windows"));
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "linux" + File.separator + "packages.xml",
					buildXML("linux"));
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "mac" + File.separator + "packages.xml", buildXML("mac"));

			byte[] sig = Crypto.createSignature(prvk, new File(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "windows" + File.separator
					+ "packages.xml"));
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "windows" + File.separator + "packages.xml.md5", Crypto
					.encodeBase64(sig));
			sig = Crypto
					.createSignature(prvk, new File(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "linux" + File.separator + "packages.xml"));
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "linux" + File.separator + "packages.xml.md5", Crypto
					.encodeBase64(sig));
			sig = Crypto.createSignature(prvk, new File(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "mac" + File.separator + "packages.xml"));
			OperationOnFile.writeToFile(((DestinationPage) getWizard().getPage(DestinationPage.PAGE_NAME)).getDirectory() + File.separator + "mac" + File.separator + "packages.xml.md5", Crypto
					.encodeBase64(sig));

		} catch (IOException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		}

		getWizard().getFinishButton().setEnabled(true);
		super.onEnterPage();
	}

	private String buildXML(String os) {
		StringBuilder xml = new StringBuilder();
		Map<String, WidgetsCollection> map = ((ConfigurationPage) getWizard().getPage(ConfigurationPage.PAGE_NAME)).getWidgetsMap();
		byte[] sig;
		WidgetsCollection collection;
		xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		xml.append("<sokkerviewer>\n");

		xml.append("\t<version>" + ((ConfigurationPage) getWizard().getPage(ConfigurationPage.PAGE_NAME)).getVersion() + "</version>\n");
		xml.append("\t<revision>" + ((ConfigurationPage) getWizard().getPage(ConfigurationPage.PAGE_NAME)).getRevision() + "</revision>\n");
		xml.append("\t<changelog>" + "" + "</changelog>\n");

		xml.append("\t<packages>\n");
		for (String packageName : map.keySet()) {
			collection = map.get(packageName);
			if (collection.getPackageOS().getText().matches(os + "/") || collection.getPackageOS().getText().matches("")) {
				xml.append("\t\t<package revision=\"" + collection.getPackageRevision().getSelection() + "\">\n");
				xml.append("\t\t\t<name>" + collection.getPackageName().getText() + "</name>\n");
				sig = Crypto.createSignature(prvk, collection.getFile());
				xml.append("\t\t\t<signature>" + Crypto.encodeBase64(sig) + "</signature>\n");
				xml.append("\t\t\t<path>" + ((ConfigurationPage) getWizard().getPage(ConfigurationPage.PAGE_NAME)).getBasePath() + collection.getPackageOS().getText()
						+ collection.getPackageLocalFilename().getText() + "</path>\n");
				xml.append("\t\t\t<author>" + collection.getPackageAuthor().getText() + "</author>\n");
				xml.append("\t\t\t<localpath>" + collection.getPackageLocalpath().getText() + "</localpath>\n");
				xml.append("\t\t\t<filename>" + collection.getPackageLocalFilename().getText() + "</filename>\n");
				xml.append("\t\t\t<description></description>\n");
				xml.append("\t\t</package>\n");
			}
		}

		UpdateXMLParser oldParser = new UpdateXMLParser();
		try {

			String file = "";

			if (os.equals("mac")) {
				file = ((OldXMLPage) getWizard().getPage(OldXMLPage.PAGE_NAME)).getMacXMLFile();
			} else if (os.equals("linux")) {
				file = ((OldXMLPage) getWizard().getPage(OldXMLPage.PAGE_NAME)).getLinuxXMLFile();
			} else {
				file = ((OldXMLPage) getWizard().getPage(OldXMLPage.PAGE_NAME)).getWindowsXMLFile();
			}
			InputSource inputOld = new InputSource(new FileReader(file));
			oldParser.parseXmlSax(inputOld, null);
		} catch (FileNotFoundException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		} catch (SAXException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		} catch (IOException e) {
			MessageDialog.openErrorMessage(getWizard().getShell(), e.getMessage());
		}

		for (String packageName : oldParser.alPackages.keySet()) {
			Object o;
			String name;
			// if (packageName.matches("core")) {
			// o = map.get(packageName + os + "/");
			// name = packageName + os + "/";
			// } else {
			o = map.get(packageName);
			name = packageName;
			// }
			if (o == null) {
				Package pkg = oldParser.alPackages.get(name);
				xml.append("\t\t<package revision=\"" + pkg.getRevision() + "\">\n");
				xml.append("\t\t\t<name>" + pkg.getName() + "</name>\n");
				xml.append("\t\t\t<signature>" + pkg.getSignature() + "</signature>\n");
				xml.append("\t\t\t<path>" + pkg.getPath() + "</path>\n");
				xml.append("\t\t\t<author>" + pkg.getAuthor() + "</author>\n");
				xml.append("\t\t\t<localpath>" + pkg.getLocalpath() + "</localpath>\n");
				xml.append("\t\t\t<filename>" + pkg.getFilename() + "</filename>\n");
				xml.append("\t\t\t<description></description>\n");
				xml.append("\t\t</package>\n");
			}
		}
		xml.append("\t</packages>\n");
		xml.append("</sokkerviewer>");

		return xml.toString();
	}
}
