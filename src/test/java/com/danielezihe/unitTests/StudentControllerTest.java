package com.danielezihe.unitTests;

import com.danielezihe.controllers.CourseController;
import com.danielezihe.controllers.StudentController;
import com.danielezihe.controllers.TeacherController;
import com.danielezihe.hibernate.entity.*;
import com.danielezihe.hibernate.util.HibernateUtilTest;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class StudentControllerTest {
    JPAQuery<Student> query;
    QStudent qStudent;

    public static final Logger logger = LogManager.getLogger(CourseControllerTest.class);

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        query = new JPAQuery<>(HibernateUtilTest.getSessionFactory().openSession());
        qStudent = QStudent.student;
    }

    @Test
    @DisplayName("shouldPassIfStudentIsSaved")
    void shouldPassIfStudentIsSaved() {
        StudentController newStudent = new StudentController("Daniel", "JSS2");

        Student student = newStudent.save();

        Student studentFromDB = StudentController.findStudentByName(student.getName());

        Assertions.assertEquals(1, StudentController.getAllStudents().size());
        Assertions.assertEquals(student.getName(), studentFromDB.getName());
    }

    @Test
    @DisplayName("shouldPassIfATeacherGuideIsAddedSuccessfully")
    void shouldPassIfATeacherGuideIsAddedSuccessfully() {
        // create a teacher
        TeacherController newTeacher = new TeacherController("John");

        Teacher teacher = newTeacher.save();

        // create a Student
        StudentController newStudent = new StudentController("Philip", "JSS2");

        Student student = newStudent.save();

        // add a guide
        StudentController.addAGuide(student, teacher);

        Student studentFromDB= StudentController.findStudentByName(student.getName());

        assert studentFromDB != null;
        assert studentFromDB.getGuide() != null;
        Assertions.assertEquals(teacher.getName(), studentFromDB.getGuide().getName());
    }

    @Test
    @DisplayName("shouldPassIfACourseIsRegisteredSuccessfully")
    void shouldPassIfACourseIsRegisteredSuccessfully() {
        // create a Student
        StudentController newStudent = new StudentController("Philip", "JSS2");

        Student student = newStudent.save();

        // create a new Course
        CourseController newCourse = new CourseController("ENGLISH");

        Course course = newCourse.save();

        // register course
        StudentController.registerNewCourse(student, course.getId());

        Student studentFromDB = StudentController.findStudentByName(student.getName());

        assert studentFromDB != null;
        assert studentFromDB.getRegisteredCoursesIds() != null;

        Assertions.assertEquals(1, studentFromDB.getRegisteredCoursesIds().size());
    }
}
