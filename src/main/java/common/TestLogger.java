package common;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static common.Constants.ERROR_PREFIX;

/**
 * Created by ispitkovskyi on 12/2/2015.
 * Configuration file for Log4j logger (log4j.properties) is located in the "resources" directory in "src/main"
 * The "log4j.rootLogger" parameter in that file refers to value of "" variable, which sets Log Level for log4j.
 * In order to customize the Log Level by setting system property, you have to specify log-level value to that system property explicitly, by passing -Dsda.logLevel=DEBUG argument to JVM, BEFORE you start testng.
 *
 * Alternatively, it's possible to change default logging level programmatically by calling "setLogLevel" methods in this class.
 */
public class TestLogger {
    private static final Logger logger = LogManager.getLogger(TestLogger.class.getName());

    public static void setLogLevel(Level level){
        if(level != null)
            //todo: Add ability to change default logging level in preferences
            logger.getRootLogger().setLevel(level);
    }

    public static Level getLogLevel(){
        return logger.getEffectiveLevel();
    }

    public static void resetConfiguration(){
        Logger.getRootLogger().getLoggerRepository().resetConfiguration();
    }

    public static void addAppender(Appender appender){
        logger.addAppender(appender);
    }

    public static void logInfo(String info){
        logger.info(info);
    }

    public static void logWarning(String warning){
        logger.warn(warning);
    }

    public static void logError(String error){
        logger.error(ERROR_PREFIX + error);
    }

    public static void logDebug(String debugInfo){logger.debug(debugInfo);}

    public static void log (Level level, String msg){
        logger.log(level, msg);
    }
}
