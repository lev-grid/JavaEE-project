package my.lev;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
//import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.bson.Document;

//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;

public class ddd extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
    public ddd()
    {
        super();
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("init");
	}
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		SecureRandom random = new SecureRandom();
		String pw = req.getParameter("pw");
		String salz = new BigInteger(130, random).toString(32);
		String rs ="";
		try {
			StringBuffer code = new StringBuffer();
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			byte bytes[] = pw.concat(salz).getBytes();
			byte digest[] = messageDigest.digest(bytes);
			for (int i = 0; i < digest.length; ++i) {
			    code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
			}
			rs = code.toString();
		} catch (Exception e) {
			rs = pw.concat(salz);
		}
		System.out.println("****");
		System.out.println(pw);
		System.out.println("****");
		System.out.println(salz);
		System.out.println("****");
		System.out.println(rs);
		System.out.println("****");
		
		/*req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("worte");
		Document user = new Document().append("cr", "сущ");
		MongoCursor<Document> cursor = null;
		try
		{
			cursor = collection.find(user).iterator();
			int count = 0;
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				@SuppressWarnings("unchecked")
				ArrayList<String> ru = (ArrayList<String>) doc.get("ru");
				String de = doc.getString("de");
				String gn = doc.getString("gn");
				
				req.setAttribute("de"+count, de);
				req.setAttribute("ru"+count, ru);
				req.setAttribute("gn"+count, gn);
				count += 1;
			}
			req.setAttribute("count", count);
		}
		finally
		{
			cursor.close();
		}
		mongoClient.close();
		req.getRequestDispatcher("/ddd.jsp").forward(req, res);*/
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		doGet(req, res);
	}

}
