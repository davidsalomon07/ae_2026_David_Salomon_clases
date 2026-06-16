package com.pucetec.students.repositories

import com.pucetec.students.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//es el que interactua con la base de datos
@Repository
interface SubjectRepository : JpaRepository<Student, Long>{

}