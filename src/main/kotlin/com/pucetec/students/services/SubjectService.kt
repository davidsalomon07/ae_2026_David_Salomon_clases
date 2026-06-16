package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService (
    private val professorRepository: ProfessorRepository,
    private val subjectRepository: SubjectRepository
) {
    fun createSubject(request: SubjectRequest): SubjectResponse {
        val professor = professorRepository.findById(request.professorId).orElseThrow{
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
}