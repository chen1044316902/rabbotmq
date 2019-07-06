
package cn.cc.rabbotmq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MsgProducerB{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //由于这个bean的scope属性为ConfigurableBeanFactory.SCOPE_PROPOTYPE 所以不能自动注入
    private RabbitTemplate rabbitTemplate;

    /**
     * 通过构造方法注入rabbitTemplate
     *
     * @param rabbitTemplate
     */
    public MsgProducerB(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String content) {

        rabbitTemplate.convertAndSend("fanout_exchange","",content);

        System.out.println("===========消息已经发送==========");

    }
}