package ecommerce;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class LogService {
	public static void main(String[] args) throws IOException {

		var logService = new LogService();
		try (var kafkaService = new KafkaService(Pattern.compile("ECOMMERCE.*"), logService::parse,
				logService.getClass().getSimpleName())) {
			kafkaService.run();
		}
	}

	void parse(ConsumerRecord<String, String> record) {

		System.out.println("------------------------------------------");
		System.out.println("LOG: " + record.topic());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());

	}

}
