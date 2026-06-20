package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals


// @ExtendWith activa la integracion de Mockito con Junit
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
        val request = StudentRequest(
            name = "test",
            email = "test@test.com"
        )

        val savedStudent = Student(
            id = 1L,
            name = "test",
            email = "test@test.com"
        )

        // when palara reservada de kotlin para reemplazar el switch
        `when`(studentRepository.save(any(Student::class.java)))
            .thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertEquals("test@test.com", response.email)
    }

    @Test
    fun `createStudent should return valid StudentResponse with empty email when email is null`() {
        val request = StudentRequest(
            name = "test",
            email = null
        )

        val savedStudent = Student(
            id = 1L,
            name = request.name,
            email = request.email
        )

        // when palabras reservada de kotlin para reemplazar el switch
        `when`(studentRepository.save(any(Student::class.java)))
            .thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("test", response.name)
        assertEquals(null, response.email)
    }

    fun `getAllStudents should return a list of StudentsResponse`() {
        val students = ListoF(
            Student(
                id = 1L,
                name = "ana",
                email = "ana@puce.com"
            ),
            Student(
                id = 2L,
                name = "bob",
                email = "bob@puce.com"
            ),
            Student(
                id = 3L,
                name = "joaquin",
                email = "joaquin@puce.com"

            )

                    `when` (studentRepository.findAll())
                .thenReturn(students)

            val response = studentService . getAllStudents ()

        assertEquals(3, response.size)
        assertEquals("ana", students[0].name)
    }

    fun `getStudentById should return a StudentResponse`() {

        val student = Student(
            id = 1L,
            name = "test",
            email = "test@test.com"
        )

        `when`(studentRepository.findById(any(Long::class.java)))
            .thenReturn(Optional.of(student))

        val response = studentService.getStudentById(1L)
        assertEquals(1L, response.id)
    }

    @Test
    fun `getStudentById should throw StudentNotFoundException`() {
        `when`(studentRepository.findById(any(Long::class.java)))
            .thenReturn(Optional.empty())

        assertThrows<StudentNotFoundException> {
            studentService.getSttudentById(1L)
        }
    }
    
    // 
}