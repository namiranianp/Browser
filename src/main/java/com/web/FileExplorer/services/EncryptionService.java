package com.web.FileExplorer.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.stream.CryptoInputStream;
import org.apache.commons.crypto.stream.CryptoOutputStream;

public class EncryptionService {

	/**
	 * Given a key and a string to encrypt, this method will encrypt the String
	 * using AES encryption. If the key is not 16 bytes long, it will be adjusted to
	 * be 16 bytes. If it is over 16 bytes, the first 16 bytes of the key will be
	 * used, if it is below then the first X bytes of the key will be appended to
	 * the end of the original key and that will be used for encryption where X is
	 * 16 - length of the String in bytes.
	 * 
	 * @param toEncrypt The string that will be encrypted
	 * @param encKey    The key that will be used for encryption, note the
	 *                  restrictions mentioned above
	 * @return A byte array containing the encrypted string that was passed in
	 */
	public byte[] AESEncrypt(String toEncrypt, String encKey) {
		String password = fixPadding(encKey, 16);
		byte[] passBytes = getUTF8Bytes(password);
		final String transform = "AES/CBC/PKCS5Padding";
		Properties properties = new Properties();
		final SecretKeySpec key = new SecretKeySpec(passBytes, "AES");
		final IvParameterSpec iv = new IvParameterSpec(passBytes);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try (CryptoOutputStream cos = new CryptoOutputStream(transform, properties, outputStream, key, iv)) {
			cos.write(getUTF8Bytes(toEncrypt));
			cos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	/**
	 * Given a key and a byte array to decrypt, this method will decrypt the String
	 * represented by the array using AES encryption. If the key is not 16 bytes
	 * long, it will be adjusted to be 16 bytes. If it is over 16 bytes, the first
	 * 16 bytes of the key will be used, if it is below then the first X bytes of
	 * the key will be appended to the end of the original key and that will be used
	 * for encryption where X is 16 - length of the String in bytes.
	 * 
	 * @param toDecrypt The byte array that will be decrypted
	 * @param decKey    The key that will be used for decryption, note the
	 *                  restrictions mentioned above
	 * @return A String containing the message that was encrypted
	 */
	public String AESDecrypt(byte[] toDecrypt, String decKey) {
		String password = fixPadding(decKey, 16);
		byte[] passBytes = getUTF8Bytes(password);
		
		final SecretKeySpec key = new SecretKeySpec(passBytes, "AES");
		final IvParameterSpec iv = new IvParameterSpec(passBytes);
		
		final String transform = "AES/CBC/PKCS5Padding";
		Properties properties = new Properties();

		InputStream inputStream = new ByteArrayInputStream(toDecrypt);
		byte[] decryptedData = new byte[Integer.MAX_VALUE / 2];
		int decryptedLen = 0;

		try (CryptoInputStream cis = new CryptoInputStream(transform, properties, inputStream, key, iv)) {
			int i;
			while ((i = cis.read(decryptedData, decryptedLen, decryptedData.length - decryptedLen)) > -1) {
				decryptedLen += i;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new String(decryptedData, 0, decryptedLen, StandardCharsets.UTF_8);
	}

	/**
	 * Used to ensure all Strings are a certain length.
	 * 
	 * If the String is the correct length, the original String will be returned
	 * 
	 * If the String is too short, what will be returned is the original String with
	 * the first X characters appended to the end where X is len - the length of the
	 * String.
	 * 
	 * If the String is too long, the first len characters will be returned
	 * 
	 * @param str The string to be padded/shortened
	 * @param len The desired length of the string
	 * @return
	 */
	public String fixPadding(String str, int len) {

		if (str == null || str.isEmpty()) {
			return str;
		}

		if (str.length() >= len) {
			return str.substring(0, len);
		}

		return str + str.substring(0, len - str.length());
	}

	/**
	 * 
	 * @param s String to be turned into a byte array
	 * @return Byte array of string given
	 */
	public byte[] getUTF8Bytes(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}
}
