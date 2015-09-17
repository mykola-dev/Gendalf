/*
package ds.gendalf;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class L {

	private static final String LOG_NAME = "test.log";
	private static Logger logger = Logger.getLogger(LOG_NAME);
	private static FileHandler fh;

	static {

		try {

			fh = new FileHandler("d:\\" + LOG_NAME, true);
			logger.addHandler(fh);
			fh.setFormatter(new SimpleFormatter());


		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void v(String s, Object... args) {
		logger.log(Level.INFO, String.format(s, args));
	}


	public static void close() {
		fh.close();
	}
}
*/
