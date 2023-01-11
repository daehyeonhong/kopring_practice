package com.practice.kopring.member.domain.entity

import com.practice.kopring.common.domain.entity.BaseEntity
import com.practice.kopring.member.domain.enumerate.Role
import jakarta.persistence.*

@Entity
class MemberEntity(
    @Column(nullable = false)
    var name: String,

    email: String,

    @Column
    var picture: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role
) : BaseEntity() {
    @Column(unique = true, nullable = false)
    var email = email
        protected set
}
