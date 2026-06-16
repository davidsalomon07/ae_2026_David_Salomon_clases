package com.pucetec.students.entities

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "subjects")
open class Subject (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",

    val code: String = "",

// Esto esta mal    val professorId: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY) //Esto esta bien
    val professor: Professor,
)