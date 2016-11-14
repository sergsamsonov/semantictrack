<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="admclass" value="active" />
    <jsp:param name="userlist" value="active" />
    <jsp:param name="hormenu" value="admmenu" />
</jsp:include>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Login</th>
            <th>First name</th>
            <th>Middle name</th>
            <th>Last name</th>
            <th>E-mail</th>
            <th>Groups</th>
            <th>Status</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="users" items="${users}">
              <tr>
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${users.login}</td>
                  <td>${users.firstname}</td>
                  <td>${users.midname}</td>
                  <td>${users.lastname}</td>
                  <td>${users.email}</td>
                  <td>${users.grnames}</td>
                  <td>
                    <c:choose>
                        <c:when test="${users.enabledval == 1}">
                        Enabled
                        </c:when>
                        <c:otherwise>
                        Disabled
                        </c:otherwise>
                    </c:choose>
                  </td>
                  <td><a href="<c:url value="/admin/users/update/${users.id}" />" >Update</td>
                  <td><a href="javascript: deleteUser(${users.id},'${users.login}');"/>Delete</td>
              </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
<jsp:include page="bottom.jsp"/>