package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.repositories.SubjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock private lateinit var enrollmentRepository: EnrollmentRepository
    @Mock private lateinit var studentRepository: StudentRepository
    @Mock private lateinit var subjectRepository: SubjectRepository

    @InjectMocks private lateinit var enrollmentService: EnrollmentService

    private val testStudent = Student(id = 1L, name = "Student", email = "test@mail.com")
    private val testProfessor = Professor(id = 1L, name = "Prof", email = null)
    private val testSubject = Subject(id = 1L, name = "Math", code = "MTH", professor = testProfessor)

    // createEnrollment
    @Test
    fun `createEnrollment throws StudentNotFoundException when student not found`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment throws SubjectNotFound when subject not found`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(SubjectNotFound::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment saves and returns enrollment`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(testSubject))

        val savedEnrollment = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(savedEnrollment)

        val response = enrollmentService.createEnrollment(request)
        assertEquals(1L, response.id)
        assertEquals("INSCRITO", response.status)
    }

    // getAllEnrollments
    @Test
    fun `getAllEnrollments returns mapped list`() {
        val e1 = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.findAll()).thenReturn(listOf(e1))

        val responses = enrollmentService.getAllEnrollments()
        assertEquals(1, responses.size)
        assertEquals(1L, responses[0].id)
    }

    // getEnrollmentById
    @Test
    fun `getEnrollmentById throws EnrollmentNotFound when not found`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    @Test
    fun `getEnrollmentById returns enrollment`() {
        val e1 = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e1))

        val response = enrollmentService.getEnrollmentById(1L)
        assertEquals(1L, response.id)
    }

    // updateEnrollment
    @Test
    fun `updateEnrollment throws EnrollmentNotFound when not found`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.updateEnrollment(99L, request)
        }
    }

    @Test
    fun `updateEnrollment saves and returns updated enrollment`() {
        val e1 = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e1))

        val request = EnrollmentUpdateRequest(status = "APROBADO")
        val e2 = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "APROBADO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(e2)

        val response = enrollmentService.updateEnrollment(1L, request)
        assertEquals("APROBADO", response.status)
    }

    // deleteEnrollment
    @Test
    fun `deleteEnrollment throws EnrollmentNotFound when not found`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.deleteEnrollment(99L)
        }
    }

    @Test
    fun `deleteEnrollment deletes when found`() {
        val e1 = Enrollment(id = 1L, student = testStudent, subject = testSubject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e1))

        enrollmentService.deleteEnrollment(1L)
        verify(enrollmentRepository, times(1)).delete(e1)
    }
}