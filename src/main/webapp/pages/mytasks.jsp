<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="tickclass" value="active" />
    <jsp:param name="hormenu" value="srchmenu" />
    <jsp:param name="tasks" value="active" />
</jsp:include>
        <div class="panel-body">
          <b>&nbsp;&nbsp;&nbsp;&nbsp;${taskssum}</b>
        </div>
        <div class="table-responsive">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Number</th>
                <th>Issue</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${tasks}" var="task">
                  <tr>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                          <a href="<c:url value="/tickets/show/${task.number}" />" >${task.number}</a></td>
                      <td>${task.issue}</td>
                  </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
<jsp:include page="bottom.jsp"/>
