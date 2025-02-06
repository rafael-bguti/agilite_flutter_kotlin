package info.agilite.boot.sse;

public class SseEmitter {
    private String uuid;
    private Long lastSend = 0L;

    public SseEmitter(String uuid) {
        this.uuid = uuid;
    }

    public void sendMessage(String message) {
        if(System.currentTimeMillis() - lastSend < 2000) {
            return;
        }

        SSeService.INSTANCE.sendMsg(uuid, message);
    }
}
