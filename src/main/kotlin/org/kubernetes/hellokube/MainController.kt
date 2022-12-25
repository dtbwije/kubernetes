package org.kubernetes.hellokube

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/h")
    fun hello(): String = "Hello...!"
}