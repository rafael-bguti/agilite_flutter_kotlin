package info.agilite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
  "info.agilite",
])
class AgiliteERPApplication

fun main(args: Array<String>) {
  runApplication<AgiliteERPApplication>(*args)
}
