package com.skybet.test.processor.mqconsumer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.skybet.test.errors.ConfigException;
import com.skybet.test.processor.Config;

public class Boot {
	
	private static Logger log = Logger.getLogger(Boot.class.getCanonicalName());

	public static void main(String[] args) throws UnknownHostException, IOException {
    	try {
			Config.RABBITMQ_HOST = System.getenv("RABBITMQ_DEFAULT_HOST");
			Config.RABBITMQ_PORT = Integer.parseInt(System.getenv("RABBITMQ_DEFAULT_PORT"));
			Config.RABBITMQ_DEFAULT_USER = System.getenv("RABBITMQ_DEFAULT_USER");
			Config.RABBITMQ_DEFAULT_PASS = System.getenv("RABBITMQ_DEFAULT_PASS");
			Config.RABBITMQ_DEFAULT_VHOST = System.getenv("RABBITMQ_DEFAULT_VHOST");
        	log.log(Level.INFO, "Setted as RabbitMQ address: " + Config.RABBITMQ_HOST + ":" + Config.RABBITMQ_PORT);
			Config.MONGODB_HOST = System.getenv("MONGODB_HOST");
			Config.MONGODB_PORT = Integer.parseInt(System.getenv("MONGODB_PORT"));
			Config.MONGODB_USER = System.getenv("MONGODB_USER");
			Config.MONGODB_PASS = System.getenv("MONGODB_PASS");
			Config.MONGODB_DB = System.getenv("MONGODB_DB");
        	log.log(Level.INFO, "Setted as MongoDB address: " + Config.MONGODB_HOST + ":" + Config.MONGODB_PORT);
    	}catch (Exception e) {
    		throw new ConfigException("Not all env vars present", e);
		}
		log.log(Level.INFO, "RabbitMQ Consumer started");
		ConsumerProcessor processor = new ConsumerProcessor();
		processor.execute();
	}

}
