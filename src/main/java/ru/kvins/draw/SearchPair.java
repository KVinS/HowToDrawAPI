/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kvins.draw;

import com.websystique.springmvc.model.Lesson;
import java.util.List;

/**
 *
 * @author Эдуард
 */
public class SearchPair<T> {

    public SearchPair(List<T> list, int total) {
        this.list = list;
        this.total = total;
    }
    
    private final List<T> list;
    private final int total;

    public List<T> getList() {
        return list;
    }

    public Integer getTotal() {
        return total;
    }
}
