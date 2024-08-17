package org.dream.intermediatechapter.exchange.directexchange.samequeuenotsameroutekey;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/8 20:32
 * @desc 演示多个消费者绑定到同一个队列上，绑定键不一样。这种其实是队列和交换机存在多个绑定关系，
 * 多个绑定键，满足条件的消息可以被路由到同一个消费者
 */
public class Producer {

    private final static String EXCHANGE_NAME = "same_direct_exchange";

    private final static String QUEUE_NAME = "same_direct_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, QUEUE_NAME);

        String message1 = "Consumer1 hello world!";

        /**
         * 这里我们发送两条消息，设置不同的路由key
         */
        // 1、交换机名称 2、routing key(direct类型交换机一般队列名称) 3、消息属性 4、消息
        channel.basicPublish(EXCHANGE_NAME, "Consumer1", null, message1.getBytes());

        String message2 = "Consumer2 hello world!";

        // 1、交换机名称 2、routing key(direct类型交换机一般队列名称) 3、消息属性 4、消息
        channel.basicPublish(EXCHANGE_NAME, "Consumer2", null, message2.getBytes());

        channel.close();
        connection.close();
    }
}
