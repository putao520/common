package common.java.Encrypt;

import org.json.gsc.JSONArray;
import org.json.gsc.JSONObject;

public class GscJson {

    /**
     * 对html编码解密
     *
     * @param html
     * @return
     */
    public static String decodeHtmlTag(String html) {
        return html.replaceAll("@t", "/").replaceAll("@w", "+");
    }

    public static String encodeHtmlTag(String html) {
        return html.replaceAll("/", "@t").replaceAll("\\+", "@w");
    }

    /**
     * 转码json->加密
     *
     * @param json
     * @return
     */
    public static String encodeJson(JSONObject json) {
        return "gsc-json&" + encodeString(json.toString());
    }

    public static String encodeJsonArray(JSONArray json) {
        return "gsc-jsonArray&" + encodeString(json.toString());
    }

    public static String encodeString(String str) {
        // add header info
        return encodeHtmlTag(Base64.encode(str));
    }

    /**
     * 转码json->解密
     *
     * @param jsonString
     * @return
     */
    public static JSONObject decodeJson(String jsonString) {
        var header = getHeader(jsonString);
        if (!getType(header).equals("json")) {
            return null;
        }
        return JSONObject.toJSON(decodeString(jsonString.substring(header.length() + 1)));
    }

    public static JSONArray decodeJsonArray(String jsonArrayString) {
        var header = getHeader(jsonArrayString);
        if (!getType(header).equals("jsonArray")) {
            return null;
        }
        return JSONArray.toJSONArray(decodeString(jsonArrayString.substring(header.length() + 1)));
    }

    public static String decodeString(String str) {
        return Base64.decode(decodeHtmlTag(str));
    }

    public static String getHeader(String str) {
        var header = str.split("&")[0];
        return header.startsWith("gsc-") ? header : null;
    }

    public static String getType(String header) {
        return header.split("-")[1];
    }
}
