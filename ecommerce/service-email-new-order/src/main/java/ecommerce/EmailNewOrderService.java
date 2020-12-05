package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.consumer.ConsumerService;
import ecommerce.consumer.KafkaService;
import ecommerce.consumer.ServiceRunner;
import ecommerce.dispatcher.KafkaDispatcher;

public class EmailNewOrderService implements ConsumerService<Order>{
	
	

	private static final int THREADS = 1;
	private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		
		new ServiceRunner(EmailNewOrderService::new).start(THREADS);
		
	}

	public void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
		System.out.println("------------------------------------------");
		System.out.println("Processing new order, preparing email");
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

		var subject = "Subject";
		var body = "Thank you for the order!";

		emailDispatcher.send("ECOMMERCE_NEW_EMAIL", order.getEmail(),
				message.getId().continueWith(EmailNewOrderService.class.getSimpleName()), new Email(subject, body));

	}

	@Override
	public String getTopic() {
		return "ECOMMERCE_NEW_ORDER";
	}

	@Override
	public String getConsumerGroup() {
		return KafkaDispatcher.class.getSimpleName();
	}
}
