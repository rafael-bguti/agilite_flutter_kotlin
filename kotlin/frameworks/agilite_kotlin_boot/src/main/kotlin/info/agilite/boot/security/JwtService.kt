package info.agilite.boot.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

interface JwtService {
  fun extractUserIdByToken(token: String?): String?
  fun generateToken(userId: Long, minutesToExpired: Long): String
  fun isTokenExpired(token: String?): Boolean
}

@Service
class JwtServiceImpl : JwtService {
  @Value("\${jwt.signing.key}")
  lateinit var jwtSigningKey: String

  override fun extractUserIdByToken(token: String?): String {
    return extractClaim<String>(token, Claims::getSubject)
  }

  override fun generateToken(userId: Long, minutesToExpired: Long): String {
    return return Jwts.builder()
      .subject(userId.toString())
      .issuedAt(Date(System.currentTimeMillis()))
      .expiration(Date(System.currentTimeMillis() + Duration.ofMinutes(minutesToExpired).toMillis()))
      .signWith(signingKey, Jwts.SIG.HS512)
      .compact()
  }

  private fun <T> extractClaim(token: String?, claimsResolvers: Function<Claims, T>): T {
    val claims: Claims = extractAllClaims(token)
    return claimsResolvers.apply(claims)
  }

  override fun isTokenExpired(token: String?): Boolean {
    try{
      return extractExpiration(token).before(Date())
    } catch (e: ExpiredJwtException) {
      return true
    }
  }

  private fun extractExpiration(token: String?): Date {
    return extractClaim<Date>(token, Claims::getExpiration)
  }

  private fun extractAllClaims(token: String?): Claims {
    return Jwts.parser()
      .verifyWith(signingKey)
      .build()
      .parseSignedClaims(token)
      .payload
  }

  private var keyCache: SecretKey? = null
  private val signingKey: SecretKey
    get() {
      if(keyCache != null) return keyCache!!

      val keyBytes: ByteArray = Decoders.BASE64.decode(jwtSigningKey)
      keyCache = SecretKeySpec(keyBytes, "HmacSHA512")
      return keyCache!!
    }
}