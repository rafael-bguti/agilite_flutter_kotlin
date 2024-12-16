package info.agilite.core.exceptions

class ToManyRequestException: RuntimeException {
  constructor(): super()
  constructor(message: String): super(message)
}