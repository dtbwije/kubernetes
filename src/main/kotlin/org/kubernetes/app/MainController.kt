package org.kubernetes.hellokube

import com.fasterxml.jackson.databind.JsonNode
import org.kubernetes.hellokube.database.MobileDataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController(
    private val mobileDataService: MobileDataService
) {

    @GetMapping("/h")
    fun hello(): String = "Hello...!"

    @GetMapping("/mobile-data")
    fun getMobileData(): List<JsonNode> = mobileDataService.getMobileData()
}