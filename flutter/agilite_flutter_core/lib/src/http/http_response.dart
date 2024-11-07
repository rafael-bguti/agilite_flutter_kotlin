import 'dart:convert';
import 'dart:typed_data';

import 'package:agilite_flutter_core/core.dart';
import 'package:http/http.dart' as http;

class HttpResponse {
  final http.Response _response;

  HttpResponse(this._response);

  Uint8List get bodyBytes => _response.bodyBytes;
  String get bodyString => bodyBytes.isEmpty ? '' : utf8.decode(bodyBytes);

  LowercaseMap get bodyMap => !hasBody
      ? throw UnexpectedException(
          'Server response is null',
        )
      : bodyString.toLowercaseMap();
  List<LowercaseMap> get bodyListLowerCaseMap => bodyString.toListLowercaseMap();

  List<Map<String, dynamic>> get bodyList => bodyString.toListMap();

  int get statusCode => _response.statusCode;
  String? get reasonPhrase => _response.reasonPhrase;
  bool get hasBody => !_response.bodyBytes.isNullOrEmpty;
}
