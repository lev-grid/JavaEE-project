package my.lev;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class vortoj extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
    public vortoj()
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
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		String[] d1 = {"der Januar", "der Februar", "der März", "der April", "der Mai", "der Juni", "der Juli", "der August", "der September", "der Oktober", "der November", "der Dezember"};
		String[] r1 = {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
		
		
		req.setAttribute("r1", r1);
		req.setAttribute("d1", d1);
		req.getRequestDispatcher("/vortoj.jsp").forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		doGet(req, res);
	}

}
