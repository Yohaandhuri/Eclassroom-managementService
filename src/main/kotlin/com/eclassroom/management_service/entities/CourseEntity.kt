package com.eclassroom.management_service.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "courses")
data class CourseEntity(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column
    val title: String,

    @Column
    val duration: String, // in hours

    @Column
    val credicts: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id") // foreign key in courses table
    var faculty: UsersEntity? = null,

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY, cascade = [CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST])
    val students: MutableSet<UsersEntity> = mutableSetOf()
)