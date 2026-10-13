package org.kubernetes.hellokube.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/saveUser")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveUser(@Valid @RequestBody request: SaveUserRequest): Mono<User> =
        userService.save(request)

    @GetMapping("/getUser")
    fun getUsers(): Flux<User> = userService.findAll()
}
