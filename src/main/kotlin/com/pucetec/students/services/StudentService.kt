package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// es el que almacena la logica del negocio
@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating student ${request.name}")

        if (request.name.isBlank()) {
            throw BlankNameException(message = "Name cannot be blank")
        } else {
            val studentEntity = request.toEntity()

            // guardar entidad
            val savedStudent = studentRepository.save(studentEntity)

            //retomar response
            return savedStudent.toResponse()
        }


        return savedStudent.toResponse()
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Getting student by id: $id")

        val student = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException("No se encontró el estudiante con ID: $id") }


        return student.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {

        return studentRepository.findAll().map { it.toResponse() }
    }
}