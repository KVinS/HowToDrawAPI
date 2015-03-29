<%--
  Created by IntelliJ IDEA.
  User: Semyon
  Date: 22.02.2015
  Time: 20:39
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
            VK.init(function () {
                VK.addCallback('onLocationChanged', onLocationChanged);
                // API initialization succeeded 
                // Your code here 
            }, function () {
                // API initialization failed 
                // Can reload page here 
            }, '5.29');

            function onLocationChanged(location) {
                if (location) {
                    selectPage(location);
                }
            }

            function selectPage(ahref) {
                if (ahref !== undefined) {
                    var arr = getUrlVars(ahref);
                    switch (arr["page"]) {
                        case "search" :
                            $("#search").val(decodeURI(arr["q"]));
                            query(decodeURI(arr["q"]), arr["page"]);
                            break;
                        case "lesson":
                            $(location).attr('href', "/HowToDraw/lesson?lessonID=" + arr["id"] + "&step=" + arr["step"]);
                            break;
                    }
                }
            }

            function getUrlVars(s)
            {
                var vars = [], hash;
                var hashes = (s).split('&');
                for (var i = 0; i < hashes.length; i++)
                {
                    hash = hashes[i].split('=');
                    vars.push(hash[0]);
                    vars[hash[0]] = hash[1];
                }
                return vars;
            }

            $(function () {
                initLoader();
                loadPopular();
                loadNew();

                
                selectPage(location.hash.toString().split("#")[1]);
                window.addEventListener("popstate", function (e) {
                    selectPage(location.hash.toString().split("#")[1]);
                }, false);

                var $searchBar = $("#search");
                $searchBar.on("keydown", function (e) {
                    if (e.keyCode == 13) {
                        query($searchBar.val(), 0);
                    } else {
                        suggestion($searchBar.val());
                    }
                });

                var $searchResults = $("#search-results");
                var $searchResultsContainer = $("#search-results-container");
                $("#close-search").click(function () {
                    $searchResultsContainer.addClass("hidden");
                });
            });
        </script>

        <div class="row">


            <div class="col s12 m12 l9">


                <div class="row hidden" id="search-results-container">

                    <div class="col s12 m12" style="position: relative">
                        <h4 class="light">Результаты поиска</h4>
                        <h6 class="light" id="page-number"></h6>
                        <i id="close-search" class="close-btn navigation-btn mdi-navigation-close right" style="margin-top: 12px;"></i>

                    </div>


                    <div class="col s12 m4 l12 center" id="preloader">

                        <div class="preloader-wrapper small active">
                            <div class="spinner-layer spinner-blue">
                                <div class="circle-clipper left">
                                    <div class="circle"></div>
                                </div><div class="gap-patch">
                                    <div class="circle"></div>
                                </div><div class="circle-clipper right">
                                    <div class="circle"></div>
                                </div>
                            </div>

                            <div class="spinner-layer spinner-red">
                                <div class="circle-clipper left">
                                    <div class="circle"></div>
                                </div><div class="gap-patch">
                                    <div class="circle"></div>
                                </div><div class="circle-clipper right">
                                    <div class="circle"></div>
                                </div>
                            </div>

                            <div class="spinner-layer spinner-yellow">
                                <div class="circle-clipper left">
                                    <div class="circle"></div>
                                </div><div class="gap-patch">
                                    <div class="circle"></div>
                                </div><div class="circle-clipper right">
                                    <div class="circle"></div>
                                </div>
                            </div>

                            <div class="spinner-layer spinner-green">
                                <div class="circle-clipper left">
                                    <div class="circle"></div>
                                </div><div class="gap-patch">
                                    <div class="circle"></div>
                                </div><div class="circle-clipper right">
                                    <div class="circle"></div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div id="search-results">

                    </div>

                    <div class="row" id="search-pagination">

                    </div>

                </div>

                <div class="row" id="new">

                    <div class="col s12 m12">
                        <h4 class="light">Новые уроки</h4>
                    </div>

                </div>

                <div class="row" id="popular">
                    <div class="col s12 m12">
                        <h4 class="light">Популярное</h4>
                    </div>

                </div>

                <div id="popular-template" class="template">
                    <div class="col s4 m4">
                        <div class="card">
                            <div class="card-image waves-effect waves-block waves-grey">
                                <img class="activator lesson-cover" src="" lesson-cover>
                            </div>
                            <div class="card-content">
                                <span class="card-title activator grey-text text-darken-4 lesson-title" lesson-title></span>
                                <p><a href="#" class="lesson-link" lesson-link>Перейти</a></p>
                            </div>
                            <div class="card-reveal">
                                <span class="card-title grey-text text-darken-4 lesson-title-overflow" lesson-title-overflow><span class="paragraph-end"></span><i class="navigation-btn mdi-navigation-close right"></i></span>
                                <p class="lesson-description" lesson-description></p>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="new-template" class="template">
                    <div class="col s6 m4 l4">
                        <a class="card-panel lighten-5 z-depth-1 waves-effect waves-grey lesson-link" lesson-link>
                            <div class="row valign-wrapper">
                                <div class="col s3 l4 img-holder">
                                    <img src="" alt="" class="circle responsive-img lesson-cover" lesson-cover> <!-- notice the "circle" class -->
                                </div>
                                <div class="col s9">
                                    <span class="black-text lesson-title" lesson-title><span class="paragraph-end"></span></span>
                                    <div class="row complexity-bar">
                                        <p class="col m6 l6 s6">Сложность</p>
                                    </div>
                                    <div class="row complexity-bar">
                                        <div class="col m6 l6 s6">
                                            <div class="progress">
                                                <div class="determinate lesson-complexity" lesson-complexity></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>

            </div>

            <div class="col s12 m12 l3">

                <ul class="collection with-header">
                    <li class="collection-header"><h4>Самые активные ученики дня</h4></li>
                    <li class="collection-item"><a href="#">Варвара</a> прошла 8 уроков</li>
                    <li class="collection-item"><a href="#">Семён</a> прошёл 4 урока</li>
                    <li class="collection-item"><a href="#">Макс</a> прошёл коротенькие 700 веков</li>
                    <li class="collection-item"><a href="#">Вы</a> прошли 2 урока</li>
                    <li class="collection-item"><a href="#">Твоя собака</a> прошла 0 уроков</li>
                </ul>
                <ul class="collection with-header">
                    <li class="collection-header"><h4>Самые активные ученики недели</h4></li>
                    <li class="collection-item"><a href="#">Варвара</a> прошла 8 уроков</li>
                    <li class="collection-item"><a href="#">Семён</a> прошёл 4 урока</li>
                    <li class="collection-item"><a href="#">Макс</a> прошёл коротенькие 700 веков</li>
                    <li class="collection-item"><a href="#">Вы</a> прошли 2 урока</li>
                    <li class="collection-item"><a href="#">Твоя собака</a> прошла 0 уроков</li>
                </ul>
                <ul class="collection with-header">
                    <li class="collection-header"><h4>Самые активные ученики месяца</h4></li>
                    <li class="collection-item"><a href="#">Варвара</a> прошла 8 уроков</li>
                    <li class="collection-item"><a href="#">Семён</a> прошёл 4 урока</li>
                    <li class="collection-item"><a href="#">Макс</a> прошёл коротенькие 700 веков</li>
                    <li class="collection-item"><a href="#">Вы</a> прошли 2 урока</li>
                    <li class="collection-item"><a href="#">Твоя собака</a> прошла 0 уроков</li>
                </ul>
            </div>

        </div>

    </body>
</html>