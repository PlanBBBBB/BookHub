package com.planb.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptUtil {
    public static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

    public static String encrypt(String message) {
        return textEncryptor.encrypt(message);
    }

    public static String decrypt(String message) {
        return textEncryptor.decrypt(message);
    }

    public static void main(String[] args) {
        //加密所需的salt(盐)
        textEncryptor.setPassword("planb123");
        //要加密的数据（数据库的用户名或密码）
        String password = encrypt("root");
        System.out.println("加密结果:" + password);
        password = decrypt(password);
        System.out.println("解密结果：" + password);

    }
}
