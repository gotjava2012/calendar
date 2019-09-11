package com.intuit.demo.controllers;


import com.intuit.demo.Service.RegistrationService;
import com.intuit.demo.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class RegistrationController {
    //using autowired annotation injects the service into the controller
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registration/courses")
    public List<Course> retrieveCourses() {
        return registrationService.retrieveAvailableCourses();
    }

    @GetMapping("/registration/instructors/{instructorId}/courses")
    public List<Course> retrieveCoursesForInstructor(@PathVariable String instructorId) {
        return registrationService.retrieveInstructorCourses(instructorId);
    }

    @GetMapping("/registration/students/{studentId}/courses")
    public List<Course> retrieveCoursesForStudent(@PathVariable String studentId) {
        return registrationService.retrieveStudentCourses(studentId);
    }

    @GetMapping("/registration/instructors/{instructorId}/courses/{courseId}")
    public Optional<Course> retrieveDetailsForInstructorCourse(@PathVariable String instructorId,
                                                            @PathVariable String courseId) {
        return registrationService.retrieveInstructorCourse(instructorId, courseId);
    }

    @GetMapping("/registration/students/{studentId}/courses/{courseId}")
    public Optional<Course> retrieveDetailsForStudentCourse(@PathVariable String studentId,
                                                     @PathVariable String courseId) {
        return registrationService.retrieveStudentCourse(studentId, courseId);
    }

    @PostMapping("/registration/courses/add")
    public ResponseEntity<Void> addNewCourse(
             @RequestBody Course newCourse) {

        if (newCourse == null)
            return ResponseEntity.noContent().build();

        registrationService.addCourse(newCourse);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(newCourse.getId()).toUri();
        //Return a status of created. Also return the location of created resource as a Response Header.
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/registration/instructors/{instructorId}/courses")
    public ResponseEntity<Void> registerInstructorForCourse(
            @PathVariable String instructorId, @RequestBody Course newCourse) {

        Course course = registrationService.addInstructorCourse(instructorId, newCourse);

        if (course == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(course.getId()).toUri();
        //Return a status of created. Also return the location of created resource as a Response Header.
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/registration/students/{studentId}/courses")
    public ResponseEntity<Void> registerStudentForCourse(
            @PathVariable String studentId, @RequestBody Course newCourse) {

        Course course = registrationService.addStudentCourse(studentId, newCourse);

        if (course == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(course.getId()).toUri();
        //Return a status of created. Also return the location of created resource as a Response Header.
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/registration/instructors/{instructorId}/courses")
    public ResponseEntity<Void> deletetInstructorForCourse(
            @PathVariable String instructorId, @RequestBody Course removeCourse) {

        Course course = registrationService.deleteInstructorCourse(instructorId, removeCourse);

        if (course == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(course.getId()).toUri();
        //Return a status of ok. Also return the location of created resource as a Response Header.
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/registration/students/{studentId}/courses")
    public ResponseEntity<Void> deletetStudentForCourse(
            @PathVariable String studentId, @RequestBody Course removeCourse) {

        Course course = registrationService.deleteStudentCourse(studentId, removeCourse);

        if (course == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(course.getId()).toUri();
        //Return a status of ok. Also return the location of created resource as a Response Header.
        return ResponseEntity.ok().build();
    }

}
