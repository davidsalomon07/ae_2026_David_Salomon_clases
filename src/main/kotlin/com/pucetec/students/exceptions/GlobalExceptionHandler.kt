package com.pucetec.students.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BlankNameException::class)
    fun handleBlankNameException(
        e: BlankMesaggeException
    ): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Nombre en blanco - ERROR",
            source = "StudentService"
        )
        return ResponseEntity
            .status(status = HttpStatus.BAD_REQUEST)
            .body(body = response)
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
}

data class ExceptionMessage(
    val message: String,
    val source: String,
)