package servlets;

import outsideCode.BaseServlet;
import site.DataStructure;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HomeServlet extends BaseServlet {
	protected final DataStructure struct;

	public HomeServlet(DataStructure dataStructure) {
		struct = dataStructure;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		prepareResponse("Home", request, response);

//		for (int i = 0; i < 4; i++) {
			struct.getDisplay().addAll(struct.getObjects());
//		}

		createBanner(out);
		listVideos(struct.getDisplay(), out);

		finishResponse(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
