package ecommerce;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import ecommerce.consumer.KafkaService;

public class LogService {
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

		var logService = new LogService();
		try (var kafkaService = new KafkaService(Pattern.compile("ECOMMERCE.*"), logService::parse,
				LogService.class.getSimpleName(), 
				Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
			kafkaService.run();
		}
	}

	private void parse(ConsumerRecord<String, Message<String>> record) {

		System.out.println("------------------------------------------");
		System.out.println("LOG: " + record.topic());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());

	}

}
