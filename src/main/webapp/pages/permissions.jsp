<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="admclass" value="active" />
    <jsp:param name="perlist" value="active" />
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
          <c:forEach items="${permissions}" var="permissions">
              <tr>
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${permissions.name}</td>
                  <td>${permissions.description}</td>
                  <td><a href="<c:url value="/admin/permissions/update/${permissions.id}" />" >Update</td>
                  <td><a href="javascript: deletePermiss(${permissions.id},'${permissions.name}');"/>Delete</td>
              </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
<jsp:include page="bottom.jsp"/>