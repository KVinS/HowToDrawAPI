/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.dao;

import com.websystique.springmvc.model.Chapter;
import com.websystique.springmvc.model.Lesson;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import ru.kvins.draw.Parameters;
import ru.kvins.draw.SearchPair;

/**
 *
 * @author KVinS
 */
@Repository("сhapterDao")
public class ChapterDAO extends SuperDAO {

    @SuppressWarnings("unchecked")
    public SearchPair<Chapter> getChapters(int page, String orderby, String sorter) {
        String sql = "SELECT * FROM chapters ORDER BY " + orderby + " " + sorter;
        Query query = getSession().createSQLQuery(sql).addEntity(Chapter.class);

        query.setFirstResult(page * Parameters.maxChaptersInResult);
        query.setMaxResults(Parameters.maxChaptersInResult);
        List<Chapter> list = query.list();

        sql = "SELECT COUNT(id) FROM chapters";
        query = getSession().createSQLQuery(sql);
        int total = (int) Math.ceil((Integer) query.uniqueResult() / Parameters.maxChaptersInResult);

        return new <Chapter>SearchPair(list, total);
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
