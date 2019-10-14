package com.intuit.demo.Service;

import com.intuit.demo.model.Course;
import com.intuit.demo.model.Instructor;
import com.intuit.demo.model.Student;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.*;
//adding this annotation notifies spring to manage and create this instance
@Component
public class RegistrationService {

    private static List<Student> students = new ArrayList<>();
    private static List<Instructor> instructors = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();

    static {
        //Initialize Data

        Course course1 = new Course("Course1", "Spring","Math");
        Course course2 = new Course("Course2", "Spring", "Science");
        Course course3 = new Course("Course3", "Spring", "History");
        Course course4 = new Course("Course4", "Spring", "PE");

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);


        Student jack = new Student( "Student1", "Jack McMuffin","History major", new ArrayList<>(asList(course1, course2, course3, course4) ));
        Student jill = new Student( "Student2", "Jill Flowers","Math major", new ArrayList<>(asList( course3, course4) ));

        students.add(jack);
        students.add(jill);

        Instructor akshay  = new Instructor("Instructor1","Akshay","PHD",  new ArrayList<>(asList( course3) ));
        Instructor stefan  = new Instructor("Instructor2","Stefan","Masters",  new ArrayList<>(asList( course2) ));
        Instructor kian  = new Instructor("Instructor3","Kian","BS", new ArrayList<>(asList( course1) ));

        instructors.add(akshay);
        instructors.add(stefan);
        instructors.add(kian);

    }

    public List<Instructor> retrieveAllInstructors(){
        return instructors;
    }
    public List<Student> retrieveAllStudents() {
        return students;
    }

    public Optional<Instructor> retrieveInstructor(String instructorId){
        return instructors.stream().filter(instructor -> instructor.getId().equalsIgnoreCase(instructorId)).findFirst();
    }

    public Optional<String> returnStringMethod(){
        Optional.ofNullable().map(x -> x.toString())
                .orElse();

    }

    public Optional<Student> retrieveStudent(String studentId){
        return students.stream().filter(instructor -> instructor.getId().equalsIgnoreCase(studentId)).findFirst();
    }

    public Optional<Student> retrieveCourses(String studentId){
        return students.stream().filter(instructor -> instructor.getId().equalsIgnoreCase(studentId)).findFirst();
    }

    public List<Course> retrieveAvailableCourses() {
        return courses;
    }

    public List<Course> retrieveInstructorCourses(String instructorId) {
        Optional<Instructor> instructor = retrieveInstructor(instructorId);

        if (!instructor.isPresent()) {
            return null;
        }
        return instructor.get().getCourses();
    }

    public List<Course> retrieveStudentCourses(String studentId) {
        Optional<Student> student = retrieveStudent(studentId);

        if (!student.isPresent()) {
            return null;
        }
        return student.get().getCourses();
    }

    public Optional<Course> retrieveInstructorCourse(String instructorId, String courseId){
        Optional<Instructor> instructor = retrieveInstructor(instructorId);

        if (!instructor.isPresent()) {
            return null;
        }
        return instructor.get().getCourses().stream().filter( item -> item.getId().contains(courseId)).findFirst();
    }

    public Optional<Course> retrieveStudentCourse(String studentId, String courseId){
        Optional<Student> student = retrieveStudent(studentId);

        if (!student.isPresent()) {
            return null;
        }
        return student.get().getCourses().stream().filter(course -> course.getId().contains(courseId)).findFirst();
    }

    private SecureRandom random = new SecureRandom();

    public Course addInstructorCourse(String instructorId, Course course) {
        Optional<Instructor> instructor = retrieveInstructor(instructorId);

        if (!instructor.isPresent()) {
            return null;
        }

        boolean okToadd = true;
        //check through all the instructors to see if the class has already been assigned.
        okToadd = isOkToaddInstructorToCourse(course, okToadd);

        if(okToadd){
            instructor.get().getCourses().add(course);
        }

        return null;
    }
    // loops through all the instructors and makes sure no other instructor has been assigned the course
    private boolean isOkToaddInstructorToCourse(Course course, boolean okToadd) {
        for( Instructor instrut : instructors){
            for(Course co : instrut.getCourses()){
                if(course.getId().equalsIgnoreCase(co.getId()))
                    okToadd = false;
            }
        }
        return okToadd;
    }

    public Course addCourse(Course course){
        if(!checkCourseAvailability(course))
            courses.add(course);
        return course;
    }
    // Loop through courses to see if its available
    private boolean checkCourseAvailability(Course course) {
       return courses.stream().anyMatch(course1 -> course1.getId().equalsIgnoreCase(course.getId()));
    }

    public Course addStudentCourse(String studentId, Course course) {
        Optional<Student> student = retrieveStudent(studentId);

        if (!student.isPresent()) {
            return null;
        }

        if( !student.get().getCourses().stream().anyMatch(course1 -> course1.getId().equalsIgnoreCase(course.getId()))) {
            student.get().getCourses().add(course);
            return course;
        }
        return null;
    }

    public Course deleteInstructorCourse(String instructorId, Course course) {
        Optional<Instructor> instructor = retrieveInstructor(instructorId);

        if (!instructor.isPresent()) {
            return null;
        }

        if( instructor.get().getCourses().stream().anyMatch(course1 -> course1.getId().equalsIgnoreCase(course.getId()))) {
            List<Course> list = new ArrayList<>();
            instructor.get().getCourses().stream().forEach(course1 -> {
                if(  !course.getId().equalsIgnoreCase(course1.getId())){
                    list.add(course1);
                }
            });
            instructor.get().setCourses(list);
            return course;

        }
        return null;
    }

    public Course deleteStudentCourse(String studentId, Course course) {
        Optional<Student> student = retrieveStudent(studentId);

        if (!student.isPresent()) {
            return null;
        }
        //check if the student is registered for class.
        if( student.get().getCourses().stream().anyMatch(course1 -> course1.getId().equalsIgnoreCase(course.getId()))) {
//            student.get().getCourses().remove(course);
            // creating new list of classes to add currently registered that are not the one being deleted
            List<Course> list = new ArrayList<>();
            student.get().getCourses().stream().forEach(course1 -> {
                if(  !course.getId().equalsIgnoreCase(course1.getId())){
                    list.add(course1);
                }
            });
            // setting the new list of still registered classes
            student.get().setCourses(list);
            return course;
        }
        return null;
    }
}
