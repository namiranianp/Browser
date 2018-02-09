package debugger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {
	private static boolean DEBUG = true;

	private static PrintWriter writer;

	public static void assignFile(String loc) {
		try {
			writer = new PrintWriter(loc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void write(String message) {
		if (DEBUG) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();

			writer.write(dateFormat.format(date) + " : " + message + "\n");
			writer.flush();
		}
	}
}
