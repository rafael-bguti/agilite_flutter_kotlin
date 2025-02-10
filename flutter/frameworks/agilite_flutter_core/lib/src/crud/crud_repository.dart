import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/crud/models/crud_edit_response.dart';

import 'crud_state.dart';
import 'models/crud_list_request.dart';

abstract class CrudRepository {
  Future<CrudState> loadData(String taskName, CrudListRequest request);
  Future<CrudEditResponse> edit(String taskName, int? id);

  Future<void> save(String taskName, Map<String, dynamic> data, dynamic id);
  Future<void> delete(String taskName, List<Object> ids);
}

class HttpCrudRepositoryAdapter extends CrudRepository {
  final HttpProvider http;

  HttpCrudRepositoryAdapter(this.http);

  @override
  Future<CrudState> loadData(String taskName, CrudListRequest request) async {
    final response = await http.post("/crud/list/find/$taskName", body: request);
    return CrudState.fromJson(response.bodyMap);
  }

  @override
  Future<CrudEditResponse> edit(String taskName, int? id) async {
    final idPath = id != null ? "/$id" : "";
    final response = await http.get("/crud/$taskName/edit$idPath");
    return CrudEditResponse.fromJson(response.bodyMap);
  }

  @override
  Future<void> save(String taskName, Map<String, dynamic> data, dynamic id) async {
    if (id == null) {
      await http.post('/crud/$taskName', body: data);
    } else {
      await http.put('/crud/$taskName/$id', body: data);
    }
  }

  @override
  Future<void> delete(String taskName, List<Object> ids) async {
    await http.post('/crud/delete/$taskName', body: ids);
  }
}
