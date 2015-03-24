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
import ru.kvins.draw.Parameters;
import ru.kvins.draw.SearchPair;
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
    

    public List<TagSynonym> findTagsSynonymByPiece (String piece, String country){
        return tagsService.getTagsSynonymByPiece(piece, Parameters.maxTagsInResult);
    }
    
    public  SearchPair<Lesson> findLessonsByQuery (String query, int page){
        return lessonsService.getLessonsByQuery(query, Parameters.maxLessonsInSearchResult, page);
    }
    
    public SearchPair<Lesson> findLessonsByTag (Tag tag, int page){
        return tag.getLessons(page);
    }
    
}
