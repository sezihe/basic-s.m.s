package com.danielezihe.hibernate.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ElementCollection
    @Column(name = "students_guiding")
    private Set<Integer> students_guiding;

    public Teacher() {
    }

    public Teacher(int id, String name, Set<Integer> students_guiding) {
        this.id = id;
        this.name = name;
        this.students_guiding = students_guiding;
    }

    public Set<Integer> getStudentsGuiding() {
        return students_guiding;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
