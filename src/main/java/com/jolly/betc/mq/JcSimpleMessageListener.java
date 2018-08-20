package com.jolly.betc.mq;

import com.alibaba.fastjson.JSON;
import com.jolly.betc.common.utils.ReflectionUtils;
import com.jolly.betc.mq.message.JcMqRequestMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * 简化版本的消息接收器
 * Created by Zuoqiuming on 2017年6月21日17:55:18.
 */
public abstract class JcSimpleMessageListener<T extends JcMqRequestMessage> extends JcBaseListener {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcSimpleMessageListener.class);

    @Override
    protected void processMessage(Message message, Channel channel) throws Exception {
        String messageJson = new String(message.getBody(), "UTF-8");
        Class<T> targetMessageBeanClass = ReflectionUtils.getFirstGenericType(this.getClass());
        T messageBean = JSON.parseObject(messageJson, targetMessageBeanClass);
        messageBean.setMessageJson(messageJson);
        logger.info("{}.processMessage={}", this.getClass().getSimpleName(), messageJson);
        onMessageReceived(messageBean);
        logger.info("{} processMessage end", this.getClass().getSimpleName());
    }

    /**
     * JSON文本类消息接收事件,处理接收到的数据
     *
     * @param messageBean:messageJson=>对应的消息对象
     * @throws Exception
     */
    public abstract void onMessageReceived(T messageBean) throws Exception;

}
