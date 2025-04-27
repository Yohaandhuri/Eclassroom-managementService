package com.eclassroom.management_service.services

import com.eclassroom.management_service.commonEnums.RoleEnum
import com.eclassroom.management_service.dto.UserDto
import com.eclassroom.management_service.dto.UserInputDto
import com.eclassroom.management_service.dto.toEntity
import com.eclassroom.management_service.dto.toUserDto
import com.eclassroom.management_service.entities.CourseEntity
import com.eclassroom.management_service.entities.UsersEntity
import com.eclassroom.management_service.repositories.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.apache.catalina.User
import org.hibernate.Hibernate
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository
) {
    val passwordEncoder = Argon2PasswordEncoder(
        16,
        32,
        1,
        65536,
        3
    )

    interface Result{
        data class Success(val msg:String,var courses:List<CourseEntity>?=null) : Result
        data class Error(val msg:String): Result
        data class NotFound(val msg:String): Result
        data class AlreadyExist(val msg:String): Result
    }

    fun getUserById(id: Long): UserDto{
        return userRepository.findById(id).getOrElse {
            throw EntityNotFoundException("User with id: ${id} does not exist")
        }.toUserDto()
    }


    fun getUsers():List<UsersEntity>{
        return userRepository.findAll()
    }

    @Transactional
    fun getUserCourses(userId:Long):Result{
        val user = userRepository.findById(userId).getOrNull()
        println("=====${user?.id}")
        if(user==null)
            return Result.NotFound("User with id:${userId} does not exist")
        val courses = userRepository.findCoursesByUserId(user.id)
        return Result.Success("Courses fetched successfully",courses?.toList())
    }

    fun registerUser(userInput: UserInputDto):Result{
        try {
            if (userRepository.findByEmail(userInput.email)!=null)
                return Result.AlreadyExist("User with email:${userInput.email} already exists, use different email.")
            val passwordHash: String? = userInput.password?.takeIf { it.isNotBlank() }
                ?.let { passwordEncoder.encode(it) }
            userRepository.save(userInput.toEntity(passwordHash,generateUniqueRoleId()))
            return Result.Success("User registered successfully")
        } catch (e:Exception){
            return Result.Error("There was an error while registering the user: ${e.message} ${e.cause}")
        }
    }

    fun getUsersByRole(role:RoleEnum):List<UsersEntity>{
        return userRepository.findByRole(role)
    }

    private fun generateUniqueRoleId(): Int {
        var candidate: Int
        do {
            candidate = (10000..99999).random()
        } while (userRepository.existsByRoleNumber(candidate))
        return candidate
    }
}