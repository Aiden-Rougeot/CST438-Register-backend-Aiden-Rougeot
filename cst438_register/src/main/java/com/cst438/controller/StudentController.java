package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;
	@RequestMapping("/students")
	public void getStudents() {
		System.out.println("Students");
	}
	
	@PostMapping("/student")
	public StudentDTO addStudent (@RequestBody StudentDTO s) {
		if(studentRepository.findByEmail(s.email) == null) {
			Student newStudent = new Student();
			newStudent.setName(s.name);
			newStudent.setEmail(s.email);
			newStudent = studentRepository.save(newStudent);
			s.student_id = newStudent.getStudent_id();
			return s;
		} else {
			System.out.println("/student already exists. "+s.email);
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student already exists. " );
		}
	}
	
	@PutMapping("/student/{id}")
	public void updateStatus(@PathVariable ("id") int sid){
		Student s = studentRepository.findById(sid).get();
		if(s != null) {
			if (s.getStatusCode() == 0) {
				s.setStatusCode(1);
			} else {
				s.setStatusCode(0);
			}
			studentRepository.save(s);
			System.out.println(s.getName() + " status code: " + s.getStatusCode());
		}
	}
}
