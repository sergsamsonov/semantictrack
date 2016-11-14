<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="acrclass" value="active" />
    <jsp:param name="acrform" value="active" />
    <jsp:param name="hormenu" value="acrmenu" />
</jsp:include>
    <div class="panel-body">
      <c:url var="saveUrl" value="/acronyms/save" />
      <form:form modelAttribute="acronymAttribute" method="POST" action="${saveUrl}">
        <c:if test="${acronymAttribute.id != null}">
        <form:hidden path="id" />
        <form:hidden path="oldacronym" name="oldacronym" class="form-control" id="oldacronym"/>
        </c:if>
        <div class="form-group">
          <label for="acronym">Acronym:</label>
          <form:input path="acronym" name="acronym" type="text" class="form-control" id="acronym"/>
          <form:errors path="acronym" cssClass="error" />
        </div>
        <div class="form-group">
          <label for="interpret">Interpretation:</label>
          <form:input path="interpret" name="interpret" type="text" class="form-control" id="interpret"/>
          <form:errors path="interpret" cssClass="error" />
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
      </form:form>
    </div>
<jsp:include page="bottom.jsp"/>