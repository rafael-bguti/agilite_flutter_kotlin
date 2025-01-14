import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:srf/tasks/srf2060/srf2060_models.dart';

class Srf2060Controller extends ViewController<Srf2060State> {
  Srf2060Controller() : super(Srf2060InitialState());

  final formFilterController = FormController();
  final spreadEnvioController = SpreadController("emails");

  @override
  void dispose() {
    formFilterController.dispose();
    spreadEnvioController.dispose();

    super.dispose();
  }

  Future<void> buscarEmails() async {
    showLoading("Buscando e-mails para ser enviados");
    final filter = Srf2060Filter.fromJson(formFilterController.buidlJson());
    final response = await httpProvider.post("/srf2060/listar", body: filter.toJson());

    final emails = response.bodyList;
    if (emails.isEmpty) {
      state = Srf2060EmptyState();
    } else {
      spreadEnvioController.fillFromList(emails);
      state = Srf2060FullState();
    }
  }

  Future<void> enviarEmails() async {
    if (state is Srf2060FullState) {
      final selectedRows = spreadEnvioController.selectedRows;
      if (selectedRows.isEmpty) {
        throw ValidationException("Selecione ao menos um e-mail para enviar");
      }
      final allEmails = spreadEnvioController.listData;
      final emailsAEnviar = selectedRows.map((row) => allEmails[row]).toList();
      showLoading("Enviando e-mails");

      await httpProvider.post(
        "/srf2060",
        body: emailsAEnviar,
        onSseMessage: showLoading,
      );

      showSuccessSnack("E-mails enviados com sucesso");
      await buscarEmails();
    }
  }
}

sealed class Srf2060State {}

class Srf2060InitialState extends Srf2060State {}

class Srf2060EmptyState extends Srf2060State {}

class Srf2060FullState extends Srf2060State {}
