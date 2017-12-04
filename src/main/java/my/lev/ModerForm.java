package my.lev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

public class ModerForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public ModerForm() {
		super();
		System.out.println("init ModerForm");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		req.getRequestDispatcher("/moder.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getParameter("schdata") != null) {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("lists");
			
			Document qry = new Document("$match", new Document("name", "gr"));
			AggregateIterable<Document> agr = coll.aggregate(Arrays.asList(qry));
			
			@SuppressWarnings("unchecked")
			List<String> valgr = (List<String>) agr.first().get("value");
			
			qry = new Document("$match", new Document("name", "cr"));
			agr = coll.aggregate(Arrays.asList(qry));
			
			@SuppressWarnings("unchecked")
			List<Document> valcr = (List<Document>) agr.first().get("value");
			
			ArrayList<ArrayList<String>> rescr = new ArrayList<ArrayList<String>>();
			for (Document d: valcr) {
				ArrayList<String> rescr1 = new ArrayList<String>();
				rescr1.add(d.getString("kt"));
				rescr1.add(d.getString("pl"));
				rescr.add(rescr1);
			}
			mongoClient.close();
			
			req.setAttribute("gr",  valgr);
			req.setAttribute("cr",  rescr);
			req.setAttribute("type",  "search");
		} else if (req.getParameter("search") != null) {
			String de = req.getParameter("de");
			String ru = req.getParameter("ru");
			String[] cr = req.getParameterValues("cr");
			String[] gr = req.getParameterValues("gr");
			
			Document qry1 = new Document("$match", new Document());
			if (de != "") ((Document) qry1.get("$match")).append("de", de);
			if (ru != "") ((Document) qry1.get("$match")).append("ru", ru);
			if (gr != null) ((Document) qry1.get("$match")).append("gr", new Document("$in", Arrays.asList(gr)));
			if (cr != null) ((Document) qry1.get("$match")).append("cr", new Document("$in", Arrays.asList(cr)));
			
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");
			
			AggregateIterable<Document> output = coll.aggregate(Arrays.asList(qry1));
			ArrayList<Document> words = new ArrayList<Document>();
			for (Document obj : output) {
				words.add(obj);
			}
			mongoClient.close();
			req.setAttribute("words",  words);
			req.setAttribute("type",  "chdata");
		} else if (req.getParameter("chdata") != null) {
			String id = req.getParameter("id");
			String de = req.getParameter("de");
			List<String> ru = Arrays.asList(req.getParameter("ru").split(", "));
			List<String> gr = Arrays.asList(req.getParameter("gr").split(", "));
			String gn = req.getParameter("gn");
			String rp = req.getParameter("rp");
			String mn = req.getParameter("mn");
			
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");
			try {
				Document qry1 = new Document("_id", new ObjectId(id));
				Document qry2 = new Document("$set", new Document("de", de).append("ru", ru).append("gr",  gr).append("gn",  gn).append("rp", rp).append("mn",  mn));
				coll.updateOne(qry1, qry2);
			} catch (MongoWriteException e) {
				System.out.println(e);
			} finally {
				mongoClient.close();
			}
			req.setAttribute("type",  null);
		} else if (req.getParameter("saddata") != null) {
			req.setAttribute("type",  "addata");
		} else if (req.getParameter("addata") != null) {
			String de = req.getParameter("de");
			List<String> ru = Arrays.asList(req.getParameter("ru").split(", "));
			String cr = "сущ";
			List<String> gr = Arrays.asList(req.getParameter("gr").split(", "));
			String gn = req.getParameter("gn");
			String rp = req.getParameter("rp");
			String mn = req.getParameter("mn");
			
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");
			try {
				Document qry = new Document("de", de).append("ru", ru).append("cr",  cr).append("gr",  gr).append("gn",  gn).append("rp", rp).append("mn",  mn);
				coll.insertOne(qry);
			} catch (MongoWriteException e) {
				System.out.println(e);
			} finally {
				mongoClient.close();
			}
			req.setAttribute("type",  null);
		} else {
			res.sendRedirect("moder");
		}
		req.getRequestDispatcher("/moder.jsp").forward(req, res);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*if (req.getParameter("chdata") != null) {
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
		}*/
	}
}
