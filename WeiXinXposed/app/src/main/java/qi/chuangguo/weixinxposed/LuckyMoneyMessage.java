package qi.chuangguo.weixinxposed;

/**
 * Created by chuangguo.qi on 2018/4/4.
 */

public class LuckyMoneyMessage {

    private int channelId;
    private int msgType;
    private String nativeUrlString;
    private String sendId;
    private String talker;

    public LuckyMoneyMessage(int msgType, int channelId, String sendId, String nativeUrlString, String talker) {
        this.msgType = msgType;
        this.channelId = channelId;
        this.sendId = sendId;
        this.nativeUrlString = nativeUrlString;
        this.talker = talker;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getNativeUrlString() {
        return nativeUrlString;
    }

    public void setNativeUrlString(String nativeUrlString) {
        this.nativeUrlString = nativeUrlString;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }
}
