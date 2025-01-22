import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class Scf2010Controller extends ViewController<String> {
  Scf2010Controller() : super("");
  final spreadEmitirController = SpreadController("boletos");

  @override
  Future<void> onViewLoaded() async {
    showLoading("Buscando boletos que serão emitidos");
    final response = await httpProvider.get("/scf2010");
    spreadEmitirController.fillFromList(response.bodyList);
    showState();
  }

  @override
  void dispose() {
    spreadEmitirController.dispose();
    super.dispose();
  }

  Future<void> emitirDocumento() async {
    final selectedNFSe = spreadEmitirController.selectedData;
    if (selectedNFSe.isEmpty) {
      throw ValidationException("Selecione ao menos um boleto para enviar");
    }
    final ids = selectedNFSe.map((e) => e.getInt("scf02id")).toList();

    showLoading("Enviando boletos para o banco");
    final response = await httpProvider.post(
      "/scf2010",
      body: ids,
    );
    showSuccessSnack("Boletos enviados com sucesso!");
    await onViewLoaded();
  }
}
