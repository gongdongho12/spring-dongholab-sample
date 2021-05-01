package com.dongholab.repository.jpa
import org.springframework.stereotype.Repository
import com.dongholab.entity.User
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface UserRepository: JpaRepository<User, String> {
}