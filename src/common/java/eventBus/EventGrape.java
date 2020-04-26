package common.java.eventBus;

public class EventGrape {
    private String eventName = null;
    private Object eventParam = null;

    public EventGrape(String eventName) {
        init(eventName, null);
    }

    public EventGrape(String eventName, Object eventParam) {
        init(eventName, eventParam);
    }

    private void init(String eventName, Object eventParam) {
        this.eventName = eventName;
        this.eventParam = eventParam;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getEventParam() {
        return eventParam;
    }

}
