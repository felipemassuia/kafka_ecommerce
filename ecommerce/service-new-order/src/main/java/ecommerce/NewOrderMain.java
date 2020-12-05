package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ecommerce.dispatcher.KafkaDispatcher;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		String email = Math.random() + "@email.com";

		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			for (int i = 0; i < 10; i++) {

				String orderId = UUID.randomUUID().toString();
				BigDecimal amount = new BigDecimal(Math.random() * 500 + 10);
				var order = new Order(orderId, amount, email);
				var id = new CorrelationId(NewOrderMain.class.getSimpleName());

				orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, id, order);

			}
		}
	}

}
