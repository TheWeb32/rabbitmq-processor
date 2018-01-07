package com.skybet.test.processor.mqconsumer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.skybet.test.processor.Config;

public class Boot {
	
	private static Logger log = Logger.getLogger(Boot.class.getCanonicalName());

	public static void main(String[] args) throws UnknownHostException, IOException {
		if(args.length >= 3) {
			Config.PROVIDER_HOST = args[0];
			Config.PROVIDER_DB_PORT = Integer.parseInt(args[1]);
			Config.PROVIDER_RABBITMQ_PORT = Integer.parseInt(args[2]);
			
		}else {
			log.log(Level.SEVERE, "No arguments passed. Insert provider host with database and after rabbitmq ports.");
			return;
		}
		log.log(Level.INFO, "RabbitMQ Consumer started with arguments: " + Arrays.asList(args));
		ConsumerProcessor processor = new ConsumerProcessor();
		processor.execute();
	}

}
