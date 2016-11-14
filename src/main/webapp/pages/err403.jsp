<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="top.jsp">
    <jsp:param name="err403" value="active" />
</jsp:include>
   <div class="panel-body">
       <div class="alert alert-danger" role="alert">
           Sorry, but you do not have any rights to access the resource.
       </div>
   </div>
<jsp:include page="bottom.jsp"/>
