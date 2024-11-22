import 'package:agilite_flutter_core/core.dart';

class ACrudEditController extends AViewController<bool> {
  final int? id;
  final formController = AFormController();

  ACrudEditController({required this.id}) : super(true);

  @override
  void onViewLoaded() async {
    showLoading("Carregando registro");
    await Future.delayed(Duration(seconds: 2));

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
}
