import 'dart:convert';
import 'dart:typed_data';

import 'package:agilite_flutter_core/core.dart';
import 'package:http/http.dart' as http;

class HttpResponse {
  final http.Response _response;

  HttpResponse(this._response);

  Uint8List get bodyBytes => _response.bodyBytes;
  String get bodyString => bodyBytes.isEmpty ? '' : utf8.decode(bodyBytes);

  Map<String, dynamic> get bodyMap => !hasBody
      ? throw UnexpectedException(
          'Server response is null',
        )
      : bodyString.toMap();
  List<LowercaseMap> get bodyListLowerCaseMap => !hasBody
      ? throw UnexpectedException(
          'Server response is null',
        )
      : bodyString.toListLowercaseMap();

  List<Map<String, dynamic>> get bodyList => !hasBody
      ? throw UnexpectedException(
          'Server response is null',
        )
      : bodyString.toListMap();

  int get statusCode => _response.statusCode;
  String? get reasonPhrase => _response.reasonPhrase;
  bool get hasBody => !_response.bodyBytes.isNullOrEmpty;
}
