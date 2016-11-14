<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="tickclass" value="active" />
    <jsp:param name="hormenu" value="srchmenu" />
    <jsp:param name="detsrchres" value="active" />
</jsp:include>
    <div class="panel-body">
      <b>&nbsp;&nbsp;&nbsp;&nbsp;${searchsum}</b>
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
            <c:forEach items="${tickets}" var="ticket">
                <tr>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value="/tickets/show/${ticket.number}" />" >${ticket.number}</a></td>
                    <td>${ticket.issue}</td>
                </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
  </div>
<jsp:include page="bottom.jsp"/>
