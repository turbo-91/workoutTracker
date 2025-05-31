package workouttracker.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    println("Hello, World from Kotlin + Spring Boot!")
    runApplication<BackendApplication>(*args)
}
