class Srf2060Filter {
  final DateTime? emissIni;
  final DateTime? emissFim;
  final bool? reenviar;

  Srf2060Filter({
    this.emissIni,
    this.emissFim,
    this.reenviar,
  });

  Map<String, dynamic> toJson() {
    return {
      'emissIni': emissIni,
      'emissFim': emissFim,
      'reenviar': reenviar,
    };
  }

  factory Srf2060Filter.fromJson(Map<String, dynamic> json) {
    return Srf2060Filter(
      emissIni: json['emissIni'],
      emissFim: json['emissFim'],
      reenviar: json['reenviar'],
    );
  }
}

class Srf2060Mail {
  final int cgs18id;
  final String cgs18nome;
  final int cgs15id;
  final String cgs15nome;
  final String cgs15titulo;
  final String? cgs80email;
  final int srf01id;
  final String srf01nome;
  final int srf01integracaoScf;
  final int srf01integracaoGdf;

  Srf2060Mail({
    required this.cgs18id,
    required this.cgs18nome,
    required this.cgs15id,
    required this.cgs15nome,
    required this.cgs15titulo,
    this.cgs80email,
    required this.srf01id,
    required this.srf01nome,
    required this.srf01integracaoScf,
    required this.srf01integracaoGdf,
  });

  factory Srf2060Mail.fromJson(Map<String, dynamic> json) {
    return Srf2060Mail(
      cgs18id: json['cgs18id'],
      cgs18nome: json['cgs18nome'],
      cgs15id: json['cgs15id'],
      cgs15nome: json['cgs15nome'],
      cgs15titulo: json['cgs15titulo'],
      cgs80email: json['cgs80email'],
      srf01id: json['srf01id'],
      srf01nome: json['srf01nome'],
      srf01integracaoScf: json['srf01integracaoScf'],
      srf01integracaoGdf: json['srf01integracaoGdf'],
    );
  }
}
