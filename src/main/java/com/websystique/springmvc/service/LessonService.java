/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;

import com.websystique.springmvc.dao.LessonDAO;
import com.websystique.springmvc.dao.SuperDAO;
import com.websystique.springmvc.model.Lesson;
import com.websystique.springmvc.model.TagSynonym;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvins.draw.Utilites;
import ru.kvins.draw.Utilites.SortType;

/**
 *
 * @author KVinS
 */
@Service("lessonService")
@Transactional
public class LessonService {

    @Autowired
    private LessonDAO lessonDAO;

    public void persistLesson(Lesson lesson) {
        lessonDAO.<Lesson>persist(lesson);
    }

    //add- persist
    //remove-remove //из хибирнейта
    //update-merge
    public void mergeLesson(Lesson lesson) {
        lessonDAO.merge(lesson);
    }

    public List<Lesson> getLessons(int page, SortType sort) {
        if (SortType.NEW == sort) {
            return lessonDAO.getLessons(page, "ID", "DESC");
        } else if (SortType.OLD == sort) {
            return lessonDAO.getLessons(page, "ID", "ASC");
        } else if (SortType.VIEWS == sort) {
            return lessonDAO.getLessons(page, "VIEWS", "DESC");
        } else if (SortType.RATING == sort) {
            return lessonDAO.getLessons(page, "RATING", "DESC");
        } else {
            return lessonDAO.getLessons(page, "ID", "DESC");
        }
    }
    
    public List<Lesson> getLessons(Collection c) {
        HashSet<Integer> lessonsSet = new HashSet<Integer>(c);
        List<Lesson> lessons = new ArrayList<Lesson>();
        for (int id : lessonsSet) {
            lessons.add(getLesson(id));
        }
        return lessons;
    }

    public List<Lesson> getLessonsByQuery (String query, int maxLessonsInResult, int page){
        return lessonDAO.getLessonsByQuery(query, maxLessonsInResult, page);
    }

    
    public void removeLesson(Lesson lesson) {
        lessonDAO.remove(lesson);
    }

    public Lesson getLesson(Integer id) {
        return lessonDAO.getLessonById(id);
    }
}
