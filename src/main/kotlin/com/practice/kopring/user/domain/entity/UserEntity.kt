package com.practice.kopring.user.domain.entity

import com.practice.kopring.common.domain.entity.PrimaryKeyEntity
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
    var role: Role
) : PrimaryKeyEntity() {

    @Column(nullable = false)
    var name = name
        protected set

    @Column(unique = true, nullable = false)
    var email = email
        protected set
}
