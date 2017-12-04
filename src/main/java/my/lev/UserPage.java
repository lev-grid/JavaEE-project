package my.lev;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public UserPage() {
        super();
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("init UserPage");
	}
    
    private String md5(String pw) {
		String rs = "";
		try {
			StringBuffer cd = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte bt[] = pw.getBytes();
			byte dg[] = md.digest(bt);
			for (int i = 0; i < dg.length; ++i) {
				cd.append(Integer.toHexString(0x0100+(dg[i]&0x00FF)).substring(1));
			}
			rs = cd.toString();
		} catch (Exception e) {
			rs = pw;
		}
		return rs;
	}
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {	
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");

		String login = (String) req.getSession().getAttribute("login");
		Document qry = new Document("$match", new Document(login, new Document("$exists", true)));
		
		AggregateIterable<Document> agr = coll.aggregate(Arrays.asList(qry));
		ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		
		for (Document obj : agr) {
			@SuppressWarnings("unchecked")
			String wrt = obj.getString("gn").concat(" ").concat(obj.getString("de")).concat(((List<String>) obj.get("ru")).toString().replace("[", " (").replace("]", "):"));
			Document gl = (Document) ((Document) obj.get(login)).get("gl");			
			String prz = "";
			if (gl.get("rh") == null) {
				prz = "0%";
			} else {
				prz = ((Integer) Math.round(100*((float) gl.getInteger("rh"))/((float) gl.getInteger("tt")))).toString().concat("%");
			}
			ArrayList<String> word = new ArrayList<String>();
			word.add(wrt);
			word.add(prz);
			words.add(word);
		}
		mongoClient.close();
		
		req.setAttribute("words",  words);
		req.getRequestDispatcher("/user.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		if (req.getParameter("chname") != null) {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("users");
			
			try {
				String login = (String) req.getSession().getAttribute("login");
				String nname = req.getParameter("nname");
				Document qry1 = new Document("_id", login);
				Document qry2 = new Document("$set", new Document("name", nname));
				coll.updateOne(qry1, qry2);
				req.getSession().setAttribute("name", nname);
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				mongoClient.close();
			}
			res.sendRedirect("user");
		} else if (req.getParameter("chpass") != null) {
			String login = (String) req.getSession().getAttribute("login");
			String npassword = req.getParameter("npassword");
			String nrpassword = req.getParameter("nrpassword");
			
			if (!npassword.equals(nrpassword)) {
				req.getSession().setAttribute("head", "Ошибка при изменении пароля");
				req.getSession().setAttribute("disc", "Пароли не совподают");
				res.sendRedirect("error");
			} else {
				SecureRandom random = new SecureRandom();
				String salz = new BigInteger(130, random).toString(32);
				String pw = md5(npassword.concat(salz));
				
				MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("users");
				try {
					Document qry1 = new Document("_id", login);
					Document qry2 = new Document("$set", new Document("salz", salz).append("password", pw));
					coll.updateOne(qry1, qry2);
				} catch (MongoWriteException e) {
					System.out.println(e);
				} finally {
					mongoClient.close();
				}
				res.sendRedirect("user");
			}
		} else if (req.getParameter("del1") != null) {
			req.setAttribute("del",  1);
			req.getRequestDispatcher("/user.jsp").forward(req, res);
		} else if (req.getParameter("del2") != null) {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoDatabase db = mongoClient.getDatabase("deja");
			try {
				String login = (String) req.getSession().getAttribute("login");
				Document qry = new Document("_id", login);
				db.getCollection("users").deleteOne(qry);
				Document qry1 = new Document(login, new Document("$exists", true));
				Document qry2 = new Document("$unset", new Document(login, 1));
				db.getCollection("worte").updateMany(qry1, qry2);
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				mongoClient.close();
			}
			req.getSession().invalidate();
			res.sendRedirect("auth");
		} else if (req.getParameter("del3") != null) {
			res.sendRedirect("user");
		} else {
			doGet(req, res);
		}
	}
}