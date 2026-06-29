package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.repositories.ProfessorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    // createProfessor
    @Test
    fun `createProfessor throws BlankNameException when name is blank`() {
        val request = ProfessorRequest(name = "  ", email = null)
        assertThrows(BlankNameException::class.java) {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `createProfessor saves and returns professor when request is valid`() {
        val request = ProfessorRequest(name = "Ana", email = "ana@mail.com")
        val savedEntity = Professor(id = 1L, name = "Ana", email = "ana@mail.com")

        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(savedEntity)

        val response = professorService.createProfessor(request)

        assertEquals(1L, response.id)
        assertEquals("Ana", response.name)
        assertEquals("ana@mail.com", response.email)
    }

    // getAllProfessors
    @Test
    fun `getAllProfessors returns mapped list of professors`() {
        val prof1 = Professor(id = 1L, name = "Ana", email = "ana@mail.com")
        val prof2 = Professor(id = 2L, name = "Juan", email = null)

        `when`(professorRepository.findAll()).thenReturn(listOf(prof1, prof2))

        val responses = professorService.getAllProfessors()

        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("Ana", responses[0].name)
        assertEquals(2L, responses[1].id)
        assertEquals("Juan", responses[1].name)
    }

    // getProfessorById
    @Test
    fun `getProfessorById throws ProfessorNotFound when not found`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(ProfessorNotFound::class.java) {
            professorService.getProfessorById(99L)
        }
    }

    @Test
    fun `getProfessorById returns professor when found`() {
        val prof = Professor(id = 1L, name = "Ana", email = "ana@mail.com")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(prof))

        val response = professorService.getProfessorById(1L)

        assertEquals(1L, response.id)
        assertEquals("Ana", response.name)
    }

    // updateProfessor
    @Test
    fun `updateProfessor throws ProfessorNotFound when not found`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())
        val request = ProfessorRequest(name = "Ana", email = null)

        assertThrows(ProfessorNotFound::class.java) {
            professorService.updateProfessor(99L, request)
        }
    }

    @Test
    fun `updateProfessor throws BlankNameException when name is blank`() {
        val prof = Professor(id = 1L, name = "Ana", email = "ana@mail.com")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(prof))
        val request = ProfessorRequest(name = "", email = null)

        assertThrows(BlankNameException::class.java) {
            professorService.updateProfessor(1L, request)
        }
    }

    @Test
    fun `updateProfessor saves and returns updated professor`() {
        val existingProf = Professor(id = 1L, name = "Ana", email = "ana@mail.com")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProf))

        val request = ProfessorRequest(name = "Ana Update", email = "new@mail.com")
        val updatedEntity = Professor(id = 1L, name = "Ana Update", email = "new@mail.com")

        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(updatedEntity)

        val response = professorService.updateProfessor(1L, request)

        assertEquals("Ana Update", response.name)
        assertEquals("new@mail.com", response.email)
    }

    // deleteProfessor
    @Test
    fun `deleteProfessor throws ProfessorNotFound when not found`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(ProfessorNotFound::class.java) {
            professorService.deleteProfessor(99L)
        }
    }

    @Test
    fun `deleteProfessor deletes when found`() {
        val existingProf = Professor(id = 1L, name = "Ana", email = "ana@mail.com")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProf))

        professorService.deleteProfessor(1L)

        verify(professorRepository, times(1)).delete(existingProf)
    }
}
