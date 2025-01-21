import 'dart:convert';

import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class Srf2030Controller extends ViewController<Srf2030State> {
  Srf2030Controller() : super(Srf2030InitialState());
  final FileController fileController = FileController();

  @override
  void dispose() {
    fileController.dispose();
    super.dispose();
  }

  Future<void> processarJson() async {
    final bytes = await fileController.bytes;
    if (bytes == null) {
      showWarningSnack("Nenhum arquivo selecionado");
    }
    showLoading("Carregando dados do arquivo...");
    final json = utf8.decode(bytes!, allowMalformed: true);
    await httpProvider.post("/srf2030", body: json);

    showSuccessSnack("Arquivo processado com sucesso");
    fileController.clear();
    showState();
  }
}

sealed class Srf2030State {}

class Srf2030InitialState extends Srf2030State {}
