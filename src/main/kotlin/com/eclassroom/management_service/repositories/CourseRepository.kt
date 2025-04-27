package com.eclassroom.management_service.repositories

import com.eclassroom.management_service.entities.CourseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : JpaRepository<CourseEntity, Long> {
    fun findByTitle(title:String):CourseEntity?

}
