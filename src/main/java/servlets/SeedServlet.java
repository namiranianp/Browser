package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ViewingObjectHolder;
import outsideCode.BaseServlet;
import outsideCode.WorkQueue;
import site.SeedWorker;

public class SeedServlet extends BaseServlet {
	protected final ViewingObjectHolder struct;
	protected final WorkQueue workers;

	public SeedServlet(ViewingObjectHolder viewingObjects, WorkQueue workQueue) {
		struct = viewingObjects;
		workers = workQueue;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		prepareResponse("Seed", request, response);

		out.printf("<script language=\"javascript\">%n");
		out.printf("\twindow.location.href = \"/home\"%n");
		out.printf("</script>%n");

		workers.execute(new SeedWorker(workers, request.getParameter("seed"), struct));

		finishResponse(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
