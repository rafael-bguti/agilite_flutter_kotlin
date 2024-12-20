import 'package:http/http.dart' as http;

class SseReceiver {
  final http.Client _client;
  final String uuid;
  final Stream<String> stream;

  void Function(String message)? onMessage;

  SseReceiver(this._client, this.uuid, this.stream, this.onMessage) {
    stream.listen(
      (event) {
        if (onMessage != null) {
          print("SSE: $event");
          if (event.startsWith("data:msg:")) {
            onMessage!(event.substring(9));
          }
        }
      },
      onError: (error) {
        print("Erro no fluxo SSE: $error"); //TODO
        _client.close();
      },
      onDone: () {
        print("Conexão SSE encerrada pelo servidor."); //TODO remover
        _client.close();
      },
      cancelOnError: true,
    );
  }
}
