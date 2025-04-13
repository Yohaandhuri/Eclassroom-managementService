package com.eclassroom.management_service.services

import com.eclassroom.management_service.dto.UserDto
import com.eclassroom.management_service.dto.toEntity
import com.eclassroom.management_service.dto.toUserDto
import com.eclassroom.management_service.entities.UsersEntity
import com.eclassroom.management_service.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    private val userRepository: UserRepository
) {

    interface Result{
        data class Success(val msg:String) : Result
        data class Error(val msg:String): Result
    }

    fun getUserById(id: UUID): UserDto{
        return userRepository.findById(id).getOrElse {
            throw EntityNotFoundException("User with id: ${id} does not exist")
        }.toUserDto()
    }


    fun getUsers():List<UsersEntity>{
        return userRepository.findAll()
    }

    fun registerUser(userInput: UserDto):Result{
        try {
            userRepository.save(userInput.toEntity(password="TEST")) // need to change the password
            return Result.Success("User registered successfully")
        } catch (e:Exception){
            return Result.Error("There was an error while registering the user: ${e.message} ${e.cause}")
        }
    }

}