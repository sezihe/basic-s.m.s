package com.danielezihe.hibernate.entity;

import javax.persistence.*;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public Course() {
    }

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
