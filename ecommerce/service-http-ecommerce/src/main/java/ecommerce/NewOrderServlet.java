package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ecommerce.dispatcher.KafkaDispatcher;

public class NewOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			orderDispatcher.close();
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

			var order = new Order(orderId, amount, email);

			orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, new CorrelationId(NewOrderServlet.class.getSimpleName()), order);

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
