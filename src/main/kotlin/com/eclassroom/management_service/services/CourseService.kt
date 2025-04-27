package com.eclassroom.management_service.services

import com.eclassroom.management_service.dto.CourseDto
import com.eclassroom.management_service.dto.CourseInputDto
import com.eclassroom.management_service.dto.toDto
import com.eclassroom.management_service.dto.toEntity
import com.eclassroom.management_service.entities.UsersEntity
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
        data class AlreadyExists(val msg: String): Result
    }

    fun getCourseById(id: Long): Result {
        val course = courseRepository.findById(id).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $id not found")
        return Result.Success( msg = "Success",entity = course.toDto())
    }

    fun getAllCourses(): List<CourseDto> {
        return courseRepository.findAll().map { it.toDto() }
    }

    fun saveCourse(courseInputDto: CourseInputDto): Result {
        try {
            if(courseRepository.findByTitle(courseInputDto.title)!=null)
                return Result.AlreadyExists("Course with title:${courseInputDto.title} already exists")

            var faculty: UsersEntity? = null
            if(courseInputDto.facultyId!=null){
                faculty = userRepository.findById(courseInputDto.facultyId).getOrNull() ?:
                return Result.NotFound("Faculty with ID:${faculty} does not exist")
            }

            val entity = courseInputDto.toEntity(faculty)
            courseRepository.save(entity)
            return Result.Success("Course data saved successfully")
        } catch (e:Exception){
            return Result.Error("Error while saving course: ${e.message} ${e.cause}")
        }
    }

    fun enrollStudent(courseId: Long, studentId: Long): Result {

        val course = courseRepository.findById(courseId).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $courseId not found")

        val student = userRepository.findById(studentId).getOrNull()
            ?: return Result.NotFound("No valid students found for provided IDs.")
        if(student.courses.contains(course))
            return Result.AlreadyExists("Student already enrolled to course")
        student.courses.add(course)

        userRepository.save(student)

        return Result.Success("Student with ID:${studentId} enrolled successfully to course: ${course.id}")
    }

    fun assignFaculty(courseId: Long, facultyId: Long): Result {
        val course = courseRepository.findById(courseId).getOrNull()
            ?: return  Result.NotFound( msg = "Course with id $courseId not found")

        val faculty = userRepository.findById(facultyId).getOrNull()
            ?: return  Result.NotFound( msg = "Faculty with id $facultyId not found")

        course.faculty = faculty
        courseRepository.save(course)

        return Result.Success("Faculty assigned successfully")
    }
}