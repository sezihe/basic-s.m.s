package com.danielezihe;

import com.danielezihe.controllers.CourseController;
import com.danielezihe.controllers.StudentController;
import com.danielezihe.controllers.TeacherController;
import com.danielezihe.hibernate.entity.Course;
import com.danielezihe.hibernate.entity.Student;
import com.danielezihe.hibernate.entity.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author EZIHE S. DANIEL
 * CreatedAt: 02/10/2021
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CourseController.populateCoursesWithData();
        TeacherController.populateTeachersWithData();
        println("Welcome!");
        goToMain();
    }

    static void handleRegisterStudent() {
        println("Kindly Enter Student name and class name both seperated with commas Eg: Daniel,JSS2");
        String[] data = scanner.nextLine().split(",");
        String name = data[0];
        String className = data[1];

        // call guava methods to validate input
        try {
            Preconditions.checkNotNull(name, "Invalid Input. Please input the student name and class both seperated with a comma.");
            Preconditions.checkNotNull(className, "Invalid Input. Please input the student name and class both seperated with a comma.");

            Preconditions.checkArgument(name.length() > 2, "Invalid Student Name");
            Preconditions.checkArgument(className.length() < 8, "Invalid Class Name");
        } catch (NullPointerException | IllegalArgumentException ex) {
            println(ex.getMessage());
        }

        StudentController newStudent = new StudentController(name, className);
        Student student = newStudent.save();

        println(student.getName() + " registered successfully");
    }

    static void handleGoToStudentDashboard() {
        println("Kindly enter Student Name: ");
        String studentName = scanner.nextLine();

        // validate Student Name with Guava
        try {
            validateStudentName(studentName);
        } catch (NullPointerException | IllegalArgumentException ex) {
            println(ex.getMessage());
        }

        Student student = StudentController.findStudentByName(studentName);

        if(student == null)
            println("Student with name '" + studentName + "' not found. Please register first!");
        else
            goToStudentDashBoard(student);
    }

    static void handleRegisterCourses(Student student, String input) {
        String[] courseIds = input.split(",");

        int coursesAlreadyRegisteredLength = student.getRegisteredCoursesIds().size();

        Range<Integer> acceptedCoursesCountRange = Range.closed(5, 7);
        if(coursesAlreadyRegisteredLength >= acceptedCoursesCountRange.upperEndpoint())
            println("You have used up all your available course registration slots");
        else {
            if(coursesAlreadyRegisteredLength <= 0 && !acceptedCoursesCountRange.contains(courseIds.length))
                println("ERROR: You are expected to Register a minimum of 5 courses and a maximum of 7");
            else if(coursesAlreadyRegisteredLength > 0 && !acceptedCoursesCountRange.contains(coursesAlreadyRegisteredLength + courseIds.length)) {
                int remainingAvailableRegistrationSpots = acceptedCoursesCountRange.upperEndpoint() - coursesAlreadyRegisteredLength;
                String courseOrCourses = remainingAvailableRegistrationSpots > 1 ? "courses" : "course";
                println("ERROR: You have already registered " + coursesAlreadyRegisteredLength + " courses, you can only register " + remainingAvailableRegistrationSpots + " more " + courseOrCourses);
            }
            else {
                int successfulRegistersCount = 0;
                for(String courseId : courseIds) {
                    if(!StudentController.registerNewCourse(student, Integer.parseInt(courseId)))
                        println("ERROR: You've already registered this course!");
                    else
                        successfulRegistersCount++;
                }
                String courseOrCourses = successfulRegistersCount > 1 ? "courses" : "course";
                println("\n" + successfulRegistersCount + " " + courseOrCourses + " registered successfully");

            }
        }

    }

    static void handleGetListOfAllStudents() {
        StringBuilder studentsStringBuilder = new StringBuilder();
        List<Student> students = StudentController.getAllStudents();

        if (students.isEmpty()) {
            println("--------------------STUDENTS--------------------");
            println("                   No Students.                  ");
            println("-------------------------------------------------");
        } else {
            for (Student student : students) {
                String studentStr = student.getId() + ". " + student.getName() + " CLASS: " +
                        student.getClassName();

                appendLine(studentsStringBuilder, studentStr);
            }
        }

        println("--------------------STUDENTS--------------------");

        println(studentsStringBuilder.toString());

        println("-------------------------------------------------");
    }

//    static void handleGetListOfCourses() {
//        StringBuilder coursesStringBuilder = new StringBuilder();
//
//        println("Kindly Enter Student Name: ");
//        String studentName = scanner.nextLine();
//
//        // call guava methods to validate input
//        try {
//            validateStudentName(studentName);
//        } catch (NullPointerException | IllegalArgumentException ex) {
//            println(ex.getMessage());
//        }
//
//        var response = StudentController.getStudentRegisteredCourses(studentName);
//
//        if (response.equals("NO_STUDENT_FOUND")) {
//            println("Student with name '" + studentName + "' was not found!");
//        } else {
//            Set<Integer> registeredCoursesIds = (Set<Integer>) response;
//            if (registeredCoursesIds.isEmpty())
//                println("No course registered! Please register your courses now!");
//            else {
//                for (int courseId : registeredCoursesIds) {
//                    Course course = CourseController.findCourse(courseId);
//
//                    appendLine(coursesStringBuilder, course.getName());
//                }
//
//                println("Courses registered by " + studentName + ":");
//
//                println(coursesStringBuilder.toString());
//
//                println("Total: " + registeredCoursesIds.size());
//            }
//        }
//    }

    static void handleGetListOfCourses(Student student) {
        StringBuilder coursesStringBuilder = new StringBuilder();

        Set<Integer> registeredCoursesIds = student.getRegisteredCoursesIds();
        if (registeredCoursesIds.isEmpty())
            println("No course registered! Press 1 to register your courses now!");
        else {
            for (int courseId : registeredCoursesIds) {
                Course course = CourseController.findCourse(courseId);

                appendLine(coursesStringBuilder, course.getName());
            }

            println("Courses registered by " + student.getName() + ":");

            println(coursesStringBuilder.toString());

            println("Total: " + registeredCoursesIds.size());
        }
    }

    static void handleGetListOfAllTeachers() {
        StringBuilder teachersStringBuilder = new StringBuilder();
        List<Teacher> teachers = TeacherController.getAllTeachers();

        assert teachers != null;
        if (teachers.isEmpty()) {
            println("--------------------TEACHERS--------------------");
            println("                   No Teachers.                 ");
            println("------------------------------------------------");
        } else {
            for (Teacher teacher : teachers) {
                String teacherStr = teacher.getId() + ". " + teacher.getName();

                appendLine(teachersStringBuilder, teacherStr);
            }
        }

        println("--------------------TEACHERS--------------------");

        println(teachersStringBuilder.toString());

        println("------------------------------------------------");
    }

//    static void handleGetTeacherAssignedToStudent() {
//        println("Kindly Enter Student Name: ");
//        String studentName = scanner.nextLine();
//
//        // validate with guava
//        validateStudentName(studentName);
//
//        Teacher teacher = StudentController.getGuide(studentName);
//
//        if(teacher == null)
//            println("You do not have a Teacher assigned to you yet. Press  to get a teacher assigned to you");
//
//        try {
//            String teacherJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(teacher);
//
//            println(teacherJsonString);
//        } catch (JsonProcessingException ex)  {
//            println(ex.getMessage());
//        }
//    }

    static void handleGetTeacherAssignedToStudent(Student student) {
        Teacher teacher = student.getGuide();

        if(teacher == null)
            println("You do not have a Teacher assigned to you yet. Press 2 to get a teacher assigned to you");
        else {
            try {
                // Jackson JSON String
                String teacherJsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(teacher);

                println(teacherJsonString);
            } catch (JsonProcessingException ex)  {
                println(ex.getMessage());
            }
        }
    }

    static void handleRequestForATeacherGuide(Student student) {
        Teacher teacherGuide = getATeacherAlgorithm();

        if(StudentController.addAGuide(student, teacherGuide) && TeacherController.guideNewStudent(teacherGuide, student))
            println("You've been assigned a new Teacher Guide. Please kindly locate Teacher with name '" + teacherGuide.getName() + "'.");
        else
            println("ERROR: You have already been assigned a Teacher Guide. Press 3 to see who.");
    }

    static void handleLogout() { goToMain(); }

    static void goToMain() {
        while (true) {
            printFirstPrompt();
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 -> handleRegisterStudent();
                case 2 -> handleGoToStudentDashboard();
                case 3 -> handleGetListOfAllStudents();
                case 4 -> handleGetListOfAllTeachers();
                case 5 -> System.exit(0);
                default -> println("Please enter a valid option");
            }
        }
    }

    static void goToStudentDashBoard(Student student) {
        boolean isRunning = true;
        while (isRunning) {
            printStudentDashBoardPrompt(student.getName());
            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 1 -> goToRegisterCourses(student);
                case 2 -> handleRequestForATeacherGuide(student);
                case 3 -> handleGetTeacherAssignedToStudent(student);
                case 4 -> handleGetListOfCourses(student);
                case 5 -> {
                    isRunning = false;
                    handleLogout();
                }
                default -> println("Please enter a valid option");
            }
        }
    }

    static void goToRegisterCourses(Student student) {
        printRegisterCoursesPrompt();
        String input = scanner.nextLine();
        switch (input) {
            case "0" -> goToStudentDashBoard(student);
            default -> handleRegisterCourses(student, input);
        }
    }

    // UTILITIES
    static void printFirstPrompt() {
        println("""
                -MAIN MENU-
                Press 1 to Register a new Student
                Press 2 to go to Student dashboard
                Press 3 to get a List of all Students in the school
                Press 4 to get a List of all the Teachers in the school
                Press 5 to Quit.""");
    }

    static void printStudentDashBoardPrompt(String studentName) {
        println("\n-STUDENT DASHBOARD-\n" +
                "Hello, " + studentName + "!\n" +
                "Press 1 to Register Courses\n" +
                "Press 2 to request for a Teacher guide\n" +
                "Press 3 to see your Teacher Guide\n" +
                "Press 4 to view all your registered Courses\n" +
                "Press 5 to logout.");
    }

    static void printRegisterCoursesPrompt() {
        StringBuilder coursesStringBuilder = new StringBuilder();
        List<Course> courses = CourseController.getAllCourses();

        for(Course course : courses) {
            String courseStr = course.getId() + ". " + course.getName();
            appendLine(coursesStringBuilder, courseStr);
        }

        println("\n-REGISTER COURSES-\n" +
                "Courses Available: \n" +
                coursesStringBuilder +
                "Press 0 to go back.\n" +
                "You are expected to Register a minimum of 5 courses and a maximum of 7.\n" +
                "Kindly register your courses by inputting the number beside the courses and separating them with a comma. Eg. 1,2,4,6,7,8");
    }

    static void validateStudentName(String studentName) throws NullPointerException, IllegalArgumentException {
        Preconditions.checkNotNull(studentName, "Student Name cannot be null");

        Preconditions.checkArgument(studentName.length() > 2, "Invalid Student Name");
    }

    static <T> void println(T value) {
        System.out.println(value);
    }

    static StringBuilder appendLine(StringBuilder builder, String line) {
        return builder.append(line).append("\n");
    }

    static StringBuilder appendTab(StringBuilder builder, String line) {
        return builder.append("\t").append(line).append("\n");
    }

    private static Teacher getATeacherAlgorithm() {
        // basically gets the Teacher with the least amount of Students guiding

        List<Teacher> teachers = TeacherController.getAllTeachers();
        int studentsGuidingCount = 0;
        Teacher selectedTeacher = null;

        for(Teacher teacher : teachers) {
            int count = teacher.getStudentsGuiding().size();
            if(count <= studentsGuidingCount) {
                studentsGuidingCount = count;
                selectedTeacher = teacher;
            }
        }

        return selectedTeacher;
    }
}
