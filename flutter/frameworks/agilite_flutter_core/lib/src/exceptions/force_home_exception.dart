class ForceHomeException implements Exception {
  final dynamic cause;
  final StackTrace? stackTrace;

  ForceHomeException(this.cause, [this.stackTrace]);

  @override
  String toString() {
    return cause?.toString() ?? super.toString();
  }
}
