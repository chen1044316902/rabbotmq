
package cn.cc.rabbotmq.producer;

import cn.cc.rabbotmq.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //由于这个bean的scope属性为ConfigurableBeanFactory.SCOPE_PROPOTYPE 所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    /**
     * 通过构造方法注入rabbitTemplate
     *
     * @param rabbitTemplate
     */
    public MsgProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMessage(String content) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, content, correlationData);
        System.out.println("===========消息已经发送==========");
    }

    /**
     * 回调处理
     *
     * @param correlationData
     * @param b
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        logger.info("回调ID：" + correlationData.getId());
        if (b) {
            logger.info("消息成功消费");
        }else{
            logger.info("");
        }
    }
}