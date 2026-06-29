package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.ArgumentMatchers.any
import java.util.*

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `createStudent should throw BlankNameException when name is blank`() {
        val request = StudentRequest(name = "", email = "test@test.com")
        assertThrows(BlankNameException::class.java) {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent should return valid StudentResponse when name is not blank`() {
        val request = StudentRequest(name = "test", email = "test@test.com")
        val savedStudent = Student(id = 1L, name = "test", email = "test@test.com")

        `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertEquals("test@test.com", response.email)
    }

    @Test
    fun `createStudent should return valid StudentResponse with empty email when email is null`() {
        val request = StudentRequest(name = "test", email = null)
        val savedStudent = Student(id = 1L, name = "test", email = null)

        `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertNull(response.email)
    }

    @Test
    fun `getAllStudents should return a list of StudentResponse`() {
        val students = listOf(
            Student(id = 1L, name = "ana", email = "ana@puce.com"),
            Student(id = 2L, name = "bob", email = "bob@puce.com")
        )

        `when`(studentRepository.findAll()).thenReturn(students)

        val response = studentService.getAllStudents()

        assertEquals(2, response.size)
        assertEquals("ana", response[0].name)
        assertEquals("bob", response[1].name)
    }

    @Test
    fun `getStudentById should return a StudentResponse`() {
        val student = Student(id = 1L, name = "test", email = "test@test.com")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val response = studentService.getStudentById(1L)
        assertEquals(1L, response.id)
        assertEquals("test", response.name)
    }

    @Test
    fun `getStudentById should throw StudentNotFoundException`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(StudentNotFoundException::class.java) {
            studentService.getStudentById(1L)
        }
    }

    @Test
    fun `updateStudent should throw StudentNotFoundException when not found`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.empty())
        val request = StudentRequest(name = "test", email = "test@test.com")
        assertThrows(StudentNotFoundException::class.java) {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `updateStudent should throw BlankNameException when name is blank`() {
        val student = Student(id = 1L, name = "test", email = "test@test.com")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val request = StudentRequest(name = "   ", email = "test@test.com")
        assertThrows(BlankNameException::class.java) {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `updateStudent should save and return updated StudentResponse`() {
        val student = Student(id = 1L, name = "test", email = "test@test.com")
        val updatedStudent = Student(id = 1L, name = "test 2", email = "test2@test.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(updatedStudent)

        val request = StudentRequest(name = "test 2", email = "test2@test.com")
        val response = studentService.updateStudent(1L, request)

        assertEquals("test 2", response.name)
        assertEquals("test2@test.com", response.email)
    }

    @Test
    fun `deleteStudent should throw StudentNotFoundException when not found`() {
        `when`(studentRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(StudentNotFoundException::class.java) {
            studentService.deleteStudent(1L)
        }
    }

    @Test
    fun `deleteStudent should delete when found`() {
        val student = Student(id = 1L, name = "test", email = "test@test.com")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        studentService.deleteStudent(1L)
        verify(studentRepository, times(1)).delete(student)
    }
}