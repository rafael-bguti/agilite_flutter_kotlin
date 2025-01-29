import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/crud/crud_repository.dart';
import 'package:flutter/cupertino.dart';

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
  final formFiltersController = FormController();

  final spreadController = SpreadController("data");

  //Navigation
  var currentPage = 0;
  var pageSize = 10;
  var groupIndex = 0;

  final $loading = false.obs;
  CrudController({
    required this.taskName,
    this.metadataToLoad,
    CrudRepository? repository,
  })  : _repository = repository ?? HttpCrudRepositoryAdapter(coreHttpProvider),
        super(CrudState.empty()) {
    formFiltersController.addControllerListener(
      (controller) {
        if (controller is FormFieldController) {
          controller.onValueChanged.addListener(onFilterChanged);
        } else {
          controller.onValueChanged.addListener(_refresh);
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

  // ------ Edição ------
  void deleteSelected() {
    print(' - Deletando selecionados');
  }

  // ------ Filtros ------
  bool get hasFilters {
    return formFiltersController.controllersValue.isNotEmpty;
  }

  void clearFilters() {
    formFiltersController.clear();
    _clearCurrentPage();
    _refresh();
  }

  // ------ Atualizar dados ------
  void onFilterChanged() {
    _clearCurrentPage();
    _searchDebouceTimer.run(_refresh);
  }

  Future<void> pageNavigate(int delta) async {
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
    _refresh();
  }

  void doRefresh() {
    _refresh();
  }

  Future<void> _refresh([bool byNextPageNavigation = false]) async {
    $loading.value = true;
    try {
      final request = buildRequest();
      final stateResponse = await _repository.loadData(taskName, request);
      if (stateResponse.data.isEmpty && byNextPageNavigation) {
        showWarningSnack("Nenhuma nova página localizada");
      } else {
        state = stateResponse;
        spreadController.fillFromList(state.data);
      }
    } finally {
      $loading.value = false;
    }
  }

  CrudListRequest buildRequest() {
    final customFilters = formFiltersController.buidlJson();
    final search = customFilters.remove(searchName);

    return CrudListRequest(
      currentPage: currentPage,
      pageSize: pageSize,
      search: search,
      customFilters: customFilters,
      groupIndex: groupIndex,
    );
  }

  // --- Diversos ---
  @override
  void dispose() {
    $loading.dispose();
    formFiltersController.dispose();
    spreadController.dispose();
    _searchDebouceTimer.dispose();

    super.dispose();
  }

  void _clearCurrentPage() {
    currentPage = 0;
  }
}
