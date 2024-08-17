package org.dream.msgconfirm.singlemsgconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/11/28 12:29
 * @desc
 */
public class Producer {

    private final static String QUEUE_NAME = "ordinary_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取链接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 信道设置为确认模式
        channel.confirmSelect();
        // 声明队列，参数：队列名称，是否持久化，是否排他，是否自动删除，其它参数
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String message = "Hello World";
        // 发送消息 参数：交换机名称 这里使用的时默认交换机，队列名称或者是路由key，一些参数，消息体
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        try {
            // 等待消息确认，返回true确认成功
            boolean b = channel.waitForConfirms();
            if (!b){
                System.out.println("消息发送失败！");
            } else {
                System.out.println("消息发送成功！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
        channel.close();
        connection.close();
    }
}
