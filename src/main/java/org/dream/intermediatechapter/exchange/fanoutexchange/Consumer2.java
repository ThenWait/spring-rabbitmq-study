package org.dream.intermediatechapter.exchange.fanoutexchange;

import com.rabbitmq.client.*;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/9 20:42
 * @desc
 */
public class Consumer2 {

    private final static String EXCHANGE_NAME = "fanout_exchange";

    private final static String QUEUE_NAME = "fanout_queue2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        // 1、队列名称 2、队列是否持久化 3、是否独占 4、是否自动删除 5、额外的属性
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列到交换机，同时指定需要订阅的routing key。可以指定多个 1、队列名称 2、交换机名称 3、routing key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "2");

        // 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("consumerTag:" + consumerTag);
                System.out.println("交换机名称:" + envelope.getExchange());
                System.out.println("消息在队列中的序列号:" + envelope.getDeliveryTag());
                System.out.println("消息的_routingKey:" + envelope.getRoutingKey());
                System.out.println("消息的contentType:" + properties.getContentType());
                System.out.println("消息的内容编码:" + properties.getContentEncoding());
                System.out.println("消息的头属性:" + properties.getHeaders());
                // body 即消息体
                String msg = new String(body);
                System.out.println(msg);
            }
        };
        // 监听队列，自动ACK
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
