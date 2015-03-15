/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websystique.springmvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
/**
 *
 * @author KVinS
 */

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;

    @Column(name = "LASTNAME", nullable = true)
    private String lastname;

    @Column(name = "AVATAR", nullable = true)
    private String avatar;

    @Column(name = "DAY_RATING")
    private int drating;

    @Column(name = "RATING")
    private int rating;
    
    @Column(name = "VK_ID", unique=true, nullable = true)
    private long vk_id;
    
    @Column(name = "IP", nullable = true)
    private String last_ip;
    
    @Column(name = "COUNTRY", nullable = true)
    private String country;

    @NotNull
    @Column(name = "SEX")
    private int sex;
    
    @NotNull
    @DateTimeFormat(pattern="dd.MM.yyyy") 
    @Column(name = "AUTHORIZATION_DATE", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate regDate;
    
    @NotNull
    @DateTimeFormat(pattern="dd.MM.yyyy") 
    @Column(name = "REGISTRATION_DATE", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate autDate;

    public long getVk_id() {
        return vk_id;
    }

    public void setVk_id(long vk_id) {
        this.vk_id = vk_id;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public LocalDate getAutDate() {
        return autDate;
    }

    public void setAutDate(LocalDate autDate) {
        this.autDate = autDate;
    }


    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    
    public int getDrating() {
        return drating;
    }

    public void setDrating(int drating) {
        this.drating = drating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
