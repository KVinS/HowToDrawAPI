/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.model;

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
import javax.persistence.Table;
import javax.persistence.OneToMany;

/**
 *
 * @author KVinS
 */
@Entity
@Table(name = "LESSONS")
public class Lesson {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "TITLE_EN", nullable = false)
    private String titleEn;
    //Рейтинг
    @Column(name = "RATING", nullable = false)
    private int rating;
    //Просмотров за день
    @Column(name = "VIEWS", nullable = false)
    private int views;
    @Column(name = "CHAPTER", nullable = false)
    private String chapter;
    //Рейтинг
    @Column(name = "LOCAL_ID", nullable = false)
    private int localId;
    //Сложность 0-3
    @Column(name = "COMPLEXITY", nullable = false)
    private int complexity;
    //Шагов
    @Column(name = "STEPS", nullable = false)
    private int steps;
    //Формат
    @Column(name = "FORMAT", nullable = false)
    private String format;
    @ManyToMany
    @JoinTable(name = "TAGS_AND_LESSONS_BOUNDS",
            joinColumns = {
        @JoinColumn(name = "LESSON_ID")},
            inverseJoinColumns = {
        @JoinColumn(name = "TAG_ID")})
    private Set<Tag> tags;

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
