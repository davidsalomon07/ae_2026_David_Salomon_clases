package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.services.EnrollmentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {
    private val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEnrollment(@RequestBody request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment")
        return enrollmentService.createEnrollment(request)
    }

    @GetMapping
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentService.getAllEnrollments()
    }

    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): EnrollmentResponse {
        logger.info("Getting enrollment by id $id")
        return enrollmentService.getEnrollmentById(id)
    }

    @PutMapping("/{id}")
    fun updateEnrollment(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentUpdateRequest
    ): EnrollmentResponse {
        logger.info("Updating enrollment by id $id")
        return enrollmentService.updateEnrollment(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEnrollment(@PathVariable id: Long) {
        logger.info("Deleting enrollment by id $id")
        enrollmentService.deleteEnrollment(id)
    }
}