package com.danielezihe.controllers;

import com.danielezihe.hibernate.entity.QTeacher;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.entity.Teacher;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class TeacherController {
    private final int id;

    private final String name;

    private final Set<Integer> students_guiding;

    static QTeacher qTeacher = QTeacher.teacher;
    static JPAQueryFactory queryFactory = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());


    public TeacherController(String name) {
        this.id = 0;
        this.name = name;
        this.students_guiding = new HashSet<>();
    }

    public Teacher save() {
        Teacher newTeacher = new Teacher(id, name, students_guiding);

        HibernateUtil.addToDB(newTeacher);

        return newTeacher;
    }

    public static boolean guideNewStudent(Teacher teacher, Student student) {
        Transaction transaction = null;

        if(teacher.getStudentsGuiding().contains(student.getId()))
            return false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            teacher.getStudentsGuiding().add(student.getId());
            session.merge(teacher);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return true;
    }

    public static Teacher findTeacherByName(String teacherName) {
        return (Teacher) queryFactory.from(qTeacher).where(qTeacher.name.eq(teacherName)).fetchOne();
    }

    public static List<Teacher> getAllTeachers() {
        return (List<Teacher>) queryFactory.from(qTeacher).orderBy(qTeacher.id.asc()).fetch();
    }


    // UTILITIES

    public static void populateTeachersWithData() {
        String[] newTeachers = new String[] { "John", "Sandra", "Stanley", "Joy", "Philip" };

        for(String newTeacher : newTeachers) {
            TeacherController teacher = new TeacherController(newTeacher);

            teacher.save();
        }

        System.out.println("Teacher populated");
    }
}
