/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.dao;


import com.websystique.springmvc.model.Tag;
import com.websystique.springmvc.model.TagSynonym;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author KVinS
 */
@Repository("tagDao")
public class TagDAO extends SuperDAO {

    private final static int tagsCol = 6;

    @SuppressWarnings("unchecked")
    public List<Tag> getTags(int page, String orderby, String sorter) {
        String sql = "SELECT * FROM tags ORDER BY " + orderby + " " + sorter ;
        Query query = getSession().createSQLQuery(sql).addEntity(Tag.class);
        query.setFirstResult( page);
                query.setMaxResults(tagsCol);

        List<Tag> list = query.list();
        return list;
    }

    public Tag getTagById(int id) {
        return (Tag) getSession().get(Tag.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<TagSynonym> findTagsSynonymByPiece(String piece, int maxTagsInResult) {
        String sql = "SELECT * FROM tags_synonyms WHERE TITLE LIKE :piece";
        Query query = getSession().createSQLQuery(sql).addEntity(TagSynonym.class);
        query.setParameter("piece", "%" + piece + "%");
        query.setMaxResults(maxTagsInResult);
        List<TagSynonym> list = query.list();
        return list;
    }
}
