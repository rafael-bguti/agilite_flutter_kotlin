package info.agilite.core.certs

import com.google.common.cache.CacheBuilder
import info.agilite.core.exceptions.ValidationException
import java.nio.file.Path
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import java.time.LocalDate
import java.time.ZoneId
import kotlin.io.path.exists
import kotlin.io.path.pathString

private const val CACHE_DE_CERTIFICADO_SIZE = 100L
private const val CACHE_DE_CERTIFICADO_EXPIRE_HOUR = 6L

data class Certificado(
  private val privateKey: PrivateKey,
  val x509cert: X509Certificate,
  val keyStore: KeyStore,
  val alias: String,
  val senha: String,
  val notAfter: LocalDate,
) {
  fun getPrivateKey(): PrivateKey {
    if(isCertificateExpired()) {
      throw ValidationException("O certificado digital '${alias}' está vencido.")
    }
    return privateKey
  }
  private fun isCertificateExpired(): Boolean {
    return LocalDate.now().isAfter(notAfter)
  }
}


object CertificadoFactory {
  //TODO remove do cache quando o usuario trocar o certficado no servidor
  private val certificateCache = CacheBuilder.newBuilder()
    .maximumSize(CACHE_DE_CERTIFICADO_SIZE)
    .expireAfterWrite(CACHE_DE_CERTIFICADO_EXPIRE_HOUR, java.util.concurrent.TimeUnit.HOURS)
    .build<String, Certificado>()

  fun loadFromWindows(alias: String): Certificado {
    return certificateCache.get(alias) {
      val keyStore = KeyStore.getInstance("Windows-MY")
      keyStore.load(null, null)

      fromKeyStore(keyStore, alias)
    }
  }

  fun loadFromPfxPath(pathPfx: Path, senha: String): Certificado {
    return certificateCache.get(pathPfx.pathString) {
      val ks = KeyStore.getInstance("pkcs12")
      if(!pathPfx.exists()) throw ValidationException("Arquivo '${pathPfx.toAbsolutePath()}' de certificado não encontrado")

      ks.load(pathPfx.toFile().inputStream(), senha.toCharArray())
      fromKeyStore(ks, ks.aliases().nextElement(), senha)
    }
  }

  private fun fromKeyStore(keyStore: KeyStore, aliasRequired: String, senha: String = "0000"): Certificado {
    val al = keyStore.aliases()
    var certificate: X509Certificate? = null
    while (al.hasMoreElements()) {
      val alias = al.nextElement()
      if (keyStore.containsAlias(alias) && alias == aliasRequired) {
        val cert = keyStore.getCertificate(alias) as X509Certificate
        certificate = cert
        break
      }
    }
    if (certificate == null) throw ValidationException("Certificado '$aliasRequired' não encontrado")

    val privateKey =
      keyStore.getKey(keyStore.getCertificateAlias(certificate), senha.toCharArray()) as PrivateKey?
        ?: throw IllegalArgumentException("Não foi possível localizar a chave privada do certificado")

    return Certificado(privateKey, certificate, keyStore, aliasRequired, senha, certificate.notAfter.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate())
  }
}

