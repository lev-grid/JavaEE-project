package my.lev;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class authcheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public authcheck() {
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("authcheck init");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.sendRedirect("auth");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("users");
		Document user = new Document("_id", login).append("password", password);
		MongoCursor<Document> cursor = null;
		try {
			cursor = collection.find(user).iterator();
			if (cursor.hasNext()) {
				Document doc = cursor.next();
				req.getSession().setAttribute("login", login);
				req.getSession().setAttribute("email", doc.getString("email"));
				req.getSession().setAttribute("password", password);
				res.sendRedirect("main");
			} else {
				req.getSession().setAttribute("head", "Ошибка авторизации");
				req.getSession().setAttribute("disc", "Неверный логин или пароль");
				res.sendRedirect("error");
			}
		} catch (Exception e) {
			req.getSession().setAttribute("head", "Ошибка");
			req.getSession().setAttribute("disc", "????");
			res.sendRedirect("error");
		}	finally {
			cursor.close();
			mongoClient.close();
		}
		
		
		/*if (login.equals(al) && password.equals(ap)) {
			req.getSession().setAttribute("login", login);
			req.getSession().setAttribute("password", password);
			res.sendRedirect("main");
		} else {
			req.getSession().setAttribute("head", "Ошибка авторизации");
			req.getSession().setAttribute("disc", "Неверный логин или пароль");
			res.sendRedirect("error");
		}*/
		
	}
}
