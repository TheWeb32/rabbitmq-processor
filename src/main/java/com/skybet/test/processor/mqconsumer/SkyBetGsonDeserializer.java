package com.skybet.test.processor.mqconsumer;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.skybet.test.beans.Event;
import com.skybet.test.beans.Market;
import com.skybet.test.beans.Message;
import com.skybet.test.beans.Outcome;
import com.skybet.test.errors.ProcessorException;
import com.skybet.test.model.Types;

public class SkyBetGsonDeserializer implements JsonDeserializer<Message> {
	
	public Message deserialize(JsonElement obj, Type arg1, JsonDeserializationContext context) throws JsonParseException {
		String type = obj.getAsJsonObject().get("type").getAsString();
		Message message = null;
		switch (type) {
		case Types.EVENT:
			message = context.deserialize(obj, Event.class);
			break;
		case Types.MARKET:
			message = context.deserialize(obj, Market.class);
			break;
		case Types.OUTCOME:
			message = context.deserialize(obj, Outcome.class);
			break;
		default:
			throw new ProcessorException("Found invalid type: " + type);
		}
	    return message;
	  }

}
