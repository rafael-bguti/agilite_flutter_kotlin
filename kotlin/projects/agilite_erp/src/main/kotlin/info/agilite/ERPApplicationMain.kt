package info.agilite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
  "info.agilite",
])
class AgiliteERPApplication

//TODO implementar os eventos que geram o financeiro
fun main(args: Array<String>) {
  runApplication<AgiliteERPApplication>(*args)
}
