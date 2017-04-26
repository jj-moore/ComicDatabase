package jdbc;

import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GlobalConnectionPool implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    ConnectionPool connectionPool = new ConnectionPool();

    String userid = servletContext.getInitParameter("db_userid");
    String password = servletContext.getInitParameter("db_password");

    // The following could be configurables in web.xml, but for simplicity I have them hard-coded
    int initialConnections = 3;
    int maxConnections = 20;
    boolean waitIfBusy = true;

    // Initialize the Connection Pool
    try {
      connectionPool.CreateConnectionPool(userid, password,
              initialConnections, maxConnections, waitIfBusy);
      servletContext.setAttribute("connectionPool", connectionPool);
      System.out.println("GlobalConnectionPool has setup the connection pool");
    } catch (SQLException e) {
      System.out.println("Init SQLException caught: " + e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext servletContext = sce.getServletContext();
    ConnectionPool connectionPool = (ConnectionPool) servletContext.getAttribute("connectionPool");
    
    if (connectionPool != null) {
      connectionPool.closeAllConnections();
      System.out.println("GlobalConnectionPool closed out the connection pool");
    }
  }
}
