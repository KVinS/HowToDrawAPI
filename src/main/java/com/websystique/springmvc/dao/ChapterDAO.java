/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.websystique.springmvc.model.Chapter;
import com.websystique.springmvc.model.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 *
 * @author KVinS
 */
@Repository("ñhapterDao")
public class ChapterDAO extends SuperDAO {

    private final static int chaptersCol = 6;

    @SuppressWarnings("unchecked")
    public List<Chapter> getChapters(int page, String orderby, String sorter) {
        String sql = "SELECT * FROM chapters ORDER BY "+orderby+" "+sorter;
        Query query = getSession().createSQLQuery(sql).addEntity(Chapter.class);

        
         query.setFirstResult(page);
         query.setMaxResults(chaptersCol);
        List<Chapter> list = query.list();
        return list;
    }

    public Chapter getChapterById(int id) {
        return (Chapter) getSession().get(Chapter.class, id);
    }

    public Chapter getChapterByCode(String code) {
        String sql = "SELECT * FROM chapters WHERE CODE = :code";
        Query query = getSession().createSQLQuery(sql).addEntity(Chapter.class);
        query.setString("code", code);
        Chapter c = (Chapter) query.uniqueResult();

        return c;
    }

}
