package ecommerce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
	public static void main(String[] args) throws IOException {

		var fraudDetectorService = new FraudDetectorService();
		try (var service = new KafkaService<>("ECOMMERCE_NEW_ORDER", fraudDetectorService::parse,
				FraudDetectorService.class.getSimpleName(), Order.class,
				new HashMap<>())) {
			service.run();
		}

	}

	void parse(ConsumerRecord<String, Order> record) {
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
		System.out.println("Order processed");

	}
}
