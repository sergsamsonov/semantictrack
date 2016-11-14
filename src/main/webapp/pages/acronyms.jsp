<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="acrclass" value="active" />
    <jsp:param name="acrlist" value="active" />
    <jsp:param name="hormenu" value="acrmenu" />
</jsp:include>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Acronym</th>
            <th>Interpretation</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${acronyms}" var="acronym">
              <tr>
                  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${acronym.acronym}</td>
                  <td>${acronym.interpret}</td>
                  <td><a href="<c:url value="/acronyms/update/${acronym.id}" />" >Update</td>
                  <td><a href="javascript: deleteAcronym(${acronym.id},'${acronym.acronym}');">Delete</td>
              </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
<jsp:include page="bottom.jsp"/>