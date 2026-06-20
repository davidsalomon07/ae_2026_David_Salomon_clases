package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment for student ${request.studentId} and subject ${request.subjectId}")
        val student = studentRepository.findById(request.studentId).orElseThrow {
            StudentNotFoundException("Estudiante ${request.studentId} no encontrado")
        }
        val subject = subjectRepository.findById(request.subjectId).orElseThrow {
            SubjectNotFound("Materia ${request.subjectId} no encontrada")
        }

        val enrollmentEntity = request.toEntity(student, subject)
        return enrollmentRepository.save(enrollmentEntity).toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Getting enrollment by id $id")
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFound("Inscripción $id no encontrada")
        }
        return enrollment.toResponse()
    }

    fun updateEnrollment(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        logger.info("Updating enrollment by id $id")
        val existingEnrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFound("Inscripción $id no encontrada")
        }

        val updatedEnrollment = Enrollment(
            id = existingEnrollment.id,
            status = request.status,
            createdAt = existingEnrollment.createdAt,
            student = existingEnrollment.student,
            subject = existingEnrollment.subject
        )

        return enrollmentRepository.save(updatedEnrollment).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        logger.info("Deleting enrollment by id $id")
        val enrollment = enrollmentRepository.findById(id).orElseThrow {
            EnrollmentNotFound("Inscripción $id no encontrada")
        }
        enrollmentRepository.delete(enrollment)
    }
}