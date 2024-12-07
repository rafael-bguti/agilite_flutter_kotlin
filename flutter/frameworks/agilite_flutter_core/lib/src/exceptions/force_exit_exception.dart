class ForceExitException implements Exception {
  final dynamic cause;

  ForceExitException(this.cause);

  @override
  String toString() {
    return cause?.toString() ?? super.toString();
  }
}
