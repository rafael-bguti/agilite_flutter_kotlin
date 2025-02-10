class CrudEditResponse {
  final Map<String, dynamic> data;
  final Map<String, dynamic>? sduiForm;
  final bool editable;

  CrudEditResponse({
    required this.data,
    required this.editable,
    this.sduiForm,
  });

  factory CrudEditResponse.fromJson(Map<String, dynamic> json) {
    return CrudEditResponse(
      data: json['data'] as Map<String, dynamic>,
      editable: json['editable'] as bool,
      sduiForm: json['sduiForm'] as Map<String, dynamic>?,
    );
  }
}
