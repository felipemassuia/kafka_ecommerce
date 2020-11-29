package ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable {
	
	private final KafkaConsumer<String, T> consumer;
	private final ConsumerFunction parse;

	public KafkaService(String topic, ConsumerFunction parse, String simpleName, Class<T> type) {
		this(parse, simpleName, type);
		consumer.subscribe(Collections.singletonList(topic));
	}

	public KafkaService(Pattern topic, ConsumerFunction parse, String simpleName, Class<T> type) {
		this(parse, simpleName, type);
		consumer.subscribe(topic);
	}
	
	private KafkaService(ConsumerFunction parse, String simpleName, Class<T> type) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<>(properties(simpleName, type));
	}

	void run() {
		while (true) {
			var records = consumer.poll(Duration.ofMillis(1));
			if (!records.isEmpty()) {
				System.out.println("Encontrei " + records.count() + " registros");
				for (var record : records) {
					parse.consume(record);
				}
			}
		}
	}

	private Properties properties(String groupId, Class<T> type) {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
		return properties;
	}

	@Override
	public void close() throws IOException {
		consumer.close();
		
	}

}
