import 'package:agilite_flutter_core/core.dart';

import 'crud_repository.dart';

class CrudEditController extends ViewController<CrudEditState> {
  final String taskName;
  final int? id;

  final formController = FormController();
  final CrudRepository _repository;

  CrudEditController({
    required this.taskName,
    required this.id,
    CrudRepository? repository,
  })  : _repository = repository ?? HttpCrudRepositoryAdapter(coreHttpProvider),
        super(CrudEditInitState());

  @override
  Future<void> onViewLoaded() async {
    showLoading("Carregando registro");
    if (id == null) {
      final data = await _repository.onNew(taskName);
      state = CrudEditEditingState(data ?? {}, true);
    } else {
      final response = await _repository.edit(taskName, id!);
      state = CrudEditEditingState(response.data, response.editable);
    }
  }

  Future<void> save() async {
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

sealed class CrudEditState {}

class CrudEditInitState extends CrudEditState {}

class CrudEditEditingState extends CrudEditState {
  final Map<String, dynamic> data;
  final bool editable;

  CrudEditEditingState(this.data, this.editable);
}
