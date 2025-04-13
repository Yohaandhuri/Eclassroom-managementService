package com.eclassroom.management_service.dto

import com.eclassroom.management_service.entities.CourseEntity
import com.eclassroom.management_service.entities.UsersEntity
import java.util.*

data class CourseDto(
    val id: UUID? = null,
    val title: String,
    val duration: String,
    val credits: String,
    val facultyId: UUID?,              // reference by ID
    val studentIds: Set<UUID> = setOf() // reference by IDs
)

fun CourseEntity.toDto(): CourseDto {
    return CourseDto(
        id = this.id,
        title = this.title,
        duration = this.duration,
        credits = this.credicts,
        facultyId = this.faculty?.id,
        studentIds = this.students.mapNotNull { it.id }.toSet()
    )
}

fun CourseDto.toEntity(
    faculty: UsersEntity?,
): CourseEntity {
    return CourseEntity(
        id = this.id ?: UUID.randomUUID(),
        title = this.title,
        duration = this.duration,
        credicts = this.credits,
        faculty = faculty
    )
}
