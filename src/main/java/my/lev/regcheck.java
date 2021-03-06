package my.lev;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;

public class regcheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public regcheck() {
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("regcheck init");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.sendRedirect("auth");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String login = req.getParameter("login");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String rpassword = req.getParameter("rpassword");
		
		if (!password.equals(rpassword)) {
			req.getSession().setAttribute("head", "Ошибка регистрации");
			req.getSession().setAttribute("disc", "Пароли не совподают");
			res.sendRedirect("error");
		} else {
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("users");
			Document user = new Document("_id", login).append("email", email).append("password", password).append("role", "user");
			try {
				collection.insertOne(user);
				req.getSession().setAttribute("login", login);
				req.getSession().setAttribute("email", email);
				req.getSession().setAttribute("password", password);
				res.sendRedirect("main");
			} catch (MongoWriteException e) {
				req.getSession().setAttribute("head", "Ошибка регистрации");
				req.getSession().setAttribute("disc", "Данный логин занят");
				res.sendRedirect("error");
			} finally {
				mongoClient.close();
			}
		}
	}
}
