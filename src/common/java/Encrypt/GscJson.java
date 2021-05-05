package common.java.Encrypt;

import org.json.gsc.JSONObject;

public class GscJson {

    /**
     * 对html编码解密
     *
     * @param html
     * @return
     */
    public static String decodeHtmlTag(String html) {
        // return html.replaceAll("@t", "/").replaceAll("@w", "+").replaceAll("@m", "=").replaceAll("@q", "&");
        return html.replaceAll("@t", "/").replaceAll("@w", "+");
    }

    public static String encodeHtmlTag(String html) {
        // return html.replaceAll("/", "@t").replaceAll("\\+", "@w").replaceAll("=", "@m").replaceAll("&", "@q");
        return html.replaceAll("/", "@t").replaceAll("\\+", "@w");
    }

    /**
     * 转码json->加密
     *
     * @param json
     * @return
     */
    public static String encode(JSONObject json) {
        return encodeString(json.toString());
    }

    public static String encodeString(String str) {
        return encodeHtmlTag( Base64.encode(str));
    }

    /**
     * 转码json->解密
     *
     * @param jsonString
     * @return
     */
    public static JSONObject decode(String jsonString) {
        return JSONObject.toJSON(decodeString(jsonString));
    }

    public static String decodeString(String str) {
        return Base64.decode( decodeHtmlTag(str));
    }
}
