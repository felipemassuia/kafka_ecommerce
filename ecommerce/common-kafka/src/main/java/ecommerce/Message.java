package ecommerce;

public class Message<T> {
	
	private final CorrelationId id;
	private final T payload;
	
	public Message(CorrelationId id, T payload){
		this.id = id;
		this.payload = payload;
	}
	
	public CorrelationId getId() {
		return id;
	}
	
	public T getPayload() {
		return payload;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [id=");
		builder.append(id);
		builder.append(", payload=");
		builder.append(payload);
		builder.append("]");
		return builder.toString();
	}
	
}
