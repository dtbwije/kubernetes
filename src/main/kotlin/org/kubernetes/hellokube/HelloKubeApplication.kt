package org.kubernetes.hellokube

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloKubeApplication

fun main(args: Array<String>) {
	runApplication<HelloKubeApplication>(*args)
}
