import 'package:agilite_flutter_core/core.dart';

import 'crud_state.dart';

abstract class CrudRepository {
  Future<CrudState> loadData(String taskName, CrudListRequest request);
}

class HttpCrudRepositoryAdapter extends CrudRepository {
  final HttpProvider http;

  HttpCrudRepositoryAdapter(this.http);

  @override
  Future<CrudState> loadData(String taskName, CrudListRequest request) async {
    final response = await http.post("/crud/list/find/$taskName", body: request);
    return CrudState.fromJson(response.bodyMap);
  }
}

class CrudListRequest {
  final int currentPage;
  final int pageSize;
  final String? search;
  final Map<String, dynamic>? customFilters;
  final int? groupIndex;

  CrudListRequest({
    required this.currentPage,
    required this.pageSize,
    this.search,
    this.customFilters,
    this.groupIndex,
  });

  Map<String, dynamic> toJson() {
    return {
      'currentPage': currentPage,
      'pageSize': pageSize,
      'search': search,
      'customFilters': customFilters,
      'groupIndex': groupIndex,
    };
  }
}
