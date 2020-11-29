package ecommerce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
	public static void main(String[] args) throws IOException {

		var emailService = new EmailService();
		try (var service = new KafkaService("ECOMMERCE_NEW_EMAIL", emailService::parse,
				EmailService.class.getSimpleName(), Email.class,
				new HashMap<>())) {
			service.run();
		}

	}

	private void parse(ConsumerRecord<String, Email> record) {
		System.out.println("------------------------------------------");
		System.out.println("Sending email");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// ignoring
			e.printStackTrace();
		}

	}

}
