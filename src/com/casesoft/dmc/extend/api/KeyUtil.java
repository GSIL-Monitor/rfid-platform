package com.casesoft.dmc.extend.api;

import java.security.MessageDigest;

/**
 * Created by john on 2016/12/30.
 */
public class KeyUtil {

    /**
     * 生成key
     * @param value 
     * @return
     * */
    public static String buildKey(String value) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = value.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String args[]) {
        String s = new String("testkey");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + buildKey(s));
     }
}