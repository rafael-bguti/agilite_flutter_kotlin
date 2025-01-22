import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class Srf2050Controller extends ViewController<Srf2050State> {
  Srf2050Controller() : super(Srf2050InitialState());

  final spreadEmitirController = SpreadController("docs");
  final inputFileDestino = FileController();

  @override
  Future<void> onViewLoaded() async {
    showLoading("Buscando documentos que serão emitidos");
    final response = await httpProvider.get("/srf2050");
    spreadEmitirController.fillFromList(response.bodyList);
    showState();
  }

  @override
  void dispose() {
    spreadEmitirController.dispose();
    inputFileDestino.dispose();

    super.dispose();
  }

  Future<void> emitirDocumento() async {
    final selectedNFSe = spreadEmitirController.selectedData;
    if (selectedNFSe.isEmpty) {
      throw ValidationException("Selecione ao menos uma NFe para enviar");
    }
    final ids = selectedNFSe.map((e) => e.getInt("srf01id")).toList();

    showLoading("Emitindo NFSe´s");
    final response = await httpProvider.post(
      "/srf2050",
      body: ids,
      headers: {'Accept': 'application/xml'},
    );
    final path = await saveFile("nfses.xml", response.bodyBytes);

    await showSuccess("Arquivo da NFSe gerado com sucesso '$path'");
    await onViewLoaded();
  }
}

sealed class Srf2050State {}

class Srf2050InitialState extends Srf2050State {}
