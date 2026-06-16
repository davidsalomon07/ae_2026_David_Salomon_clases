package com.pucetec.students.controllers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.* // Import necesario para @PathVariable y @GetMapping

@RestController
open class StudentController(
    val studentService: StudentService
) {

    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    fun createStudent(
        @RequestBody
        request: StudentRequest
    ): StudentResponse {
        logger.info("Creating student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping("/api/students")
    open fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
    }

    @GetMapping("/api/students/{id}")
    fun getStudentById(
        @PathVariable
        id: Long
    ): StudentResponse{
    logger.info("Getting student by id $id")}
}