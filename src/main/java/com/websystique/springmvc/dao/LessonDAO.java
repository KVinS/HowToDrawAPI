/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.websystique.springmvc.model.Lesson;
import com.websystique.springmvc.model.TagSynonym;
import com.websystique.springmvc.model.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 *
 * @author KVinS
 */
@Repository("lessonDao")
public class LessonDAO extends SuperDAO {

    private final static int lessonsCol = 6;

    @SuppressWarnings("unchecked")
    public List<Lesson> getLessons(int page, String orderby, String sorter) {
        String sql = "SELECT * FROM lessons ORDER BY " + orderby + " " + sorter;
        Query query = getSession().createSQLQuery(sql).addEntity(Lesson.class);

        query.setFirstResult(page);
        query.setMaxResults(lessonsCol);
        List<Lesson> list = query.list();
        return list;
    }

    public Lesson getLessonById(int id) {
        return (Lesson) getSession().get(Lesson.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Lesson> getLessonsByQuery(String squery, int maxLessonsInResult, int page) {
        String sql = "SELECT * FROM lessons WHERE ID IN (SELECT LESSON_ID FROM tags_and_lessons_bounds WHERE TAG_ID IN (SELECT DISTINCT TAG_ID FROM tags_synonyms WHERE TITLE LIKE :squery))";
        Query query = getSession().createSQLQuery(sql).addEntity(Lesson.class);
        query.setParameter("squery", "%" + squery + "%");
        query.setMaxResults(lessonsCol);
        query.setFirstResult(page);
        List<Lesson> list = query.list();
        return list;
    }
}
