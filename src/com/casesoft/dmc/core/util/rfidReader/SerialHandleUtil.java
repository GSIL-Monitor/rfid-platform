package com.casesoft.dmc.core.util.rfidReader;

import java.util.List;

/**
 * Created by yushen on 2018/5/30.
 */

public class SerialHandleUtil {

    //校验和
    public static byte sumCheck(byte[] msgArray) {
        byte mSum = 0;

        for (byte msg : msgArray) {
            mSum = (byte) (mSum + msg);
        }
        mSum = (byte) ((~mSum) + 1);
        return mSum;
    }

    //校验和
    public static byte sumCheck(List<Byte> msgArray) {
        byte mSum = 0;

        for (byte msg : msgArray) {
            mSum = (byte) (mSum + msg);
        }
        mSum = (byte) ((~mSum) + 1);
        return mSum;
    }

    //byte转16进制字符串
    public static String epcToHexString(List<Byte> bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            if (b < 0) {
                int i = b + 256;
                String str = Integer.toHexString(i);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(Integer.toHexString(i));
                }
            } else {
                String str = Integer.toHexString(b);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(Integer.toHexString(b));
                }
            }
        }
        return sb.toString();
    }

    // byte转16进制字符串
    public static String epcToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : bytes) {
            if (b < 0) {
                int i = b + 256;
                String str = Integer.toHexString(i);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(Integer.toHexString(i));
                }
            } else {
                String str = Integer.toHexString(b);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(Integer.toHexString(b));
                }
            }
        }
        return sb.toString();
    }

    public static Integer calculateBytes(byte... bytes) {
        String hexString = epcToHexString(bytes);
        return Integer.parseInt(hexString, 16);
    }
}
