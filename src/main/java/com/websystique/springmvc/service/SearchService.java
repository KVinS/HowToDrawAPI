/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;

import com.websystique.springmvc.dao.LessonDAO;
import com.websystique.springmvc.model.Lesson;
import com.websystique.springmvc.model.Tag;
import com.websystique.springmvc.model.TagSynonym;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kvins.draw.Utilites;

/**
 *
 * @author KVinS
 */
@Service("searchService")
@Transactional
public class SearchService {
    @Autowired
    TagService tagsService;
    
    @Autowired
    LessonService lessonsService;
    
    private static int maxTagsInResult = 20;
    private static int maxLessonsInResult = 20;
    
    public List<TagSynonym> findTagsSynonymByPiece (String piece, String country){
        return tagsService.getTagsSynonymByPiece(piece, maxTagsInResult);
    }
    
    public List<Lesson> findLessonsByQuery (String query, int page){
        return lessonsService.getLessonsByQuery(query, maxLessonsInResult, page);
    }
    
}
