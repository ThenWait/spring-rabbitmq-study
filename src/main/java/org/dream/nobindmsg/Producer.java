package org.dream.nobindmsg;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/8 20:32
 * @desc 这个例子是为了理解绑定。演示生产者发送消息，但是不进行绑定，这样的话，交换机会显示收到消息，
 * 但是随后消息会丢失，因为没有路由到任何队列，之前说（生产者可以不进行队列的绑定，但是消费者要先启动才行）
 * 是因为消费者端进行绑定了，如果先启动生产者，那么在消费者启动之前，消息是会丢失的。所以如果生产者要不进行绑定
 * 就必须保证其他地方交换机和队列绑定上了，所以书上写生产者和消费者都应该尝试进行绑定。
 */
public class Producer {

    private final static String EXCHANGE_NAME = "no_bind_direct_exchange";

    private final static String QUEUE_NAME = "no_bind_direct_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, QUEUE_NAME);

        String message1 = "Consumer1 hello world!";


        // 1、交换机名称 2、routing key(direct类型交换机一般队列名称) 3、消息属性 4、消息
        channel.basicPublish(EXCHANGE_NAME, "Consumer1", null, message1.getBytes());

        channel.close();
        connection.close();
    }
}
