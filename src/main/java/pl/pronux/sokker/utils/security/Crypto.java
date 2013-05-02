package pl.pronux.sokker.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import pl.pronux.sokker.exceptions.BadArgumentException;
import pl.pronux.sokker.utils.Log;

public class Crypto {

	public static SecretKeySpec convertByteArrayToSymmetricKey(byte[] keyBytes, String xform) {
		// The bytes can be converted back to a SecretKey
		return new SecretKeySpec(keyBytes, xform);
	}

	public static byte[] decryptSymmetric(byte[] encryptedMessage, SecretKeySpec skeySpec, String xform) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(xform);

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		return cipher.doFinal(encryptedMessage);
	}

	public static boolean verifySignature(PublicKey publicKey, byte[] signature, byte[] buffer) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("SHA1withRSA"); //$NON-NLS-1$
		sig.initVerify(publicKey);
		sig.update(buffer, 0, buffer.length);
		return sig.verify(signature);
	}

	public static boolean verifySignature(PublicKey publicKey, byte[] signature, File file) {
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			int c;
			StringBuilder sb = new StringBuilder();
			while ((c = in.read()) != -1) {
				sb.append(c);
			}

			in.close();

			byte[] buffer = sb.toString().getBytes();

			return verifySignature(publicKey, signature, buffer);
		} catch (FileNotFoundException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (IOException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (InvalidKeyException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (SignatureException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		}

		return false;
	}

	public static byte[] encryptSymmetric(byte[] message, SecretKeySpec keySpec, String xform) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ShortBufferException {

		Cipher cipher = Cipher.getInstance(xform);
		// SecretKeySpec keySpec = new SecretKeySpec("fedcba9876543210".getBytes(),
		// "Rijndael");
		// new JCERSAPrivateCrtKey()
		IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes()); //$NON-NLS-1$
		// cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		// byte[] outText = cipher.doFinal(fromHexString(input));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		message = enlargeByteTable(message);

		return cipher.doFinal(message);
	}

	public static String encodeBase64(byte[] buf) {
//		return new BASE64Encoder().encode(buf);
		return new String(Base64Coder.encode(buf));
		// return new String(Base64.encode(buf));
	}

	public static byte[] decodeBase64(String key) throws BadArgumentException {
		return Base64Coder.decode(key);
	}

	public static byte[] createSignature(PrivateKey key, File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			int c;
			StringBuilder sb = new StringBuilder();
			while ((c = in.read()) != -1) {
				sb.append(c);
			}

			in.close();

			byte[] buffer = sb.toString().getBytes();

			Signature sig = Signature.getInstance("SHA1withRSA"); //$NON-NLS-1$
			sig.initSign(key);
			sig.update(buffer, 0, buffer.length);
			return sig.sign();
		} catch (SignatureException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (InvalidKeyException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		} catch (IOException e) {
			Log.warning("Crypto", e); //$NON-NLS-1$
		}
		return null;
	}

	private static byte[] enlargeByteTable(byte[] in) {
		int blocksize = 16;
		int ciphertextLength = 0;
		int remainder = in.length % blocksize;
		if (remainder == 0) {
			ciphertextLength = in.length;
		} else {
			ciphertextLength = in.length - remainder + blocksize;
		}
		byte[] out = new byte[ciphertextLength];
		System.arraycopy(in, 0, out, 0, in.length);
		return out;
	}

	public static PublicKey convertByteArrayToPublicKey(byte[] publicKeyBytes, String xform) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// The bytes can be converted back to public and private key objects
		KeyFactory keyFactory = KeyFactory.getInstance(xform);
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		return keyFactory.generatePublic(publicKeySpec);
	}

	public static KeyPair generateKeyPair(String xform) throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(xform);
		kpg.initialize(1024);

		return kpg.generateKeyPair();
	}

	public static PrivateKey convertByteArrayToPrivateKey(byte[] privateKeyBytes, String xform) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// The bytes can be converted back to public and private key objects
		KeyFactory keyFactory = KeyFactory.getInstance(xform);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

		return keyFactory.generatePrivate(privateKeySpec);
	}
}
