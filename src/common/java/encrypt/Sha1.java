package common.java.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha1 {
    public final static String build(String str){
        return new String(buildBytes(str));
    }
    public final static String build(byte[] str){
        return new String(buildBytes(str));
    }
    public final static byte[] buildBytes(String str){
        return DigestUtils.sha1(str);
    }
    public final static byte[] buildBytes(byte[] str){
        return DigestUtils.sha1(str);
    }
}
