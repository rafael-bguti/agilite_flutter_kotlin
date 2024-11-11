class AWidth {
  static const _characterWidth = 8.0;

  final bool hasFixedWidth;
  final double width;

  double get scrollWidth => hasFixedWidth ? width : 0;

  const AWidth.fixed(this.width) : hasFixedWidth = true;
  const AWidth.byCharCount(int charCount)
      : hasFixedWidth = true,
        width = (charCount * _characterWidth) + 16;
  const AWidth.flex(int flex)
      : hasFixedWidth = false,
        width = flex * 1.0;
}
