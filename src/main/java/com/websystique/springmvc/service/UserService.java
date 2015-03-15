/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.service;


import com.websystique.springmvc.dao.SuperDAO;
import com.websystique.springmvc.model.User;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author KVinS
 */

@Service("userService")
@Transactional
public class UserService {

    @Autowired
    private SuperDAO userDAO;
    

    public void persistUser(User user) {
        userDAO.<User>persist(user);
    }
    
    //add- persist
    //remove-remove //из хибирнейта
    //update-merge
    

    public void mergeUser(User user) {
        userDAO.merge(user);
    }

    public User getUserById(Integer id) {
        return (User) userDAO.getUserById(id);
    }
    

    public List getAllUsers() {
        return userDAO.getAllUsers();
    }
    
   
    public void removeUser(User user) {
        userDAO.remove(user);
    }
}
