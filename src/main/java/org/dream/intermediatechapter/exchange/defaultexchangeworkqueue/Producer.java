package org.dream.intermediatechapter.exchange.defaultexchangeworkqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2024/3/30 19:39
 * @desc
 */
public class Producer {

    private final static String QUEUE_NAME = "work_queue";

    /**
     * work消息模型，竞争消费
     * 对于竞争消费模型，在消费者不设置channel.basicQos(1);且自动ack的情况下，消费者必须先启动，因为多个消费者
     * 消费一个队列，策略是轮询，先后启动两个消费者，队列无法动态重新计算，只有一开始平分。
     * 当设置了channel.basicQos(1);且手动ack的情况下，队列在接收到ack才会给消费者发送下一条消息，这样就可以根据消费者的处理能力来分配，
     * 可以做到动态的，消费者不用一开始就必须都启动
     */

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取链接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, true, null);
        for (int i = 0; i < 100; i++) {
            String message = "Hello World " + i;
            // 发送消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
        channel.close();
        connection.close();
    }
}
