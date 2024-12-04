package info.agilite.core.http

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


private val httpClient = HttpClient.newHttpClient()

object AgRequest {

  fun send(request: HttpRequest): HttpResponse<String> {
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
  }

}