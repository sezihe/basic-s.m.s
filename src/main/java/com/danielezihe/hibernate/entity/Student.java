package com.danielezihe.hibernate.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String className;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id")
    private Teacher guide;

    @ElementCollection
    @Column(name = "registered_courses_ids")
    private Set<Integer> registered_courses_ids;

    public Student() {
    }

    public Student(int id, String name, String className, Teacher guide, Set<Integer> registered_courses_ids) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.guide = guide;
        this.registered_courses_ids = registered_courses_ids;
    }

    public void setGuide(@Nonnull Teacher guide) {
        this.guide = guide;
    }

    public Set<Integer> getRegisteredCoursesIds() {
        return registered_courses_ids;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    @Nullable
    public Teacher getGuide() {
        return guide;
    }
}
