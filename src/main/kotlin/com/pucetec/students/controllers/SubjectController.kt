package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController(
    val subjectService: SubjectService
) {

    @PostMapping("/api/subject")
    fun createSubject(
        @RequestBody
        request: SubjectRequest
    ): SubjectResponse {
        return subjectService.createSubject(request)
    }

}