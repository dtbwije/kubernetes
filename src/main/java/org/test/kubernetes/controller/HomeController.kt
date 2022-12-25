package org.test.kubernetes.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HomeController {

    @GetMapping("/")
    fun hello() :String {
        return "Hello"
    }
}