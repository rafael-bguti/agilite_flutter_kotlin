import 'package:agilite_flutter_core/core.dart';

class CrudEditController extends ViewController<bool> {
  final int? id;
  final formController = FormController();

  CrudEditController({required this.id}) : super(true);

  @override
  Future<void> onViewLoaded() async {
    showLoading("Carregando registro");

    formController.value = {
      "id": id,
      "nome": "Nome do registro",
      "descricao": "Descrição do registro",
      "valor": 123.45,
      "data": DateTime.now(),
      "ativo": true,
    };
    state = true;
  }

  Future<void> save() async {
    if (!formController.validate()) return;

    showLoading("Salvando registro");
    print(formController.buidlJson());

    hideLoading();
    ANavigator.pop("ok");
  }
}
