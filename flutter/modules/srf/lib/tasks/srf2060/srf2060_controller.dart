import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:srf/tasks/srf2060/srf2060_models.dart';

const String spreadEmailsControllerName = "emails";

class Srf2060Controller extends ViewController<bool> {
  Srf2060Controller() : super(true);

  final formController = FormController();

  @override
  void dispose() {
    formController.dispose();
    super.dispose();
  }

  Future<void> buscarEmails() async {
    showLoading("Buscando e-mails para ser enviados");
    final filter = Srf2060Filter.fromJson(formController.buidlJson());
    final response = await httpProvider.post("/srf2060/listar", body: filter.toJson());
    final emails = response.bodyList;

    final spreadController = formController.getSpreadController(spreadEmailsControllerName);
    spreadController.fillFromList(emails);
    showState();
  }

  Future<void> enviarEmails() async {
    showLoading("Enviando e-mails");
    final spreadController = formController.getSpreadController(spreadEmailsControllerName);

    final allEmails = spreadController.listData;
    final selectedRows = spreadController.selectedRows;
    final emails = selectedRows.map((row) => allEmails[row]).toList();

    final response = await httpProvider.post(
      "/srf2060",
      body: emails,
      onSseMessage: showLoading,
    );
    showSuccessSnack("E-mails enviados com sucesso");
    await buscarEmails();
  }
}
