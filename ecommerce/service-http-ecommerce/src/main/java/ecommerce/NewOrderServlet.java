package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
	private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			orderDispatcher.close();
			emailDispatcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {

			var email = req.getParameter("email");
			var orderId = UUID.randomUUID().toString();
			var amount = new BigDecimal(req.getParameter("amount"));

			String subject = "Subject";
			String body = "Thank you! We are processing your order!";
			var order = new Order(orderId, amount, email);
			var emailObject = new Email(subject, body);

			orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);
			emailDispatcher.send("ECOMMERCE_NEW_EMAIL", email, emailObject);

			System.out.println("New order sent successfully.");

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().println("New order sent");

		} catch (ExecutionException e) {
			throw new ServletException();
		} catch (InterruptedException e) {
			throw new ServletException();
		}

	}

}
