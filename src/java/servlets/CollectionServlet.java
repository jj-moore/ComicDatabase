package servlets;

import comics.ComicCollection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdbc.ConnectionPool;

public class CollectionServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    ServletContext application = getServletContext();
    ConnectionPool connectionPool = (ConnectionPool) application.getAttribute("connectionPool");
    Connection connection;
    Statement statement;
    ComicCollection comicDatabase = null;

    try {
      comicDatabase = (ComicCollection) application.getAttribute("comicDatabase");

      connection = connectionPool.getConnection();
      if (connection != null) {
        statement = connection.createStatement();
        if (statement != null) {
          comicDatabase.updateCollections(statement, request);
          statement.close();
          connectionPool.free(connection);
        }
      }
    } catch (SQLException e) {
      comicDatabase.setErrorMessage(e.toString());
    }

    RequestDispatcher dispatcher = application.getRequestDispatcher("/ManageLists.jsp");
    dispatcher.forward(request, response);
  }
}
