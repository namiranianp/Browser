package site;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import data.ViewingObjectHolder;
import outsideCode.WorkQueue;
import servlets.HomeServlet;
import servlets.SeedServlet;

public class SiteServer {
	protected final WorkQueue workers;
	protected final int PORT;
	protected final ViewingObjectHolder struct;

	public static final String srcPath = "/Users/pedramaranian/Code/Browser/";
	public static final String sitePath = "http://localhost/";

	public SiteServer() {
		workers = new WorkQueue(15);
		PORT = 8080;
		struct = new ViewingObjectHolder();
	}

	/**
	 * Creates the server and adds all the correct servlets to it
	 */
	public void begin() {
		Server server = new Server(PORT);

		HomeServlet home = new HomeServlet(struct);
		SeedServlet seeder = new SeedServlet(struct, workers);

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);

		ServletHandler handler = new ServletHandler();

		handler.addServletWithMapping(new ServletHolder(seeder), "/seed");
		handler.addServletWithMapping(new ServletHolder(home), "/");

		server.setHandler(handler);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
