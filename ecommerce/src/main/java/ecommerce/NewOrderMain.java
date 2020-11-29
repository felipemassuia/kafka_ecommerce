package ecommerce;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

		try (var dispatcher = new KafkaDispatcher()) {

			for (int i = 0; i < 10; i++) {

				String id = UUID.randomUUID().toString();
				dispatcher.send("ECOMMERCE_NEW_ORDER", id, "Valor 5128929843");
				dispatcher.send("ECOMMERCE_NEW_EMAIL", id, "Thank you! We are processing your order!");

			}
		}
	}
}
