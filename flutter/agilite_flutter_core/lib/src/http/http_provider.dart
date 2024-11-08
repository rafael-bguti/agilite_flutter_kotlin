import 'dart:async';
import 'dart:convert';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

String get apiBaseUrl => const String.fromEnvironment('API_BASE_URL', defaultValue: 'undefined_url');

abstract class HttpProvider {
  Future<HttpResponse> get(
    String path, {
    Map<String, dynamic>? queryParameters,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout,
    String? sseUid,
  });

  Future<HttpResponse> post(
    String path, {
    dynamic body,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout,
    String? sseUid,
  });

  Future<HttpResponse> put(
    String path, {
    dynamic body,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout,
  });

  Future<HttpResponse> delete(
    String path, {
    HttpResponseHandler? responseHandler,
    Duration timeout,
  });
}

typedef AuthorizationTokenGetter = FutureOr<String?> Function();

class HttpProviderImpl extends HttpProvider {
  static String? authorizationHeaderKey = 'Authorization';

  final AuthorizationTokenGetter? authorizationTokenGetter;
  final HttpResponseHandler? responseHandler;
  final Map<String, String>? defaultHeaders;

  HttpProviderImpl({
    this.authorizationTokenGetter,
    this.responseHandler,
    this.defaultHeaders,
  });

  @override
  Future<HttpResponse> delete(
    String path, {
    HttpResponseHandler? responseHandler,
    Duration timeout = const Duration(seconds: 60),
  }) {
    return _go(
      'delete',
      path: path,
      responseHandler: this.responseHandler ?? responseHandler,
      timeout: timeout,
    );
  }

  @override
  Future<HttpResponse> get(
    String path, {
    Map<String, dynamic>? queryParameters,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout = const Duration(seconds: 60),
    String? sseUid,
  }) {
    return _go(
      'get',
      path: path,
      data: queryParameters,
      headers: headers,
      responseHandler: this.responseHandler ?? responseHandler,
      timeout: timeout,
      sseUid: sseUid,
    );
  }

  @override
  Future<HttpResponse> post(
    String path, {
    dynamic body,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout = const Duration(seconds: 60),
    String? sseUid,
  }) {
    return _go(
      'post',
      path: path,
      data: body,
      headers: headers,
      responseHandler: this.responseHandler ?? responseHandler,
      timeout: timeout,
      sseUid: sseUid,
    );
  }

  @override
  Future<HttpResponse> put(
    String path, {
    dynamic body,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    Duration timeout = const Duration(seconds: 60),
  }) {
    return _go(
      'put',
      path: path,
      data: body,
      headers: headers,
      responseHandler: this.responseHandler ?? responseHandler,
      timeout: timeout,
    );
  }

  Future<HttpResponse> _go(
    String method, {
    required String path,
    required Duration timeout,
    dynamic data,
    Map<String, String>? headers,
    HttpResponseHandler? responseHandler,
    String? sseUid,
  }) async {
    debugPrint(' ---> method -> $path -> $data');

    Map<String, String> localHeaders = await _configureHeader(headers);
    if (sseUid != null) {
      localHeaders['X-SSE-UID'] = sseUid;
    }

    Uri uri;
    http.Response response;

    final client = http.Client();
    try {
      if (method == 'post') {
        final jsonBody = parseBody(data, localHeaders);
        uri = _url(path);
        response = await client.post(uri, headers: localHeaders, body: jsonBody).timeout(timeout);
      } else if (method == 'put') {
        final jsonBody = parseBody(data, localHeaders);
        uri = _url(path);
        response = await client.put(uri, headers: localHeaders, body: jsonBody).timeout(timeout);
      } else if (method == 'get') {
        uri = _url(path, data as Map<String, dynamic>?);
        response = await client.get(uri, headers: localHeaders).timeout(timeout);
      } else if (method == 'delete') {
        uri = _url(path);
        response = await client.delete(uri, headers: localHeaders).timeout(timeout);
      } else {
        throw UnexpectedException('Method $method for HTTP is not implemented');
      }
    } finally {
      client.close();
    }
    final httpResponse = HttpResponse(response);
    return await (responseHandler ?? DefaultHttpResponseHandler()).handle(this, httpResponse);
  }

  Future<Map<String, String>> _configureHeader(Map<String, String>? headers) async {
    var localHeaders = headers ?? {};
    localHeaders = {
      ...defaultHeaders ?? {},
      ...localHeaders,
    };
    _configureDefaultHeaders(localHeaders);
    await _addCurrentUserTokenHeader(localHeaders);
    return localHeaders;
  }

  String? parseBody(dynamic data, Map<String, String>? headers) {
    if (data == null) return null;

    bool needEncode = false;
    if (headers != null && data.runtimeType != String) {
      final contentType = headers['Content-Type'];
      if (contentType != null && contentType.contains('application/json')) {
        needEncode = true;
      }
    }

    return needEncode ? jsonEncode(data) : data.toString();
  }

  Uri _url(String path, [Map<String, dynamic>? queryParameters]) {
    if (path.startsWith("http://") || path.startsWith("https://")) {
      return Uri.parse(path).replace(queryParameters: queryParameters);
    } else {
      final String url = '$apiBaseUrl$path';
      return Uri.parse(url).replace(queryParameters: queryParameters);
    }
  }

  _configureDefaultHeaders(Map<String, String> headers) {
    headers.putIfAbsent('Content-Type', () => 'application/json');
    headers.putIfAbsent('Accept', () => 'application/json');
  }

  Future<void> _addCurrentUserTokenHeader(Map<String, String> headers) async {
    if (authorizationHeaderKey != null && authorizationTokenGetter != null) {
      if (headers.containsKey(authorizationHeaderKey!)) return;
      String? authorizationHeader = await authorizationTokenGetter!();
      if (authorizationHeader != null) {
        headers.putIfAbsent(authorizationHeaderKey!, () => authorizationHeader);
      }
    }
  }
}
