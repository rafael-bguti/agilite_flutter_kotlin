package info.agilite.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
  "info.agilite.shared",
  "info.agilite.server_core",
  "info.agilite.boot",
  "info.agilite.erp"
])
class AgiliteERPApplication

fun main(args: Array<String>) {
  runApplication<AgiliteERPApplication>(*args)
}
