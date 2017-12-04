package my.lev;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

public class AdminForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public AdminForm() {
		super();
		System.out.println("init AdminForm");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("lists");
		
		Document qry = new Document("$match", new Document("name", "role"));
		AggregateIterable<Document> agr = coll.aggregate(Arrays.asList(qry));
		
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) agr.first().get("value");
		
		mongoClient.close();
		
		req.setAttribute("roles",  roles);
		req.getRequestDispatcher("/admin.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getParameter("chrole") != null) {
			String login = req.getParameter("login");
			String role = req.getParameter("role");
			
			
			// db.users.update({_id: "moder"}, {$set: {role: "moder"}});
			// 4.34.15
			
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("users");
			Document user = new Document("_id", login);
			Document newu = new Document("$set", new Document("role", role));
			try {
				UpdateResult ur = collection.updateOne(user, newu);
				if (ur.getMatchedCount() != 0) {
					res.sendRedirect("admin");
				} else {
					req.getSession().setAttribute("head", "Ошибка поиска");
					req.getSession().setAttribute("disc", "Логин не существует");
					res.sendRedirect("error");
				}
			} catch (Exception e) {
				req.getSession().setAttribute("head", "Ошибка");
				req.getSession().setAttribute("disc", "????");
				res.sendRedirect("error");
			}	finally {
				mongoClient.close();
			}
		}
	}
}
