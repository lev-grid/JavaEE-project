package my.lev.filters;  //196

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class AuthFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("AuthFilter init");
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
	
	private Map<String, String> doAuth(String login, String password) {
		Map<String, String> result = new HashMap<String, String>();
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("users");
		Document user = new Document("_id", login);
		MongoCursor<Document> cursor = null;
		try {
			cursor = collection.find(user).iterator();
			if (cursor.hasNext()) {
				Document doc = cursor.next();
				String salz = doc.getString("salz");
				if (md5(password.concat(salz)).equals(doc.getString("password"))) {
					result.put("code", "ok");
					result.put("name", doc.getString("name"));
					result.put("role", doc.getString("role"));
				} else {
					result.put("code", "no");
				}
			} else {
				result.put("code", "no");
			}
		} catch (Exception e) {
			result.put("code", "no");
		}	finally {
			cursor.close();
			mongoClient.close();
		}
		return result;
		
	}
	
	private Map<String, String> doReg(String name, String login, String password, String rpassword) {
		Map<String, String> result = new HashMap<String, String>();
		if (!password.equals(rpassword)) {
			result.put("code", "no");
			result.put("head", "Ошибка регистрации");
			result.put("disc", "Пароли не совподают");
		} else {
			SecureRandom random = new SecureRandom();
			String salz = new BigInteger(130, random).toString(32);
			String pw = md5(password.concat(salz));
			
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			MongoCollection<Document> collection = mongoClient.getDatabase("deja").getCollection("users");
			Document user = new Document("_id", login).append("name", name).append("role", "user").append("salz", salz).append("password",  pw);
			try {
				collection.insertOne(user);
				result.put("code", "ok");
				result.put("role", "user");
			} catch (MongoWriteException e) {
				result.put("code", "no");
				result.put("head", "Ошибка регистрации");
				result.put("disc", "Данный логин занят");
			} finally {
				mongoClient.close();
			}
		}
		return result;
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("----doAuthFilter----");
		req.setCharacterEncoding("UTF-8");
		HttpServletRequest hreq = (HttpServletRequest) req;
		String path = hreq.getRequestURI();
		System.out.println("----".concat(path));
		
		if (path.startsWith("/deutsch/static/")) {
			chain.doFilter(req, res);
		} else {
			HttpServletResponse hres = (HttpServletResponse) res;
			HttpSession session = hreq.getSession(true);
			String user = (String) session.getAttribute("login");
			if (user != null) {
				if (path.endsWith("auth")) {
					hres.sendRedirect("main");
				} else if (path.endsWith("menu")) {
					if (req.getParameter("exit") != null) {
						session.invalidate();
						hres.sendRedirect("auth");
					} else if (req.getParameter("admin") != null) {
						hres.sendRedirect("admin");
					} else if (req.getParameter("moder") != null) {
						hres.sendRedirect("moder");
					} else if (req.getParameter("user") !=null) {
						hres.sendRedirect("user");
					} else {
						hres.sendRedirect("main");
					}
				} else {
					chain.doFilter(req, res);
				}
			} else {
				if (path.endsWith("auth") || path.endsWith("error")) {
					chain.doFilter(req, res);
				} else if (path.endsWith("main")) {
					if (req.getParameter("btest") != null) {
						session.setAttribute("name", "Гость");
						session.setAttribute("login", "gast");
						session.setAttribute("role", "gast");
						session.setAttribute("password", "----");
						hres.sendRedirect("main");
					} else if (req.getParameter("bauth") != null) {
						String login = req.getParameter("login");
						String password = req.getParameter("password");
						
						Map<String, String> result = doAuth(login, password);
						if (result.get("code").equals("ok")) {
							session.setAttribute("login", login);
							session.setAttribute("role", result.get("role"));
							session.setAttribute("name", result.get("name"));
							hres.sendRedirect("main");
						} else {
							session.setAttribute("head", "Ошибка авторизации");
							session.setAttribute("disc", "Неверный логин или пароль");
							hres.sendRedirect("error");
						}
					} else if (req.getParameter("breg") != null) {
						String name = req.getParameter("name");
						String login = req.getParameter("login");
						String password = req.getParameter("password");
						String rpassword = req.getParameter("rpassword");
						
						Map<String, String> result =  doReg(name, login, password, rpassword);
						if (result.get("code").equals("ok")) {
							session.setAttribute("login", login);
							session.setAttribute("role", result.get("role"));
							session.setAttribute("name", name);
							hres.sendRedirect("main");
						} else {
							session.setAttribute("head", result.get("head"));
							session.setAttribute("disc", result.get("disc"));
							hres.sendRedirect("error");
						}
					} else {
						hres.sendRedirect("auth");
					}
				} else {
					hres.sendRedirect("auth");
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		System.out.println("AuthFilter destroy");
	}
}