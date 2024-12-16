package info.agilite.integradores.bancos.inter

import com.google.common.cache.CacheBuilder
import info.agilite.core.extensions.toHttpFormData
import info.agilite.core.json.JsonUtils
import java.net.URI
import java.net.http.HttpRequest
import java.time.Duration


private const val PATH_TOKEN = "/oauth/v2/token"
object GetTokenBancoInter {
  private val tokensCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofHours(2)).build<String, TokenBancoInter>()

  fun execute(config: BancoInterConfig, scope: String): TokenBancoInter {
    val key = "${config.clientId}:${config.clientSecret}:$scope"
    val token = tokensCache.getIfPresent(key)

    return if (token != null && token.isValid()) {
      token
    } else {
      val newToken = getNewToken(config, scope)
      tokensCache.put(key, newToken)
      newToken
    }
  }

  private fun getNewToken(config: BancoInterConfig, scope: String): TokenBancoInter {
    val httpClient = BancoInterHttpClientBuilder.build(config)
    val formData = mapOf(
      "client_id" to config.clientId,
      "client_secret" to config.clientSecret,
      "scope" to scope,
      "grant_type" to "client_credentials"
    ).toHttpFormData()

    val request = HttpRequest.newBuilder()
      .uri(URI.create("${config.getUrl()}$PATH_TOKEN"))
      .header("Content-Type", "application/x-www-form-urlencoded")
      .POST(java.net.http.HttpRequest.BodyPublishers.ofString(formData))
      .build()

    val response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString())
    val body = BancoInterHttpClientBuilder.handleResponse(response)
    return JsonUtils.fromJson(body, TokenBancoInter::class.java)
  }
}

