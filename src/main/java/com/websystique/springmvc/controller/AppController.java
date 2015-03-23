package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.Chapter;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.websystique.springmvc.model.Lesson;
import com.websystique.springmvc.model.Tag;
import com.websystique.springmvc.model.TagSynonym;
import com.websystique.springmvc.service.ChapterService;
import com.websystique.springmvc.service.LessonService;
import com.websystique.springmvc.service.SearchService;
import com.websystique.springmvc.service.TagService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kvins.draw.Utilites;
import ru.kvins.draw.Utilites.SortType;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    LessonService lessonsService;
    @Autowired
    ChapterService chaptersService;
    @Autowired
    TagService tagsService;
    @Autowired
    SearchService searchService;

    @RequestMapping(value = "/API/lesson/{id}", method = RequestMethod.GET)
    public @ResponseBody
    void getLesson(ModelMap model, @PathVariable Integer id, @RequestParam Integer step, HttpServletResponse response) throws IOException {
        Lesson l = lessonsService.getLesson(id);

        if (step > l.getSteps()) {
            step = l.getSteps();
        }

        String baseURI = lessonsService.getBaseURI();
        String path = baseURI.replace("#{lessonID}", "" + id);
        path = path.replace("#{chapter}", l.getChapter());
        path = path.replace("#{localID}", "" + l.getLocalId());
        path = path.replace("#{stepNum}", "" + step);

        Utilites.writeImageToResponse(path, response);
    }

    @RequestMapping(value = "/API/lessons/{page}", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getLessons(ModelMap model, @PathVariable Integer page, @RequestParam(value = "sort", required = false, defaultValue = "NEW") SortType sort, @RequestParam(value = "tag", required = false) Integer tag) {
        List<Lesson> lessons;
        if (tag != null) {
            //lessons= lessonsService.getLessonsByTag(tag, page, sort);
            Tag st = tagsService.getTag(tag);
            lessons = st.getLessons(page);
        } else {
            lessons = lessonsService.getLessons(page, sort);
        }
        JSONObject obj = new JSONObject();
        obj.put("lessons", lessons);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = "/API/hints/", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getHints(ModelMap model, @RequestParam String q) {
        List<TagSynonym> hints = searchService.findTagsSynonymByPiece(q, "ru");
        System.out.println("! " + q);
        JSONObject obj = new JSONObject();
        obj.put("hints", hints);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = "/API/search/{page}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject search(ModelMap model, @PathVariable Integer page, @RequestParam String q) {
        List<Lesson> lessons = searchService.findLessonsByQuery(q, page);
        JSONObject obj = new JSONObject();
        obj.put("lessons", lessons);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = "/API/chapters/{page}", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getChapters(ModelMap model, @PathVariable Integer page, @RequestParam(value = "sort", required = false, defaultValue = "NEW") SortType sort) {
        List<Chapter> chapters = chaptersService.getChapters(page, sort);
        JSONObject obj = new JSONObject();
        obj.put("chapters", chapters);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = "/API/lesson_prev/{id}", method = RequestMethod.GET)
    public void getLessonPreview(ModelMap model, @PathVariable Integer id, HttpServletResponse response) {
        Lesson l = lessonsService.getLesson(id);
        if (l != null) {

            String baseURI = lessonsService.getLessonPreviewURI();
            String path = baseURI.replace("#{lessonID}", "" + id);
            path = path.replace("#{chapter}", l.getChapter());
            path = path.replace("#{localID}", "" + l.getLocalId());

            Utilites.writeImageToResponse(path, response);
        }
    }

    @RequestMapping(value = "/API/chapter_prev/{id}", method = RequestMethod.GET)
    public void getChapterPreview(ModelMap model, @PathVariable Integer id, HttpServletResponse response) {
        Chapter c = chaptersService.getChapter(id);
        if (c != null) {

            String baseURI = lessonsService.getLessonPreviewURI();
            String path = baseURI.replace("#{lessonID}", "" + id);
            path = path.replace("#{chapter}", c.getCode());

            if (!"".equals(c.getImg())) {
                path = c.getImg();
            }
            Utilites.writeImageToResponse(path, response);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String openGeneral(final ModelMap model) {
        return "general";
    }

    @RequestMapping(value = "/lesson", method = RequestMethod.GET)
    public String openLesson(final ModelMap model, final @RequestParam(required = true) Integer lessonID) {
        model.put("lessonID", lessonID);
        return "lesson";
    }


}
