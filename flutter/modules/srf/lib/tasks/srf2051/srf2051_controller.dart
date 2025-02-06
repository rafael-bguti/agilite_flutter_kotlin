import 'dart:convert';

import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class Srf2051Controller extends ViewController<Srf2051State> {
  Srf2051Controller() : super(Srf2051State.initial());

  final fileController = FileController();

  @override
  Future<void> onViewLoaded() async {
    final response = await httpProvider.get("/srf2051");
    state = Srf2051State.fromJson(response.bodyMap);
  }

  @override
  void dispose() {
    fileController.dispose();
    super.dispose();
  }

  Future<void> processarRetorno() async {
    final bytes = await fileController.bytes;
    if (bytes == null) {
      showWarningSnack("Nenhum arquivo selecionado");
      return;
    }
    showLoading("Carregando dados do arquivo...");
    final xml = utf8.decode(bytes, allowMalformed: true);
    final response = await httpProvider.post(
      "/srf2051",
      headers: {"Content-Type": "text/plain"},
      body: xml,
      onSseMessage: showLoading,
    );

    fileController.clear();
    state = Srf2051State.fromJson(response.bodyMap);
  }
}

class Srf2051State {
  final int qtdDocsPraProcessar;
  final List<Map<String, dynamic>> docsProcessados;

  Srf2051State(this.qtdDocsPraProcessar, this.docsProcessados);

  factory Srf2051State.initial() {
    return Srf2051State(0, []);
  }
  factory Srf2051State.fromJson(Map<String, dynamic> json) {
    return Srf2051State(
      json["qtdDocsPraProcessar"] as int,
      List<Map<String, dynamic>>.from(json["docsProcessados"] as List),
    );
  }
}
