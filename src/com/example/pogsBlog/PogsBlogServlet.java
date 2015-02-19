package com.example.pogsBlog;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PogsBlogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//resp.setContentType("text/plain");
		resp.sendRedirect("/index.jsp");
	}
}
