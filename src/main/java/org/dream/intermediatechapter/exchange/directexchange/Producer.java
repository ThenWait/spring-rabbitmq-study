package org.dream.intermediatechapter.exchange.directexchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/8 20:32
 * @desc 在direct交换机下，routing key 和binding key需要完全匹配才能使用，理解：生产者一般不需要进行交换机和队列的绑定，
 * 生产者只需要在发送消息时指定routekey
 * 消费者来进行绑定，通过和routekey相同的绑定key将交换机和队列绑定
 * 这种发送消息时指定了队列的情况，可以先启动生产者
 */
public class Producer {

    private final static String EXCHANGE_NAME = "direct_exchange";

    private final static String QUEUE_NAME = "direct_queue";

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
