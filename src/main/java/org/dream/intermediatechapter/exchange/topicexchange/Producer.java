package org.dream.intermediatechapter.exchange.topicexchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/9 20:24
 * @desc
 */
public class Producer {

    private final static String EXCHANGE_NAME = "topic_exchange";

    private final static String QUEUE_NAME = "topic_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, false, true, false, null);
        String message = "hello world!";
        channel.basicPublish(EXCHANGE_NAME, "test.queue.info", null, message.getBytes());
        channel.close();
        connection.close();
    }
}
