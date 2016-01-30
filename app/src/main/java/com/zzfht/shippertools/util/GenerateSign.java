package com.zzfht.shippertools.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by think on 2016-01-14.
 */
public class GenerateSign {

    public static String generateSign(String from){
        String s1 = from.toLowerCase();
        String sign = getMd5(s1);
        Log.e("from:",s1);

        return sign;
    }

    public static String getMd5(String str) {
        MessageDigest md5=null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(40);
            for(byte x:bs) {
                if((x & 0xff)>>4 == 0) {
                    sb.append("0").append(Integer.toHexString(x & 0xff));
                } else {
                    sb.append(Integer.toHexString(x & 0xff));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
