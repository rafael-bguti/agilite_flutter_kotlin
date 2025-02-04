class CrudEditResponse {
  final Map<String, dynamic> data;
  final bool editable;

  CrudEditResponse({
    required this.data,
    required this.editable,
  });

  factory CrudEditResponse.fromJson(Map<String, dynamic> json) {
    return CrudEditResponse(
      data: json['data'] as Map<String, dynamic>,
      editable: json['editable'] as bool,
    );
  }
}
