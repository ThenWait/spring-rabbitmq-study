package org.dream.alternateExchange;

import com.rabbitmq.client.*;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/8 20:40
 * @desc
 */
public class Consumer {

    private static final String BACK_EXCHANGE_NAME = "backup-exchange";
    private static final String BINDING_KEY = "bingkey_demo";
    private static final String BACK_QUEUE_NAME = "unRoutingQueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        // 声明一个 fanout 类型的交换器，建议此处使用 fanout 类型的交换器
        channel.exchangeDeclare(BACK_EXCHANGE_NAME, "direct", true, false, null);
        // 消息没有被路由的之后存入的队列
        channel.queueDeclare(BACK_QUEUE_NAME, true, false, false, null);
        channel.queueBind(BACK_QUEUE_NAME, BACK_EXCHANGE_NAME, "not-exists-routing-key");

        // 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) {
                // body 即消息体
                String msg = new String(body);
                System.out.println(msg);
            }
        };
        // 监听队列，自动ACK
        channel.basicConsume(BACK_QUEUE_NAME, true, consumer);
    }
}
