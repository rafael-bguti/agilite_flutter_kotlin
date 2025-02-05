import 'package:json_annotation/json_annotation.dart';

part 'crud_state.g.dart';

@JsonSerializable(createToJson: false)
class CrudState {
  final int currentPage;
  final int pageSize;
  final List<Map<String, dynamic>> data;

  //Crud com agrupamento - é exibido um TabHeader com os grupos em cima da Spread de dados
  final List<CrudStatusGroup>? groups;
  final int? selectedGroupIndex;

  CrudState({
    required this.currentPage,
    required this.pageSize,
    required this.data,
    this.groups,
    this.selectedGroupIndex,
  });

  CrudState.empty()
      : currentPage = 0,
        pageSize = 10,
        data = [],
        groups = null,
        selectedGroupIndex = null;

  factory CrudState.fromJson(Map<String, dynamic> json) => _$CrudStateFromJson(json);

  int get currentPageSize {
    if (pageSize == 0) return 10;
    return pageSize;
  }
}

@JsonSerializable(createToJson: false)
class CrudStatusGroup {
  final int? qtd;
  final String title;
  final String? subtitle;

  CrudStatusGroup(this.title, {this.qtd, this.subtitle});

  factory CrudStatusGroup.fromJson(Map<String, dynamic> json) => _$CrudStatusGroupFromJson(json);
}
