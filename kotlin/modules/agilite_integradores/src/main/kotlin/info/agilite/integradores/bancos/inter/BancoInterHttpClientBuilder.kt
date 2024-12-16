package info.agilite.integradores.bancos.inter

import com.fasterxml.jackson.core.JsonProcessingException
import com.sun.tools.example.debug.expr.ExpressionParserConstants.THROW
import info.agilite.core.exceptions.ToManyRequestException
import info.agilite.core.exceptions.ValidationException
import info.agilite.core.json.JsonUtils
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyFactory
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Duration
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLParameters


private const val CLIENT_ERROR_BASE = 400
private const val SERVER_ERROR_BASE = 500
private const val INTER_TOO_MANY_REQUESTS = 429

object BancoInterHttpClientBuilder {
  fun callGet(url: String, bancoConfig: BancoInterConfig, scope: String, requestParam: Map<String, String>): String {
    val httpClient = build(bancoConfig)
    val token = GetTokenBancoInter.execute(bancoConfig, scope)
    val formData = requestParam.toQueryParams()

    val requestBuilder = HttpRequest.newBuilder()
      .uri(URI.create("$url?$formData"))
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer ${token.access_token}")
      .GET()

    if(bancoConfig.contaCorrente != null){
      requestBuilder.headers("x-conta-corrente", bancoConfig.contaCorrente)
    }

    val request = requestBuilder.build()
    val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    return handleResponse(response)
  }

  fun build(config: BancoInterConfig): HttpClient {
    val sslContext = buildSSLContext(config.certPath, config.keyPath)
    val sslParameters = SSLParameters().apply { needClientAuth = true }
    val httpClient = createHttpClient(sslContext, sslParameters)

    return httpClient
  }

  fun handleResponse(response: HttpResponse<String>): String {
    if (response.statusCode() == INTER_TOO_MANY_REQUESTS) {
      throw ToManyRequestException()
    }

    if (response.statusCode() >= CLIENT_ERROR_BASE) {
      val body = response.body()
      val error = try {
        JsonUtils.fromJson(body, BancoInterError::class.java)
      } catch (e: JsonProcessingException) {
        BancoInterError(response.toString())
      }

      throw ValidationException("Erro desconhecido ao se conectar ao Inter(${response.statusCode()}). Motivo: \n${error.toString()}")
    }

    return response.body()
  }


  private fun createHttpClient(sslContext: SSLContext, sslParameters: SSLParameters) =
    java.net.http.HttpClient.newBuilder()
      .sslContext(sslContext)
      .sslParameters(sslParameters)
      .connectTimeout(Duration.ofSeconds(60))
      .build()

  private fun buildSSLContext(crtPath: String, keyPath: String): SSLContext {
    val cert = Files.readAllBytes(Path.of(crtPath))
    val key = Files.readAllBytes(Path.of(keyPath))
      .toString(Charsets.UTF_8)
      .replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "")
      .replace("\n", "")
      .toByteArray(Charsets.UTF_8)

    val certificateFactory = CertificateFactory.getInstance("X.509")
    val chain = certificateFactory.generateCertificates(ByteArrayInputStream(cert)) as Collection<Certificate>
    val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(key))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)

    val keyStore = KeyStore.getInstance("jks").apply {
      load(null, null)
      setKeyEntry("bancoInter", privateKey, "password".toCharArray(), chain.toTypedArray())
    }

    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509").apply {
      init(keyStore, "password".toCharArray())
    }

    return SSLContext.getInstance("TLSv1.2").apply {
      init(keyManagerFactory.keyManagers, null, null)
    }

  }
  private fun Map<String, String>.toQueryParams(): String =
    entries.joinToString("&") { (key, value) ->
      "${URLEncoder.encode(key, StandardCharsets.UTF_8)}=${URLEncoder.encode(value, StandardCharsets.UTF_8)}"
    }
}