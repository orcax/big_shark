/**
 * 404 Studio
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.prj.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 相关工具类
 * @author yiliang.gyl
 * @version $Id: MD5Tool.java, v 0.1 Jun 21, 2015 10:59:31 AM yiliang.gyl Exp $
 */
public class MD5Tool {

    private static String MD5_RANDOM = "py458as586_v2";

    public static String getMd5(String plainText) {
        plainText = plainText.trim();
        plainText = plainText + MD5_RANDOM;
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

}
