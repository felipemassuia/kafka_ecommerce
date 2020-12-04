package ecommerce.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.Message;

public interface ConsumerFunction<T> {

	void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
