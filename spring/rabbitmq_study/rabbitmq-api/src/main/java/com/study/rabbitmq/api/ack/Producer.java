package com.study.rabbitmq.api.ack;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("111.230.115.242");
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost("/");

    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();

    String exchange = "test_ack_exchange";
    String routingKey = "ack.save";

    for (int i = 0; i < 5; i++) {
      Map<String, Object> headers = new HashMap<String, Object>();
      headers.put("num", i);

      AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
          .deliveryMode(2)
          .contentEncoding("UTF-8")
          .headers(headers)
          .build();
      String msg = "Hello RabbitMQ ACK Message " + i;
      channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
    }

  }
}
