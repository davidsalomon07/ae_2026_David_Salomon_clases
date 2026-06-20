package com.pucetec.students.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BlankNameException::class)
    fun handleBlankNameException(
        e: BlankNameException
    ): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Nombre en blanco - ERROR",
            source = "StudentService"
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFoundException(
        e: StudentNotFoundException
    ): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Estudiante no encontrado",
            source = "StudentService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(ProfessorNotFound::class)
    fun handleProfessorNotFoundException(
        e: ProfessorNotFound
    ): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Profesor no encontrado",
            source = "ProfessorService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(SubjectNotFound::class)
    fun handleSubjectNotFoundException(
        e: SubjectNotFound
    ): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Materia no encontrada",
            source = "SubjectService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(EnrollmentNotFound::class)
    fun handleEnrollmentNotFoundException(
        e: EnrollmentNotFound
    ): ResponseEntity<ExceptionMessage> {
        val response = ExceptionMessage(
            message = e.message ?: "Inscripción no encontrada",
            source = "EnrollmentService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }
}

data class ExceptionMessage(
    val message: String,
    val source: String,
)