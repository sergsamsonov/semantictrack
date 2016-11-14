<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<jsp:include page="top.jsp">
    <jsp:param name="admclass" value="active" />
    <jsp:param name="userform" value="active" />
    <jsp:param name="hormenu" value="admmenu" />
</jsp:include>
    <div class="panel-body">
      <c:url var="saveUrl" value="/admin/users/save" />
        <form:form modelAttribute="userAttribute" method="POST" action="${saveUrl}">
            <c:if test="${userAttribute.id != null}">
                <form:hidden path="id" name="id" class="form-control" id="id"/>
                <form:hidden path="oldlogin" name="oldlogin" class="form-control" id="oldlogin"/>
            </c:if>
            <div class="form-group">
              <label for="login">Login:</label>
              <form:input path="login" name="login" type="text" class="form-control" id="login"/>
              <form:errors path="login" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="firstname">First name:</label>
              <form:input path="firstname" name="firstname" type="text" class="form-control" id="firstname"/>
              <form:errors path="firstname" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="midname">Middle name:</label>
              <form:input path="midname" name="midname" type="text" class="form-control" id="midname"/>
              <form:errors path="midname" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="lastname">Last name:</label>
              <form:input path="lastname" name="lastname" type="text" class="form-control" id="lastname"/>
              <form:errors path="lastname" cssClass="error" />
            </div>
            <div class="form-group">
              <label for="email">Email:</label>
              <form:input path="email" name="email" type="text" class="form-control" id="email"/>
              <form:errors path="email" cssClass="error" />
            </div>
            <div class="form-group">
                <label for="grlist">Groups:</label>
                <form:select class="form-control"  multiple="multiple" path="grlist">
                    <form:options items="${groups}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="grlist" cssClass="error" />
            </div>
            <label for="enabledval">Status:</label>
            <div class="radio">
              <label><form:radiobutton path="enabledval" value="1"/> Enabled</label>
            </div>
            <div class="radio">
              <label><form:radiobutton path="enabledval" value="0"/> Disabled</label>
            </div>
            <br>
            <form:errors path="enabledval" cssClass="error" />
            <c:choose>
                <c:when test="${userAttribute.id == null}">
                    <div class="form-group">
                      <label for="password">Password:</label>
                      <form:password path="password" name="password" class="form-control" id="password"/>
                      <form:errors path="password" cssClass="error" />
                    </div>
                    <div class="form-group">
                      <label for="confirmPassword">Confirm password:</label>
                      <form:password path="confirmPassword" name="confirmPassword" class="form-control" id="confirmPassword"/>
                      <form:errors path="confirmPassword" cssClass="error" />
                    </div>
                </c:when>
                <c:otherwise>
                    <form:hidden path="password" name="password" class="form-control" id="password"/>
                </c:otherwise>
            </c:choose>
            <button type="submit" class="btn btn-primary">Save</button>
        </form:form>
    </div>
<jsp:include page="bottom.jsp"/>
