package com.web.FileExplorer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.web.FileExplorer.services.EncryptionService;

public class Driver {


	@SuppressWarnings("resource")
	public static void main(String[] args) {

		try {
			EncryptionService enc = new EncryptionService();
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			String str = "secret";
			byte[] pass = enc.getUTF8Bytes(enc.fixPadding("password", 16));
			Key key = new SecretKeySpec(pass, "AES");
			byte[] bytes = str.getBytes();

			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			c.init(Cipher.ENCRYPT_MODE, key, ivspec);

//			byte[] b = c.update(bytes, 0, bytes.length);
//			System.out.println(b.toString());

			
			byte[] buf = new byte[1024];
			
			InputStream tempIn1 = new ByteArrayInputStream(bytes);
			InputStream in1 = new CipherInputStream(tempIn1, c);

			ByteArrayOutputStream tempOut1 = new ByteArrayOutputStream();
			OutputStream out1 = new CipherOutputStream(tempOut1, c);
			
			int inRead = -1;
			
			try {
				while ((inRead = in1.read(buf)) >= 0) {
					out1.write(buf, 0, inRead);
				}
				out1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("enc: " + tempOut1.toByteArray().toString());
			
			// now decrypting
			
			Cipher other = Cipher.getInstance("AES/CBC/PKCS5Padding");
			other.init(Cipher.DECRYPT_MODE, key, ivspec);

			InputStream tempIn = new ByteArrayInputStream(bytes);
			InputStream in = new CipherInputStream(tempIn, other);

			ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
			OutputStream out = new CipherOutputStream(tempOut, other);

			int numRead = -1;

			try {
				while ((numRead = in.read(buf)) >= 0) {
					out.write(buf, 0, numRead);
				}
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(tempOut.toByteArray().toString());

//			other.init(Cipher.DECRYPT_MODE, key2, ivspec);

//			byte[] dec1 = other.update(b, 0, b.length);
//			System.out.println(dec1.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(9);

		a();

	}

	public static void b() {
		int cipherMode = -1;
		String key = "password";
		File inputFile = new File("temp2.txt");
		File outputFile = new File("temp.txt");
		String ALGORITHM = "AES";
		String TRANSFORMATION = "AES";

		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void a() {
		EncryptionService enc = new EncryptionService();
		String secret = "I am a SUPER secret!";
		String password = "passwordpassword";

		File target = new File("temp.txt");

		byte[] encrypted = enc.AESEncrypt(secret, password);
		System.out.println("Encry: " + encrypted.toString());

		System.out.println(enc.AESDecrypt(encrypted, password));

		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(target));
			write.write(encrypted.toString());
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader read = new BufferedReader(new FileReader(target));
			String line;

			while ((line = read.readLine()) != null) {
				System.out.println("lines: " + line);
				byte[] temp = enc.getUTF8Bytes(line);
				System.out.println("bytes: " + new String(temp));
				System.out.println("dec: " + enc.AESDecrypt(temp, password));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String decrypted = enc.AESDecrypt(encrypted, password);
		System.out.println("Decrypted: " + decrypted);

		System.exit(9);
	}
}
