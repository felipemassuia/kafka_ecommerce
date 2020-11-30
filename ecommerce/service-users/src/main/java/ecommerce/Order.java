package ecommerce;

import java.math.BigDecimal;

public class Order {
	
	private final String orderId;
	private final BigDecimal amount;
	private final String email;
	
	public Order(String orderId, BigDecimal amount, String email) {
		this.orderId = orderId;
		this.amount = amount;
		this.email = email;
	}
	

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getEmail() {
		return email;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Order [orderId=");
		builder.append(orderId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}


	
}
