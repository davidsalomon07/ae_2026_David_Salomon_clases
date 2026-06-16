package com.pucetec.students.mappers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.entities.Student

// mapea un request a un entity
fun StudentRequest.toEntity(): Student(
    name = this.name,
    email = this.email
)


//mapea un entity a un response
fun Student.toResponse(): StudentResponse(
    id = this.id,
    name = this.name,
    email = this.email
)