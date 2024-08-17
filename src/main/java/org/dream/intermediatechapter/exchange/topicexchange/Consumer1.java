package org.dream.intermediatechapter.exchange.topicexchange;

import com.rabbitmq.client.*;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/9 20:32
 * @desc
 */
public class Consumer1 {

    private final static String EXCHANGE_NAME = "topic_exchange";

    private final static String QUEUE_NAME = "topic_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        // 1、队列名称 2、队列是否持久化 3、是否独占 4、是否自动删除 5、额外的属性
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机，同时指定需要订阅的routing key。可以指定多个 1、队列名称 2、交换机名称 3、routing key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.queue.info");

        // 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                // body 即消息体
                String msg = new String(body);
                System.out.println(msg);
            }
        };
        // 监听队列，自动ACK
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
