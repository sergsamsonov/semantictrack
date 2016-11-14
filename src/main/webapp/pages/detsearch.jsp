<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="tickclass" value="active" />
    <jsp:param name="hormenu" value="srchmenu" />
    <jsp:param name="detsrch" value="active" />
</jsp:include>
    <div class="panel-body">
      <c:url var="searchUrl" value="/tickets/getsearchres.htm" />
        <form:form id="detsrchform" modelAttribute="searchAttr" method="POST" action="${searchUrl}">
            <div class="form-group">
              <label for="request">Request:</label>
              <form:input path="request" name="request" type="text" class="form-control" id="request"/>
              <form:errors path="request" cssClass="error" />
            </div>
            <label for="simfield">Calculate similarity between request and field of:</label>
            <div class="radio">
              <label><form:radiobutton path="simfield" value="issue"/> Issue</label>
            </div>
            <div class="radio">
              <label><form:radiobutton path="simfield" value="issuedescr"/> Issue description</label>
            </div>
            <div class="radio">
              <label><form:radiobutton path="simfield" value="solution"/> Solution</label>
            </div>
            <div class="radio">
              <label><form:radiobutton path="simfield" value="solutiondet"/> Solution description</label>
            </div>
            <form:errors path="simfield" cssClass="error" />
            <br>
            <br>
            <button type="submit" class="btn btn-primary">Search</button>
        </form:form>
    </div>
<jsp:include page="bottom.jsp"/>
