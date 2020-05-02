package pt.isel.ls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInterface {

    /**
     * - A logger is typically a private static member of any class that uses logging.
     * - Having the same logger instance be shared by multiple instance is safe because
     * the logger is thread safe.
     * - Typically the logger instance is obtained via a LoggerFactory, given the
     * classes instance for the hosting class.
     */
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void log(String txt) {
        log.info(txt);
    }

    public static void msg(String info) {
        System.out.println(info);
    }

    public static void info(String log, String msg) {
        log(log);
        msg(msg);
    }
}
