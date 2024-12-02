package info.agilite.shared.exceptions

class FieldValidationException(
  val fieldName: String,
  message: String
) : RuntimeException(message)