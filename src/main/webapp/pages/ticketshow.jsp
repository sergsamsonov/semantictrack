<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="tickclass" value="active" />
    <jsp:param name="tickform" value="active" />
</jsp:include>
    <div class="panel-body">
      <c:url var="updUrl" value="/tickets/update/${ticketAttribute.number}" />
      <form:form modelAttribute="ticketAttribute" method="GET" action="${saveUrl}">
          <form:hidden path="number" />
          <div class="form-group">
             <label for="tickstat">Ticket status:</label>
             <input readonly="readonly" name="tickstat" type="text" class="form-control" value="${tickstat.name}">
          </div>
          <div class="form-group">
            <label for="issue">Issue:</label>
            <form:input readonly="true" path="issue" name="issue" type="text" class="form-control" id="issue" />
          </div>
          <div class="form-group">
            <label for="issuedescr">Issue description:</label>
            <form:textarea readonly="true" path="issuedescr" class="form-control" rows="5" id="issuedescr"/>
          </div>
          <div class="form-group">
             <label for="solution">Solution:</label>
             <form:input readonly="true" path="solution" name="solution" type="text" class="form-control" id="solution" />
          </div>
          <div class="form-group">
            <label for="solutiondet">Solution details:</label>
            <form:textarea readonly="true" path="solutiondet" class="form-control" rows="5" id="solutiondet"/>
          </div>
          <div class="form-group">
            <label for="srcfiles">Source files:</label>
            <form:textarea readonly="true" path="srcfiles" class="form-control" rows="5" id="srcfiles"/>
          </div>
          <div class="form-group">
             <label for="ticktype">Ticket type:</label>
             <input readonly="readonly" name="ticktype" type="text" class="form-control" value="${ticktype.name}">
          </div>
          <div class="form-group">
             <label for="ticktask">Current task:</label>
             <input readonly="readonly" name="ticktask" type="text" class="form-control" value="${ticktask.name}">
          </div>
          <div class="form-group">
             <label for="resp">Responsible:</label>
             <input readonly="readonly" name="resp" type="text" class="form-control" value="${resp.firstname} ${resp.lastname}">
          </div>
      </form:form>
      <sec:authorize access="hasAnyRole('TICKEDIT')">
        <a href="${updUrl}" class="btn btn-primary">Update</a>
      </sec:authorize>
    </div>
<jsp:include page="bottom.jsp"/>
