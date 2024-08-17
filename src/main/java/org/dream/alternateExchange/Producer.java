package org.dream.alternateExchange;

import com.rabbitmq.client.*;
import org.dream.ConnectionUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/12 10:37
 * @desc 备份交换机
 */
public class Producer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String BINDING_KEY = "bingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";

    private static final String ALTERNATE_EXCHANGE_NAME = "alternate_exchange_demo";
    private static final String ALTERNATE_QUEUE_NAME = "alternate_queue_demo";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange", ALTERNATE_EXCHANGE_NAME);


        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, arguments);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.exchangeDeclare(ALTERNATE_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true, false, null);
        channel.queueDeclare(ALTERNATE_QUEUE_NAME, true, false, false, null);
        // 下面这句话如果不写，那么备份交换机也无法路由到队列中。
        channel.queueBind(ALTERNATE_QUEUE_NAME, ALTERNATE_EXCHANGE_NAME, "");

        // 发送一条持久化消息
        String message = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 没有被正确的路由到消息队列，此时此消息会进入 unRoutingQueue";
        for (int i = 0; i < 50000; i++) {
            // 使用 routingKey，第三个参数时mandatory，没有路由到队列则返回到生产者，但是这里配置了备份交换机，则进入备份交换机
            channel.basicPublish(EXCHANGE_NAME, "not-exists-routing-key", true, MessageProperties.PERSISTENT_TEXT_PLAIN, (message + i).getBytes(StandardCharsets.UTF_8));
        }
        channel.close();
        connection.close();
    }
}
