package org.kubernetes.hellokube.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidation(ex: WebExchangeBindException): ResponseEntity<Map<String, Any>> {
        val errors = ex.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to "validation failed", "fields" to errors))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, Any>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "bad request")))
}
