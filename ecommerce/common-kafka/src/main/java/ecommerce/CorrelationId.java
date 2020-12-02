package ecommerce;

import java.util.UUID;

public class CorrelationId {
	
	private final String id;
	
	CorrelationId(String name){
		id = name + "(" + UUID.randomUUID().toString() + ")";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CorrelationId [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	public CorrelationId continueWith(String simpleName) {
		return new CorrelationId(id + "-" + simpleName);
	}
	
}
