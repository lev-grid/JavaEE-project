package my.lev;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class testcheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public testcheck() {
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		System.out.println("testcheck init");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.sendRedirect("auth");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.getWriter().append("Served at: ").append(req.getContextPath());
		req.getSession().setAttribute("login", "Гость");
		req.getSession().setAttribute("email", "----");
		req.getSession().setAttribute("password", "----");
		res.sendRedirect("main");
	}
}
