package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ViewingObjectHolder;
import outsideCode.BaseServlet;

public class HomeServlet extends BaseServlet {
	protected final ViewingObjectHolder struct;

	public HomeServlet(ViewingObjectHolder dataStructure) {
		struct = dataStructure;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		prepareResponse("Home", request, response);

		createBanner(out);

		finishResponse(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
