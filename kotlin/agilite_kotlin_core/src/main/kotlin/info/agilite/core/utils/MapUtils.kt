package info.agilite.core.utils

import java.util.*


class MapUtils {
  companion object {
    fun <K, V> newMap(vararg keyAndValues: Any?): Map<K, V>? {
      if (keyAndValues.isEmpty()) return null
      if (keyAndValues.size % 2 != 0) throw RuntimeException("Deve ser informado um número par de parâmetros para gerar o MAP")

      val retorno = TreeMap<K, V>()

      var i = 0
      while (i < keyAndValues.size) {
        val key = keyAndValues[i]
        val value = keyAndValues[i + 1]
        retorno[key as K] = value as V
        i += 2
      }

      return retorno
    }
  }
}