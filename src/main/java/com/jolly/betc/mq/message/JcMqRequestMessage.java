package com.jolly.betc.mq.message;

/**
 * @author zuoqiuming
 * @create 2018/01/23 上午 11:03
 */
public class JcMqRequestMessage {

    /**
     * 非参数,为原始json文本
     */
    private String messageJson;

    private JcBaseMessageSource source;

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String messageJson) {
        this.messageJson = messageJson;
    }

    public JcBaseMessageSource getSource() {
        return source;
    }

    public void setSource(JcBaseMessageSource source) {
        this.source = source;
    }
}
