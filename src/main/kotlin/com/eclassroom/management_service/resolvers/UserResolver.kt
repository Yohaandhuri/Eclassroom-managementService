package com.eclassroom.management_service.resolvers

import com.eclassroom.management_service.dto.UserDto
import com.eclassroom.management_service.dto.toUserDto
import com.eclassroom.management_service.services.UserService
import graphql.GraphQLException
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
    fun getUser(@Argument id: UUID): UserDto {
        return userService.getUserById(id)
    }

    @MutationMapping
    fun registerUser(userInput: UserDto): String {
        return when (val result = userService.registerUser(userInput)) {
            is UserService.Result.Success -> result.msg
            is UserService.Result.Error -> throw GraphQLException(result.msg)
            else -> {throw GraphQLException("There was an error.")}
        }
    }
}