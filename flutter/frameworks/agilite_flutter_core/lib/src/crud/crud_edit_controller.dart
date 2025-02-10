import 'package:agilite_flutter_core/core.dart';

import 'crud_repository.dart';

class CrudEditController extends ViewController<bool> {
  final String taskName;
  final formController = FormController();
  final CrudRepository _repository;

  CrudEditController({
    required this.taskName,
    CrudRepository? repository,
  })  : _repository = repository ?? HttpCrudRepositoryAdapter(coreHttpProvider),
        super(true);

  @override
  void dispose() {
    formController.dispose();
    super.dispose();
  }

  Future<void> save(int? id) async {
    if (!formController.validate()) return;
    showLoading("Salvando registro");
    try {
      await _repository.save(taskName, formController.buidlJson(true), id);
    } on ValidationException catch (e, stack) {
      showError(e, stack);
      return;
    }

    hideLoading();
    ANavigator.pop("ok");
  }
}
