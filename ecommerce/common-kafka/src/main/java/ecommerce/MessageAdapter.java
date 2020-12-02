package ecommerce;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MessageAdapter implements JsonSerializer<Message>{

	@Override
	public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", src.getPayload().getClass().getName());
		obj.add("payload", context.serialize(src.getPayload()));
		obj.add("correlationId", context.serialize(src.getId()));
		
		return obj;
	}
	
}
