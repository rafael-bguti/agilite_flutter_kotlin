class ValidationException implements Exception {
  final String message;

  ValidationException(this.message);

  @override
  String toString() {
    return message;
  }
}

class UnexpectedException implements Exception {
  final String message;

  UnexpectedException(this.message);

  @override
  String toString() {
    return message;
  }
}
