package org.dream.msgttl.everymsgsetttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/11/28 12:29
 * @desc 给消息单独设置ttl，但是给消息单独设置过期时间，过期了也不会马上删除，因为每条消息是否过期是在投递给消费者
 * 时才知道，但是如果过期的消息刚好是在头部，那么是可以马上删除的
 */
public class Producer {

    private final static String QUEUE_NAME = "ttl_msg_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取链接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明队列，参数：队列名称，是否持久化，是否排他，是否自动删除，其它参数
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String message = "Hello World";
        // 发送消息 参数：交换机名称 这里使用的时默认交换机，队列名称或者是路由key，一些参数，消息体、
        AMQP.BasicProperties properties = new AMQP.BasicProperties();
        AMQP.BasicProperties.Builder builder = properties.builder();
        // 设置为2表示持久化，1表示非持久化
        builder.deliveryMode(2);
        builder.expiration("20000");
        // 这种写法最后还要builder.build，不小心就漏掉了
        AMQP.BasicProperties build = builder.build();
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        channel.basicPublish("", QUEUE_NAME, build, message.getBytes());

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
        channel.close();
        connection.close();
    }
}
