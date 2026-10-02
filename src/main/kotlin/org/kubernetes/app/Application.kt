package org.kubernetes.hellokube

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class HelloKubeApplication

fun main(args: Array<String>) {
	runApplication<HelloKubeApplication>(*args)
}
