package common.java.Encrypt;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha1 {
    public static String build(String str) {
        return new String(buildBytes(str));
    }

    public static String build(byte[] str) {
        return new String(buildBytes(str));
    }

    public static byte[] buildBytes(String str) {
        return DigestUtils.sha1(str);
    }

    public static byte[] buildBytes(byte[] str) {
        return DigestUtils.sha1(str);
    }
}
