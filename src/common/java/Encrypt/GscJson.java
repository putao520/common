package common.java.Encrypt;

import org.json.simple.JSONObject;

public class GscJson {

    /**对html编码解密
     * @param html
     * @return
     */
    public final static String decodeHtmlTag(String html){
        // return html.replaceAll("@t", "/").replaceAll("@w", "+").replaceAll("@m", "=").replaceAll("@q", "&");
        return html.replaceAll("@t", "/").replaceAll("@w", "+");
    }
    public final static String encodeHtmlTag(String html){
        // return html.replaceAll("/", "@t").replaceAll("\\+", "@w").replaceAll("=", "@m").replaceAll("&", "@q");
        return html.replaceAll("/", "@t").replaceAll("\\+", "@w");
    }

    /**转码json->加密
     * @param json
     * @return
     */
    public final static String encode(JSONObject json) {
        return encodeString(json.toJSONString());
    }

    public final static String encodeString(String str) {
        return encodeHtmlTag( Base64.encode(str) );
    }

    /**转码json->解密
     * @param jsonString
     * @return
     */
    public final static JSONObject decode(String jsonString) {
        return JSONObject.toJSON(decodeString(jsonString));
    }
    public final static String decodeString(String str) {
        return Base64.decode( decodeHtmlTag(str));
    }
}
