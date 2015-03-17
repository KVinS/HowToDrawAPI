/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.kvins.draw.Parameters;

/**
 *
 * @author KVinS
 */
@Entity
@Table(name = "TAGS")
public class Tag {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "TITLE_EN", nullable = false)
    private String titleEn;
    @ManyToMany(mappedBy = "tags")
    private List<Lesson> lessons;

    @JsonIgnore
    public List<Lesson> getLessons(int page) {
        int start = Parameters.maxLessonsInResult * page;
        int finish = start + Parameters.maxLessonsInResult;

        if (finish > lessons.size()) {
            finish = lessons.size();
            start = finish - Parameters.maxLessonsInResult;
            if (start < 0) {
                start = 0;
            }
        }

        List<Lesson> l = new LinkedList<Lesson>();

        for (int i = start; i < finish; i++) {
            l.add(lessons.get(i));
        }
        return l;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }
}
