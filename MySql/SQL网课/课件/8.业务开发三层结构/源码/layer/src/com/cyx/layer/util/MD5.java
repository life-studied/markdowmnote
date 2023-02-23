package com.cyx.layer.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class MD5 {
    
    private static final char[] chars = {
            'a','b','c','d','e','f','g','h','i','j','k','l',
            'm','n','o','p','q','r','s','t','u','v','w','x',
            'y','z','0','1','2','3','4','5','6','7','8','9'
    };

    public static String randomStr(int length){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++){
            int index = r.nextInt(chars.length);
            sb.append(chars[index]);
        }
        return sb.toString();
    }

    public static String encrypt(String password, String secret){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int len1 = password.length();
            int len2 = secret.length();
            String str = password.substring(0, len1) + secret.substring(0, len2) + password.substring(len1) + secret.substring(len2);
            byte[] data = str.getBytes();
            byte[] result = md5.digest(data);
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
