package com.assignment.filter;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/Pages/Accounts/*")
public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)  {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            HttpSession session = request.getSession(false);
            //this will store true if user is logged in, false other wise
            boolean loggedIn = session != null && session.getAttribute("infos") != null;
            if (!request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0); // Proxies.
            }

             String loginUri = request.getContextPath() + "/Pages/index.xhtml";

            if (!loggedIn)//if user not logged in, he can't access secured foleder
            {
                if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
                    //if this was from an ajax request 
                    response.setContentType("text/xml");
                    response.getWriter()
                            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                            .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", loginUri);
                } else {
                    response.sendRedirect(loginUri);
                }

            } else {
                chain.doFilter(request, response);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ServletException ex) {
            ex.printStackTrace();
        }
    }	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
