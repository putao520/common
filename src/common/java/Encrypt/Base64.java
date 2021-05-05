package common.java.Encrypt;

public class Base64 {
    public static String encode(String str) {
        // return java.util.Base64.getEncoder().encodeToString(str.getBytes());
        return org.apache.commons.codec.binary.Base64.encodeBase64String(str.getBytes());
    }

    public static String decode(String str) {
        // return new String( java.util.Base64.getDecoder().decode(str) );
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes()));
    }

    public static byte[] decodeHex(String str) {
        // return java.util.Base64.getDecoder().decode(str);
        return org.apache.commons.codec.binary.Base64.decodeBase64(str);
    }

    public static String encode(byte[] str) {
        // return java.util.Base64.getEncoder().encodeToString(str);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(str);
    }

    public static String decode(byte[] str) {
        // return new String( java.util.Base64.getDecoder().decode(str) );
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(str));
    }

}
