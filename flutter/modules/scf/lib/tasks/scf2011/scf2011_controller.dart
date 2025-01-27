import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class Scf2011Controller extends ViewController<Scf2011State> {
  final formController = FormController();

  @override
  Future<void> onViewLoaded() async {
    await metadataRepository.loadFieldsByNames([
      'cgs38RecBoletoComApi',
    ]);
    state = Scf2011InitialState();
  }

  Future<void> processarBoletos() async {
    if (!formController.validate()) return;
    showLoading("baixando boletos");
    final selectedCgs38 = formController.getValue('cgs38RecBoletoComApi') as RemoteOption;

    final response = await httpProvider.post('/scf2011/${selectedCgs38.key}');
    final boletosProcessados = response.bodyList.map((e) => BoletoProcessado.fromJson(e)).toList();

    state = Scf2011CompleteState(boletosProcessados);
  }
}

abstract class Scf2011State {}

class Scf2011InitialState extends Scf2011State {}

class Scf2011CompleteState extends Scf2011State {
  final List<BoletoProcessado> boletosProcessados;

  Scf2011CompleteState(this.boletosProcessados);
}

class BoletoProcessado {
  final String? numero;
  final DateTime? dataVencimento;

  final BoletoRecebido recebimento;
  final bool baixadoComSucesso;
  final String status;

  BoletoProcessado({
    this.numero,
    this.dataVencimento,
    required this.recebimento,
    required this.baixadoComSucesso,
    required this.status,
  });

  factory BoletoProcessado.fromJson(Map<String, dynamic> json) {
    return BoletoProcessado(
      numero: json['numero'] as String?,
      dataVencimento: json['dataVencimento'] != null ? DateTime.parse(json['dataVencimento'] as String) : null,
      recebimento: BoletoRecebido.fromJson(json['recebimento']),
      baixadoComSucesso: json['baixadoComSucesso'] as bool,
      status: json['status'] as String,
    );
  }
}

class BoletoRecebido {
  final String codigoSolicitacao;
  final String seuNumero;
  final String situacao;
  final DateTime dataSituacao;
  final double valorTotalRecebido;
  final PagadorRecebido? pagador;

  BoletoRecebido({
    required this.codigoSolicitacao,
    required this.seuNumero,
    required this.situacao,
    required this.dataSituacao,
    required this.valorTotalRecebido,
    this.pagador,
  });

  factory BoletoRecebido.fromJson(Map<String, dynamic> json) {
    return BoletoRecebido(
      codigoSolicitacao: json['codigoSolicitacao'] as String,
      seuNumero: json['seuNumero'] as String,
      situacao: json['situacao'] as String,
      dataSituacao: DateTime.parse(json['dataSituacao'] as String),
      valorTotalRecebido: json['valorTotalRecebido'] as double,
      pagador: json['pagador'] != null ? PagadorRecebido.fromJson(json['pagador']) : null,
    );
  }
}

class PagadorRecebido {
  final String? nome;
  final String? cpfCnpj;

  PagadorRecebido({
    this.nome,
    this.cpfCnpj,
  });

  factory PagadorRecebido.fromJson(Map<String, dynamic> json) {
    return PagadorRecebido(
      nome: json['nome'] as String?,
      cpfCnpj: json['cpfCnpj'] as String?,
    );
  }
}
