package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student
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
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")

        // consultar todos los estudiantes
        val savedStudents = studentRepository.findAll() // lista de estudiantes o un arreglo vacio

        //convertir al response adecuado
        return savedStudents.map {
            it.toResponse()
        }
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Getting student by id $id")
        val student = studentRepository.findById(id).orElseThrow {
            StudentNotFoundException("Estudainte $id no encontrado")
        }
        return student.toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        logger.info("Updating student by id $id")
        studentRepository.findById(id).orElseThrow {
            StudentNotFoundException("Estudainte $id no encontrado")
        }
        
        if (request.name.isBlank()) {
            throw BlankNameException(message = "Name cannot be blank")
        }

        val updatedStudent = Student(
            id = id,
            name = request.name,
            email = request.email
        )
        
        return studentRepository.save(updatedStudent).toResponse()
    }

    fun deleteStudent(id: Long) {
        logger.info("Deleting student by id $id")
        val student = studentRepository.findById(id).orElseThrow {
            StudentNotFoundException("Estudainte $id no encontrado")
        }
        studentRepository.delete(student)
    }
}