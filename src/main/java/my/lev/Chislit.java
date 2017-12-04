package my.lev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

public class Chislit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Chislit() {
        super();
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("init Chislit");
	}
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ArrayList<Integer> nr = new ArrayList<Integer>();
		Random rn = new Random();
		for (int i= 0; i < 8; ++i) nr.add(rn.nextInt(1000));
		
		
		
		Map<Integer, String> izdb = new HashMap<Integer, String>();
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("chisl");
		AggregateIterable<Document> output = coll.aggregate(Arrays.asList(new Document("$match", new Document())));
		for (Document obj : output) {
			Integer zf = obj.getDouble("zf").intValue();
			String de = obj.getString("de");
			izdb.put(zf, de);
		}
		mongoClient.close();
		
		
		ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 8 ; ++i) {
			Integer zf = nr.get(i);
			Integer sot = nr.get(i)/100;
			Integer des = nr.get(i)%100/10;
			Integer edn = nr.get(i)%10;
			String str = zf == 0 ? "null" : "";
			if (sot != 0) {
				if (sot == 1) str = str.concat(izdb.get(100));
				else str = str.concat(izdb.get(sot)).concat(izdb.get(100));
			}
			if (des == 1) {
				str = str.concat(izdb.get(10+edn));
			} else if (des == 0) {
				if (edn != 0) str = str.concat(izdb.get(edn));
			} else {
				if (edn != 0) str = str.concat(izdb.get(edn));
				str = str.concat("und").concat(izdb.get(des*10));
			}
			ArrayList<String> word = new ArrayList<String>();
			word.add(new Integer(i).toString());
			if (i < -10) {
				word.add(zf.toString());
				word.add(str);
			} else {
				word.add(str);
				word.add(zf.toString());
			}
			words.add(word);
		}
		
		req.setAttribute("words",  words);
		req.getRequestDispatcher("/chisl.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		if (req.getParameter("check") != null) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) req.getServletContext().getAttribute("words");
			List<String> otv = Arrays.asList(req.getParameterValues("otv"));
			
			for (int i = 0; i < words.size(); ++i) {
				Boolean rh = words.get(i).get(2).equals(otv.get(i));
				words.get(i).add(3, otv.get(i));
				words.get(i).add(4, Boolean.toString(rh));
			}

			req.setAttribute("words",  words);
			req.setAttribute("type",  "result");
			req.getRequestDispatcher("/chisl.jsp").forward(req, res);
		} else if (req.getParameter("start") != null) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) req.getServletContext().getAttribute("words");
			Collections.shuffle(words);
			req.setAttribute("words",  words);
			req.getRequestDispatcher("/chisl.jsp").forward(req, res);
		} else {
			res.sendRedirect("main");
		}
	}

}
