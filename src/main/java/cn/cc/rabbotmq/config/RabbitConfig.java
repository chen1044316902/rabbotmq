package cn.cc.rabbotmq.config;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.FileNameMap;


@Configuration
public class RabbitConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Value("${spring.rabbitmq.host}")
    private String host;


    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    //  定义三个交换机
    public static final String EXCHANGE_A = "my_mq_exchange_A";

    public static final String EXCHANGE_B = "my_mq_exchange_B";

    public static final String EXCHANGE_C = "my_mq_exchange_C";

    public static final String QUEUE_A = "QUEUE_A";

    public static final String QUEUE_B = "QUEUE_B";

    public static final String QUEUE_C = "QUEUE_C";

    public static final String ROUTINGKEY_A = "springboot_routingKey_A";

    public static final String ROUTINGKEY_B = "springboot_routingKey_B";

    public static final String ROUTINGKEY_C = "springboot_routingKey_C";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    /**
     * 获取队列A
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true);
    }

    /**
     * 将队列A和ExchangeA 通过RoutingKeyA绑定到一起
     *
     * @return
     */
    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(queueA()).to(exchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(exchange()).with(ROUTINGKEY_B);
    }

//    /**
//     * 定义一个处理消息的bean 队列A
//     *
//     * @return
//     */
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer smc = new SimpleMessageListenerContainer(connectionFactory());
//        smc.setQueues(queueA());
//        smc.setExposeListenerChannel(true);
//        smc.setMaxConcurrentConsumers(10);
//        smc.setConcurrentConsumers(1);
//        smc.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        smc.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                channel.basicQos(1);
//                byte[] body = message.getBody();
//                log.info("接收处理的消息为 ： " + new String(body));
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//
//            }
//        });
//        return smc ;
//    }


    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_exchange");
    }

    @Bean
    public Binding bindingA(FanoutExchange fanoutExchange) {
        log.info("交换机name ： " + fanoutExchange.getName());
        return BindingBuilder.bind(queueA()).to(fanoutExchange);
    }

    @Bean
    public Binding bindingB(FanoutExchange fanoutExchange) {
        log.info("交换机那么：" + fanoutExchange.getName());
        return BindingBuilder.bind(queueB()).to(fanoutExchange);
    }

}