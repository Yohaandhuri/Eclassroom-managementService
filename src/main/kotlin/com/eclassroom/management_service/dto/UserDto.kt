package com.eclassroom.management_service.dto

import com.eclassroom.management_service.commonEnums.GenderEnum
import com.eclassroom.management_service.commonEnums.RoleEnum
import com.eclassroom.management_service.commonEnums.StatusEnum
import com.eclassroom.management_service.entities.UsersEntity
import java.time.LocalDate

data class UserDto (
    val firstName: String,
    val lastName: String,
    val email: String,
    val dob: LocalDate? = null,
    val phoneNumber: String? = null,
    val status: StatusEnum,
    val role: RoleEnum,
    val gender: GenderEnum?,
)

fun UserDto.toEntity(password:String): UsersEntity =
    UsersEntity(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        dob = this.dob,
        phoneNumber = this.phoneNumber,
        status = this.status,
        passwordHash = password,  // need to change this
        role = this.role,
        gender = this.gender,
    )

fun UsersEntity.toUserDto() : UserDto = UserDto(
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    dob = this.dob,
    phoneNumber = this.phoneNumber,
    status = this.status,
    role = this.role,
    gender = this.gender
)
