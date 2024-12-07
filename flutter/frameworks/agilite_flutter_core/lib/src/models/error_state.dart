class ErrorState {
  final dynamic error;
  final StackTrace stack;
  final String? title;
  final void Function()? onBackButtonPressed;

  const ErrorState({
    required this.error,
    required this.stack,
    this.title,
    this.onBackButtonPressed,
  });
}
