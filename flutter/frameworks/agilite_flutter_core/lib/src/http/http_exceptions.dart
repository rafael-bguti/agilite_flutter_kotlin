abstract class HttpException implements Exception {
  final String? message;
  final String? body;

  HttpException(this.message, [this.body]);
}

class BadRequestException extends HttpException {
  BadRequestException([String? body]) : super(body, body);
}

class NotFoundException extends HttpException {
  NotFoundException([super.message, super.body]);
}

class UnauthenticatedException extends HttpException {
  UnauthenticatedException([super.message, super.body]);

  @override
  String toString() {
    return 'Seu usuário não está autenticado no sistema.';
  }
}

class ForbiddenException extends HttpException {
  ForbiddenException([super.message, super.body]);
  @override
  String toString() {
    return 'Você não tem permissão para acessar esta tarefa.';
  }
}

class ServerException extends HttpException {
  ServerException([super.message, super.body]);
}
