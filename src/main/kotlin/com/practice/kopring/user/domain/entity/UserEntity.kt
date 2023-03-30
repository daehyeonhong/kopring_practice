package com.practice.kopring.user.domain.entity

import PrimaryKeyEntity
import com.practice.kopring.user.domain.enumerate.Provider
import com.practice.kopring.user.domain.enumerate.Role
import jakarta.persistence.*

@Entity
class UserEntity(
    name: String,
    email: String,
    @Column
    var picture: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var provider: Provider
) : PrimaryKeyEntity() {

    @Column(nullable = false)
    var name = name
        protected set

    @Column(unique = true, nullable = false)
    var email = email
        protected set

    fun loginUpdate(
        name: String,
        picture: String
    ): Unit {
        this.name = name
        this.picture = picture
    }
}
