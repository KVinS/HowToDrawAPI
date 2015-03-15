/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;

import com.websystique.springmvc.dao.TagDAO;
import com.websystique.springmvc.dao.SuperDAO;
import com.websystique.springmvc.model.Tag;
import com.websystique.springmvc.model.TagSynonym;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvins.draw.Utilites.SortType;

/**
 *
 * @author KVinS
 */
@Service("tagsService")
@Transactional
public class TagService {

    @Autowired
    private TagDAO tagDAO;

    public void persistTag(Tag tag) {
        tagDAO.<Tag>persist(tag);
    }

    //add- persist
    //remove-remove //из хибирнейта
    //update-merge
    public void mergeTag(Tag tag) {
        tagDAO.merge(tag);
    }
    
    
     public List<TagSynonym> getTagsSynonymByPiece (String piece, int maxTagsInResult){
        return tagDAO.findTagsSynonymByPiece(piece, maxTagsInResult);
    }

    public List<Tag> getTags(int page, SortType sort) {
        if (SortType.NEW == sort) {
            return tagDAO.getTags(page, "ID", "DESC");
        } else if (SortType.OLD == sort) {
            return tagDAO.getTags(page, "ID", "ASC");
        } else if (SortType.VIEWS == sort) {
            return tagDAO.getTags(page, "VIEWS", "DESC");
        } else if (SortType.RATING == sort) {
            return tagDAO.getTags(page, "RATING", "DESC");
        } else {
            return tagDAO.getTags(page, "ID", "DESC");
        }
    }

    public void removeTag(Tag tag) {
        tagDAO.remove(tag);
    }

    public Tag getTag(Integer id) {
        return tagDAO.getTagById(id);
    }
}
