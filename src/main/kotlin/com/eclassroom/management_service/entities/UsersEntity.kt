package com.eclassroom.management_service.entities

import com.eclassroom.management_service.commonEnums.GenderEnum
import com.eclassroom.management_service.commonEnums.RoleEnum
import com.eclassroom.management_service.commonEnums.StatusEnum
import com.eclassroom.management_service.dto.UserDto
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "users")
data class UsersEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long = 0L,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String? = null,

    @Column(name = "date_of_birth")
    val dob: LocalDate? = null,

    @Column(name = "phone_number")
    val phoneNumber: String? = null,

    @Column(name = "role_number", nullable = false, unique = true)
    val roleNumber: Int,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    val gender: GenderEnum? = null,

//    @Column(name = "profile_picture")
//    val profilePicture: String? = null,
//
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: StatusEnum,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: RoleEnum, // Could use an enum converter if needed

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "student_courses",
        joinColumns = [JoinColumn(name = "student_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    @JsonIgnore
    val courses: MutableSet<CourseEntity> = mutableSetOf()

) : AuditableEntity()
