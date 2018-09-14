import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.LoggerFactory;
import servlet.HeartBeatServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "500000000");

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new HeartBeatServlet()),"/HeartBeat");


        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context });

        server.setHandler(contexts);

        server.start();
        server.join();
    }
}
