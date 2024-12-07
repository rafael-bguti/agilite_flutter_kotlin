// ignore_for_file: public_member_api_docs, sort_constructors_first

class ClientWhere {
  String where;
  Map<String, dynamic>? params;

  ClientWhere(
    this.where, {
    this.params,
  });

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'filter': where,
      'params': params,
    };
  }
}
