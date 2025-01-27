import 'dart:async';

import 'package:agilite_flutter_core/core.dart';

abstract class SduiContentProvider {
  FutureOr<String?> getContent();
}

class LocalSduiContentProvider implements SduiContentProvider {
  final String sduiJson;

  LocalSduiContentProvider(this.sduiJson);

  @override
  FutureOr<String?> getContent() {
    return sduiJson;
  }
}

//TODO adicionar um cache - porém quem deve definir se o componente deve ou não ser cacheado é o servidor
class RemoteSduiContentProvider implements SduiContentProvider {
  final String url;
  final Map<String, String?>? pathParams;

  RemoteSduiContentProvider({
    required this.url,
    this.pathParams,
  });

  @override
  FutureOr<String?> getContent() async {
    return coreHttpProvider.get(url, queryParameters: pathParams).then((response) {
      return response.bodyString;
    });
  }
}
