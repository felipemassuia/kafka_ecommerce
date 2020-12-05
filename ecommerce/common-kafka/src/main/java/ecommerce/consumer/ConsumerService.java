package ecommerce.consumer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.Message;

public interface ConsumerService<T> {
	
	void parse(ConsumerRecord<String, Message<T>> record) throws Exception;
	String getTopic();
	String getConsumerGroup();
}
