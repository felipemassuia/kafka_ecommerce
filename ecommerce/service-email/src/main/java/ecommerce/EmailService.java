package ecommerce;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.consumer.ConsumerService;
import ecommerce.consumer.KafkaService;
import ecommerce.consumer.ServiceRunner;

public class EmailService implements ConsumerService<String> {
	private static final int THREADS = 5;

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		

		new ServiceRunner(EmailService::new).start(THREADS);
		
		
	}
	
	public String getConsumerGroup() {
		return EmailService.class.getSimpleName();
	}
	
	public String getTopic() {
		return "ECOMMERCE_NEW_EMAIL";
	}

	public void parse(ConsumerRecord<String, Message<String>> record) {
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
