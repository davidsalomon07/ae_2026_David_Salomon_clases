package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    // createSubject
    @Test
    fun `createSubject throws BlankNameException when name is blank`() {
        val request = SubjectRequest(name = "   ", code = "123", professorId = 1L)
        assertThrows(BlankNameException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject throws BlankNameException when code is blank`() {
        val request = SubjectRequest(name = "Math", code = "   ", professorId = 1L)
        assertThrows(BlankNameException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject throws ProfessorNotFound when professor does not exist`() {
        val request = SubjectRequest(name = "Math", code = "MTH", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(ProfessorNotFound::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject saves and returns subject on valid request`() {
        val request = SubjectRequest(name = "Math", code = "MTH", professorId = 1L)
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val savedSubject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(savedSubject)

        val response = subjectService.createSubject(request)

        assertEquals(1L, response.id)
        assertEquals("Math", response.name)
        assertEquals("MTH", response.code)
    }

    // getAllSubjects
    @Test
    fun `getAllSubjects returns mapped list of subjects`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val s1 = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        val s2 = Subject(id = 2L, name = "Science", code = "SCI", professor = professor)

        `when`(subjectRepository.findAll()).thenReturn(listOf(s1, s2))

        val responses = subjectService.getAllSubjects()
        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("Math", responses[0].name)
        assertEquals(2L, responses[1].id)
    }

    // getSubjectById
    @Test
    fun `getSubjectById throws SubjectNotFound when not found`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(SubjectNotFound::class.java) {
            subjectService.getSubjectById(99L)
        }
    }

    @Test
    fun `getSubjectById returns subject when found`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val subject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val response = subjectService.getSubjectById(1L)
        assertEquals(1L, response.id)
        assertEquals("Math", response.name)
    }

    // updateSubject
    @Test
    fun `updateSubject throws SubjectNotFound when subject not found`() {
        val request = SubjectRequest(name = "Math", code = "MTH", professorId = 1L)
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(SubjectNotFound::class.java) {
            subjectService.updateSubject(99L, request)
        }
    }

    @Test
    fun `updateSubject throws BlankNameException when name is blank`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val subject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val request = SubjectRequest(name = "   ", code = "MTH", professorId = 1L)
        assertThrows(BlankNameException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject throws BlankNameException when code is blank`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val subject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val request = SubjectRequest(name = "Math 2", code = "   ", professorId = 1L)
        assertThrows(BlankNameException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject throws ProfessorNotFound when professor not found`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val subject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val request = SubjectRequest(name = "Math 2", code = "MTH2", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject saves and returns updated subject`() {
        val professor1 = Professor(id = 1L, name = "Prof. Josu", email = null)
        val professor2 = Professor(id = 2L, name = "Prof. Ana", email = null)
        val existingSubject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor1)
        val updatedSubject = Subject(id = 1L, name = "Math 2", code = "MTH2", professor = professor2)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        val request = SubjectRequest(name = "Math 2", code = "MTH2", professorId = 2L)
        `when`(professorRepository.findById(2L)).thenReturn(Optional.of(professor2))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(updatedSubject)

        val response = subjectService.updateSubject(1L, request)

        assertEquals("Math 2", response.name)
        assertEquals("MTH2", response.code)
    }

    // deleteSubject
    @Test
    fun `deleteSubject throws SubjectNotFound when subject not found`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(SubjectNotFound::class.java) {
            subjectService.deleteSubject(99L)
        }
    }

    @Test
    fun `deleteSubject deletes when found`() {
        val professor = Professor(id = 1L, name = "Prof. Josu", email = null)
        val subject = Subject(id = 1L, name = "Math", code = "MTH", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        subjectService.deleteSubject(1L)
        verify(subjectRepository, times(1)).delete(subject)
    }
}
