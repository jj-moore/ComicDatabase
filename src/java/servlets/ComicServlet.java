package servlets;

import comics.ComicCollection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.ConnectionPool;

public class ComicServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    ServletContext application = getServletContext();
    HttpSession session = request.getSession();
    ComicCollection comicDatabase = null;

    switch (request.getParameter("action")) {
      case "Manage Lists":
        response.sendRedirect("/JH7_jjmoore/ManageLists.jsp");
        break;
      case "Logout":
        Cookie cookie = new Cookie("userID", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        session.removeAttribute("userID");
        session.removeAttribute("userName");
        response.sendRedirect("/JH7_jjmoore/index.jsp");
        break;
      case "add":
      case "remove":
      case "update":
      case "rebuild":
        try {
          ConnectionPool connectionPool = (ConnectionPool) application.getAttribute("connectionPool");
          Connection connection;
          Statement statement;

          comicDatabase = (ComicCollection) application.getAttribute("comicDatabase");
          if (comicDatabase == null) {
            comicDatabase = new ComicCollection();
            application.setAttribute("comicDatabase", comicDatabase);
          }

          connection = connectionPool.getConnection();
          if (connection != null) {
            statement = connection.createStatement();
            if (statement != null) {
              comicDatabase.updateCollection(statement, request);
              statement.close();
              connectionPool.free(connection);
            }
          }
        } catch (SQLException e) {
          comicDatabase.setErrorMessage(e.toString());
        }
        RequestDispatcher dispatcher = application.getRequestDispatcher("/ComicCollection.jsp");
        dispatcher.forward(request, response);
        break;
    }

  }
}
