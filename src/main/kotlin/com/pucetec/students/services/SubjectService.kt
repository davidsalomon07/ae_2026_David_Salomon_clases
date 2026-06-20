package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubjectService (
    private val professorRepository: ProfessorRepository,
    private val subjectRepository: SubjectRepository
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    fun createSubject(request: SubjectRequest): SubjectResponse {
        logger.info("Creating subject ${request.name}")
        if (request.name.isBlank()) {
            throw BlankNameException("Name cannot be blank")
        }
        if (request.code.isBlank()) {
            throw BlankNameException("Code cannot be blank")
        }

        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFound(message = "Professor con id: ${request.professorId} no encontrado")
        }

        val subjectEntity = Subject(
            name = request.name,
            code = request.code,
            professor = professor
        )

        val savedSubject = subjectRepository.save(subjectEntity)

        return savedSubject.toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Getting subject by id $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            SubjectNotFound("Materia $id no encontrada")
        }
        return subject.toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        logger.info("Updating subject by id $id")
        subjectRepository.findById(id).orElseThrow {
            SubjectNotFound("Materia $id no encontrada")
        }

        if (request.name.isBlank()) {
            throw BlankNameException("Name cannot be blank")
        }
        if (request.code.isBlank()) {
            throw BlankNameException("Code cannot be blank")
        }

        val professor = professorRepository.findById(request.professorId).orElseThrow {
            ProfessorNotFound(message = "Professor con id: ${request.professorId} no encontrado")
        }

        val updatedSubject = Subject(
            id = id,
            name = request.name,
            code = request.code,
            professor = professor
        )

        return subjectRepository.save(updatedSubject).toResponse()
    }

    fun deleteSubject(id: Long) {
        logger.info("Deleting subject by id $id")
        val subject = subjectRepository.findById(id).orElseThrow {
            SubjectNotFound("Materia $id no encontrada")
        }
        subjectRepository.delete(subject)
    }
}