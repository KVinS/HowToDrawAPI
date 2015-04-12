/**
 * Created by Semyon on 05.03.2015.
 */

/**
 * called from html after dom finished loading
 */
function initLoader() {
    loadPopular = loadPopular();
    loadNew = loadNew();
    query = query();
    suggestion = suggestion();
}


var loadPopular = function () {

    var $popularContainer = $("#popular");

    function createPopular(data) {
        var clone = $("#popular-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/chapter_prev/" + data.id;
        var description = "Пошаговые уроки рисования на тему " + data.title;
        var link =  "javascript:query('"+data.code+"', 0)";

        clone.find(".lesson-title").text(title);
        var overflowTitle = clone.find(".lesson-title-overflow")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-description").text(description);
        clone.find(".lesson-link").attr("href", link);

        return clone;
    }

    return function () {
        $.ajax({url: "/HowToDraw/API/chapters/0?sort=VIEWS", contentType: "application/json", dataType: "json"})
//        getMockPopular()
                .done(function (data) {
                    if (data.success) {
                        var chapters = data.chapters;
                        for (var i = 0; i < chapters.length; i++) {
                            var _chapter = createPopular(chapters[i]);
                            $popularContainer.append(_chapter);
                        }
                    } else {
                        handlerError(data.error);
                    }
                }, function (error) {
                    handlerError(error);
                });
    }
};

var suggestion = function () {

    var $searchBox = $("#search");
    var suggest = new Suggest.Local("search", "suggest", []);

    return function (query_string) {
        $.ajax({url: "/HowToDraw/API/hints/?q=" + encodeURI(query_string), contentType: "application/json", dataType: "json"})
                //getMockNew()
                .done(function (data) {
                    if (data.success) {
                        var hints = [];
                        for (var i = 0; i < data.hints.length; i++) {
                            var h = data.hints[i];
                            hints.push(h.title);
                        }
                        suggest.setCandidateList(hints);
                        suggest.search();
                    } else {
                        handlerError(data.error);
                    }
                }, function (error) {
                    handlerError(error);
                });
    }

};

function createPagination(curPage, pageQuantity, callback) {
    curPage = parseInt(curPage);
    
    if (pageQuantity <= 0) {
        return null;
    }
    var $pagination = $('<ul class="pagination"></ul>');
    var $leftChevron = $('<li class="waves-effect ' + (curPage > 0 ? '' : 'class="disabled"') + '"><i class="mdi-navigation-chevron-left"></i></a></li>');
    if (curPage > 0) {
        $leftChevron.click(function () {
            callback(curPage - 1);
        });
    }

    var $startElement = $('<li ' + (0 == curPage ? 'class="active"' : 'class="waves-effect"') + '>' + (1) + '</a></li>');
    $startElement.click(function (page) {
        return function () {
            callback(page);
        };
    }(0));

    var $finishElement = $('<li ' + (pageQuantity - 1 == curPage ? 'class="active"' : 'class="waves-effect"') + '>' + (pageQuantity) + '</a></li>');
    $finishElement.click(function (page) {
        return function () {
            callback(page);
        };
    }(pageQuantity - 1));

    var $rightChevron = $('<li class="waves-effect ' + (curPage + 1 < pageQuantity ? '' : 'disabled') + '"><i class="mdi-navigation-chevron-right"></i></a></li>');
    if (curPage + 1 < pageQuantity) {
        $rightChevron.click(function () {
            callback(curPage + 1);
        });
    }

    $pagination.append($leftChevron);
    $pagination.append($startElement);

    var iLeft = 4;
    var iRight = 5;

    var start = 1;
    var finish = pageQuantity;

    if (curPage > iLeft && curPage < (pageQuantity - iRight)) {
        start = curPage - iLeft;
        finish = curPage + iRight;
    } else if (curPage <= iLeft) {
        start = 1;
        if ((pageQuantity - (iRight + curPage)) >= 0) {
            finish = iRight + curPage - 1;
        } else {
            finish = pageQuantity - 1;
        }
    } else {
        if (curPage > iLeft && curPage <= (pageQuantity))
        {
            start = curPage - iLeft;
            finish = pageQuantity - 1;
        }
    }

    for (i = start; i < finish; i++)
    {
        var $element = $('<li ' + (i == curPage ? 'class="active"' : 'class="waves-effect"') + '>' + (i + 1) + '</a></li>');
        $pagination.append($element);
        $element.click(function (page) {
            return function () {
                callback(page);
            };
        }(i));
    }

    $pagination.append($finishElement);
    $pagination.append($rightChevron);
    return $pagination;
}

var query = function () {

    var $searchResults = $("#search-results");
    var $searchResultsContainer = $("#search-results-container");
    var $searchPagination = $("#search-pagination");

    function createNew(data) {
        var clone = $("#new-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/lesson_prev/" + data.id;
        var description = data.titleEn;
        var complexity = data.complexity;

        var overflowTitle = clone.find(".lesson-title")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-link").attr("href", "/HowToDraw/lesson?lessonID=" + data.id);
        //clone.find(".lesson-link").attr("href", "#page=lesson&id="+ data.id+"&step=0");
        clone.find(".lesson-complexity").addClass("complexity-" + complexity);

        return clone;
    }

    var pendingRequest = null;

    return function (query_string, page) {

        var preloader = $("#preloader");
        if (pendingRequest != null) {
            try {
                pendingRequest.abort();
            } catch (e) {

            }
        }
        preloader.show();
        $searchResultsContainer.removeClass("hidden");

        pendingRequest = $.ajax({url: "/HowToDraw/API/search/" + page + "?q=" + encodeURI(query_string), contentType: "application/json", dataType: "json"})
                //getMockNew()
                .done(function (data) {
                    VK.callMethod("setLocation", "page=search&p=" + page + "&q=" + encodeURI(query_string), false);
                    history.pushState(null, null, "/HowToDraw/#page=search&p=" + page + "&q=" + encodeURI(query_string));
                    if (data.success) {
                        var lessons = data.lessons;
                        $searchResults.empty();
                        $searchPagination.empty();
                        $searchPagination.append(createPagination(page, data.total, function (page) {
                            query(query_string, page);
                        }));
                        for (var i = 0; i < lessons.length; i++) {
                            var _lesson = createNew(lessons[i]);
                            $searchResults.append(_lesson);
                        }
                    } else {
                        handlerError(data.error);
                    }
                }, function (error) {
                    handlerError(error);
                }).always(function () {
            preloader.hide();
        });
    }

};

var loadNew = function () {

    var $newContainer = $("#new");
    var $newPagination = $("#new-pagination");

    function createNew(data) {
        var clone = $("#new-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/lesson_prev/" + data.id;
        var description = data.titleEn;
        var complexity = data.complexity;

        var overflowTitle = clone.find(".lesson-title")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-link").attr("href", "/HowToDraw/lesson?lessonID=" + data.id);
        //clone.find(".lesson-link").attr("href", "#page=lesson&id="+ data.id+"&step=0");
        clone.find(".lesson-complexity").addClass("complexity-" + complexity);

        return clone;
    }

    return function (page) {
        $.ajax({url: "/HowToDraw/API/lessons/" + page + "?sort=NEW", contentType: "application/json", dataType: "json"})
                //getMockNew()
                .done(function (data) {
                    if (data.success) {
                        var lessons = data.lessons;
                        var total = data.total;
                        $newPagination.empty();
                        $newContainer.empty();
                        $newPagination.append(createPagination(page, total, function (page) {
                            loadNew(page);
                        }));
                        for (var i = 0; i < lessons.length; i++) {
                            var _lesson = createNew(lessons[i]);
                            $newContainer.append(_lesson);
                        }

                        VK.callMethod("setLocation", "page=new_lessons&p=" + page, false);
                        history.pushState(null, null, "/HowToDraw/#page=new_lessons&p=" + page);
                    } else {
                        handlerError(data.error);
                    }
                }, function (error) {
                    handlerError(error);
                });
    }

};

var loadLesson = function (img, lessonId, step) {
    $(img).unbind("load");
    $(img).attr('src', '/HowToDraw/API/lesson/' + lessonId + '?step=' + step)
            .load(function () {
                if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                    alert('broken image!');
                } else {
                    console.log("good");
                }
            })

};

