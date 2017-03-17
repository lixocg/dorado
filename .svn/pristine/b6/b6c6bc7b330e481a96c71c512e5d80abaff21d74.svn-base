package com.experian.core.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CiperUtil {
	private static Log log = LogFactory.getLog(CiperUtil.class);

	public static final String KEY_ALGORITHM = "DES";
	public static final String DES_ECB_ALGORITHM = "DES/ECB/PKCS5Padding";
	public static final String DES_CBC_ALGORITHM = "DES/CBC/PKCS5Padding";
	public static final String DES_CBC_NOPADDING = "DES/CBC/NoPadding";
	/**
	 * 解密加密密钥，双方约定
	 */
	public static String SECURITY_KEY = "experian!@#bj!dorado";

	public static final byte[] DES_CBC_IV = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	public static void setSECURITY_KEY(String sECURITY_KEY) {
		SECURITY_KEY = sECURITY_KEY;
	}

	public static byte[] hex2Ascii(byte[] in) {
		byte[] temp1 = new byte[1];
		byte[] temp2 = new byte[1];
		byte[] out = new byte[in.length * 2];
		int i = 0;
		for (int j = 0; i < in.length; ++i) {
			temp1[0] = in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0xF);
			temp2[0] = in[i];
			temp2[0] = (byte) (temp2[0] & 0xF);
			if ((temp1[0] >= 0) && (temp1[0] <= 9))
				out[j] = (byte) (temp1[0] + 48);
			else if ((temp1[0] >= 10) && (temp1[0] <= 15)) {
				out[j] = (byte) (temp1[0] + 87);
			}

			if ((temp2[0] >= 0) && (temp2[0] <= 9))
				out[(j + 1)] = (byte) (temp2[0] + 48);
			else if ((temp2[0] >= 10) && (temp2[0] <= 15)) {
				out[(j + 1)] = (byte) (temp2[0] + 87);
			}
			j += 2;
		}
		return out;
	}

	public static byte[] ascii2Hex(byte[] in) {
		byte[] temp1 = new byte[1];
		byte[] temp2 = new byte[1];
		int i = 0;
		byte[] out = new byte[in.length / 2];
		for (int j = 0; i < in.length; ++j) {
			temp1[0] = in[i];
			temp2[0] = in[(i + 1)];
			if ((temp1[0] >= 48) && (temp1[0] <= 57)) {
				int tmp53_52 = 0;
				byte[] tmp53_51 = temp1;
				tmp53_51[tmp53_52] = (byte) (tmp53_51[tmp53_52] - 48);
				temp1[0] = (byte) (temp1[0] << 4);

				temp1[0] = (byte) (temp1[0] & 0xF0);
			} else if ((temp1[0] >= 97) && (temp1[0] <= 102)) {
				int tmp101_100 = 0;
				byte[] tmp101_99 = temp1;
				tmp101_99[tmp101_100] = (byte) (tmp101_99[tmp101_100] - 87);
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xF0);
			}

			if ((temp2[0] >= 48) && (temp2[0] <= 57)) {
				int tmp149_148 = 0;
				byte[] tmp149_146 = temp2;
				tmp149_146[tmp149_148] = (byte) (tmp149_146[tmp149_148] - 48);

				temp2[0] = (byte) (temp2[0] & 0xF);
			} else if ((temp2[0] >= 97) && (temp2[0] <= 102)) {
				int tmp192_191 = 0;
				byte[] tmp192_189 = temp2;
				tmp192_189[tmp192_191] = (byte) (tmp192_189[tmp192_191] - 87);

				temp2[0] = (byte) (temp2[0] & 0xF);
			}
			out[j] = (byte) (temp1[0] | temp2[0]);
			i += 2;
		}
		return out;
	}

	public static byte[] genSecretKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
			keyGenerator.init(56);
			SecretKey secretKey = keyGenerator.generateKey();
			return hex2Ascii(secretKey.getEncoded());
		} catch (Exception e) {
			log.error("exception:", e);
		}
		return null;
	}

	private static Key toKey(byte[] key) {
		try {
			DESKeySpec des = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			SecretKey secretKey = keyFactory.generateSecret(des);
			return secretKey;
		} catch (Exception e) {
			log.error("exception:", e);
		}
		return null;
	}

	public static byte[] encrypt(byte[] data, byte[] key, String algorithm) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(algorithm);
			if (DES_CBC_ALGORITHM.equals(algorithm) || DES_CBC_NOPADDING.equals(algorithm)) {
				IvParameterSpec spec = new IvParameterSpec(DES_CBC_IV);
				cipher.init(Cipher.ENCRYPT_MODE, k, spec);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, k);
			}
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error("exception:", e);
		}
		return null;
	}

	public static byte[] decrypt(byte[] data, byte[] key, String algorithm) {
		try {
			Key k = toKey(key);
			Cipher cipher = Cipher.getInstance(algorithm);
			if (DES_CBC_ALGORITHM.equals(algorithm) || DES_CBC_NOPADDING.equals(algorithm)) {
				IvParameterSpec spec = new IvParameterSpec(DES_CBC_IV);
				cipher.init(Cipher.DECRYPT_MODE, k, spec);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, k);
			}
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error("exception:", e);
		}
		return null;
	}

	public static String encrypt(String securityKey, String data) {
		byte[] aa = encrypt(data.getBytes(), ascii2Hex(securityKey.getBytes()), DES_ECB_ALGORITHM);
		return new String(hex2Ascii(aa));
	}

	public static String decrypt(String securityKey, String data) {
		byte[] aa = ascii2Hex(data.getBytes());
		return new String(decrypt(aa, ascii2Hex(securityKey.getBytes()), DES_ECB_ALGORITHM));
	}

	public static String encrypt(String securityKey, String data, String algorithm) {
		byte[] aa = encrypt(data.getBytes(), ascii2Hex(securityKey.getBytes()), algorithm);
		return new String(hex2Ascii(aa));
	}

	public static String decrypt(String securityKey, String data, String algorithm) {
		byte[] aa = ascii2Hex(data.getBytes());
		return new String(decrypt(aa, ascii2Hex(securityKey.getBytes()), algorithm));
	}

	public static byte[] paddingZero(byte[] in) {
		if (in == null || in.length == 0) {
			return null;
		}
		int inLen = in.length;
		int m = inLen % 8;
		byte[] out = null;
		if (m == 0) {
			out = new byte[inLen];
		} else {
			out = new byte[inLen + 8 - m];
		}
		int outLen = out.length;
		for (int i = 0; i < outLen; i++) {
			if (i < inLen) {
				out[i] = in[i];
			} else {
				out[i] = 0x00;
			}
		}
		return out;
	}


	public static void main(String[] args) {
		String orign = "1991-07-09 12:00";
		System.out.println("初始:" + orign);
		System.out.println("初始长度:" + orign.length());
		System.out.println("加密后:" + CiperUtil.encrypt(SECURITY_KEY, orign));
		System.out.println("加密后长度:" + CiperUtil.encrypt(SECURITY_KEY, orign).length());
		System.out.println("解密后:" + CiperUtil.decrypt(SECURITY_KEY, CiperUtil.encrypt(SECURITY_KEY, orign)));

		System.out.println(CiperUtil.encrypt("aissss-wsssswwww-sss", orign));
		System.out.println(CiperUtil.decrypt("aissss-wsssswwww-sss", CiperUtil.encrypt("aissss-wsssswwww-sss", orign)));
	}
}
