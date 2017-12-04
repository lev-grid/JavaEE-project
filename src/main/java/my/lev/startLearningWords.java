package my.lev;

import java.io.IOException;
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
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

public class startLearningWords extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public startLearningWords() {
        super();
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("init startLearningWords");
	}
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {	
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
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
		req.setAttribute("type", req.getParameter("type"));
		req.getRequestDispatcher("/slw.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

}
