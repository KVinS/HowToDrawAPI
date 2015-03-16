package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.Chapter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.websystique.springmvc.model.Employee;
import com.websystique.springmvc.model.Lesson;
import com.websystique.springmvc.model.TagSynonym;
import com.websystique.springmvc.service.ChapterService;
import com.websystique.springmvc.service.EmployeeService;
import com.websystique.springmvc.service.LessonService;
import com.websystique.springmvc.service.SearchService;
import com.websystique.springmvc.service.TagService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kvins.draw.Utilites.SortType;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    EmployeeService service;
    @Autowired
    LessonService lessonsService;
    @Autowired
    ChapterService chaptersService;
    @Autowired
    TagService tagsService;
    @Autowired
    SearchService searchService;

    /*
     * This method will list all existing employees.
     */
    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    public String listEmployees(ModelMap model) {

        List<Employee> employees = service.findAllEmployees();
        model.addAttribute("employees", employees);
        return "allemployees";
    }

    @RequestMapping(value = {"/API/lesson/{id}"}, method = RequestMethod.GET)
    public @ResponseBody
    void getLesson(ModelMap model, @PathVariable Integer id, @RequestParam Integer step, HttpServletResponse response) throws IOException {



        Lesson l = lessonsService.getLesson(id);

        if (step > l.getSteps()) {
            step = l.getSteps();
        }

        try {
            //InputStream is = new FileInputStream("\\data\\" + l.getChapter() + "\\lesson" + l.getLocalId() + "prew.png");        
            //InputStream is = new FileInputStream("\\site\\wwwroot\\data\\" + l.getChapter() + "\\res\\drawable\\lesson_" + l.getLocalId() + "_step_"+step+".png");


            InputStream is = new FileInputStream("D:\\home\\site\\wwwroot\\data\\" + l.getChapter() + "\\res\\drawable\\lesson_" + l.getLocalId() + "_step_" + step + ".png");

            response.setContentType("image/png");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());


            response.flushBuffer();
        } catch (IOException ex) {
            File path;
            File[] files;

            path = new File("D:\\home");
            if (!path.exists()) {
                throw new IOException("Cannot access : No such file or directory");
            }
            if (path.isFile()) {
                files = new File[]{path};
            } else {
                files = path.listFiles();
            }
            String s = "";
            for (File f : files) {
                s += f.getName() + ((f.isDirectory()) ? File.separator : " | ");
            }
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            // throw new RuntimeException("IOError reading step " + step+  " from " + id + " to output stream"+ex.toString());
            throw new RuntimeException(s);
        }
    }

    @RequestMapping(value = {"/API/lessons/{page}"}, method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getLessons(ModelMap model, @PathVariable Integer page, @RequestParam SortType sort) {
        List<Lesson> lessons = lessonsService.getLessons(page, sort);
        JSONObject obj = new JSONObject();
        obj.put("lessons", lessons);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = {"/API/hints/"}, method = RequestMethod.POST)
    public @ResponseBody
    JSONObject getHints(ModelMap model, @RequestParam String q) {
        List<TagSynonym> hints = searchService.findTagsSynonymByPiece(q, "ru");
        JSONObject obj = new JSONObject();
        obj.put("hints", hints);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = {"/API/search/{page}"}, method = RequestMethod.POST)
    public @ResponseBody
    JSONObject getHints(ModelMap model, @PathVariable Integer page, @RequestParam String q) {
        List<Lesson> lessons = searchService.findLessonsByQuery(q, page);
        JSONObject obj = new JSONObject();
        obj.put("lessons", lessons);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = {"/API/chapters/{page}"}, method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getChapters(ModelMap model, @PathVariable Integer page, @RequestParam SortType sort) {
        List<Chapter> chapters = chaptersService.getChapters(page, sort);
        JSONObject obj = new JSONObject();
        obj.put("chapters", chapters);
        obj.put("success", true);
        return obj;
    }

    @RequestMapping(value = {"/API/lesson_prev/{id}"}, method = RequestMethod.GET)
    public void getLessonPreview(ModelMap model, @PathVariable Integer id, HttpServletResponse response) {
        Lesson l = lessonsService.getLesson(id);
        try {
            //InputStream is = new FileInputStream("\\data\\" + l.getChapter() + "\\lesson" + l.getLocalId() + "prew.png");
            InputStream is = new FileInputStream("D:\\home\\site\\wwwroot\\data\\" + l.getChapter() + "\\res\\drawable\\lesson" + l.getLocalId() + "prew.png");
            response.setContentType("image/png");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);

            throw new RuntimeException("IOError reading preview " + id + " to output stream: " + ex.toString());
        }
        //List<Lesson> lessons = lessonsService.getLessons(page, sort);
        //return lessons;
    }

    @RequestMapping(value = {"/g"}, method = RequestMethod.GET)
    public String openGeneral(ModelMap model) {
        //Employee employee = new Employee();
        //model.addAttribute("employee", employee);
        return "general";
    }

    /*
     * This method will provide the medium to add a new employee.
     */
    @RequestMapping(value = {"/new"}, method = RequestMethod.GET)
    public String newEmployee(ModelMap model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "registration";
    }

    /*
     * This method will be called on form submission, handling POST request for
     * saving employee in database. It also validates the user input
     */
    @RequestMapping(value = {"/new"}, method = RequestMethod.POST)
    public String saveEmployee(@Valid Employee employee, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "registration";
        }

        service.saveEmployee(employee);

        model.addAttribute("succss", "Employee " + employee.getName()
                + " registered successfully");
        return "success";
    }

    /*
     * This method will delete an employee by it's SSN value.
     */
    @RequestMapping(value = {"/delete-{ssn}-employee"}, method = RequestMethod.GET)
    public String deleteEmployee(@PathVariable String ssn) {
        service.deleteEmployeeBySsn(ssn);
        return "redirect:/list";
    }
}
