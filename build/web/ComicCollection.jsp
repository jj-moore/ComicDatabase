<%@ include file="/Includes/header.jsp" %>

<form action="ComicServlet">
  <table id="inputForm">
    <tr>
      <td>Title:</td>
      <td><input type="text" name="title" maxlength="40"></td>
      <td>Issue No.</td>
      <td><input type="text" name="issueNum" style="text-align:right;" id="shortText"></td>
      <td><button name="action" type="submit" value="add" id="button">Add New Comic</button></td>
    </tr>
    <tr>
      <td>Cost:</td>
      <td><input type="text" name="cost" style="text-align:right;" id="shortText"></td>
      <td>Issue Date:</td>
      <td><input type="text" name="releaseDate" style="text-align:right;" id="shortText"></td>
      <td><input name="action" type="submit" value="Manage Lists" id="button"></td> 
    </tr>  
    <tr>
      <td>Protagonist:</td>
      <td>
        <select name="protagonistID">
          <c:forEach var="protagonist" items="${comicDatabase.protagonistCollection}">
            <option value="${protagonist.protagonistID}">${protagonist.name}</option>
          </c:forEach>
          <option value="addNew">-Add new option below</option>
        </select>
      </td>
      <td>Story Line</td>
      <td>
        <select name="collectionID">
          <c:forEach var="collection" items="${comicDatabase.collectionCollection}">
            <option value="${collection.collectionID}">${collection.name}</option>
          </c:forEach>
          <option value="addNew">-Add new option below</option>
        </select>
      </td>
      <td><button name="action" type="submit" value="rebuild"  id="button">Rebuild Array</button></td>
    <tr>
      <td></td>
      <td><input type="text" name="addProtagonist" id="newrecord"></td>
      <td></td>
      <td><input type="text" name="addCollection" id="newrecord"></td>
      <td><input name="action" type="submit" value="Logout" id="button"></td>
    </tr>
  </table>
</form>

<p>${comicDatabase.errorMessage}</p>
<hr>

<table>
  <tr><th>Title</th><th>Issue No.</th><th>Issue Date</th>
    <th>Cost</th><th>Protagonist</th><th>Story Line</th><th>Action</th></tr>
      <c:forEach var="comic" items="${comicDatabase.comicCollection}" varStatus="loopData">
    <form action="ComicServlet" id="display_form">
      <tr>
        <td><input name="title" type="text" value="${comic.title}"></td>
        <td><input name="issueNum" type="text" value="${comic.issueNum}" style="text-align:center;" id="shortText"></td>
        <td><input name="releaseDate" type="text" value="${comic.releaseDate}" style="text-align:center;" id="shortText"></td>
        <td><input name="cost" type="text" value="${comic.cost}"  style="text-align:right;" id="shortText"></td>

        <td><select name="protagonistID">
            <c:forEach var="protagonist" items="${comicDatabase.protagonistCollection}">
              <option value="${protagonist.protagonistID}" ${protagonist.protagonistID == comic.protagonistID ? 'selected' : ''}>${protagonist.name}</option>
            </c:forEach>
          </select></td>

        <td><select name="collectionID">
            <c:forEach var="collection" items="${comicDatabase.collectionCollection}">
              <option value="${collection.collectionID}" ${collection.collectionID == comic.collectionID ? 'selected' : ''}>${collection.name}</option>
            </c:forEach>
          </select></td>

        <td><button name="action" type="submit" value="remove" id="arrayButton">Remove</button>
          <button name="action" type="submit" value="update" id="arrayButton">Update</button>
          <input name="arrayIndex" type="hidden" value="${loopData.index}">
          <input name="comicID" type="hidden" value="${comic.comicID}"></td>
      </tr>
    </form>
  </c:forEach>
</table>
<%@ include file="/Includes/footer.html" %>
