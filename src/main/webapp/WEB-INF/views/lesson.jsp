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
        <link href="${pageContext.request.contextPath}/resources/css/colpick.css" type="text/css" rel="stylesheet" media="screen,projection">
        <script src="${pageContext.request.contextPath}/resources/js/jquery-2.1.4.min.js"></script>

        <script src="${pageContext.request.contextPath}/resources/js/mocklessons.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/loader.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/errorhandler.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/materialize.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/colpick.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/supapaint.js"></script>


        <script src="//vk.com/js/api/xd_connection.js?2"  type="text/javascript"></script>
    </head>
    <body>

        <script>
            function onScrollTop(a, b, c,d){
                console.log('СКРОЛЛ='+a+'>Высота ЭКРАНА='+b+'>ДО ИФРЕЙМА='+c+'>ХЗ='+d);
                VK.callMethod("resizeWindow",1000,b-c);
            }
            function newSizeWindow() {
                VK.callMethod('scrollTop');
            }

            VK.init(function () {
                // API initialization succeeded
                // Your code here
                VK.addCallback("onScrollTop",onScrollTop);
                setInterval(newSizeWindow, 1000);
            }, function () {
                // API initialization failed 
                // Can reload page here 
            }, '5.29');
            var lessonId = ${lessonID};
            var curStep = ${stepNum};
            function updateStep() {
                //Если поставить push state, то "Назад" будет переходить к предыдущему шагу.
                //history.pushState(null, null, "/HowToDraw/#page=lesson&id=" + ${lessonID} + "&step=" + curStep);

                $("#lessonTitle").html("Неизвестный урок. Шаг "+(curStep+1)+" из ХЗСКОЛЬКИ");
                history.replaceState({}, '', "/HowToDraw/#page=lesson&id=" + ${lessonID} + "&step=" + curStep);
                VK.callMethod("setLocation", "page=lesson&id=" + ${lessonID} + "&step=" + curStep, false);
            }




            $(function () {
                //TODO: lesson model
                //TODO: Вызывать на back popstate
                var $img = $("#lessonImage");
                $("#back").click(function () {
                    prewStep()

                });
                $("#forward").click(function () {
                    nextStep()
                });

                function nextStep() {
                    loadLesson($img, lessonId, ++curStep);
                    updateStep();
                }

                function prewStep() {
                    loadLesson($img, lessonId, --curStep);
                    updateStep();
                }

                updateStep();
                $.fn.material_select();
                loadLesson($img, lessonId, curStep);
                var paint = new SuperPaint();
                paint.init($("#paint"), $("#layers_select"));
                $("#create_layer").click(function () {
                    paint.addLayer($("#layer_name").val());
                });
                $("#clear_layer").click(function () {
                    paint.cleareLayer();
                });
                function updateMod(mod) {
                    $("#flood").removeClass("vk-toggle").addClass("grey");
                    $("#erase").removeClass("vk-toggle").addClass("grey");
                    switch (mod) {
                        case 'erase':
                            $("#erase").addClass("vk-toggle").removeClass("grey");
                            break
                        case 'flood':
                            $("#flood").addClass("vk-toggle").removeClass("grey");
                            break;
                    }
                }

                $("#flood").click(function () {
                    paint.toggleMod("flood");
                    updateMod(paint.getMod());
                });
                $("#erase").click(function () {
                    paint.toggleMod("erase");
                    updateMod(paint.getMod());
                });
                $("#remove_layer").click(function () {
                    paint.removeLayer($("#layer_name").val());
                });
                paint.setColor("ff8800", {r: 255, g: 136, b: 0, a: 255});
                $('.color-box').colpick({
                    colorScheme: 'dark',
                    layout: 'rgbhex',
                    color: 'ff8800',
                    onSubmit: function (hsb, hex, rgba, el) {
                        $(el).css('background-color', '#' + hex);
                        $(el).css('background-color', 'rgba(' + rgba.r + ', ' + rgba.g + ', ' + rgba.b + ', ' + rgba.a / 255 + ')');
                        $(el).colpickHide();
                        paint.setColor(hex, rgba);
                    }
                }).css('background-color', '#ff8800');

                $(document).keyup(function (event) {
                    if (event.keyCode == 65 || event.keyCode == 37) {
                        prewStep();
                    } else if (event.keyCode == 68 || event.keyCode == 39) {
                        nextStep();
                    }
                });


            });


        </script>

        <style>
            .outer {
                width: 100%;
                overflow: hidden;
                position: relative;
                background: white;
                top: -1px;
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

            .colpick {
                z-index: 10000;
            }
        </style>


        <div class="row">
            <div class="row col s2 m2 l2" id = "menu">
                <ul class="collection">
                    <li class="collection-item">Меню</li>
                    <a href="/HowToDraw/" class="menu-item waves-effect waves-light btn vk">Назад</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn vk">Сохранить</a>
                    <li class="collection-item">Инструменты</li>
                    <a href="#!" class="menu-item waves-effect waves-light btn grey">Карандаш</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn vk-toggle">Кисть</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn grey" id="flood" title="Заливка замкнутой области изображения">Заливка</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn grey" id="erase" title="Режим удаления фрагмента">Ластик</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn color-box">Цвет</a>
                    <li class="collection-item">Слой</li>
                    <input id="layer_name" class="hidden menu-item" type="text"/>
                    <select id="layers_select" class="browser-default menu-item waves-effect waves-light btn vk"></select>
                    <a href="#!" class="hidden menu-item waves-effect waves-light btn vk" id="create_layer" title="Создать слой">Создать</a>
                    <a href="#!" class="hidden menu-item waves-effect waves-light btn vk" id="remove_layer" title="Удалить слой">Удалить</a>
                    <a href="#!" class="menu-item waves-effect waves-light btn vk" id="clear_layer" title="Очистить слой">Очистить</a>
                </ul>
            </div>

            <div class="row col s10 m10 l10" id = "workspace">
            <div class="col s12 m12 l12">
                <div class="row">
                    <div class="col s12 m12 l12">
                        <div class="outer">
                            <div class="inner">
                                <img id="lessonImage" src="">
                            </div>
                            <div id="paint" >
                            </div>
                        </div>
                        <div class="row" style="position: relative; top: -36px;">
                            <div class="waves-effect waves-light btn vk col s2 m2 l2" id="back" title="Предыдущий шаг"><</div>
                            <h6 class="col s8 m8 l8 center" id="lessonTitle"></h6>
                            <div class="waves-effect waves-light btn vk col s2 m2 l2" id="forward" title="Следующий шаг">></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col s12 m12 l12">
                    </div>
                </div>
            </div>
        </div>
        </div>

    </body>
</html>