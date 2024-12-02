package info.agilite.shared.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object ZipUtils {
    fun compressToBase64(content: String): String {
        val bytes = compress(content)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun compress(content: String): ByteArray {
        return compress(content.toByteArray(StandardCharsets.UTF_8))
    }

    fun compress(content: ByteArray?): ByteArray {
        ByteArrayOutputStream().use { os ->
            try {
                GZIPOutputStream(os).use { zipOs -> zipOs.write(content) }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return os.toByteArray()
        }
    }

    fun decompressToString(conteudo: ByteArray?): String {
        return String(decompress(conteudo), StandardCharsets.UTF_8)
    }

    fun decompress(conteudo: ByteArray?): ByteArray {
        ByteArrayOutputStream().use { out ->
            try {
                GZIPInputStream(ByteArrayInputStream(conteudo)).use { `is` -> `is`.transferTo(out) }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return out.toByteArray()
        }
    }
}
