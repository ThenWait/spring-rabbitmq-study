package org.dream;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq Hello world!
 * @author zhanghang
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.22");
        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("zhang_hang");
//        connectionFactory.setUsername("zhanghang");
//        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setRequestedHeartbeat(5);
        // 通过工厂获取连接
        Connection connection = connectionFactory.newConnection();
        return connection;
    }
}
