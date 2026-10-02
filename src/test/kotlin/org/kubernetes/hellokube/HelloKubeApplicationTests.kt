package org.kubernetes.hellokube

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = [
	"spring.flyway.enabled=false",
	"spring.kafka.listener.auto-startup=false"
])
class HelloKubeApplicationTests {

	@Test
	fun contextLoads() {
	}

}
