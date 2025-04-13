package com.eclassroom.management_service.services

import com.eclassroom.management_service.dto.CourseDto
import com.eclassroom.management_service.dto.toDto
import com.eclassroom.management_service.dto.toEntity
import com.eclassroom.management_service.repositories.CourseRepository
import com.eclassroom.management_service.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {

    interface Result{
        data class Success(val msg:String,var entity:CourseDto?=null) : Result
        data class Error(val msg:String): Result
        data class NotFound(val msg: String): Result
    }

    fun getCourseById(id: UUID): Result {
        val course = courseRepository.findById(id).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $id not found")
        return Result.Success( msg = "Success",entity = course.toDto())
    }

    fun getAllCourses(): List<CourseDto> {
        return courseRepository.findAll().map { it.toDto() }
    }

    fun saveCourse(courseDto: CourseDto): Result {
        try {
            val faculty = courseDto.facultyId?.let { userRepository.findById(it).getOrNull() }
            val entity = courseDto.toEntity(faculty)
            courseRepository.save(entity)
            return Result.Success("Course data saved successfully")
        } catch (e:Exception){
            return Result.Error("Error while saving course: ${e.message} ${e.cause}")
        }
    }

    fun enrollStudents(courseId: UUID, studentIds: List<UUID>): Result {
        if (studentIds.isEmpty()) {
            return Result.Error("No student IDs provided for enrollment.")
        }

        val course = courseRepository.findById(courseId).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $courseId not found")

        val students = userRepository.findAllById(studentIds)
        if (students.isEmpty()) {
            return Result.NotFound("No valid students found for provided IDs.")
        }

        course.students.addAll(students)
        courseRepository.save(course)

        return Result.Success("${students.size} student(s) enrolled successfully to course: ${course.id}")
    }

    fun assignFaculty(courseId: UUID, facultyId: UUID): Result {
        val course = courseRepository.findById(courseId).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $courseId not found")

        val faculty = userRepository.findById(facultyId).getOrNull()
            ?: return  Result.NotFound( msg = "Faculty with id $facultyId not found")

        course.faculty = faculty
        courseRepository.save(course)

        return Result.Success("Faculty assigned successfully")
    }
}