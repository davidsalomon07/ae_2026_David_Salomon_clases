package com.pucetec.students.entities

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "professors")
open class Professor (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String = "",

    val email: String? = null,

    @OneToMany(mappedBy = "professor") // mapeamos igual que en Subject
    val subjects: MutableList<Subject> = mutableListOf(),
)