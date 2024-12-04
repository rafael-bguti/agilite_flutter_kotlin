package info.agilite.core.utils

import info.agilite.core.orm.Entity

class ValidationUtils {
  companion object {
    fun <T> notNull(value: T?, throwError: (()->Exception)? = null): T {
      if(value == null || (value is Entity && value.id == null)) {
        throwError?.let { throw it() } ?: throw NullPointerException("Valor não pode ser nulo - ValidationUtils")
      }
      return value
    }
  }
}