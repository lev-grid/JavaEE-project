package my.lev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

public class learningWords extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public learningWords() {
        super();
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("init learningWords");
	}
    
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.sendRedirect("main");
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		if (req.getParameter("create") != null) {
			Integer size = Integer.parseInt(req.getParameter("size"));
			Boolean flag = Boolean.parseBoolean(req.getParameter("flag"));
			Boolean snew = Boolean.parseBoolean(req.getParameter("new"));
			String type = (String) req.getServletContext().getAttribute("type");
			
			Document qry1 = new Document("$match", new Document());
			Document qry2 = new Document("$sample", new Document("size", size));
			
			String[] cr = req.getParameterValues("cr");
			String[] gr = req.getParameterValues("gr");
			
			if (gr != null) ((Document) qry1.get("$match")).append("gr", new Document("$in", Arrays.asList(gr)));
			if (cr != null) ((Document) qry1.get("$match")).append("cr", new Document("$in", Arrays.asList(cr)));
			String login = (String) req.getSession().getAttribute("login");
			((Document) qry1.get("$match")).append(login, new Document("$exists", snew));
			
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");
			
			AggregateIterable<Document> output = coll.aggregate(Arrays.asList(qry1, qry2));
			ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
			for (Document obj : output) {
				String id = obj.getObjectId("_id").toString();
				String rstr = "";
				String lstr = "";
				if (type.equals("gl")) {
					rstr = ((List<String>) obj.get("ru")).toString().replace("[", "").replace("]", "");
					lstr = flag ? obj.getString("gn").concat(" ").concat(obj.getString("de")) : obj.getString("de");
				} else if (type.equals("ddd")) {
					lstr = flag ? obj.getString("de").concat(((List<String>) obj.get("ru")).toString().replace("[", " (").replace("]", ")")) : obj.getString("de");
					rstr = obj.getString("gn");
				} else if (type.equals("mn")) {
					lstr = flag ? obj.getString("de").concat(((List<String>) obj.get("ru")).toString().replace("[", " (").replace("]", ")")) : obj.getString("de");
					rstr = obj.getString("mn");
				}
				
				ArrayList<String> word = new ArrayList<String>();
				word.add(id);
				word.add(lstr);
				word.add(rstr);
				words.add(word);
			}
			mongoClient.close();
			req.setAttribute("words",  words);
			req.getRequestDispatcher("/lw1.jsp").forward(req, res);
		} else if (req.getParameter("start") != null) {
			ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) req.getServletContext().getAttribute("words");
			Collections.shuffle(words);
			req.setAttribute("words",  words);
			req.getRequestDispatcher("/lw2.jsp").forward(req, res);
		} else if (req.getParameter("check") != null) {
			ArrayList<ArrayList<String>> words = (ArrayList<ArrayList<String>>) req.getServletContext().getAttribute("words");
			List<String> otv = Arrays.asList(req.getParameterValues("otv"));
			Boolean gast = req.getSession().getAttribute("role").equals("gast");
			
			if (gast) {
				for (int i = 0; i < words.size(); ++i) {
					Boolean rh = words.get(i).get(2).equals(otv.get(i));
					words.get(i).add(3, otv.get(i));
					words.get(i).add(4, Boolean.toString(rh));
				}
			} else {
				MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
				MongoCollection<Document> coll = mongoClient.getDatabase("deja").getCollection("worte");
				String login = (String) req.getSession().getAttribute("login");

				try {
					for (int i = 0; i < words.size(); ++i) {
						Boolean rh = words.get(i).get(2).equals(otv.get(i));
						words.get(i).add(3, otv.get(i));
						words.get(i).add(4, Boolean.toString(rh));
						
						Document qry1 = new Document("_id", new ObjectId(words.get(i).get(0)));
						Document qry2 = new Document("$inc", new Document(login.concat(".gl.tt"), 1));
						if (rh) {
							((Document) qry2.get("$inc")).append(login.concat(".gl.rh"), 1);
						}
						coll.updateOne(qry1, qry2);
					}
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					mongoClient.close();
				}
			}
			
			req.setAttribute("words",  words);
			req.getRequestDispatcher("/lw3.jsp").forward(req, res);
		} else {
			res.sendRedirect("main");
		}
	}

}
