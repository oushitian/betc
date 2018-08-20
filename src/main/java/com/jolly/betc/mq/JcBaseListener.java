package com.jolly.betc.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * Created by lichong on 15/4/20.
 */
public abstract class JcBaseListener implements ChannelAwareMessageListener {
    public static String ERROR_QUEUE_SUFFIX = ".error" ;
    public static final String ERROR_MESSAGE_KEY="errorMsg";

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcBaseListener.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            processMessage(message,channel);
        }catch (Exception e){
            logger.error("onMessage error:",e);
            try {
                logger.error(e.getMessage(),e);
                addError(message, channel,e);
            }catch (Exception e2){
                logger.error("onMessage error:",e);
            }
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 发生异常时将消息存入错误消息队列
     * @param message
     * @param channel
     * @throws Exception
     */
    private void addError(Message message, Channel channel,Exception e) throws Exception{
        byte[] errorMsgBody = null;
        try{
            JSONObject jsonObj = JSON.parseObject(new String(message.getBody()));
            jsonObj.put(ERROR_MESSAGE_KEY,e.getMessage());
            errorMsgBody = jsonObj.toJSONString().getBytes();
        }catch(JSONException e1){
            logger.error("Original Message Body: "+new String(message.getBody()));
            logger.error("onMessage error:",e1);
            errorMsgBody = message.getBody();
        }
        String queueName = message.getMessageProperties().getConsumerQueue() ;
        String errorQueueName = queueName + ERROR_QUEUE_SUFFIX ;

        channel.queueDeclare(errorQueueName, true, false, false, null) ;
        channel.basicPublish("", errorQueueName,
                MessageProperties.PERSISTENT_TEXT_PLAIN, errorMsgBody);
    }

    protected abstract void processMessage(Message message,Channel channel) throws Exception ;

}
