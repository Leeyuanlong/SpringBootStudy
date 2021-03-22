package com.study.rabbitmq.api.dlx;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

  public static void main(String[] args) throws Exception {

    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("111.230.115.242");
    connectionFactory.setPort(5672);
    connectionFactory.setVirtualHost("/");

    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();

    // 这就是一个普通的交换机 和 队列 以及路由
    String exchangeName = "test_dlx_exchange";
    String routingKey = "dlx.#";
    String queueName = "test_dlx_queue";

    channel.exchangeDeclare(exchangeName, "topic", true, false, null);

    Map<String, Object> agruments = new HashMap<String, Object>();
    agruments.put("x-dead-letter-exchange", "dlx.exchange");
    //这个agruments属性，要设置到声明队列上
    channel.queueDeclare(queueName, true, false, false, agruments);
    channel.queueBind(queueName, exchangeName, routingKey);

    //死信队列与死信交换机绑定
    channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
    channel.queueDeclare("dlx.queue", true, false, false, null);
    //routeKey="#"，所有的消息都会被路由到这个队列
    channel.queueBind("dlx.queue", "dlx.exchange", "#");

    channel.basicConsume(queueName, true, new MyConsumer(channel));
  }
}
