package outsideCode;

import site.Driver;
import site.SiteServer;
import site.ViewingObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * provides base functionality to all servlets used for the SearchServer
 */
@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	public static final String SEARCHES = "Searches";
	protected static String VISIT_DATE = "Visited";
	protected static final String FOCUS_BREAK = "_";
	protected static final String TIME_BREAK = "|";
	protected static boolean hidden = false;

	/**
	 * Prints out the style sheet onto page because I give up
	 *
	 * @param out
	 */
	protected static void printStyle(PrintWriter out) {
		out.printf("<style>%n");
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(SiteServer.srcPath + "HTML/styles.css")))) {
			String style;
			while ((style = reader.readLine()) != null) {
				out.write(style + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//TODO
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.printf("</style>%n");
	}

	/**
	 * Loads the HTML for the banner that will be at the top of all pages
	 *
	 * @param out {@link PrintWriter} where the HTML is being writeen to
	 */
	protected static void createBanner(PrintWriter out) {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(SiteServer.srcPath + "HTML/Top.html")))) {
			String html;
			while ((html = reader.readLine()) != null) {
				out.printf(html + "%n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//TODO
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prepares the HTTP response by setting the content type and adding header
	 * HTML code.
	 *
	 * @param title    - web page title
	 * @param response - HTTP response
	 * @throws IOException
	 * @see #finishResponse(HttpServletRequest, HttpServletResponse)
	 */
	protected static void prepareResponse(String title, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();

		out.printf("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"");
		out.printf("\"http://www.w3.org/TR/html4/strict.dtd\">%n%n");
		out.printf("<html>%n%n");
		out.printf("<head>%n");
		out.printf("\t<title>%s</title>%n", title);
		printStyle(out);
		out.printf("<content=\"text/html;charset=utf-8\">%n");
		out.printf("</head>%n%n");
		out.printf("<body>%n%n");
	}

	/**
	 * Finishes the HTTP response by adding footer HTML code and setting the
	 * response code.
	 *
	 * @param request  - HTTP request
	 * @param response - HTTP response
	 * @throws IOException
	 */
	protected static void finishResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.printf("%n");
		out.printf("<p style=\"font-size: 10pt; font-style: italic; text-align: center;");
		out.printf("border-top: 1px solid #eeeeee; margin-bottom: 1ex;\">");

		out.printf("Page <a href=\"%s\">%s</a> generated on %s by thread %s. ", request.getRequestURL(),
				request.getRequestURL(), getShortDate(), Thread.currentThread().getName());

		out.printf("</p>%n%n");
		out.printf("</body>%n");
		out.printf("</html>%n");

		out.flush();

		response.setStatus(HttpServletResponse.SC_OK);
		response.flushBuffer();
	}

	/**
	 * Returns the current date and time in a short format.
	 *
	 * @return current date and time
	 * @see #getLongDate()
	 */
	protected static String getShortDate() {
		String format = "yyyy-MM-dd hh:mm a";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(Calendar.getInstance().getTime());
	}

	/**
	 * Returns the current date and time in a long format.
	 *
	 * @return current date and time
	 * @see #getShortDate()
	 */
	protected static String getLongDate() {
		String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}

	/**
	 * Gets the cookies form the HTTP request, and maps the cookie key to the
	 * cookie value.
	 *
	 * @param request - HTTP request from web server
	 * @return map from cookie key to cookie value
	 */
	protected static Map<String, String> getCookieMap(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<>();
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}

		return map;
	}

	/**
	 * Clears all of the cookies included in the HTTP request.
	 *
	 * @param request  - HTTP request
	 * @param response - HTTP response
	 */
	protected static void clearCookies(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	@Override
	protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	@Override
	protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
