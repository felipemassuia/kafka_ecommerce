package ecommerce;

import java.math.BigDecimal;

public class Order {
	
	private final String userId;
	private final String orderId;
	private final BigDecimal amount;
	
	public Order(String userId, String orderId, BigDecimal amount) {
		this.userId = userId;
		this.orderId = orderId;
		this.amount = amount;
	}

	public String getUserId() {
		return userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public String getEmail() {
		return "email";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Order [userId=");
		builder.append(userId);
		builder.append(", orderId=");
		builder.append(orderId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}
	
	

}
