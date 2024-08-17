package org.dream.dlx;

import com.rabbitmq.client.BuiltinExchangeType;
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
 * @desc
 */
public class Producer {

    private final static String EXCHANGE_NAME = "dlx_ordinary_exchange";
    private final static String QUEUE_NAME = "dlx_ordinary_queue";

    private final static String DLX_EXCHANGE_NAME = "dlx_exchange";
    private final static String DLX_QUEUE_NAME = "dlx_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取链接
        Connection connection = ConnectionUtil.getConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列，参数：队列名称，是否持久化，是否排他，是否自动删除，其它参数
        Map<String, Object> arg = new HashMap<>();
        arg.put("x-message-ttl", 10000);
        arg.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        // 这里是设置死信消息的路由键，相当于basicPublish发送消息时指定的路由键，这个需要和死信队列绑定的绑定键相关联
        arg.put("x-dead-letter-routing-key", "xxx");
        channel.queueDeclare(QUEUE_NAME, false, false, false, arg);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, QUEUE_NAME);

        channel.exchangeDeclare(DLX_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DLX_QUEUE_NAME,false, false, false, null);
        channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, "xxx");
        String message = "Hello World";
        // 发送消息 参数：交换机名称 这里使用的时默认交换机，队列名称或者是路由key，一些参数，消息体
        channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes());

        //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
        channel.close();
        connection.close();
    }
}
