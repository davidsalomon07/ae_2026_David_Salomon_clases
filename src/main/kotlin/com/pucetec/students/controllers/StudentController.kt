package com.pucetec.students.controllers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.* // Import necesario para @PathVariable y @GetMapping

@RestController
@RequestMapping("/api/students")
open class StudentController(
    val studentService: StudentService
) {

    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(
        @RequestBody
        request: StudentRequest
    ): StudentResponse {
        logger.info("Creating student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping
    open fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudentById(
        @PathVariable
        id: Long
    ): StudentResponse {
        logger.info("Getting student by id $id")
        return studentService.getStudentById(id)
    }

    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): StudentResponse {
        logger.info("Updating student by id $id")
        return studentService.updateStudent(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(
        @PathVariable id: Long
    ) {
        logger.info("Deleting student by id $id")
        studentService.deleteStudent(id)
    }
}