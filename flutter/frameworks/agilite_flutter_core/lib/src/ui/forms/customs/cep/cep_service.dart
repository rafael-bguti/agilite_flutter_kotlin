import 'package:agilite_flutter_core/core.dart';

import 'cep.dart';

abstract class CepService {
  Future<Cep> find(String cep);
}

class CepServiceViaCepImpl implements CepService {
  final HttpProvider http;

  CepServiceViaCepImpl(this.http);

  static const _baseUrl = 'https://viacep.com.br/ws/';
  @override
  Future<Cep> find(String cep) async {
    cep = cep.onlyNumbers() ?? "";
    if (cep.length != 8) throw ValidationException('Informe um CEP válido');

    final response = await http.get('$_baseUrl$cep/json');
    if (response.statusCode == 200) {
      final map = response.bodyMap;
      if (map['erro'] == true) {
        throw ValidationException('CEP não encontrado');
      }

      return Cep.fromJson(response.bodyMap);
    } else {
      throw Exception('Failed to load CEP');
    }
  }
}
