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

        <nav>
            <div class="nav-wrapper">
                <a href="#" class="brand-logo right"></a>
                <ul id="nav-mobile" class="left hide-on-med-and-down">
                    <li><a href="/HowToDraw/">Главная</a></li>
                    <li><a href="components.html">Темы</a></li>
                    <li><a href="javascript.html">Уроки</a></li>
                </ul>
            </div>
        </nav>

        <script>
            VK.init(function () {
                // API initialization succeeded 
                // Your code here 
            }, function () {
                // API initialization failed 
                // Can reload page here 
            }, '5.29');
            var lessonId = ${lessonID};
            var curStep = ${stepNum};
            function updateUrl() {
                //Если поставить push state, то "Назад" будет переходить к предыдущему шагу.
                //history.pushState(null, null, "/HowToDraw/#page=lesson&id=" + ${lessonID} + "&step=" + curStep);
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
                    updateUrl();
                }

                function prewStep() {
                    loadLesson($img, lessonId, --curStep);
                    updateUrl();
                }

                updateUrl();
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
                    $("#flood").removeClass("green").addClass("grey");
                    $("#erase").removeClass("green").addClass("grey");
                    switch (mod) {
                        case 'erase':
                            $("#erase").addClass("green").removeClass("grey");
                            break
                        case 'flood':
                            $("#flood").addClass("green").removeClass("grey");
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
                    if (event.keyCode == 65) {
                        prewStep();
                    } else if (event.keyCode == 68) {
                        nextStep();
                    }
                });


            });


        </script>

        <style>
            .outer {
                height: 1000px;
                width: 100%;
                border: 1px solid #4d7198;
                overflow: hidden;
                position: relative;
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

            .color-box {
                display: inline-block;
            }

            .colpick {
                z-index: 10000;
            }

        </style>


        <div class="row">


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

                    </div>
                </div>
                <div class="row">
                    <div class="col s12 m12 l12">
                        <div class="waves-effect waves-light btn" id="back" title="Предыдущий шаг"><</div>
                        <div class="waves-effect waves-light btn grey" id="flood" title="Заливка замкнутой области изображения">Заливка</div>
                        <div class="waves-effect waves-light btn grey" id="erase" title="Режим удаления фрагмента">Ластик</div>

                        <div class="waves-effect waves-light btn color-box" title="Выбрать цвет">Цвет</div>

                        <div class="waves-effect waves-light btn" id="forward" title="Следующий шаг">></div>
                        <input id="layer_name" type="text"/>
                        <div class="waves-effect waves-light btn" id="create_layer" title="Создать слой">Create layer</div>
                        <div class="waves-effect waves-light btn" id="remove_layer" title="Удалить слой">Remove layer</div>
                        <div class="waves-effect waves-light btn" id="clear_layer" title="Удалить слой">Очистить</div>
                        <select id="layers_select"></select>
                    </div>
                </div>
            </div>

        </div>

    </body>
</html>