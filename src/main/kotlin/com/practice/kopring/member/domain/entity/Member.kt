package com.practice.kopring.member.domain.entity

import com.practice.kopring.member.domain.enumerate.Role
import jakarta.persistence.*


class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column
    val picture: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)
