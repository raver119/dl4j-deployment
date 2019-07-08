package org.deeplearning4j;

import lombok.val;
import lombok.var;
import org.deeplearning4j.kafka.ConsumerThread;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * This application does two things:
 * 1) Sets up REST API endpoint, which accepts RSS URLs to be fetched and classified
 * 2) Creates Apache Kafka consumer and reads RSS URLs
 */
public class FetcherApp
{
    public static void main( String[] args) throws Exception {
        // first of all we start HTTP server to handle REST API requests
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        var server = new Server(8080);
        server.setHandler(context);

        var jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", "org.deeplearning4j.endpoints.Addresses");

        // actually start the server
        server.start();

        // start Kafka consumer
        val consumerThread = new ConsumerThread();
        consumerThread.start();

        // run till the end of eternity
        server.join();
        consumerThread.join();
    }
}
