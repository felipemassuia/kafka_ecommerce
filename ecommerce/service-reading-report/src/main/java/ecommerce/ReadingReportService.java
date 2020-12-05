package ecommerce;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.consumer.ConsumerService;
import ecommerce.consumer.KafkaService;
import ecommerce.consumer.ServiceRunner;

public class ReadingReportService implements ConsumerService<User>{
	
	private static final Path SOURCE = new File("/Users/felipemassuia/git/kafka_ecommerce/ecommerce/service-reading-report/src/main/resources/report.txt").toPath();
	private static final int THREADS = 5;
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		new ServiceRunner(ReadingReportService::new).start(THREADS);
		
	
	}
	
	
	public void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
		System.out.println("------------------------------------------");
		System.out.println("Processing report for " + record.value());
		
		var message = record.value();
		
		var user = message.getPayload();
		var target = new File(user.getReportPath());
		
		IO.copyTo(SOURCE, target);
		IO.append(target, "Created for" + user.getUuid());
		
		System.out.println("File created:" + target.getAbsolutePath());
		
		
	}


	@Override
	public String getTopic() {
		return "ECOMMERCE_USER_GENERATE_READING_REPORT";
	}


	@Override
	public String getConsumerGroup() {
		return ReadingReportService.class.getSimpleName();
	}
	
}
