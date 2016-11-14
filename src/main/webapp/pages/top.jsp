<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>SemanticTrack</title>

    <link href="<c:url value="/pages/css/bootstrap.min.css" />" rel="stylesheet">

    <c:choose>

        <c:when test="${param.login == 'active'}">

        <link href="<c:url value="/pages/css/signin.css" />" rel="stylesheet">

        </c:when>
        <c:otherwise>

        <link href="<c:url value="/pages/css/sweetalert.css" />" rel="stylesheet">

        <link href="<c:url value="/pages/css/dashboard.css" />" rel="stylesheet">

        <c:if test="${param.hormenu == 'admmenu'}">
            <script type="text/javascript">
                function processTickets(){
                    swal({
                           title: 'Are you sure?',
                           text: 'Are you sure you want to process all tickets?',
                           type: 'warning',
                           showCancelButton: true,
                           confirmButtonColor: '#428bca',
                           confirmButtonText: 'Yes, process them',
                           cancelButtonText: 'Cancel'
                         }, function(isConfirm) {
                                 if (isConfirm) {
                                    document.location.href = "${pageContext.request.contextPath}/admin/processtick";
                                 }
                            }
                    );
                }
            </script>
        </c:if>
        <c:choose>
            <c:when test="${param.hormenu == 'srchmenu' || param.hormenu == 'srchres'}">
                <script type="text/javascript">
                    function checkTickets(url){
                        $.ajax({
                                type: "POST",
                                url: "${pageContext.request.contextPath}/tickets/ticketsProcessedCheck",
                                success: function(response){
                                    if (response === "") {
                                       document.location.href = url;
                                    } else {
                                       swal({
                                              title: 'Error!',
                                              text: response,
                                              type: 'error',
                                              confirmButtonColor: '#428bca'
                                            });
                                    };
                                },
                                error: function(e){
                                    swal({
                                           title: 'Error!',
                                           text: e,
                                           type: 'error',
                                           confirmButtonColor: '#428bca'
                                         });
                                }
                            });
                    }
                </script>
            </c:when>
            <c:when test="${param.userlist == 'active'}">
                <script type="text/javascript">
                    function deleteUser(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete user "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel'
                             }, function(isConfirm) {
                                     if (isConfirm) {
                                        document.location.href =
                                        "${pageContext.request.contextPath}/admin/users/delete/"
                                        + id;
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.grouplist == 'active'}">
                <script type="text/javascript">
                    function deleteGroup(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete group "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel',
                               closeOnConfirm: false,
                               closeOnCancel: false
                             }, function(isConfirm) {
                                    if (isConfirm) {
                                        $.ajax({
                                                type: "POST",
                                                url: "${pageContext.request.contextPath}/admin/groups/groupUsageCheck",
                                                data: 'groupid=' + id + '&groupname="' + name + '"',
                                                success: function(response){
                                                    if (response === "") {
                                                       document.location.href =
                                                           "${pageContext.request.contextPath}/admin/groups/delete/" + id;
                                                    } else {
                                                       swal({
                                                              title: 'Error!',
                                                              text: response,
                                                              type: 'error',
                                                              confirmButtonColor: '#428bca'
                                                            });
                                                    };
                                                },
                                                error: function(e){
                                                    swal({
                                                           title: 'Error!',
                                                           text: e,
                                                           type: 'error',
                                                           confirmButtonColor: '#428bca',
                                                         });
                                                }
                                            });
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.perlist == 'active'}">
                <script type="text/javascript">
                    function deletePermiss(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete permission "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel',
                               closeOnConfirm: false,
                               closeOnCancel: false
                             }, function(isConfirm) {
                                    if (isConfirm) {
                                        $.ajax({
                                                type: "POST",
                                                url: "${pageContext.request.contextPath}/admin/permissions/permissUsageCheck",
                                                data: 'permissid=' + id + '&permissname="' + name + '"',
                                                success: function(response){
                                                    if (response === "") {
                                                       document.location.href =
                                                           "${pageContext.request.contextPath}/admin/permissions/delete/"
                                                           + id;
                                                    } else {
                                                       swal({
                                                              title: 'Error!',
                                                              text: response,
                                                              type: 'error',
                                                              confirmButtonColor: '#428bca',
                                                            });
                                                    };
                                                },
                                                error: function(e){
                                                    swal({
                                                           title: 'Error!',
                                                           text: e,
                                                           type: 'error',
                                                           confirmButtonColor: '#428bca',
                                                         });
                                                }
                                            });
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.tasks == 'active'}">
                <script type="text/javascript">
                    function deleteTask(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete task "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel'
                             }, function(isConfirm) {
                                     if (isConfirm) {
                                        document.location.href =
                                        "${pageContext.request.contextPath}/admin/tasks/delete/" + id;
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.ticktypes == 'active'}">
                <script type="text/javascript">
                    function deleteTicketsType(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete tickets type "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel'
                             }, function(isConfirm) {
                                     if (isConfirm) {
                                        document.location.href =
                                        "${pageContext.request.contextPath}/admin/ticktypes/delete/" + id;
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.tickstlist == 'active'}">
                <script type="text/javascript">
                    function deleteTickStatus(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete tickets status "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel'
                             }, function(isConfirm) {
                                     if (isConfirm) {
                                        document.location.href =
                                        "${pageContext.request.contextPath}/admin/ticketstat/delete/"
                                        + id;
                                     }
                                }
                        );
                    }
                </script>
            </c:when>
            <c:when test="${param.acrlist == 'active'}">
                <script type="text/javascript">
                    function deleteAcronym(id,name){
                        swal({
                               title: 'Are you sure?',
                               text: 'Are you sure you want to delete acronym "' + name + '"?',
                               type: 'warning',
                               showCancelButton: true,
                               confirmButtonColor: '#428bca',
                               confirmButtonText: 'Yes, delete it',
                               cancelButtonText: 'Cancel'
                             }, function(isConfirm) {
                                     if (isConfirm) {
                                        document.location.href =
                                        "${pageContext.request.contextPath}/acronyms/delete/" + id;
                                     }
                                }
                        );
                    }
                </script>
                </c:when>
            </c:choose>
        </c:otherwise>
    </c:choose>
  </head>

  <body>
    <c:if test="${param.login != 'active'}">
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">SemanticTrack</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
              <sec:authentication property="principal.firstname"/> <sec:authentication property="principal.lastname"/>
              <span class="caret"></span></a>
              <ul class="dropdown-menu dropdown-menu-left">
                <li><a href="<c:url value="/profile/passwchng" />">Change password</a></li>
                <li><a href="<c:url value="/logout" />">Sign out</a></li>
              </ul>
            </li>
            <li><a href="<c:url value="/help" />">Help</a></li>
            nbsp;
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <c:if test = "${param.passwchng == 'active' || param.passwchngmes == 'active'}">
                <li class="active"><a href="<c:url value="/profile/passwchng" />" />Password change</a></li>
            </c:if>
            <c:if test = "${param.help == 'active'}">
                <li class="active"><a href="#" />Help</a></li>
            </c:if>
            <c:if test = "${param.err403 == 'active'}">
                <li class="active"><a href="#" />Access error</a></li>
            </c:if>
            <sec:authorize access="hasAnyRole('TICKBRW', 'TICKEDIT')">
            <li class="${param.tickclass}"><a href="<c:url value="/tickets/tasks" />">Tickets</a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ACRON')">
            <li class="${param.acrclass}"><a href="<c:url value="/acronyms/list" />">Acronyms </a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ADMIN')">
            <li class="${param.admclass}"><a href="<c:url value="/admin/users/list" />">Administration</a></li>
            </sec:authorize>
          </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
              <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <div id="navbar" class="navbar-collapse collapse">
                      <ul class="nav navbar-nav navbar-left">
                <c:choose>
                    <c:when test="${param.passwchng == 'active' || param.passwchngmes == 'active'}">
                               <li class="active">
                                <a href="<c:url value="#" />">
                                    User <sec:authentication property="principal.username"/>
                                </a>
                               </li>
                             </ul>
                          </div>
                       </div>
                    </c:when>
                    <c:when test="${param.help == 'active'}">
                               <li class="active">
                                <a href="<c:url value="#" />">Contacts</a>
                               </li>
                             </ul>
                          </div>
                       </div>
                    </c:when>
                    <c:when test="${param.err403 == 'active'}">
                               <li class="active">
                                <a href="<c:url value="#" />">Access error</a>
                               </li>
                             </ul>
                          </div>
                       </div>
                    </c:when>
                    <c:when test="${param.tickform == 'active' && ticketAttribute.number != null}">
                               <li class="active"><a href="<c:url value="#" />">Ticket ${ticketAttribute.number}</a></li>
                             </ul>
                          </div>
                       </div>
                    </c:when>
                    <c:when test="${param.hormenu == 'srchmenu' || param.hormenu == 'srchres' || (param.tickform == 'active' && ticketAttribute.number == null)}">
                               <li class="${param.tasks}"><a href="<c:url value="/tickets/tasks" />">My tasks <span class="sr-only">(current)</span></a></li>
                               <sec:authorize access="hasRole('TICKCR')">
                                  <li class="${param.tickform}"><a href="<c:url value="/tickets/create" />">New ticket</a></li>
                               </sec:authorize>
                               <li class="${param.detsrch}"><a href="javascript: checkTickets('<c:url value="/tickets/detsearch" />');">Detailed search</a></li>
                           <c:if test="${param.srchres == 'active'}">
                               <li class="active"><a href="#">Search results</a></li>
                           </c:if>
                           <c:if test="${param.detsrchres == 'active'}">
                               <li class="active"><a href="#">Detailed search results</a></li>
                           </c:if>
                               <a name="searchform" class="navbar-form navbar-left">
                                 <input name="searchString" type="text" class="form-control"
                                    placeholder="Search..." onkeypress="javascript: if(event.keyCode==13){checkTickets('<c:url value="/tickets/search/" />'+this.value);}">
                               </a>
                           </div>
                        </div>
                      </ul>
                    </c:when>
                    <c:when test="${param.hormenu == 'acrmenu'}">
                       <c:choose>
                            <c:when test="${acronymAttribute.id == null}">
                               <li class="${param.acrlist}"><a href="<c:url value="/acronyms/list" />">Acronyms <span class="sr-only">(current)</span></a></li>
                               <li <c:if test="${param.acrform == 'active'}">class="active"</c:if>><a href="<c:url value="/acronyms/create" />">New acronym</a></li>
                              </ul>
                             </div>
                            </div>
                            </c:when>
                            <c:otherwise>
                                <li class="active"><a href="<c:url value="#" />">Acronym ${acronymAttribute.oldacronym}</a></li>
                               </ul>
                              </div>
                             </div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${param.hormenu == 'admmenu'}">
                       <li class="${param.userlist}"><a href="<c:url value="/admin/users/list" />">Users<span class="sr-only">(current)</span></a></li>
                       <c:if test="${param.userlist == 'active' || param.userform == 'active'}">
                           <li <c:if test="${param.userform == 'active' && userAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/users/create" />">
                                New user<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.userform == 'active' && userAttribute.id != null}">
                            <li class="active"><a href="#">User ${userAttribute.oldlogin}</a></li>
                       </c:if>
                       <li class="${param.grouplist}"><a href="<c:url value="/admin/groups/list" />">Groups</a></li>
                       <c:if test="${param.grouplist == 'active' || param.groupform == 'active'}">
                           <li <c:if test="${param.groupform == 'active' && groupAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/groups/create" />">
                                New Group<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.groupform == 'active' && groupAttribute.id != null}">
                            <li class="active"><a href="#">Group ${groupAttribute.oldname}</a></li>
                       </c:if>
                       <li class="${param.perlist}"><a href="<c:url value="/admin/permissions/list" />">Permissions</a></li>
                       <c:if test="${param.perlist == 'active' || param.perform == 'active'}">
                           <li <c:if test="${param.perform == 'active' && permissAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/permissions/create" />">
                                New permission<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.perform == 'active' && permissAttribute.id != null}">
                            <li class="active"><a href="#">Permission ${permissAttribute.oldname}</a></li>
                       </c:if>
                       <li class="${param.tickstlist}"><a href="<c:url value="/admin/ticketstat/list" />">Tickets status</a></li>
                       <c:if test="${param.tickstlist == 'active' || param.tickstform == 'active'}">
                           <li <c:if test="${param.tickstform == 'active' && tickstatAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/ticketstat/create" />">
                                New tickets status<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.tickstform == 'active' && tickstatAttribute.id != null}">
                            <li class="active"><a href="#">Ticket status ${tickstatAttribute.oldname}</a></li>
                       </c:if>
                       <li class="${param.ticktypes}"><a href="<c:url value="/admin/ticktypes/list" />">Tickets types</a></li>
                       <c:if test="${param.ticktypes == 'active' || param.ticktypeform == 'active'}">
                           <li <c:if test="${param.ticktypeform == 'active' && ticktypeAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/ticktypes/create" />">
                                New tickets type<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.ticktypeform == 'active' && ticktypeAttribute.id != null}">
                            <li class="active"><a href="#">Ticket type ${ticktypeAttribute.oldname}</a></li>
                       </c:if>
                       <li class="${param.tasks}"><a href="<c:url value="/admin/tasks/list" />">Tasks</a></li>
                       <c:if test="${param.tasks == 'active' || param.taskform == 'active'}">
                           <li <c:if test="${param.taskform == 'active' && taskAttribute.id == null}">class="active"</c:if>>
                            <a href="<c:url value="/admin/tasks/create" />">
                                New task<span class="sr-only">(current)</span>
                            </a>
                           </li>
                       </c:if>
                       <c:if test="${param.taskform == 'active' && taskAttribute.id != null}">
                            <li class="active"><a href="#">Task ${taskAttribute.oldname}</a></li>
                       </c:if>
                       <li class="${param.proctick}"><a href="javascript: processTickets();">Process tickets</a></li>
                      </ul>
                     </div>
                    </div>
                </c:when>
            </c:choose>
        </c:if>