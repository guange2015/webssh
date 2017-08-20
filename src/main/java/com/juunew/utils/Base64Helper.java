package com.juunew.utils;


import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by guange on 23/02/2017.
 */
public class Base64Helper {

    /**
     * base64编码
     *
     * @param code
     * @return
     */
    public static String encode(String code) {
        byte[] encode = Base64.encodeBase64URLSafe(code.getBytes(StandardCharsets.UTF_8));
        return new String(encode, StandardCharsets.UTF_8);
    }

    public static byte[] encodeBytes(byte[] codes) {
        return Base64.encodeBase64(codes);
    }


    /**
     * base64解码
     *
     * @param code
     * @return
     */
    public static String decode(String code) {
        byte[] decode = Base64.decodeBase64(code);
        return new String(decode, StandardCharsets.UTF_8);
    }

    /**
     * base64再解码，把原本的非URL safe编码转换为URL safe编码
     *
     * @param code
     * @return
     */
    public static String reencode(String code) {
        String str = decode(code);
        str = str.replace("\r\n", "\n").trim();
        str = str.replace("\n", "\r\n");
        return encode(str);
    }

}
