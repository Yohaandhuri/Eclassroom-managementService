package com.eclassroom.management_service.repositories

import com.eclassroom.management_service.entities.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UsersEntity,UUID>{
}