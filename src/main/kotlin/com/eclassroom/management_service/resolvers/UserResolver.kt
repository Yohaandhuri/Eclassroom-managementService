package com.eclassroom.management_service.resolvers

import com.eclassroom.management_service.commonEnums.RoleEnum
import com.eclassroom.management_service.dto.*
import com.eclassroom.management_service.services.UserService
import graphql.ErrorType
import graphql.GraphQLException
import graphql.GraphqlErrorException
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class UserResolver(
    private val userService: UserService
) {
    @QueryMapping
    fun getUsers(): List<UserDto>{
        return userService.getUsers().map { it.toUserDto() }
    }

    @QueryMapping
    fun getUser(@Argument id: Long): UserDto {
        return userService.getUserById(id)
    }

    @MutationMapping
    fun registerUser(@Argument userInput: UserInputDto): String {
        return when (val result = userService.registerUser(userInput)) {
            is UserService.Result.Success -> result.msg
            is UserService.Result.Error -> throw GraphQLException(result.msg)
            is UserService.Result.AlreadyExist -> throw (GraphqlErrorException.newErrorException()
                .message(result.msg)
                .extensions(mapOf("errorCode" to "USER_ALREADY_EXISTS"))
                .build())
            else -> {throw GraphQLException("There was an error.")}
        }
    }

    @QueryMapping
    fun getUserCourses(@Argument userId:Long): List<CourseDto>{
        return when (val result = userService.getUserCourses(userId)) {
            is UserService.Result.Success -> result.courses?.map { it.toDto() } ?: listOf()
            is UserService.Result.Error -> throw GraphQLException(result.msg)
            is UserService.Result.NotFound -> throw (GraphqlErrorException.newErrorException()
                .message(result.msg)
                .extensions(mapOf("errorCode" to "USER_DOES_NOT_EXISTS"))
                .build())
            else -> {throw GraphQLException("There was an error.")}
        }
    }

    @QueryMapping
    fun getUsersSpecific(@Argument role: RoleEnum) : List<UserDto>{
        return userService.getUsersByRole(role).map { it.toUserDto() }
    }
}