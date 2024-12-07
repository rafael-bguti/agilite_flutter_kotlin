import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/crud/crud_repository.dart';
import 'package:flutter/cupertino.dart';

class CrudController extends ViewController<CrudState> {
  static const String spreadDataName = 'data';
  static const String searchName = 'search';
  static const String groupIndexName = 'groupIndex';
  static const String pageSizeName = 'pageSize';
  static const String currentPageName = 'currentPage';
  static const String spreadDataSelectedColumnName = 'selected';

  final CrudRepository? _repository;

  //Controllers
  final formFiltersController = FormController();
  final dataFormController = FormController();

  final $loading = false.obs;
  CrudController({CrudRepository? repository})
      : _repository = repository ?? HttpCrudRepositoryAdapter(coreHttpProvider),
        super(CrudState.empty());

  @override
  @mustCallSuper
  Future<void> onViewLoaded() async {
    showLoading("Carregando dados do cadastro");
    await refresh(null);
  }

  // ------ Edição ------
  void deleteSelecteds() {
    print(' - Deletando selecionados');
  }

  // ------ Filtros ------
  bool get hasFilters {
    return formFiltersController.controllersValue.isNotEmpty;
  }

  void clearFilters() {
    formFiltersController.clear();
    doRefresh();
  }

  // ------ Atualizar dados ------
  Future<void> doRefresh() async {
    refresh(getFilters());
  }

  Future<void> refresh(Map<String, dynamic>? filters) async {
    $loading.value = true;
    print(' - Refreshing data by filters: $filters');

    await Future.delayed(const Duration(seconds: 1));
    state = CrudState(
      currentPage: 1,
      pageSize: 10,
      totalRecords: Random().nextInt(2569) + 50,
      data: [
        {"id": 50, "name": "João", "email": "joao@gmail.com"},
        {"id": 97, "name": "Maria", "email": "maria@gmail.com"},
      ],
      groups: [
        CrudStatusGroup(775, "todos"),
        CrudStatusGroup(50, "em aberto"),
        CrudStatusGroup(125, "aprovado"),
        CrudStatusGroup(22, "enviando"),
        CrudStatusGroup(8, "finalizado"),
      ],
      selectedGroupIndex: 3,
    );
    dataFormController.showValues({"data": state.data});
    $loading.value = false;
  }

  Map<String, dynamic> getFilters() {
    return formFiltersController.buidlJson();
  }

  // --- Diversos ---
  @override
  void dispose() {
    $loading.dispose();
    formFiltersController.dispose();

    super.dispose();
  }
}

class CrudState {
  final int currentPage;
  final int pageSize;
  final int totalRecords;
  final List<Map<String, dynamic>> data;

  //Crud com agrupamento - é exibido um TabHeader com os grupos em cima da Spread de dados
  final List<CrudStatusGroup>? groups;
  final int? selectedGroupIndex;

  CrudState({
    required this.currentPage,
    required this.pageSize,
    required this.totalRecords,
    required this.data,
    this.groups,
    this.selectedGroupIndex,
  });

  CrudState.empty()
      : currentPage = 0,
        pageSize = 10,
        totalRecords = 0,
        data = [],
        groups = null,
        selectedGroupIndex = null;
}

class CrudStatusGroup {
  final int qtd;
  final String title;
  final String? subtitle;

  CrudStatusGroup(this.qtd, this.title, {this.subtitle});
}
