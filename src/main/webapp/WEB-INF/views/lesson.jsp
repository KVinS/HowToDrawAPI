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
    <script src="${pageContext.request.contextPath}/resources/js/supapaint.js"></script>
</head>
<body>

<nav>
    <div class="nav-wrapper">
        <a href="#" class="brand-logo right"></a>
        <ul id="nav-mobile" class="left hide-on-med-and-down">
            <li><a href="sass.html">Главная</a></li>
            <li><a href="components.html">Темы</a></li>
            <li><a href="javascript.html">Уроки</a></li>
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

        $.fn.material_select();

        loadLesson($img, lessonId, curStep);

        var paint = new SuperPaint();
        paint.init($("#paint"), $("#layers_select"));

        $("#create_layer").click(function() {
           paint.addLayer($("#layer_name").val());
        });
        $("#remove_layer").click(function() {
            paint.removeLayer($("#layer_name").val());
        });

    });
</script>

<style>
    .outer {
        height: 1000px;
        width: 100%;
        border: 1px solid red;
        overflow: hidden;
        position: relative;
    }
    .inner {
        float: left;
        position: relative;
        left: 50%;
        height: 100%;
    }
    #lessonImage {
        display: block;
        position: relative;
        left: -50%;
        height: 100%;
    }

    #paint {
        position: relative;
        height: 100%;
        width: 100%;
    }

    #paint canvas {
        position: absolute;
        left: 0;
        top: 0;
    }

</style>

<div class="row">


    <div class="col s12 m12 l12">

        <div class="row">
            <div class="col s12 m12 l12">
                <div class="waves-effect waves-light btn" id="back">Back</div>
                <div class="waves-effect waves-light btn" id="forward">Forward</div>
                <input id="layer_name" type="text"/>
                <div class="waves-effect waves-light btn" id="create_layer">Create layer</div>
                <div class="waves-effect waves-light btn" id="remove_layer">Remove layer</div>
                <select id="layers_select"></select>
            </div>
        </div>
        <div class="row">
            <div class="col s12 m12 l12">
                <div class="outer">
                    <div class="inner">
                        <img id="lessonImage" src="">
                    </div>
                    <div id="paint">
                    </div>
                </div>

            </div>
        </div>

    </div>

</div>

</body>
</html>