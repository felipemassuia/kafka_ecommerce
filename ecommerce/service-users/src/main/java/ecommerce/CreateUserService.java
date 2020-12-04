package ecommerce;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import ecommerce.consumer.KafkaService;

public class CreateUserService {

	private Connection connection;

	CreateUserService() throws SQLException {
		String url = "jdbc:sqlite:target/users_database.db";
		this.connection = DriverManager.getConnection(url);
		try{connection.createStatement().execute("create table Users ( "
				+ "uuid varchar(200) primary key, email varchar(200))");}
		catch(SQLException ex) {
			//Be careful, the sql could be wrong
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, SQLException, InterruptedException, ExecutionException {

		var userService = new CreateUserService();
		try (var service = new KafkaService<>("ECOMMERCE_ECOMMERCE_NEW_ORDER", userService::parse,
				CreateUserService.class.getSimpleName(), new HashMap<>())) {
			service.run();
		}

	}

	void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException, SQLException {
		System.out.println("------------------------------------------");
		System.out.println("Processing new order, checking for new user");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		
		var order = record.value().getPayload();
		
		if(isNewUser(order.getEmail())) {
			insertNewUser(order.getEmail());
		}

	}

	private void insertNewUser(String email) throws SQLException {
		var insert = connection.prepareStatement("insert into Users (uuid, email) values (?,?)");
		insert.setString(1, UUID.randomUUID().toString());
		insert.setString(2, email);
		insert.execute();
		System.out.println("Usuario uuid e " + email + " adicionado com sucesso!");
	}

	private boolean isNewUser(String email) throws SQLException {
		var select = connection.prepareStatement("select uuid from Users where email = ? limit 1");
		select.setString(1, email);
		var resultado = select.executeQuery();
		if(resultado.next()) {
			return false;
		}
		return true;
	}

}
