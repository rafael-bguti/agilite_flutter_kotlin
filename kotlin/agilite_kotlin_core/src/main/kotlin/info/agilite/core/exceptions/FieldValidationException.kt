package info.agilite.core.exceptions

class FieldValidationException(
  val fieldName: String,
  message: String
) : RuntimeException(message)