package com.practice.kopring.common.domain.presentation

import org.springframework.util.Assert


class AddMemberRequest(name: String, price: Int) {
    private val name: String
    private val price: Int

    init {
        this.price = price
        this.name = name
        Assert.hasText(name, "상품명은 필수입니다.")
        Assert.isTrue(price > 0, "상품 가격은 0보다 커야 합니다.")
    }
}
