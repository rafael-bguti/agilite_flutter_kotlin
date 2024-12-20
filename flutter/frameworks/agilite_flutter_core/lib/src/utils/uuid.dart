import 'package:uuid/uuid.dart';

const _uuid = Uuid();

class UuidUtils {
  static String generate() {
    return _uuid.v4();
  }
}
