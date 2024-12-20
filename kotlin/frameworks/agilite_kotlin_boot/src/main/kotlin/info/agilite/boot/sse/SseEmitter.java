package info.agilite.boot.sse;

public class SseEmitter {
    private String uuid;

    public SseEmitter(String uuid) {
        this.uuid = uuid;
    }

    public void sendMessage(String message) {
        SSeService.INSTANCE.sendMsg(uuid, message);
    }
}
