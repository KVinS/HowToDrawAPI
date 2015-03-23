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

var loadPopular = function() {

    var $popularContainer = $("#popular");

    function createPopular(data) {
        var clone = $("#popular-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/lesson_prev/"+ data.id;
        var description = "��� �������� " + data.titleEn;
        var link = data.link;
        var complexity = data.complexity;

        clone.find(".lesson-title").text(title);
        var overflowTitle = clone.find(".lesson-title-overflow")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-description").text(description);
        clone.find(".lesson-link").attr("href", link);

        return clone;
    }

    return function() {
   $.ajax({url: "/HowToDraw/API/lessons/0?sort=VIEWS", contentType: "application/json", dataType: "json"})
//        getMockPopular()
            .done(function(data) {
            if (data.success) {
                var lessons = data.lessons;
                for (var i = 0; i < lessons.length; i++) {
                    var _lesson = createPopular(lessons[i]);
                    $popularContainer.append(_lesson);
                }
            } else {
                handlerError(data.error);
            }
        }, function(error) {
            handlerError(error);
        });
    }
};

var suggestion  = function() {

    var $searchBox = $("#search");
    var suggest = new Suggest.Local("search", "suggest", []);

    return function(query_string) {
        $.ajax({url: "/HowToDraw/API/hints/?q=" + encodeURI(query_string), contentType: "application/json", dataType: "json"})
            //getMockNew()
            .done(function(data) {
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
            }, function(error) {
                handlerError(error);
            });
    }

};

var query  = function() {

    var $searchResults = $("#search-results");
    var $searchResultsContainer = $("#search-results-container");

    function createNew(data) {
        var clone = $("#new-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/lesson_prev/"+ data.id;
        var description = data.titleEn;
        var complexity = data.complexity;

        var overflowTitle = clone.find(".lesson-title")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-link").attr("href", "/HowToDraw/lesson?lessonID=" + data.id);
        clone.find(".lesson-complexity").addClass("complexity-" + complexity);

        return clone;
    }

    var pendingRequest = null;

    return function(query_string, preloader) {
        if (pendingRequest != null) {
            try {
                pendingRequest.abort();
            } catch (e) {

            }
        }
        preloader.show();
        $searchResultsContainer.removeClass("hidden");
        pendingRequest = $.ajax({url: "/HowToDraw/API/search/0?q=" + encodeURI(query_string), contentType: "application/json", dataType: "json"})
            //getMockNew()
            .done(function(data) {
                if (data.success) {
                    $searchResults.empty();
                    var lessons = data.lessons;
                    for (var i = 0; i < lessons.length; i++) {
                        var _lesson = createNew(lessons[i]);
                        $searchResults.append(_lesson);
                    }
                } else {
                    handlerError(data.error);
                }
            }, function(error) {
                handlerError(error);
            }).always(function() {
                preloader.hide();
            });
    }

};

var loadNew = function() {

    var $newContainer = $("#new");

    function createNew(data) {
        var clone = $("#new-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "/HowToDraw/API/lesson_prev/"+ data.id;
        var description = data.titleEn;
        var complexity = data.complexity;

        var overflowTitle = clone.find(".lesson-title")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-link").attr("href", "/HowToDraw/lesson?lessonID=" + data.id);
        clone.find(".lesson-complexity").addClass("complexity-" + complexity);

        return clone;
    }

    return function() {
   $.ajax({url: "/HowToDraw/API/lessons/0?sort=NEW", contentType: "application/json", dataType: "json"})
       //getMockNew()
        .done(function(data) {
            if (data.success) {
                var lessons = data.lessons;
                for (var i = 0; i < lessons.length; i++) {
                    var _lesson = createNew(lessons[i]);
                    $newContainer.append(_lesson);
                }
            } else {
                handlerError(data.error);
            }
        }, function(error) {
            handlerError(error);
        });
    }

};

var loadLesson = function(img, lessonId, step) {
    $(img).unbind("load");
    $(img).attr('src', '/HowToDraw/API/lesson/' + lessonId + '?step=' + step)
        .load(function() {
            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                alert('broken image!');
            } else {
                console.log("good");
            }
        })

};

