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
    } else {
      final response = await _repository.edit(taskName, id!);
      formController.value = response.data;
      state = CrudEditEditingState(response.data, response.editable);
    }
  }

  Future<void> save() async {
    if (!formController.validate()) return;
    showLoading("Salvando registro");
    await _repository.save(taskName, formController.buidlJson(), id);

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
