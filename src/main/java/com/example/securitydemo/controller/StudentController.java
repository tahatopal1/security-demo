package com.example.securitydemo.controller;

import com.example.securitydemo.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final List<Student> studentList = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(){
        return new ResponseEntity<>(studentList, HttpStatus.OK);
    }

    @GetMapping("/{reg}")
    public ResponseEntity<Student> getStudentByRegistrationNumber(@PathVariable("reg") Integer regNum){
        Student student = getStudent(regNum);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> saveStudent(@RequestBody Student student){
        boolean doesExist = studentList.stream().anyMatch(st -> st.getRegistrationNumber()
                .equals(student.getRegistrationNumber()));
        if (doesExist){
            throw new RuntimeException("There's already a student with registration number: " + student.getRegistrationNumber());
        }
        studentList.add(student);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{reg}")
    public ResponseEntity<Void> updateStudent(@PathVariable("reg") Integer regNum, @RequestBody Student studentDTO){
        Student student = getStudent(regNum);
        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{reg}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("reg") Integer regNum){
        Student student = getStudent(regNum);
        studentList.remove(student);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private Student getStudent(Integer regNum) {
        Optional<Student> studentOpt = studentList.stream()
                .filter(student -> student.getRegistrationNumber().equals(regNum))
                .findFirst();
        if (studentOpt.isEmpty()){
            throw new RuntimeException("There's no such student with registration number: " + regNum);
        }
        return studentOpt.get();
    }

}
