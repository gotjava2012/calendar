package com.intuit.demo.controllers;

import com.intuit.demo.Service.RegistrationService;
import com.intuit.demo.model.Course;
import com.intuit.demo.model.Instructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegistrationController.class, secure = false)
public class RegistrationControllerTest {



        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RegistrationService registrationService;

        Course mockCourse = new Course("Course1", "Spring", "Math");

        String exampleCourseJson = "{\"id\":\"Course1\",\"name\":\"Spring\",\"instructor\":\"\",\"description\":\"Math\"}";


        @Test
        public void retrieveDetailsForCourse() throws Exception {

            Mockito.when(
                    registrationService.retrieveStudentCourse(Mockito.anyString(),
                            Mockito.anyString())).thenReturn(java.util.Optional.ofNullable(mockCourse));

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                    "/registration/students/Student1/courses/Course1").accept(
                    MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            System.out.println(result.getResponse());
            String expected = "{\"id\":\"Course1\",\"name\":\"Spring\",\"description\":\"Math\"}";

            JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        }

    @Test
    public void createInstructorCourse() throws Exception {
        Course mockCourse = new Course("Course1", "Spring", "Math");

        // registrationService.addCourse to respond back with mockCourse
        Mockito.when(
                registrationService.addInstructorCourse(Mockito.anyString(),
                        Mockito.any(Course.class))).thenReturn(mockCourse);

        // Send course as body to /instructor/Instructor1/courses
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/registration/instructors/Instructor1/courses")
                .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost/registration/instructors/Instructor1/courses/Course1",
                response.getHeader(HttpHeaders.LOCATION));

    }

        @Test
        public void createStudentCourse() throws Exception {
            Course mockCourse = new Course("Course1", "Spring", "Math");

            // registrationService.addCourse to respond back with mockCourse
            Mockito.when(
                    registrationService.addStudentCourse(Mockito.anyString(),
                            Mockito.any(Course.class))).thenReturn(mockCourse);

            // Send course as body to /students/Student1/courses
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/registration/students/Student1/courses")
                    .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                    .contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            MockHttpServletResponse response = result.getResponse();

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());

            assertEquals("http://localhost/registration/students/Student1/courses/Course1",
                    response.getHeader(HttpHeaders.LOCATION));

        }

    }