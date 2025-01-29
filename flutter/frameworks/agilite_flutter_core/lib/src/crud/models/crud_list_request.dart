class CrudListRequest {
  final int currentPage;
  final int pageSize;
  final String? search;
  final Map<String, dynamic>? customFilters;
  final int? groupIndex;

  CrudListRequest({
    required this.currentPage,
    required this.pageSize,
    this.search,
    this.customFilters,
    this.groupIndex,
  });

  Map<String, dynamic> toJson() {
    return {
      'currentPage': currentPage,
      'pageSize': pageSize,
      'search': search,
      'customFilters': customFilters,
      'groupIndex': groupIndex,
    };
  }
}
