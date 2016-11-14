<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="login" value="active" />
</jsp:include>
    <div class="container" style="width: 300px;">
        <c:url var="loginUrl" value="/j_spring_security_check" />
        <form class="form-signin" action="${loginUrl}" method="post">
            <h2 class="form-signin-heading">Please sign in</h2>
            <c:if test="${param.error != null}">
                <h5 class="error">
                    Invalid password or login
                </h5>
            </c:if>
            <input type="text" id="inputEmail" class="form-control" name="j_username" placeholder="Login">
            <input type="password" id="inputPassword" class="form-control" name="j_password" placeholder="Password">
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>
<jsp:include page="bottom.jsp"/>