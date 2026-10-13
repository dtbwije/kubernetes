package org.kubernetes.hellokube

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = [
    "spring.flyway.enabled=false",
    "app.kafka.enabled=false"
])
class HelloKubeApplicationTests {

    @Test
    fun contextLoads() {
    }
}
