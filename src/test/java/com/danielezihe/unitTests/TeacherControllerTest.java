package com.danielezihe.unitTests;

import com.danielezihe.controllers.CourseController;
import com.danielezihe.controllers.StudentController;
import com.danielezihe.controllers.TeacherController;
import com.danielezihe.hibernate.entity.*;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class TeacherControllerTest {
    QTeacher qTeacher;

    static {
        // Log4j
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
    }

    @BeforeEach
    void setUp() {
        qTeacher = QTeacher.teacher;
    }

    @Test
    @DisplayName("shouldPassIfTeacherIsSavedSuccessfully")
    void shouldPassIfTeacherIsSavedSuccessfully() {
        TeacherController newTeacher = new TeacherController("John");
        newTeacher.save();

        Assertions.assertEquals(1, TeacherController.getAllTeachers().size());
    }

    @Test
    @DisplayName("shouldPassIfATeacherCanSuccessfullyGuideAStudent")
    void shouldPassIfATeacherCanSuccessfullyGuideAStudent() {
        // create a teacher
        TeacherController newTeacher = new TeacherController("John");

        Teacher teacher = newTeacher.save();

        // create a Student
        StudentController newStudent = new StudentController("Philip", "JSS2");

        Student student = newStudent.save();

        // add a new student under teacher
        TeacherController.guideNewStudent(teacher, student);

        Teacher teacherFromDB = TeacherController.findTeacherByName(teacher.getName());

        Assertions.assertEquals(1, teacherFromDB.getStudentsGuiding().size());
        Assertions.assertTrue(teacherFromDB.getStudentsGuiding().contains(student.getId()));
    }
}
