package com.skybet.test.processor.mqconsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.skybet.test.beans.Message;
import com.skybet.test.processor.Config;
import com.skybet.test.services.impl.IntermediateSaveMessageServiceImpl;

public class ConsumerProcessor {
	
	private static Logger log = Logger.getLogger(ConsumerProcessor.class.getCanonicalName());
	private Connection connection;
	private Channel channel;

	private Connection getRabbitMQConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(Config.RABBITMQ_DEFAULT_USER);
        factory.setPassword(Config.RABBITMQ_DEFAULT_PASS);
        factory.setVirtualHost(Config.RABBITMQ_DEFAULT_VHOST);
        factory.setHost(Config.RABBITMQ_HOST);
        factory.setPort(Config.RABBITMQ_PORT);
		return factory.newConnection();
	}

	public void execute() {
		try {
			if(connection == null)
				connection = getRabbitMQConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(Config.EXCHANGEMQ_MESSAGES_NAME, "direct");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, Config.EXCHANGEMQ_MESSAGES_NAME, "");
			ConsumerHandler consumer = new ConsumerHandler(channel);
		    GsonBuilder gsonBuilder = new GsonBuilder();
		    gsonBuilder.registerTypeAdapter(Message.class, new SkyBetGsonDeserializer());
		    gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    Gson gson = gsonBuilder.create();
			consumer.setGson(gson);
			consumer.setSaveMessageService(new IntermediateSaveMessageServiceImpl());
			channel.basicConsume(queueName, true, consumer);
		}catch (Exception e) {
			log.log(Level.SEVERE, "RabbitMQ Consumer error", e);
		}
	}

}
