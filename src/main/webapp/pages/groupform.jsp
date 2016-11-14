<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="admclass" value="active" />
    <jsp:param name="groupform" value="active" />
    <jsp:param name="hormenu" value="admmenu" />
</jsp:include>
    <div class="panel-body">
      <c:url var="saveUrl" value="/admin/groups/save" />
        <form:form modelAttribute="groupAttribute" method="POST" action="${saveUrl}">
            <c:if test="${groupAttribute.id != null}">
            <form:hidden path="id" name="id" class="form-control" id="id"/>
            <form:hidden path="oldname" name="oldname" class="form-control" id="oldname"/>
            </c:if>
            <div class="form-group">
              <label for="name">Name:</label>
              <form:input path="name" name="name" type="text" class="form-control" id="name"/>
              <form:errors path="name" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="description">Description:</label>
              <form:input path="description" name="description" type="text" class="form-control" id="description"/>
              <form:errors path="description" cssClass="error" />
            </div>
            <div class="form-group">
                <label for="perlist">Permissions:</label>
                <form:select class="form-control"  multiple="multiple" path="perlist">
                    <form:options items="${permissions}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="perlist" cssClass="error" />
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form:form>
    </div>
<jsp:include page="bottom.jsp"/>
