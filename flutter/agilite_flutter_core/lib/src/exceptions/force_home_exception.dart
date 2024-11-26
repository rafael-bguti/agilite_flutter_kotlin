class ForceHomeException implements Exception {
  final dynamic cause;

  ForceHomeException(this.cause);

  @override
  String toString() {
    return cause?.toString() ?? super.toString();
  }
}
