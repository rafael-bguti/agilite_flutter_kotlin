package info.agilite.server_core.spring

import org.springframework.core.annotation.AliasFor
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
@RestController
annotation class RestMapping(
  @get:AliasFor(
    annotation = RequestMapping::class,
    attribute = "value"
  ) vararg val value: String
) 