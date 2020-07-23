package pt.isel.daw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DawProjectApplication

fun main(args: Array<String>) {
    runApplication<DawProjectApplication>(*args)
}
