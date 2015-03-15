/**
 * Created by Semyon on 05.03.2015.
 */

$(function() {
    loadPopular = loadPopular();
    loadNew = loadNew();
});

var loadPopular = function() {

    var $popularContainer = $("#popular");

    function createPopular(data) {
        var clone = $("#popular-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "http://howtodraw.azurewebsites.net/HowToDraw/API/lesson_prev/"+ data.id;
        var description = "Как рисовать " + data.titleEn;
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
   $.ajax({url: "http://howtodraw.azurewebsites.net/HowToDraw/API/lessons/0?sort=VIEWS", contentType: "application/json", dataType: "json"})
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

var loadNew = function() {

    var $newContainer = $("#new");

    function createNew(data) {
        var clone = $("#new-template").clone();
        clone.removeAttr("id");
        clone = clone.children(0);

        var title = data.titleEn;
        var coverUri = "http://howtodraw.azurewebsites.net/HowToDraw/API/lesson_prev/"+ data.id;
        var description = data.titleEn;
        var link = data.link;
        var complexity = data.complexity;

        var overflowTitle = clone.find(".lesson-title")[0];
        overflowTitle.innerHTML = title + overflowTitle.innerHTML;
        clone.find(".lesson-cover").attr("src", coverUri);
        clone.find(".lesson-link").attr("href", link);
        clone.find(".lesson-complexity").addClass("complexity-" + complexity);

        return clone;
    }

    return function() {
   $.ajax({url: "http://howtodraw.azurewebsites.net/HowToDraw/API/lessons/0?sort=NEW", contentType: "application/json", dataType: "json"})
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

