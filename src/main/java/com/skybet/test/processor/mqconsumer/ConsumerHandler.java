package com.skybet.test.processor.mqconsumer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.skybet.test.beans.Message;
import com.skybet.test.errors.ProcessorException;
import com.skybet.test.services.SaveMessageService;

public class ConsumerHandler extends DefaultConsumer {
	
	private static Logger log = Logger.getLogger(ConsumerHandler.class.getCanonicalName());
	
	private SaveMessageService saveMessageService;
	private Gson gson;

	public ConsumerHandler(Channel channel) {
		super(channel);
	}

	public SaveMessageService getSaveMessageService() {
		return saveMessageService;
	}

	public void setSaveMessageService(SaveMessageService saveMessageService) {
		this.saveMessageService = saveMessageService;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
	
	private Message parseJson(byte[] body) {
		try {
			return gson.fromJson(new String(body, "UTF-8"), Message.class);
		} catch (JsonSyntaxException | UnsupportedEncodingException | ProcessorException e) {
			log.log(Level.SEVERE, "Invalid data format", e);
			return null;
		}
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		Message message = parseJson(body);
		if(message != null)
			saveMessageService.save(message);
	}

}
