package com.practice.kopring.user.presentation

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class UserController {
    @GetMapping("/u")
    fun index(model: Model): String? {
        println("asmr")
        return "/index"
    }
}
