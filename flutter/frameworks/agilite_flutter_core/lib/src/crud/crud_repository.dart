import 'package:agilite_flutter_core/core.dart';

abstract class CrudRepository {}

class HttpCrudRepositoryAdapter extends CrudRepository {
  final HttpProvider http;

  HttpCrudRepositoryAdapter(this.http);
}
