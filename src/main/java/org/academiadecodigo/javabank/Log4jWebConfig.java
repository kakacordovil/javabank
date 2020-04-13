package org.academiadecodigo.javabank;

import org.apache.logging.log4j.web.Log4jServletContextListener;
import org.apache.logging.log4j.web.Log4jWebSupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A {@link ServletContextListener} responsible for setting the configuration for Log4j2's log4j-web module
 */
public class Log4jWebConfig implements ServletContextListener {

    public static final String LOGGER_BASE_PATH = "WEB-INF/log4j2/log4j2";
    private Log4jServletContextListener listener = new Log4jServletContextListener();

    /**
     * Sets the configuration file for log4j-web module according to the active Spring profile
     *
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String activeProfile = event.getServletContext().getInitParameter("spring.profiles.active");

        String loggerPath = LOGGER_BASE_PATH + (activeProfile.equals("prod") ? ".xml" : "-" + activeProfile + ".xml");

        event.getServletContext().setInitParameter(Log4jWebSupport.LOG4J_CONFIG_LOCATION, loggerPath);
        listener.contextInitialized(event);
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        listener.contextDestroyed(event);
    }
}
