<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="passwchng" value="active" />
</jsp:include>
    <div class="panel-body">
      <c:url var="saveUrl" value="/profile/setpassword" />
      <form:form modelAttribute="passwchngAttr" method="POST" action="${saveUrl}">
        <div class="form-group">
          <label for="oldPassword">Old password:</label>
          <form:input path="oldPassword" name="oldPassword" type="password" class="form-control" id="oldPassword"/>
          <form:errors path="oldPassword" cssClass="error" />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <form:input path="password" name="password" type="password"  class="form-control" id="password"/>
          <form:errors path="password" cssClass="error" />
        </div>
        <div class="form-group">
          <label for="confirmPassword">Confirm password:</label>
          <form:input path="confirmPassword" name="confirmPassword" type="password" class="form-control" id="confirmPassword"/>
          <form:errors path="confirmPassword" cssClass="error" />
        </div>
        <button type="submit" class="btn btn-primary">Confirm</button>
      </form:form>
    </div>
<jsp:include page="bottom.jsp"/>
