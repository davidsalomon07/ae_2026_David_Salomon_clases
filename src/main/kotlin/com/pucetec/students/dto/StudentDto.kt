package com.pucetec.students.dto

/***
 * {name: ana, email: ana@puce.com}
 * {name: juan}
 */
data class StudentRequest(
    val name: String,
    val email: String?,
)

/***
 * {id: 1, name: ana, email: ana@puce.com}
 * {id: 2, name: juan}
 */
data class StudentResponse(
    val id: Long,
    val name: String,
    val email: String?,
)