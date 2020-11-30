package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		String email = Math.random() + "@email.com";
		
		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			try (var emailDispatcher = new KafkaDispatcher<Email>()) {

				for (int i = 0; i < 10; i++) {

					String userId = UUID.randomUUID().toString();
					String orderId = UUID.randomUUID().toString();
					BigDecimal amount = new BigDecimal(Math.random() * 500 + 10);
					String subject = "Subject";
					String body = "Thank you! We are processing your order!";
					var order = new Order(userId, orderId, amount, email);
					var emailObject = new Email(subject, body);
					

					orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
					emailDispatcher.send("ECOMMERCE_NEW_EMAIL", userId, emailObject);

				}
			}
		}
	}
}
