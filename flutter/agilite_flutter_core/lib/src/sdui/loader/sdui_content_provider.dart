import 'dart:async';

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
    //TODO Código de testes

    return """
    {
     "name": "SduiCrud",
     "descr": {
        "singular": "Cliente"
     },
     "columns": [
        {
          "name": "name",
          "label": "Nome",
          "type": "string"
        },
        {
          "name": "email",
          "label": "Email",
          "type": "string"
        }
      ]
    }
    """;

    // return """
    // {
    //  "name": "SduiColumn",
    //  "spacing": 64,
    //   "children": [
    //     {
    //       "name": "SduiText",
    //       "text": "Hello World"
    //     },
    //     {
    //       "name": "SduiText",
    //       "text": "Hello World 2"
    //     }
    //
    //   ]
    // }
    // """;

    //TODO - Código correto
    // return coreHttpProvider.get(url, queryParameters: pathParams).then((response) {
    //   return response.bodyString;
    // });
  }
}
