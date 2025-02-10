import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

abstract class HttpResponseHandler {
  Future<HttpResponse> handle(HttpResponse response);
}

class DefaultHttpResponseHandler extends HttpResponseHandler {
  @override
  Future<HttpResponse> handle(HttpResponse response) async {
    debugPrint(' <--- Length(${response.bodyBytes.lengthInBytes / 1024}kb) - ${response.bodyString}');

    switch (response.statusCode) {
      case 200:
      case 204:
        return response;
      case 400:
        throw BadRequestException(response.bodyString);
      case 401:
        throw UnauthenticatedException(response.reasonPhrase, response.bodyString);
      case 403:
        throw ForbiddenException(response.reasonPhrase, response.bodyString);
      case 404:
        throw NotFoundException(response.reasonPhrase, response.bodyString);
      case 428:
        //428 - Precondition Required - Validação no Servidor
        throw ValidationException(response.bodyString);
      case 500:
        throw ServerException(response.reasonPhrase, response.bodyString);
      default:
        throw UnexpectedException('HttpErrorUnexpected: ${response.statusCode}-${response.reasonPhrase}');
    }
  }
}
