<%--
  Created by IntelliJ IDEA.
  User: Semyon
  Date: 23.03.2015
  Time: 0:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
    <title>Draw</title>
    <link href="${pageContext.request.contextPath}/resources/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection">
    <link href="${pageContext.request.contextPath}/resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection">
    <script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.3.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/mocklessons.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/loader.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/errorhandler.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/materialize.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/suggest.js"></script>
</head>
<body>

<nav>
    <div class="nav-wrapper">
        <a href="#" class="brand-logo right"></a>
        <ul id="nav-mobile" class="left hide-on-med-and-down">
            <li><a href="sass.html">Главная</a></li>
            <li><a href="components.html">Темы</a></li>
            <li><a href="javascript.html">Уроки</a></li>
            <li>
                <div class="input-field" style="height:100%;">
                    <div class="container">
                        <div class="helper">
                            <div class="content">
                                <input class="search" id="search" type="text" required="">
                                <div id="suggest"></div>
                                <label for="search"><i class="mdi-action-search"></i></label>
                            </div>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</nav>

<script>
    $(function() {
        var lessonId = ${lessonID};

        //TODO: lesson model

        var curStep = 0;

        var $img = $("#lessonImage");

        $("#back").click(function() {
            loadLesson($img, lessonId, --curStep);
        });
        $("#forward").click(function() {

            loadLesson($img, lessonId, ++curStep);
        });

        loadLesson($img, lessonId, curStep);

    });
</script>

<div class="row">


    <div class="col s12 m12 l12">

        <div class="row">
            <div class="col s12 m12 l12">
                <div class="waves-effect waves-light btn" id="back">Back</div>
                <div class="waves-effect waves-light btn" id="forward">Forward</div>
            </div>
        </div>
        <div class="row">
            <div class="col s12 m12 l12">
                <img id="lessonImage" src="" style="height: 500px; max-width: 100%; margin: 0 auto;">
            </div>
        </div>

    </div>

</div>

</body>
</html>