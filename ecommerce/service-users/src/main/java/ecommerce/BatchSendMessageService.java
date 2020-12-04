package ecommerce;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class BatchSendMessageService {
	private Connection connection;
	private final KafkaDispatcher<User> dispatcher = new KafkaDispatcher<>();

	BatchSendMessageService() throws SQLException {
		String url = "jdbc:sqlite:target/users_database.db";
		this.connection = DriverManager.getConnection(url);
		try {
			connection.createStatement()
					.execute("create table Users ( " + "uuid varchar(200) primary key, email varchar(200))");
		} catch (SQLException ex) {
			// Be careful, the sql could be wrong
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, SQLException {

		var batchService = new BatchSendMessageService();
		try (var service = new KafkaService<>("ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS", batchService::parse,
				CreateUserService.class.getSimpleName(), new HashMap<>())) {
			service.run();
		}

	}

	void parse(ConsumerRecord<String, Message<String>> record)
			throws InterruptedException, ExecutionException, SQLException {
		System.out.println("------------------------------------------");
		System.out.println("Processing new batch");

		var message = record.value();

		System.out.println("Topic: " + message.getPayload());

		for (User user : getAllUsers()) {
			dispatcher.sendAsync(message.getPayload(), user.getUuid(),
					message.getId().continueWith(BatchSendMessageService.class.getSimpleName()), user);
		}

	}

	private List<User> getAllUsers() throws SQLException {
		var select = connection.prepareStatement("select uuid from Users");
		var resultado = select.executeQuery();

		List<User> users = new ArrayList<>();

		while (resultado.next()) {
			users.add(new User(resultado.getString(1)));
		}

		return users;
	}

}
