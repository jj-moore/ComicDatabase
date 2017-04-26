package servlets;

import comics.ComicCollection;
import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jdbc.ConnectionPool;

public class LoginServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    ServletContext application = request.getServletContext();
    HttpSession session = request.getSession();
    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
    ComicCollection comicDatabase = new ComicCollection();

    ConnectionPool connectionPool = (ConnectionPool) application.getAttribute("connectionPool");
    try {
      Connection connection = connectionPool.getConnection();
      if (connection != null) {
        Statement statement = connection.createStatement();
        if (statement != null) {
          String sql = "SELECT * FROM users WHERE username='" + request.getParameter("username").toLowerCase()
                  + "' AND password='" + request.getParameter("password") + "';";
          ResultSet results = statement.executeQuery(sql);
          if (results.first()) {
            Cookie cUserID = new Cookie("userID", Integer.toString(results.getInt("userID")));
            session.setAttribute("userID", results.getInt("userID"));
            session.setAttribute("userName", results.getString("userName").substring(0, 1).toUpperCase()
                    + results.getString("userName").substring(1).toLowerCase());
            comicDatabase.buildArray(statement, request);
            application.setAttribute("comicDatabase", comicDatabase);
            cUserID.setMaxAge(2500000);
            response.addCookie(cUserID);
            dispatcher = request.getRequestDispatcher("/ComicCollection.jsp");
          }
          statement.close();
          connectionPool.free(connection);
        }
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
    dispatcher.forward(request, response);
  }

}
