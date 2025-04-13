package com.eclassroom.management_service.resolvers

import com.eclassroom.management_service.dto.CourseDto
import com.eclassroom.management_service.services.CourseService
import graphql.GraphQLException
import jakarta.persistence.EntityNotFoundException
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class CourseResolver(
    private val courseService: CourseService
) {

    @QueryMapping
    fun getAllCourses(): List<CourseDto> {
        return courseService.getAllCourses()
    }

    @QueryMapping
    fun getCourseById(@Argument id: UUID): CourseDto? {
        return when (val result = courseService.getCourseById(id)) {
            is CourseService.Result.Success -> result.entity
            is CourseService.Result.Error -> throw GraphQLException(result.msg)
            is CourseService.Result.NotFound -> throw EntityNotFoundException(result.msg)
            else -> {throw GraphQLException("There was an error.")}
        }
    }

    @MutationMapping
    fun saveCourse(@Argument course: CourseDto): String {
        return when (val result = courseService.saveCourse(course)) {
            is CourseService.Result.Success -> result.msg
            is CourseService.Result.Error -> throw GraphQLException(result.msg)
            else -> {throw GraphQLException("There was an error.")}
        }
    }

    @MutationMapping
    fun enrollStudentsToCourse(@Argument courseId: UUID, @Argument studentIds: List<UUID>): String {
        return when (val result = courseService.enrollStudents(courseId, studentIds)) {
            is CourseService.Result.Success -> result.msg
            is CourseService.Result.Error -> throw GraphQLException(result.msg)
            is CourseService.Result.NotFound -> throw EntityNotFoundException(result.msg)
            else -> {throw GraphQLException("There was an error.")}
        }
    }

    @MutationMapping
    fun assignFacultyToCourse(@Argument courseId: UUID, @Argument facultyId: UUID): String {
        return when (val result = courseService.assignFaculty(courseId, facultyId)) {
            is CourseService.Result.Success -> result.msg
            is CourseService.Result.Error -> throw GraphQLException(result.msg)
            is CourseService.Result.NotFound -> throw EntityNotFoundException(result.msg)
            else -> {throw GraphQLException("There was an error.")}
        }
    }

}