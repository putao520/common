package common.java.Http;

import common.java.String.StringHelper;
import org.json.gsc.JSONObject;

public class HttpHelper {
    private final JSONObject data;
    public static HttpHelper build(JSONObject _data) {
        return new HttpHelper(_data);
    }

    public static HttpHelper build(String _dataString) {
        return new HttpHelper(JSONObject.toJSON(_dataString));
    }

    private HttpHelper(JSONObject _data) {
        this.data = _data;
    }
    public String toPost(){
        return StringHelper.build(out()).removeLeadingFrom().toString();
    }
    public String toGet(){
        String out = out();
        char[] tempChar = out.toCharArray();
        tempChar[0] = '?';
        return String.valueOf(tempChar) ;
    }
    private String out(){
        StringBuilder out = new StringBuilder();
        for(String key : data.keySet()){
            out.append("&").append(key).append("=").append(data.getString(key));
        }
        return out.toString();
    }
}
