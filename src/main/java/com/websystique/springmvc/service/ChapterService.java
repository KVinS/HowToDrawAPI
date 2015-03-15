/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;

import com.websystique.springmvc.dao.ChapterDAO;
import com.websystique.springmvc.dao.SuperDAO;
import com.websystique.springmvc.model.Chapter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvins.draw.Utilites.SortType;

/**
 *
 * @author KVinS
 */
@Service("chapterService")
@Transactional
public class ChapterService {

    @Autowired
    private ChapterDAO chapterDAO;

    public void persistChapter(Chapter chapter) {
        chapterDAO.<Chapter>persist(chapter);
    }

    //add- persist
    //remove-remove //из хибирнейта
    //update-merge
    public void mergeChapter(Chapter chapter) {
        chapterDAO.merge(chapter);
    }

    public List<Chapter> getChapters(int page, SortType sort) {
        if (SortType.NEW==sort) {
            return chapterDAO.getChapters(page, "ID", "DESC");
        } else if (SortType.OLD==sort) {
            return chapterDAO.getChapters(page, "ID", "ASC");
        } else if (SortType.VIEWS==sort) {
            return chapterDAO.getChapters(page, "VIEWS", "DESC");
        } else if (SortType.RATING==sort) {
            return chapterDAO.getChapters(page, "VIEWS", "DESC");
        } else {
            return chapterDAO.getChapters(page, "ID", "DESC");
        }
    }

    public void removeChapter(Chapter chapter) {
        chapterDAO.remove(chapter);
    }

    public Chapter getChapter(Integer id) {
        return chapterDAO.getChapterById(id);
    }

    public Chapter getChapter(String code) {
        return chapterDAO.getChapterByCode(code);
    }
}
