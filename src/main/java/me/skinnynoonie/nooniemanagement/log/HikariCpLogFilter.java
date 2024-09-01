package me.skinnynoonie.nooniemanagement.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public final class HikariCpLogFilter extends AbstractFilter {
    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            Logger logger = (Logger) LogManager.getRootLogger();
            logger.addFilter(new HikariCpLogFilter());
            initialized = true;
        }
    }

    private HikariCpLogFilter() {
    }

    @Override
    public Result filter(LogEvent event) {
        if (event != null && event.getLoggerName().contains("me.skinnynoonie.nooniemanagement.hikari")) {
            return Result.DENY;
        } else {
            return Result.NEUTRAL;
        }
    }
}
