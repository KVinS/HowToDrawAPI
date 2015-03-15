/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.dao;

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
@Primary
@Repository("superDao")
public class SuperDAO {

    @Autowired
    private SessionFactory sessionFactory;

    
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public <T> void persist(final Object o, final Class<T> clazz) {
        persist((T) o);

    }

    public <T> void persist(final Object o) {
        getSession().persist((T) o);
    }

    public <T> void merge(final Object o, final Class<T> clazz) {
        merge((T) o);
    }

    public <T> void merge(final Object o) {
        getSession().merge((T) o);
    }

    public <T> void remove(final Object o, final Class<T> clazz) {
        remove((T) o);
    }

    public <T> void remove(final Object o) {
        getSession().delete((T) o);
    }

    public User getUserById(long id) {
        return (User) getSession().get(User.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        Criteria criteria = getSession().createCriteria(User.class);
        return (List<User>) criteria.list();
    }

    public void deleteUserById(int id) {
        Query query = getSession().createSQLQuery("delete from Users where id = :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
}
