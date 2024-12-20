import 'dart:convert';

import 'package:agilite_flutter_core/core.dart';
import 'package:http/http.dart' as http;
import 'package:uuid/uuid.dart';

abstract class SseProvider {
  Future<SseReceiver> createReceiver(void Function(String message)? onMessage);
}

class SseProviderImpl extends SseProvider {
  @override
  Future<SseReceiver> createReceiver(void Function(String message)? onMessage) async {
    final uuid = UuidUtils.generate();

    var client = http.Client();
    var request = http.Request("POST", Uri.parse("$apiBaseUrl/public/messenger"));
    request.headers['Accept'] = 'text/event-stream';
    request.headers['Content-Type'] = 'application/json';
    request.body = uuid;

    var response = await client.send(request);

    return SseReceiver(
      client,
      uuid,
      response.stream.transform(const Utf8Decoder()).transform(const LineSplitter()),
      onMessage,
    );
  }
}
