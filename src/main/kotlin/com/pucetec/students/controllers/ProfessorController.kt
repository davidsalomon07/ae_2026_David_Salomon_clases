package com.pucetec.students.controllers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.services.ProfessorService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService
) {
    private val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessor(@RequestBody request: ProfessorRequest): ProfessorResponse {
        logger.info("Creating professor ${request.name}")
        return professorService.createProfessor(request)
    }

    @GetMapping
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        return professorService.getAllProfessors()
    }

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ProfessorResponse {
        logger.info("Getting professor by id $id")
        return professorService.getProfessorById(id)
    }

    @PutMapping("/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ProfessorResponse {
        logger.info("Updating professor by id $id")
        return professorService.updateProfessor(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfessor(@PathVariable id: Long) {
        logger.info("Deleting professor by id $id")
        professorService.deleteProfessor(id)
    }
}
