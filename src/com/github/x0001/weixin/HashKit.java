package com.github.x0001.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashKit {
	public static String md5(String value) {
		try {
			return hash(MessageDigest.getInstance("md5"), value);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String sha1(String value) {
		try {
			return hash(MessageDigest.getInstance("SHA1"), value);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static String hash(MessageDigest digest,String src) {
		return toHexString(digest.digest(src.getBytes()));
	}
	
	private static String toHexString(byte[] bytes) {
		char[] values = new char[bytes.length * 2];
		int i=0;
		for(byte b : bytes) {
			values[i++] = LETTERS[((b & 0xF0) >>> 4)];
			values[i++] = LETTERS[b & 0xF];
		}
		return String.valueOf(values);
	}
	
	private static final char[] LETTERS = "0123456789ABCDEF".toCharArray();
}
