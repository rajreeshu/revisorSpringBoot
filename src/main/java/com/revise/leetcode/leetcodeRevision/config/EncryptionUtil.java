package com.revise.leetcode.leetcodeRevision.config;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
	private static final String ALGORITHM = "AES";
	private static final byte[] KEY = "0b5e39475ee044ba3eb0c4748696c167".getBytes(); // Use a 16, 24, 32 bytes key

	public static String encrypt(String value) {
		try {
			SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedValue = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encryptedValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String encryptedValue) {
		try {
			SecretKey secretKey = new SecretKeySpec(KEY, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
			byte[] decryptedValue = cipher.doFinal(decodedValue);
			return new String(decryptedValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
