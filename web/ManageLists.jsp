<%@ include file="/Includes/header.jsp" %>
<div class="manageLists">
  <form action="ComicCollection.jsp">
    <input type="submit" value="Return to Collection" id="listReturn">
  </form>
  <div class="listTables">
    <table id="protagonist">
      <form action="ProtagonistServlet">
        <tr><th>Add new protagonist:</th></tr>
        <tr>
          <td><input type="text" name="newProtagonist" id="listNew"</td>
          <td><input name="action" type="submit" value="add" id="listButton"></td>
        </tr>
      </form>
      <c:forEach var="protagonist" items="${comicDatabase.protagonistCollection}" varStatus="loopData">
        <form action="ProtagonistServlet">
          <tr>
            <td><input type="text" name="protagonist" value="${protagonist.name}"></td>
            <td><button name="action" type="submit" value="remove">Remove</button>
              <button name="action" type="submit" value="update">Update</button>
              <input name="protagonistIndex" type="hidden" value="${loopData.index}">
              <input name="protagonistID" type="hidden" value="${protagonist.protagonistID}"></td>
          </tr>
        </form>
      </c:forEach>
    </table>

    <table id="collection">
      <form action="CollectionServlet">
        <tr><th>Add new story line:</th></tr>
        <tr>
          <td><input type="text" name="newCollection" id="listNew"</td>
          <td><input name="action" type="submit" value="add" id="listButton"></td>
        </tr>
      </form>
      <c:forEach var="collection" items="${comicDatabase.collectionCollection}" varStatus="loopData">
        <form action="CollectionServlet">
          <tr>
            <td><input type="text" name="collection" value="${collection.name}"></td>
            <td><button name="action" type="submit" value="remove">Remove</button>
              <button name="action" type="submit" value="update">Update</button>
              <input name="collectionIndex" type="hidden" value="${loopData.index}">
              <input name="collectionID" type="hidden" value="${collection.collectionID}"></td>
          </tr>
        </form>
      </c:forEach>
    </table>
  </div>
</div>
<%@ include file="/Includes/footer.html" %>
