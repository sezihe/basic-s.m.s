package com.danielezihe.controllers;

import com.danielezihe.hibernate.entity.Course;
import com.danielezihe.hibernate.entity.QCourse;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class CourseController {
    private int id;
    private String courseName;

    static QCourse qCourse = QCourse.course;
    static JPAQueryFactory queryFactory = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());


    public CourseController(String courseName) {
        this.id = 1;
        this.courseName = courseName;
    }

    public Course save() {
        Course newCourse = new Course(id, courseName);

        HibernateUtil.addToDB(newCourse);

        return newCourse;
    }

    public static Course findCourse(int courseId) {
        Course course = (Course) queryFactory.from(qCourse).where(qCourse.id.eq(courseId)).fetchOne();

        return course;
    }

    public static List<Course> getAllCourses() {
        return (List<Course>) queryFactory.from(qCourse).orderBy(qCourse.id.asc()).fetch();
    }

    // UTILITIES

    public static void populateCoursesWithData() {
        String[] courses = new String[] {"MATHS", "ENGLISH", "FINE ART", "FURTHER MATHS", "AGRIC", "BIOLOGY", "CHEMISTRY", "PHYSICS", "ACCOUNTING"};

        for(String course : courses) {
            CourseController newCourse = new CourseController(course);

            newCourse.save();
        }

        System.out.println("Courses Populated");
    }
}
