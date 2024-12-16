package info.agilite.integradores.bancos.inter

import java.time.LocalDateTime
import java.time.ZoneOffset

private const val ADDITIONAL_TIME = 60;
data class TokenBancoInter(
  val access_token: String,
  val token_type: String,
  val expires_in: Int,
  val scope: String,
  val createdAt: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
) {
  fun isValid(): Boolean {
    val expirationDate: Long = createdAt + expires_in
    val now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    return (now + ADDITIONAL_TIME) <= expirationDate
  }
}