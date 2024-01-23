package com.planb.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author zhaozheng
 * 加密与解密
 */
public class CipherTextUtil {

    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 加密算法/加密模式/填充
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 密钥
     */
    private static final String KEY = "lgusnjh273dgjfyi2781hfksuinbd672";

    /**
     * 数据块长度
     */
    private static final int KEY_LENGTH = 256;


    /**
     * 输出编码（Base64/Hex）
     */
    private static final Boolean IS_HEX = true;


    /**
     * 对字符串加密
     *
     * @param obj utf8编码的字符串
     */
    public static String desEncrypt(Object obj) {
        try {
            String str = obj.toString();
            if (str == null) {
                return null;
            }
            str = str.replaceAll("\\\\", "");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), getIv());
            byte[] bytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            if (IS_HEX) {
                return bytes2HexString(bytes).replaceAll("\r|\r|", "");
            } else {
                return jdkBase64String(bytes).replaceAll("\r|\r|", "");
            }
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("加密错误");
        }
    }

    /**
     * 使用DES对数据解密
     *
     * @param str utf8编码的二进制数据
     * @return 解密结果
     */
    public static String desDecrypt(String str) {
        try {
            byte[] bytes;
            if (IS_HEX) {
                bytes = hexString2Bytes(str);
            } else {
                bytes = jdkBase64Decoder(str);
            }
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(), getIv());
            bytes = cipher.doFinal(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("解码错误");
        }
    }

    /**
     * 使用base64解决乱码
     *
     * @param secretKey 加密后的字节码
     */
    public static String jdkBase64String(byte[] secretKey) {
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(secretKey);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(secretKey);
    }


    /**
     * 使用Hex解决乱码
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        for (byte value : b) {
            result.append(String.format("%02X", value));
        }
        return result.toString();
    }

    /**
     * 使用jdk的base64 解密字符串 返回为null表示解密失败
     *
     * @throws IOException e
     */
    public static byte[] jdkBase64Decoder(String str) throws IOException {
//        BASE64Decoder decoder = new BASE64Decoder();
//        return decoder.decodeBuffer(str);
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(str);
    }


    /**
     * 使用jdk的hex 解密字符串 返回为null表示解密失败
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }


    //获得数据块
    private static SecretKeySpec getKey() {
        byte[] keyBytes = new byte[KEY_LENGTH / 8];
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = KEY.getBytes(StandardCharsets.UTF_8);
        int length = Math.min(passwordBytes.length, keyBytes.length);
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    //获得偏移量
    private static IvParameterSpec getIv() {
        String key = KEY.substring(0, 32);
        byte[] iv = new byte[16];
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(bytes, 0, iv, 0, 16);
        return new IvParameterSpec(iv);
    }

    /**
     * 这里是通过反射移除了isRestricted 的变量修饰符：final,然后将isRestricted 赋值为false即可
     */
    static {
        try {
            Class<?> clazz = Class.forName("javax.crypto.JceSecurity");
            Field nameField = clazz.getDeclaredField("isRestricted");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);
            nameField.setAccessible(true);
            nameField.set(null, Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
     */
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }


    //测试
    public static void main(String[] args) throws Exception {
//        String openId = "加密结果:" + desEncrypt("{" +
//                "   \"at\": \"吴系挂\"," +
//                "   \"cid\": \"1\"," +
//                "   \"groupid\": 2 ," +
//                "   \"username\": \"12154545\"," +
//                "}");
//        System.out.println(openId);
//        String s = openId.replace("加密结果:", "");

        String s = "05291E41756B8F674F1C1D6AE3BD297F4ED4D7248F36DD86023E4115749589FFCAD3448A7F30A5235BF2F20E2B4ECB81C7B28DAE1134F4BD07B25B4D9CA27A2D0E1F1A4B0CE0B05B55FB59FEE0B08F2EB526094EEB04768B4F571A6D1A58490EB513CD6822E4076952752BF17D4FDB299D592F259A3B331D87B30C0F12AF52E4AAF8C1A8BD826A04718182545F456906";
        String desDecrypt = desDecrypt(s);
        System.out.println("解密结果:" + desDecrypt);
//        String s1 = "1504CDC3FFD012D236AF27628691DD9F20FAE428F2F9BEF1274FA5CEA331FA353DA153F768186EE4A287ADDADD1864CE";
//        String test = desDecrypt(s1);
//        System.out.println("解密结果:" + test);
    }
}
