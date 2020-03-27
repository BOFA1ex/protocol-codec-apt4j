package com.bofa.commons.apt4j.management.internal.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author bofa1ex
 * @version 1.0
 * @since  2020/3/3
 */
public interface JavaModelConstant {

    /**
     * System properties
     */
    String JAVA_VERSION = System.getProperty("java.version");
    String JAVA_HOME = System.getProperty("java.home");
    String JAVA_CLASSPATH = System.getProperty("java.class.path");
    String OS_NAME = System.getProperty("os.name");
    String FILE_SEPARATOR = System.getProperty("file.separator");
    String PATH_SEPARATOR = System.getProperty("path.separator");
    String LINE_SEPARATOR = System.getProperty("line.separator");
    String USER_NAME = System.getProperty("user.name");
    String USER_HOME = System.getProperty("user.home");
    String USER_DIR = System.getProperty("user.dir");

    /**
     * date format pattern
     */
    String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    String YYYYMMDDHH = "yyyy-MM-dd HH";
    String YYYYMMDD = "yyyy-MM-dd";
    String YYYYMM = "yyyy-MM";
    String YYYY = "yyyy";

    /**
     * javadoc template
     */
    String JAVADOC_START = "/**";
    String JAVADOC_SEPARATOR = "*";
    String JAVADOC_END = "*/";

    String DEFAULT_JAVADOC_TEMPLATE =
            String.format(
            "%1$3s " + "\n" +
                    " %2$2s generate by freemarker" + "\n" +
                    " %2$2s \n" +
                    " %2$2s @author\t%4$s" + "\n" +
                    " %2$2s @since\t%5$s"  + "\n" +
            " %3$3s",
            JAVADOC_START, JAVADOC_SEPARATOR, JAVADOC_END,
            USER_NAME, LocalDateTime.now().format(DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS)));
}
