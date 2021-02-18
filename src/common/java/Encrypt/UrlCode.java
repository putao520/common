package common.java.Encrypt;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlCode {
    /**字符串编码
     * @param _in
     * @return
     */
    public static String[] encode(String[] _in) {
        int len = _in.length;
        String[] rString = new String[len];
        for (int i = 0; i < len; i++) {
            rString[i] = URLEncoder.encode(_in[i], StandardCharsets.UTF_8);
        }
        return rString;
    }
    public static String encode(String _in) {
        String rString;
        rString = URLEncoder.encode(_in, StandardCharsets.UTF_8);
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
        for (int i = startIdx; i < _in.length; i++) {
            _in[i] = URLDecoder.decode(_in[i], StandardCharsets.UTF_8);
        }
        return _in;
    }
    public static String decode(String _in) {//字符串解码
        String rString;
        rString = URLDecoder.decode(_in, StandardCharsets.UTF_8);
        rString = rString.replaceAll("/\"", "\\\"");
        return rString;
    }
}
