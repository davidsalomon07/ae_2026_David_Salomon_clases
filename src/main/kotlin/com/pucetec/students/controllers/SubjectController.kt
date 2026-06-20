package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/subjects")
class SubjectController(
    val subjectService: SubjectService
) {
    private val logger = LoggerFactory.getLogger(SubjectController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubject(
        @RequestBody
        request: SubjectRequest
    ): SubjectResponse {
        logger.info("Creating subject ${request.name}")
        return subjectService.createSubject(request)
    }

    @GetMapping
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectService.getAllSubjects()
    }

    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): SubjectResponse {
        logger.info("Getting subject by id $id")
        return subjectService.getSubjectById(id)
    }

    @PutMapping("/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        logger.info("Updating subject by id $id")
        return subjectService.updateSubject(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubject(@PathVariable id: Long) {
        logger.info("Deleting subject by id $id")
        subjectService.deleteSubject(id)
    }

}