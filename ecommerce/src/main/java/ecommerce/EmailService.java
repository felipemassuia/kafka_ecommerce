package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
    public static void main(String[] args) {
    	
    	var emailService = new EmailService();
    	var service = new KafkaService("ECOMMERCE_NEW_EMAIL", emailService::parse, EmailService.class.getSimpleName());
    	service.run();
    	
    }
    
    private void parse(ConsumerRecord<String, String> record) {
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
