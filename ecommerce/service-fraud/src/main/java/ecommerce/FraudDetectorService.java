package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		var fraudDetectorService = new FraudDetectorService();
		try (var service = new KafkaService<>("ECOMMERCE_NEW_ORDER", fraudDetectorService::parse,
				FraudDetectorService.class.getSimpleName(), new HashMap<>())) {
			service.run();
		}

	}

	private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

	void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
		System.out.println("------------------------------------------");
		System.out.println("Processing new order, checking for fraud");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// ignoring
			e.printStackTrace();
		}

		var message = record.value();
		var order = message.getPayload();

		if (isFraud(order)) {
			System.out.println("Order is a fraud!!!!" + order);
			kafkaDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(),
					message.getId().continueWith(FraudDetectorService.class.getSimpleName()), order);
		} else {
			System.out.println("Order approved: " + order);
			kafkaDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(),
					message.getId().continueWith(FraudDetectorService.class.getSimpleName()), order);
		}

	}

	private boolean isFraud(Order order) {
		return order.getAmount().compareTo(new BigDecimal("380")) >= 0;
	}

}
