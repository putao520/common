package common.java.encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UrlCode {
    /**字符串编码
     * @param _in
     * @return
     */
    public static String[] encode(String[] _in) {
        int len = _in.length;
        String[] rString = new String[len];
        try{
            for(int i =0; i < len; i++){
                rString[i] = URLEncoder.encode(_in[i],"UTF-8");
            }
        }
        catch (UnsupportedEncodingException e){}
        return rString;
    }
    public static String encode(String _in) {
        String rString;
        try {
            rString = URLEncoder.encode(_in,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            rString = _in;
        }
        return rString;
    }
    /**字符串解码
     * @param _in
     * @return
     */
    public static String[] decode(String[] _in) {//字符串解码
        return decode(_in, 0);
    }

    public static String[] decode(String[] _in, int startIdx) {//字符串解码
        try {
            for (int i = startIdx; i < _in.length; i++) {
                _in[i] = URLDecoder.decode(_in[i], "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
        }
        return _in;
    }
    public static String decode(String _in) {//字符串解码
        String rString;
        try {
            rString = URLDecoder.decode(_in,"UTF-8");
            rString = rString.replaceAll("/\"", "\\\"");
        } catch (UnsupportedEncodingException e) {
            rString = _in;
        }
        return rString;
    }
}
