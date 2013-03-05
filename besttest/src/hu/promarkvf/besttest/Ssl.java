package hu.promarkvf.besttest;

import java.io.*;
import java.security.*;
import java.security.spec.*;

public class Ssl {
	public static final String OPENSSL_KEYTYPE_RSA = "RSA";
	public static final int OPENSSL_KEY_SIZE = 1024;

	private KeyPair keyPair;

	public Ssl() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(OPENSSL_KEYTYPE_RSA);

			keyGen.initialize(OPENSSL_KEY_SIZE);
			keyPair = keyGen.genKeyPair();

		}
		catch ( Exception e ) {
			e.printStackTrace();
			return;
		}
	}

	public String get_privateKey_string() {
		PrivateKey priv = keyPair.getPrivate();
		return getHexString(priv.getEncoded());
	}

	public String get_publicKey_string() {
		PublicKey pub = keyPair.getPublic();
		return getHexString(pub.getEncoded());
	}

	private String getHexString(byte[] b) {
		String result = "";
		for ( int i = 0; i < b.length; i++ ) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring(1);
		}
		return result;
	}

	public byte[] get_privateKey() {
		PrivateKey privateKey = keyPair.getPrivate();
		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		return pkcs8EncodedKeySpec.getEncoded();
	}

	public byte[] get_publicKey() {
		PublicKey publicKey = keyPair.getPublic();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		return x509EncodedKeySpec.getEncoded();
	}

	public KeyPair LoadKeyPair(String path, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Read Public Key.
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis = new FileInputStream(path + "/public.key");
		byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
		fis.read(encodedPublicKey);
		fis.close();

		// Read Private Key.
		File filePrivateKey = new File(path + "/private.key");
		fis = new FileInputStream(path + "/private.key");
		byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
		fis.read(encodedPrivateKey);
		fis.close();

		// Generate KeyPair.
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

		return new KeyPair(publicKey, privateKey);
	}
}
