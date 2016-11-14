<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="admclass" value="active" />
    <jsp:param name="tickstlist" value="active" />
    <jsp:param name="hormenu" value="admmenu" />
</jsp:include>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Name</th>
            <th>Description</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${tickstat}" var="tickstat">
              <tr>
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${tickstat.name}</td>
                  <td>${tickstat.description}</td>
                  <td><a href="<c:url value="/admin/ticketstat/update/${tickstat.id}" />" >Update</td>
                  <td><a href="javascript: deleteTickStatus(${tickstat.id},'${tickstat.name}');"/>Delete</td>
              </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
<jsp:include page="bottom.jsp"/>