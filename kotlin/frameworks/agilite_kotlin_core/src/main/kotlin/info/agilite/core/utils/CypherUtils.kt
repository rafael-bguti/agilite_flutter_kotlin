package info.agilite.core.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object CypherUtils {
  fun encryptToPassword(password: String, steps: Int): String{
    return try {
      var chave = password
      val md: MessageDigest = MessageDigest.getInstance("SHA-256")
      for (i in 0 until steps) {
        val strSnh: ByteArray = chave.toByteArray()
        md.update(strSnh)
        chave = BigInteger(1, md.digest()).toString(16)
      }
      chave
    } catch (e: Exception) {
      throw RuntimeException("Erro ao criptografar texto", e)
    }
  }

  fun decryptAES(encryptedData: String, keyValue: String): String {
    val key = SecretKeySpec(keyValue.toByteArray(), "AES");
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, key)
    val decodedBytes: ByteArray = Base64.getUrlDecoder().decode(encryptedData)
    val originalBytes = cipher.doFinal(decodedBytes)

    return String(originalBytes)
  }
}