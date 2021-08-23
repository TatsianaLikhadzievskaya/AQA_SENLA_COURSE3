package utils;

import org.apache.log4j.*;


public class Log {

    private static final Logger Log = Logger.getLogger(Log.class);

    public static void trace(String message) { Log.trace(message);}

    public static void trace(String message, Throwable exception) { Log.trace(message, exception);}

    public static void debug(String message) { Log.debug(message);}

    public static void debug(String message, Throwable exception) { Log.debug(message, exception);}

    public static void info(String message) { Log.info(message);}

    public static void info(String message, Throwable exception) { Log.info(message, exception);}

    public static void warn(String message) { Log.warn(message);}

    public static void warn(String message, Throwable exception) { Log.warn(message, exception);}


    public static void error(String message, Throwable throwable){
        Log.error(message, throwable);
        throw new AssertionError(throwable);
    }

    public static void error(String message) {
        Log.error(message);
    }
    public static void error(Exception error){
        Log.error(" --- Exception occurs ---- ", error);
        throw new AssertionError(error);
    }

}
