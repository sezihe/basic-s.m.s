package com.danielezihe.controllers;

import com.danielezihe.hibernate.entity.Course;
import com.danielezihe.hibernate.entity.QStudent;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.entity.Teacher;
import com.danielezihe.hibernate.util.HibernateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class StudentController {
    private final int id;
    private final String name;
    private final String className;
    private final Set<Integer> registered_courses;

    static QStudent qStudent = QStudent.student;
    static JPAQueryFactory queryFactory = new JPAQueryFactory(HibernateUtil.getSessionFactory().openSession());


    public StudentController(String name, String className) {
        this.id = 1;
        this.name = name;
        this.className = className;
        this.registered_courses = new HashSet<>();
    }

    public Student save() {
        Student newStudent =  new Student(id, name, className, null, registered_courses);

        HibernateUtil.addToDB(newStudent);

        return newStudent;
    }

    public static boolean addAGuide(Student student, Teacher guide) {
        Transaction transaction = null;

        if(student.getGuide() != null)
            return false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            student.setGuide(guide);

            session.merge(student);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return true;
    }

    public static boolean registerNewCourse(Student student, int courseId) {
        Transaction transaction = null;

        if(student.getRegisteredCoursesIds().contains(courseId))
            return false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            student.getRegisteredCoursesIds().add(courseId);
            session.merge(student);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return true;
    }

    public static Student findStudentByName(String studentName) {
        return (Student) queryFactory.from(qStudent).where(qStudent.name.eq(studentName)).fetchOne();
    }

    public static List<Student> getAllStudents() {
        return (List<Student>) queryFactory.from(qStudent).orderBy(qStudent.id.asc()).fetch();
    }
}
