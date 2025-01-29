import 'package:agilite_flutter_core/core.dart';

List<Option<String>> get ufComboOptions {
  return [
    LocalOption('AC', 'AC-Acre'),
    LocalOption('AL', 'AL-Alagoas'),
    LocalOption('AP', 'AP-Amapá'),
    LocalOption('AM', 'AM-Amazonas'),
    LocalOption('BA', 'BA-Bahia'),
    LocalOption('CE', 'CE-Ceará'),
    LocalOption('DF', 'DF-Distrito Federal'),
    LocalOption('ES', 'ES-Espírito Santo'),
    LocalOption('GO', 'GO-Goiás'),
    LocalOption('MA', 'MA-Maranhão'),
    LocalOption('MT', 'MT-Mato Grosso'),
    LocalOption('MS', 'MS-Mato Grosso do Sul'),
    LocalOption('MG', 'MG-Minas Gerais'),
    LocalOption('PA', 'PA-Pará'),
    LocalOption('PB', 'PB-Paraíba'),
    LocalOption('PR', 'PR-Paraná'),
    LocalOption('PE', 'PE-Pernambuco'),
    LocalOption('PI', 'PI-Piauí'),
    LocalOption('RJ', 'RJ-Rio de Janeiro'),
    LocalOption('RN', 'RN-Rio Grande do Norte'),
    LocalOption('RS', 'RS-Rio Grande do Sul'),
    LocalOption('RO', 'RO-Rondônia'),
    LocalOption('RR', 'RR-Roraima'),
    LocalOption('SC', 'SC-Santa Catarina'),
    LocalOption('SP', 'SP-São Paulo'),
    LocalOption('SE', 'SE-Sergipe'),
    LocalOption('TO', 'TO-Tocantins'),
  ];
}

String? formatFone(final String? fone) {
  if (fone.isNullOrBlank) return null;
  String? foneNumbers = fone!.onlyNumbers();
  if (foneNumbers == null) return null;

  if (foneNumbers.length == 8) {
    return '${foneNumbers.substring(0, 4)}-${foneNumbers.substring(4)}';
  } else if (foneNumbers.length == 10) {
    return '(${foneNumbers.substring(0, 2)}) ${foneNumbers.substring(2, 6)}-${foneNumbers.substring(6)}';
  } else if (foneNumbers.length == 11) {
    return '(${foneNumbers.substring(0, 2)}) ${foneNumbers.substring(2, 7)}-${foneNumbers.substring(7)}';
  } else if (foneNumbers.length > 11) {
    final fonePart = foneNumbers.substring(foneNumbers.length - 11);
    final prefix = foneNumbers.substring(0, foneNumbers.length - 11);

    return '$prefix (${fonePart.substring(0, 2)}) ${fonePart.substring(2, 7)}-${fonePart.substring(7)}';
  } else {
    return foneNumbers;
  }
}
