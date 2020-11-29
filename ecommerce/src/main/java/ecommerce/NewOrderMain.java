package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			try (var emailDispatcher = new KafkaDispatcher<String>()) {

				for (int i = 0; i < 10; i++) {

					String userId = UUID.randomUUID().toString();
					String orderId = UUID.randomUUID().toString();
					BigDecimal amount = new BigDecimal(Math.random() * 500 + 10);

					var order = new Order(userId, orderId, amount);
					var email = "email@gmail.com";

					orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
					emailDispatcher.send("ECOMMERCE_NEW_EMAIL", userId, email);

				}
			}
		}
	}
}
