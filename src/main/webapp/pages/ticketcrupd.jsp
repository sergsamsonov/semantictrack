<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="tickclass" value="active" />
    <jsp:param name="tickform" value="active" />
</jsp:include>
    <div class="panel-body">
      <c:url var="saveUrl" value="/tickets/save" />
      <form:form modelAttribute="ticketAttribute" method="POST" action="${saveUrl}">
        <c:if test="${ticketAttribute.number != null}">
        <form:hidden path="number" />
        </c:if>
        <div class="form-group">
           <label for="tickstat">Ticket status:</label>
           <form:select class="form-control" path="tickstat" name="tickstat">
             <form:option value="0" label="-- Select status" />
             <form:options items="${tickstatus}" itemValue="id" itemLabel="name" />
           </form:select>
        </div>
        <div class="form-group">
          <label for="issue">Issue:</label>
          <form:input path="issue" name="issue" type="text" class="form-control" id="issue"/>
          <form:errors path="issue" cssClass="error" />
        </div>
        <div class="form-group">
          <label for="issuedescr">Issue description:</label>
          <form:textarea path="issuedescr" class="form-control" rows="5" id="issuedescr"/>
          <form:errors path="issuedescr" cssClass="error" />
        </div>
        <c:if test="${ticketAttribute.number != null}">
            <div class="form-group">
              <label for="solution">Solution:</label>
              <form:input path="solution" name="solution" type="text" class="form-control" id="solution" />
              <form:errors path="solution" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="solutiondet">Solution details:</label>
              <form:textarea path="solutiondet" class="form-control" rows="5" id="solutiondet"/>
              <form:errors path="solutiondet" cssClass="error" />
            </div>
        </c:if>
        <div class="form-group">
           <label for="ticktype">Ticket type:</label>
           <form:select class="form-control" path="ticktype" name="ticktype">
             <form:option value="0" label="-- Select type" />
             <form:options items="${ticktypes}" itemValue="id" itemLabel="name" />
           </form:select>
           <form:errors path="ticktype" cssClass="error" />
        </div>
        <c:if test="${ticketAttribute.number != null}">
            <div class="form-group">
              <label for="srcfiles">Source files:</label>
              <form:textarea path="srcfiles" class="form-control" rows="5" id="srcfiles"/>
            </div>
        </c:if>
        <div class="form-group">
           <label for="task">Current task:</label>
           <form:select class="form-control" path="task" name="task">
             <form:option value="0" label="-- Select task" />
             <form:options items="${tasks}" itemValue="id" itemLabel="name" />
           </form:select>
           <form:errors path="task" cssClass="error" />
        </div>
        <div class="form-group">
           <label for="responsible">Responsible:</label>
           <form:select class="form-control" path="responsible" name="responsible">
             <form:option value="" label="-- Select responsible employee" />
             <c:forEach var="users" items="${users}">
                 <form:option value="${users.id}"><c:out value="${users.firstname} ${users.lastname}"/></form:option>
             </c:forEach>
           </form:select>
           <form:errors path="responsible" cssClass="error" />
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
      </form:form>
    </div>
<jsp:include page="bottom.jsp"/>
