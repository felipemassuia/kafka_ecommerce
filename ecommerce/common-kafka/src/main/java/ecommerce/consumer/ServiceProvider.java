package ecommerce.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ServiceProvider<T> implements Callable<Void> {
	
	private final ServiceFactory<T> factory;
	
	public ServiceProvider(ServiceFactory<T> factory) {
		this.factory = factory;
	}

	public Void call() throws InterruptedException, ExecutionException, IOException {
		var myService = factory.create();
		try (var service = new KafkaService(myService.getTopic(), myService::parse,
		myService.getConsumerGroup(), 
		new HashMap<>())) {
			service.run();
		}
		return null;
	}

}
