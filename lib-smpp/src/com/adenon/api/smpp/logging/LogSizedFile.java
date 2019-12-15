package com.adenon.api.smpp.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;


public class LogSizedFile extends LogCreatorImpl {

    private String fileSize       = "20000KB";
    private int    maxBackupCount = 20;
    private String pattern        = "|%-9r|%d [%-20.20t] %-5p - %m%n";
    private String logDirectory   = "";
    private Level  level          = Level.INFO;


    public LogSizedFile() {
    }

    @Override
    public LoggerWrapper getloggerImpl(final String logName) {
        try {
            final PatternLayout layout = new PatternLayout(this.getPattern());
            RollingFileAppender appender = null;
            appender = new RollingFileAppender(layout, this.getLogDirectory() + logName + ".log", true);
            appender.setName(logName + "FileApp");
            appender.setMaxFileSize(this.getFileSize());
            appender.setMaxBackupIndex(this.getMaxBackupCount());
            final Logger logger = Logger.getLogger(logName);
            logger.setAdditivity(false);
            logger.addAppender(appender);
            logger.setLevel(this.getLevel());
            final LoggerWrapper retlogger = new LoggerWrapper(logger);
            return retlogger;
        } catch (final Exception e) {
            System.err.println(" : Error : " + e.getMessage());
            System.err.println(e);
        }
        return null;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public int getMaxBackupCount() {
        return this.maxBackupCount;
    }

    public LogSizedFile setMaxBackupCount(final int maxBackupCount) {
        this.maxBackupCount = maxBackupCount;
        return this;
    }

    public LogSizedFile setFileSize(final String fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getPattern() {
        return this.pattern;
    }

    public LogSizedFile setPattern(final String pattern) {
        this.pattern = pattern;
        return this;
    }

    public String getLogDirectory() {
        return this.logDirectory;
    }

    public LogSizedFile setLogDirectory(final String logDirectory) {
        this.logDirectory = logDirectory;
        return this;
    }

    public Level getLevel() {
        return this.level;
    }

    public LogSizedFile setLevel(final Level level) {
        this.level = level;
        return this;
    }
}
