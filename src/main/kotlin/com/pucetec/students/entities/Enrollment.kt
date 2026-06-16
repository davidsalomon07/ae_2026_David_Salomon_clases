package com.pucetec.students.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "enrollments")
open class Enrollment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val status: String = "",

    //camelCase --> variables, metodos, atributos
    //snake_case --> jsons, nombres de tablas, nombres de columnas
    //kebab-case


    //val created_at: String = "", esto es un cero automatico

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY) //mapeamos igual que en Subject y Professor
    val subject: Subject,

    @ManyToOne(fetch = FetchType.LAZY) //Aca igual
    val student: Student,
)