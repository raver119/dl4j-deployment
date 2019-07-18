package org.deeplearning4j;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.deeplearning4j.serving.ModelHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

@Slf4j
public class TextServingApp {
    public static void main( String[] args ) throws Exception {
        log.info("Starting TextServing app");

        // initialize before starting jersey
        val holder = ModelHolder.getInstance();

        // first of all we start HTTP server to handle REST API requests
        var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        var server = new Server(8080);
        server.setHandler(context);

        var jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", "org.deeplearning4j.endpoints.Serving");

        // actually start the server
        server.start();
        // run till the end of eternity
        server.join();
    }
}
