package ecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDispatcher<T> implements Closeable{
	
	private final KafkaProducer<String, Message<T>> producer;
	
	public KafkaDispatcher () {
		
		this.producer = new KafkaProducer<>(properties());

		
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		return properties;
	}

	public void send(String topic, String id, T payload) throws InterruptedException, ExecutionException {
		
		var value = new Message<>(new CorrelationId(), payload);
		var record = new ProducerRecord<>(topic, id, value);

		Callback callback = (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println(
					"Sucesso de envio" + data.topic() + "partition " + data.partition() + "offset" + data.offset());
		};

		producer.send(record, callback).get();		
	}

	@Override
	public void close() throws IOException {
		producer.close();
		
	}
	
}
