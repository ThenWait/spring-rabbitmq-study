package org.dream.intermediatechapter.exchange.fanoutexchange;

import cn.hutool.http.HttpRequest;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.dream.ConnectionUtil;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author zhanghang
 * @date 2021/12/9 20:41
 * @desc
 */
public class Producer {

    private final static String EXCHANGE_NAME = "fanout_exchange";

    private final static String QUEUE_NAME = "fanout_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

//        String enc = new String(new BASE64Encoder().encode(("guest:guest").getBytes()));

//        Map<String,String> map1 = new HashMap<>();
//        map1.put("Authorization", "Basic " + enc);
//        map1.put("Content-Type","application/json");
//        HttpRequest.put("http://127.0.0.1:15672/api/users/newuser")
//                .addHeaders(map1)
//                .body("{\"username\": \"newuser\",\"password\": \"password\",\"tags\": \"administrator\"}")
//                .execute();

//        String enc = new String(new BASE64Encoder().encode(("guest:guest").getBytes()));
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("Authorization", "Basic " + enc);
//        map1.put("Content-Type","application/json");
//        HttpRequest.put("http://127.0.0.1:15672/api/permissions/zhang_hang/newuser")
//                .addHeaders(map1)
//                .body("{\"configure\":\".*\",\"write\":\".*\",\"read\":\".*\"}")
//                .execute();

//        URL url = new URL("http://127.0.0.1:15672/api/users/newuser");
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestMethod("PUT");
//        httpURLConnection.setRequestProperty("Content-Type","application/json");
//        String requestBody = "{\"username\": \"newuser\",\"password\": \"password\",\"tags\": \"administrator\"}";
//        httpURLConnection.setDoOutput(true);
//        try(OutputStream os = httpURLConnection.getOutputStream()){
//            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//        }


        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String message = "hello world!";

        // 1、交换机名称 2、routing key(direct类型交换机一般队列名称, topic类型交换机是*.*.*这种,
        // fanout类型交换机随便是啥，就算也是*.*.*都行) 3、消息属性 4、消息
        channel.basicPublish(EXCHANGE_NAME, "*.*.*", null, message.getBytes());

        channel.close();
        connection.close();
    }
}
