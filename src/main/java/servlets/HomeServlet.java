package servlets;

import outsideCode.BaseServlet;
import site.DataStructure;
import site.Driver;
import site.ViewingObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class HomeServlet extends BaseServlet {
	protected final List<ViewingObject> display;

	public HomeServlet(List<ViewingObject> toDisplay) {
		display = toDisplay;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		prepareResponse("Home", request, response);

		createBanner(out);
		listVideos(display, out);

		finishResponse(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
