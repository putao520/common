package common.java.nLogger;

import common.java.Time.TimeHelper;
import org.json.gsc.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogInfo {
    private final Exception e;
    private InfoType type;

    public enum InfoType {
        LOG("Log"), WARN("Warn"), ERROR("Error"), DEBUG("Debug");
        private final String text;

        InfoType(String in) {
            text = in;
        }

        public String toString() {
            return text;
        }
    }
    private String info;
    private LogInfo(Exception e,InfoType type){
        this.e = e;
        this.type = type;
        if( this.e != null ){
            this.info = this.e.getMessage();
        }
    }
    public static final LogInfo build(){
        return new LogInfo(null, InfoType.LOG);
    }
    public static final LogInfo build(Exception e){
        return new LogInfo(e, InfoType.LOG);
    }
    public static final LogInfo build(Exception e,InfoType type){
        return new LogInfo(e, type);
    }
    public LogInfo level(InfoType type){
        this.type = type;
        return this;
    }
    public LogInfo info(String in){
        this.info = in;
        return this;
    }
    private String stack(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    public String toString(){
        String rs = "";
        rs += "[" + TimeHelper.build().nowDatetime() + "]";
        rs += "[" + this.type.toString() + "]";
        rs += "[" + Thread.currentThread().getId() + "]";
        rs += "[" + this.info + "]";
        if( e != null ){
            rs += "================================\n";
            rs += stack(e);
        }
        return rs;
    }
    public JSONObject toJson(){
        JSONObject rs = JSONObject.putx("time", TimeHelper.build().nowDatetime())
                .puts("LEVEL", type.toString())
                .puts("ThreadID", Thread.currentThread().getId())
                .puts("message", this.info);
        if( e != null ){
            rs.put("stack", stack(e));
        }
        return rs;
    }
}
