import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/cupertino.dart';

import 'crud_repository.dart';
import 'crud_state.dart';
import 'models/crud_list_request.dart';

class CrudController extends ViewController<CrudState> {
  static const String spreadDataName = 'data';
  static const String searchName = 'search';

  final String taskName;
  final String? metadataToLoad;

  final CrudRepository _repository;
  final _searchDebouceTimer = DebounceTimer();

  //Controllers
  final filtersFormController = FormController();
  final moreFiltersFormController = FormController();
  final spreadController = SpreadController("data");

  //Edição
  final bool canEdit;

  //Navigation
  var currentPage = 0;
  var pageSize = 10;
  var groupIndex = 0;

  final $loading = false.obs;
  CrudController({
    required this.taskName,
    this.canEdit = true,
    this.metadataToLoad,
    CrudRepository? repository,
  })  : _repository = repository ?? HttpCrudRepositoryAdapter(coreHttpProvider),
        super(CrudState.empty()) {
    filtersFormController.addControllerListener(
      (controller) {
        if (controller is FormFieldController) {
          controller.addValueChangeListener(onFilterChanged);
        } else {
          controller.addValueChangeListener(() {
            _clearCurrentPage();
            _refresh();
          });
        }
      },
    );
  }

  @override
  @mustCallSuper
  Future<void> onViewLoaded() async {
    if (metadataToLoad != null) {
      await metadataRepository.loadFieldsByNames([metadataToLoad!]);
    }
    await _refresh();
  }

  Future<void> onBtnRefreshClick() async {
    _clearCurrentPage();
    await _refresh();
  }

  // ------ Filtros ------
  bool get hasFilters {
    return filtersFormController.getControllersValue().isNotEmpty || moreFiltersFormController.getControllersValue().isNotEmpty;
  }

  void onClearFiltersClicked() {
    filtersFormController.clear();
    moreFiltersFormController.clear();
    onBtnRefreshClick();
  }

  // ------ Atualizar dados ------
  void onFilterChanged() {
    _clearCurrentPage();
    _searchDebouceTimer.run(_refresh);
  }

  Future<void> onPageNavigateClicked(int delta) async {
    final newPage = state.currentPage + delta;
    currentPage = max(0, newPage);
    _refresh(delta > 0);
  }

  Future<void> onPageSizeChange(int pgSize) async {
    _clearCurrentPage();
    pageSize = pgSize;
    _refresh();
  }

  Future<void> onGroupChanged(int groupIndex) async {
    this.groupIndex = groupIndex;
    onBtnRefreshClick();
  }

  void onDeleteClicked() {
    final selectedRows = spreadController.selectedRows;
    if (selectedRows.length == 0) {
      showWarningSnack("Nenhum registro selecionado para exclusão");
      return;
    }

    final msg = selectedRows.length == 1 ? "do registro selecionado?" : "dos ${selectedRows.length} registros selecionados?";
    showQuestionMessage("Confirma a exclusão $msg", (ctx) => _onConfirmDeleteSelected(ctx, selectedRows));
  }

  Future<void> _onConfirmDeleteSelected(BuildContext context, List<int> selectedRows) async {
    try {
      showLoading("Excluindo registros selecionados...");
      final List<Object> selectedIds = [];

      for (final rowIndex in selectedRows) {
        selectedIds.add(_getIdByRowIndex(rowIndex));
      }

      await _repository.delete(taskName, selectedIds);

      await onBtnRefreshClick();
    } catch (error, stack) {
      showError(error, stack);
    }
  }

  Object _getIdByRowIndex(int rowIndex) {
    final row = state.data[rowIndex];
    final id = row["id"];
    if (id == null) {
      throw 'Não foi possível localizar o valor do ID do registro';
    }
    return id as Object;
  }

  Future<void> _refresh([bool byNextPageNavigation = false]) async {
    $loading.value = true;
    try {
      final request = _buildRequest();
      final stateResponse = await _repository.loadData(taskName, request);
      if (stateResponse.data.isEmpty && byNextPageNavigation) {
        showWarningSnack("Nenhuma nova página localizada");
      } else {
        state = stateResponse;
      }
    } finally {
      $loading.value = false;
    }
  }

  CrudListRequest _buildRequest() {
    final customFilters = filtersFormController.buidlJson();
    final search = customFilters.remove(searchName) as String?;

    return CrudListRequest(
      currentPage: currentPage,
      pageSize: pageSize,
      search: search,
      customFilters: customFilters,
      groupIndex: groupIndex,
      dialogMoreFiltersValue: moreFiltersFormController.buidlJson(),
    );
  }

  // --- Edição ---
  Future<void> onEdit(int? id, BuildContext context, CrudDescr descr, Widget? form) async {
    showLoading("Carregando dados para edição...");
    final response = await _repository.edit(taskName, id);
    if (response.sduiForm != null) {
      form = SduiRender.renderFromJson(context, response.sduiForm!);
    }
    hideLoading();

    final saved = await ASideDialog.showBottom(
      builder: (context) => AEditCrud(
        taskName: taskName,
        descr: descr,
        data: response.data,
        formBody: form!,
        id: id,
      ),
      barrierDismissible: false,
    );

    if (saved == true) {
      await onBtnRefreshClick();
    }
  }

  // --- Diversos ---
  @override
  void dispose() {
    $loading.dispose();
    filtersFormController.dispose();
    spreadController.dispose();
    _searchDebouceTimer.dispose();
    moreFiltersFormController.dispose();

    super.dispose();
  }

  void _clearCurrentPage() {
    currentPage = 0;
  }
}
