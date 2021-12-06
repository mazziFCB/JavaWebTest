package com.assignment.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

	
	 private static String  key = "MA19840402SA19870730MAIA";
	    Cipher ecipher;
	    Cipher dcipher;

	    public Encryptor(SecretKey key) {
	        try {
	            ecipher = Cipher.getInstance("DESede");
	            dcipher = Cipher.getInstance("DESede");
	            ecipher.init(Cipher.ENCRYPT_MODE, key);
	            dcipher.init(Cipher.DECRYPT_MODE, key);

	        } catch (Exception ex) {

	        }
	    }

	   
	    
	    public String encrypt(String str) {
	        try {
	            // Encode the string into bytes using utf-8
	            byte[] utf8 = str.getBytes("UTF8");

	            // Encrypt
	            byte[] enc = ecipher.doFinal(utf8);

	            // Encode bytes to base64 to get a string
	            return new sun.misc.BASE64Encoder().encode(enc);
	        } catch (Exception ex) {
	            
	        }
	        return null;
	    }

	    public String decrypt(String str) {
	        try {
	            // Decode base64 to get bytes
	            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

	            // Decrypt
	            byte[] utf8 = dcipher.doFinal(dec);

	            // Decode using utf-8
	            return new String(utf8, "UTF8");
	        } catch (Exception ex) {
	            
	        }
	        return null;
	    }

	    public static String doEncrypt(String text) {
	        
	        String key24 = String.format("%24s", key).replace(" ", "0").substring(0, 24);

	        SecretKey secretKey = new SecretKeySpec(key24.getBytes(), "DESede");

	        Encryptor encrypter = new Encryptor(secretKey);

	        return encrypter.encrypt(text);

	    }

	    public static String doDecrypt(String encryptedText) {

	        String key24 = String.format("%24s", key).replace(" ", "0").substring(0, 24);

	        SecretKey secretKey = new SecretKeySpec(key24.getBytes(), "DESede");

	        Encryptor decrypter = new Encryptor(secretKey);

	        return decrypter.decrypt(encryptedText);
	    }  
//	    public static void main(String[] args) {
//	        System.out.println(DESedeEncrypter.doDecrypt("oOWsK8QzZWs="));
//	    }
	    
	public static String getStringHashed(String text){
		try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        
	        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
	        final StringBuilder hexString = new StringBuilder();
	        for (int i = 0; i < hash.length; i++) {
	            final String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) 
	              hexString.append('0');
	            hexString.append(hex);
	        }
	        return hexString.toString();
			
		}catch(NoSuchAlgorithmException ex){
			ex.printStackTrace();
		}
		return "";
	}
}
