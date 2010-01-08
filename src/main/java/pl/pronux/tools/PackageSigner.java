package pl.pronux.tools;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.ui.widgets.dialogs.MessageDialog;
import pl.pronux.sokker.ui.widgets.shells.BugReporter;
import pl.pronux.sokker.utils.file.OperationOnFile;
import pl.pronux.sokker.utils.security.Crypto;

public class PackageSigner {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// byte[] pubk = new BASE64Decoder().decodeBuffer(SERVER_PUBLIC_KEY);
		// byte[] prvk = new BASE64Decoder().decodeBuffer(SERVER_PRIVATE_KEY);
		Display display = new Display();
		try {
			new PackageSigner(display);
		} catch (NoSuchAlgorithmException e) {
			new BugReporter(display).openErrorMessage("Package Signer", e);
		} catch (InvalidKeySpecException e) {
			new BugReporter(display).openErrorMessage("Package Signer", e);
		} catch (IOException e) {
			new BugReporter(display).openErrorMessage("Package Signer", e);
		} catch (BadArgumentException e) {
			new BugReporter(display).openErrorMessage("Package Signer", e);
		}

	}

	private Shell shell;

	private Table table;

	private String file;

	private String[] filenames;

	private Button signButton;

	private Button verifyButton;

	private Text signatureText;

	private PublicKey pubk;

	private PrivateKey prvk;

	protected byte[] sig;

	protected String directory;

	// final static String SERVER_PUBLIC_KEY = "" + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVI2+8Sj++dr68S59468aGTKBbDuwXr3+akNYB\n" +
	// "yea8ZtzIWuT4eFolC78jJCVZuAJzbJvcDy4iu0RhLKXNo+CujtrtLiM9rhfQvMtpyMSEcx1LVL82\n" + "4aEMpA0k/Lh+kEZ54uCQwTaVvpOj6GDvnZli9M49ZIOJamd86vkwhI/AJwIDAQAB";
	private final static String SERVER_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKMjwf/S2GfaPBEf+FY4Z4rduwIOgHtMn0fIqVlPaMPy8D/8I9yi7hQ8KTwcs8NWVu/loRN7nva37DPkSfqTbjASOw7gflxCJxndo8zm1gNzUBufRSy21nsMm8KodCpM/bGu41nguy1gebVZAbwJ9PRoNSwrXQyWXLRmJKWZW6eFAgMBAAECgYAjQ1ltyM+KMzwEn5p4WJzZAXCo0yWmgTt0ssUYTvfgUyTnT8MYsAcyTIbFPZcFxCXqmQImSfRkdAEUfmYXScLpO/UfaPzq/as5L+sgdi2f2coXaeJNpPLhvqvX5kWpCQK5iC8PTI5LQtUCbzflJATA+d1TrUWiC36/xMdfBT7riQJBAOq5vnPuzHvTtU7s5vJGVoDn+kGYqvJ458e8VWhQDC+8D1L57986D7wmYddNB/c9LVi0m1wpolJmGbnr50IZ0k8CQQCx7QgHI1Bn2CqQTI8d1bhObXUugzRBDIfInvlEFq9cQfxf4HXCXr9J5CC4yBsLtUQIDSIzi8K5DUdxuM0l9ZfrAkApGiVzf5lnBYd2Lp2yRwbWw8havDUTCIDtxNjfz0STB0dXrFSIrk4bEE2Juf9vF9Nc+SNLXC8EZsSSffCc7pCZAkEAgWT9GpvPBMZLJgfXaP4dfEPUHZBjdw8SzI74flD1FOYUEPHfEgzvlmAmf9te9+PKZwSOI+h3IzcKByU7ZGi4IwJBANlm8P0KU0Ra9wQnmm9zs7PhY9OCuo5MeDEv+sOolGZtViR1C/WjeacBymWWNYHar6qEZ6c5XZD+kDi+aOs4XFY=";
	//
	// final static String SERVER_PRIVATE_KEY = "" + "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANUjb7xKP752vrxLn3jrxoZMoFsO\n" +
	// "7Bevf5qQ1gHJ5rxm3Mha5Ph4WiULvyMkJVm4AnNsm9wPLiK7RGEspc2j4K6O2u0uIz2uF9C8y2nI\n" +
	// "xIRzHUtUvzbhoQykDST8uH6QRnni4JDBNpW+k6PoYO+dmWL0zj1kg4lqZ3zq+TCEj8AnAgMBAAEC\n" +
	// "gYBFPFqqlo/4Zgh1kDdAh46Yx8F7cvB8jTTtV8k9EYITh3KG3wfsMuRNjnBLkQ1VBLI/HnPO21uI\n"
	// + "Sr7etisyDt8IHPfyXWQvEdoufHw49UwqFKjdpFdr7MwYaLnfkbazmt5ugKNBaFkBuxZFmn7p4jH7\n" +
	// "JvI4lZWiVnDLo+Rxl+o+IQJBAP2Z9/tC+BKeC/GXTXziN6hfZJbgvAHNk8PMyFHl+XFddiA/p39f\n" +
	// "vi6aDTzAk8TolTk9nDagc/c6qLLvy4+7xVcCQQDXJ380md9AC/C/xXOE+icq+nrHYUvwKvOUxGSP\n" +
	// "hbl+pxnr5rc2xXDhQHeyY+eTVQZVzhU8GROxf8CZDaU2rcmxAkAbxKgqkwKmxzd3lKGfcwW1IfzZ\n"
	// + "qHHtoJz1a47jqNLPXvR4Q4ALqmggoi2g4VVM0krEocJhGOCZyYp/TVJ0wpKxAkEAjYmdJBRpii+Q\n" +
	// "tAeHxB3wMFTQ+mKmWxtWXDKKWORGr+vroIWJV42xgW0wkPkp+YFAhqfozj1M+EKrh8QzQHI5MQJB\n" +
	// "AJTc+Vl21elGs2ies9pg4udGXOKkgh4wZllrjzvGWyqaAyqHnU/DBB2BCINSGvwbhECWXU2oqr9E\n" + "+K+tsU8m4bk=";

	// final static String SERVER_PUBLIC_KEY =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVI2+8Sj++dr68S59468aGTKBbDuwXr3+akNYByea8ZtzIWuT4eFolC78jJCVZuAJzbJvcDy4iu0RhLKXNo+CujtrtLiM9rhfQvMtpyMSEcx1LVL824aEMpA0k/Lh+kEZ54uCQwTaVvpOj6GDvnZli9M49ZIOJamd86vkwhI/AJwIDAQAB";
	private final static String SERVER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjI8H/0thn2jwRH/hWOGeK3bsCDoB7TJ9HyKlZT2jD8vA//CPcou4UPCk8HLPDVlbv5aETe572t+wz5En6k24wEjsO4H5cQicZ3aPM5tYDc1Abn0UsttZ7DJvCqHQqTP2xruNZ4LstYHm1WQG8CfT0aDUsK10Mlly0ZiSlmVunhQIDAQAB";

	public PackageSigner(Display display) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, BadArgumentException {

		// pubk = Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(SERVER_PUBLIC_KEY), "RSA");
		pubk = Crypto.convertByteArrayToPublicKey(Crypto.decodeBase64(SERVER_PUBLIC_KEY), "RSA");

		prvk = Crypto.convertByteArrayToPrivateKey(Crypto.decodeBase64(SERVER_PRIVATE_KEY), "RSA");

		file = "";

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
		shell.setText("Package signer");
		shell.setSize(800, 600);
		shell.setMinimumSize(800, 600);
		shell.setLayout(new FormLayout());

		Rectangle splashRect = shell.getBounds();
		Rectangle displayRect = display.getPrimaryMonitor().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		shell.setLocation(x, y);

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 20);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -100);
		formData.bottom = new FormAttachment(50, 0);

		table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		table.setBackground(shell.getBackground());
		table.setLayoutData(formData);

		new TableColumn(table, SWT.LEFT);
		table.getColumn(0).pack();

		formData = new FormData();
		formData.top = new FormAttachment(0, 20);
		formData.left = new FormAttachment(table, 5);
		formData.height = 25;
		formData.right = new FormAttachment(100, -5);

		Button button = new Button(shell, SWT.BORDER | SWT.PUSH);
		button.setText("add");
		button.setLayoutData(formData);

		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				String[] extensions = { "*.xml;*.jar;*.zip;*.png;*.jpg;*.gif" };
				String defaultPath = "";
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
				if (new File(defaultPath).exists()) {
					fileDialog.setFilterPath(defaultPath);
				}
				fileDialog.setText("Open File");
				fileDialog.setFilterExtensions(extensions);

				fileDialog.open();
				filenames = fileDialog.getFileNames();
				directory = fileDialog.getFilterPath() + File.separator;
				// if(filenames.length > 1) {
				// // for(int i = 0; i < filenames.length; i++) {
				// // filenames = filenames[i];
				// // if (filename != null) {
				// //// table.setText(propsInputFile);
				// // file = propsInputFile;
				// // signButton.setEnabled(true);
				// // verifyButton.setEnabled(true);
				// // }
				// // }
				// } else
				// table.removeAll();
				if (filenames != null) {
					if (filenames.length > 0) {
						for (int i = 0; i < filenames.length; i++) {
							new TableItem(table, SWT.NONE).setText(directory + filenames[i]);
						}
						table.getColumn(0).pack();
						signButton.setEnabled(true);
						verifyButton.setEnabled(true);
					}

				}
			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(button, 20);
		formData.left = new FormAttachment(table, 5);
		formData.height = 25;
		formData.right = new FormAttachment(100, -5);

		button = new Button(shell, SWT.BORDER | SWT.PUSH);
		button.setText("remove");
		button.setLayoutData(formData);

		button.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {

				table.remove(table.getSelectionIndices());

			}

		});

		formData = new FormData();
		formData.top = new FormAttachment(table, 15);
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.bottom = new FormAttachment(100, -40);

		signatureText = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		signatureText.setLayoutData(formData);

		formData = new FormData(70, 25);
		formData.top = new FormAttachment(signatureText, 5);
		formData.right = new FormAttachment(50, -5);

		signButton = new Button(shell, SWT.PUSH);
		signButton.setText("SIGN");
		signButton.setEnabled(false);
		signButton.setLayoutData(formData);

		signButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				signatureText.setText("");
				for (int i = 0; i < table.getItemCount(); i++) {
					sig = Crypto.createSignature(prvk, new File(table.getItem(i).getText()));
					try {
						OperationOnFile.writeToFile(table.getItem(i).getText() + ".md5", Crypto.encodeBase64(sig));
					} catch (IOException e) {
						MessageDialog.openErrorMessage(shell, e.getMessage());
					}
					signatureText.setText(signatureText.getText() + directory + table.getItem(i).getText() + "\r\n\r\n<signature>" + Crypto.encodeBase64(sig)
										  + "</signature>\r\n\r\n");
				}
			}

		});

		formData = new FormData(70, 25);
		formData.top = new FormAttachment(signatureText, 5);
		formData.left = new FormAttachment(50, 5);

		verifyButton = new Button(shell, SWT.PUSH);
		verifyButton.setText("VERIFY");
		verifyButton.setEnabled(false);
		verifyButton.setLayoutData(formData);

		verifyButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event arg0) {
				boolean verify = Crypto.verifySignature(pubk, sig, new File(file));
				if (verify) {
					shell.setText("OK!");
				} else {
					shell.setText("WRONG!");
				}
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
