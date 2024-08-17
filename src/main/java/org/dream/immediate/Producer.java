package org.dream.immediate;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/11/28 12:29
 * @desc
 */
public class Producer {

    private final static String QUEUE_NAME = "immediate_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取链接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 声明队列，参数：队列名称，是否持久化，是否排他，是否自动删除，其它参数
        // 和mandatory相比，这里就不注释了，只要不启动消费就行了
        // 但是由于rabbitmq3.0去掉了对immediate参数的支持，所以这个启动会报错
        // connection is already closed due to connection error; protocol method:
        // #method<connection.close>(reply-code=540, reply-text=NOT_IMPLEMENTED - immediate=true, class-id=60, method-id=40)
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World";
        // 发送消息 参数：交换机名称 这里使用的时默认交换机，队列名称或者是路由key，一些参数，消息体
        channel.basicPublish("", QUEUE_NAME, true,true, null, message.getBytes());
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                String message = new String(bytes);
                System.out.println("返回的结果是:" + message);
            }
        });

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
        channel.close();
        connection.close();
    }
}
