package com.danielezihe.unitTests;

import com.danielezihe.controllers.CourseController;
import com.danielezihe.hibernate.entity.Course;
import com.danielezihe.hibernate.entity.QCourse;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.danielezihe.hibernate.util.HibernateUtilTest;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.checkerframework.checker.units.qual.C;
import org.h2.engine.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class CourseControllerTest {
    QCourse qCourse;

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        qCourse = QCourse.course;
    }

    @Test
    @DisplayName("shouldPassIfCourseIsSaved")
    void shouldPassIfCourseIsSaved() {
        CourseController newCourse = new CourseController("English");

        Course course = newCourse.save();

        Course courseFromDB = CourseController.findCourse(course.getId());

        Assertions.assertEquals(1, CourseController.getAllCourses().size());
        Assertions.assertEquals(course.getName(), courseFromDB.getName());
    }

}
