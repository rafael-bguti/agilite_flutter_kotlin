import 'package:json_annotation/json_annotation.dart';

part 'a_width.g.dart';

enum AWidthType {
  fixed,
  flex,
  byCharCount,
}

@JsonSerializable()
class AWidth {
  static const _characterWidth = 8.0;

  final AWidthType type;
  final double _width;

  bool get hasFixedWidth => type == AWidthType.fixed || type == AWidthType.byCharCount;
  double get scrollWidth => hasFixedWidth ? _width : 0;

  AWidth({
    required this.type,
    required double width,
  }) : _width = _calculateWidth(type, width);

  const AWidth.fixed(double width)
      : type = AWidthType.fixed,
        _width = width;

  const AWidth.byCharCount(int charCount)
      : type = AWidthType.byCharCount,
        _width = (charCount * _characterWidth) + 16;

  const AWidth.flex(int flex)
      : type = AWidthType.flex,
        _width = flex * 1.0;

  double get width => _width;

  static double _calculateWidth(AWidthType type, double width) {
    if (type == AWidthType.byCharCount) {
      return (width * _characterWidth) + 16;
    }

    return width;
  }

  factory AWidth.fromJson(Map<String, dynamic> json) => _$AWidthFromJson(json);
}
