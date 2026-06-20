package com.pucetec.students.repositories

import com.pucetec.students.entities.Professor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//es el que interactua con la base de datos
@Repository
interface ProfessorRepository : JpaRepository<Professor, Long>{

}