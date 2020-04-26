package common.java.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;

public class Md5 {
    public final static String build(String str) {
        return DigestUtils.md5Hex(str);
    }
    public final static String build(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public final static String build(InputStream data){
        try{
            return DigestUtils.md5Hex(data);
        }
        catch (Exception e){
            return null;
        }
    }

}
