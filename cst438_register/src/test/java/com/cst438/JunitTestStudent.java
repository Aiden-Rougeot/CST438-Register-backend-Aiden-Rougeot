package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;



@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	
	public static final int TEST_STUDENT_ID = 9;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";
	public static final int TEST_STUDENT_CODE_HOLD = 1;
	public static final int TEST_STUDENT_CODE_NO_HOLD = 0;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
    private MockMvc mvc;
	
	@Test
	public void addNewStudent() throws Exception{
		MockHttpServletResponse response;
		
		Student newStudent = new Student();
		newStudent.setStudent_id(TEST_STUDENT_ID);
		newStudent.setName(TEST_STUDENT_NAME);
		newStudent.setEmail(TEST_STUDENT_EMAIL);
		
		StudentDTO newStudentDTO = new StudentDTO();
		newStudentDTO.name = TEST_STUDENT_NAME;
		newStudentDTO.email = TEST_STUDENT_EMAIL;
		

		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(newStudent);
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .content(asJsonString(newStudentDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.student_id);
		
		verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void addOldStudent() throws Exception{
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.name = TEST_STUDENT_NAME;
		studentDTO.email = TEST_STUDENT_EMAIL;
		
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .content(asJsonString(studentDTO))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void updateStatus() throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setStudent_id(TEST_STUDENT_ID);
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		
		Optional <Student> optional = Optional.of(student);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(optional);
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/student/9"))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		verify(studentRepository).save(any(Student.class));
		
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
