package my.lev.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("RoleFilter init");
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		System.out.println("----doRoleFilter----");
		HttpServletRequest hreq = (HttpServletRequest) req;
		HttpServletResponse hres = (HttpServletResponse) res;
        String path = hreq.getRequestURI();
		System.out.println("----".concat(path));
        String role = (String) hreq.getSession().getAttribute("role");

        boolean admin = path.startsWith("/deutsch/admin") && !role.equals("admin");
        boolean moder = path.startsWith("/deutsch/moder") && !role.equals("admin") && !role.equals("moder");
        
        if (admin || moder) {
        	hres.sendRedirect("main");
        } else {
        	chain.doFilter(req, res);
        }
	}
	
	@Override
	public void destroy() {
		System.out.println("RoleFilter destroy");
	}
}