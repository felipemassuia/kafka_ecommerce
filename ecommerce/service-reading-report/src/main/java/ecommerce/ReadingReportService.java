package ecommerce;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ReadingReportService {
	
	private static final Path SOURCE = new File("/Users/felipemassuia/git/kafka_ecommerce/ecommerce/service-reading-report/src/main/resources/report.txt").toPath();
	
	public static void main(String[] args) throws IOException {

		var readingReportService = new ReadingReportService();
		try (var service = new KafkaService<>("USER_GENERATE_READING_REPORT", readingReportService::parse,
				ReadingReportService.class.getSimpleName(), User.class, new HashMap<>())) {
			service.run();
		}

	}

	
	void parse(ConsumerRecord<String, Message<User>> record) throws IOException {
		System.out.println("------------------------------------------");
		System.out.println("Processing report for " + record.value());
		
		var message = record.value();
		
		var user = message.getPayload();
		var target = new File(user.getReportPath());
		
		IO.copyTo(SOURCE, target);
		IO.append(target, "Created for" + user.getUuid());
		
		System.out.println("File created:" + target.getAbsolutePath());
		
		
	}
	
}
