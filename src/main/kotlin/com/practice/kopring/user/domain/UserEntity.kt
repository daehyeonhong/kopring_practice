package com.practice.kopring.user.domain

import com.practice.kopring.common.domain.PrimaryKeyEntity
import com.practice.kopring.user.enumerate.Provider
import com.practice.kopring.user.enumerate.Role
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(value = [AuditingEntityListener::class])
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
    var provider: Provider,
    oAuth2Id: String
) : PrimaryKeyEntity() {

    @Column(nullable = false)
    var name = name
        protected set

    @Column(unique = true, nullable = false)
    var email = email
        protected set

    @Column(nullable = false)
    var oAuth2Id: String = oAuth2Id
        protected set

    fun loginUpdate(
        name: String,
        picture: String
    ): Unit {
        this.name = name
        this.picture = picture
    }
}
