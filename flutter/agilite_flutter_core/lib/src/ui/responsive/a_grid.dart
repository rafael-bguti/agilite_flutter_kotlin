// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'responsive_size.dart';

class AGrid extends StatelessWidget {
  final List<Widget> children;

  /// Informar as linhas separadas por \n as colunas separadas por , e os dispositivos separados por -
  /// o Dispositivo segue o padrao phone[-tablet[-desktop]]
  /// Exemplo: 6-2,6-4,6-2-1
  ///          5,*    -- * indica resto do espaco
  ///          10-6-4
  ///
  final List<String> rows;
  final bool recursiveGrid;
  final double spacing;

  final GridSizes _gridSizes;
  AGrid({
    required this.children,
    this.rows = const ['12'],
    this.recursiveGrid = true,
    this.spacing = 4,
    super.key,
  }) : _gridSizes = GridSizes(rows, recursiveGrid);

  @override
  Widget build(BuildContext context) {
    assert(children.isNotEmpty);
    final screenSize = ScreenSize(context);

    return LayoutBuilder(
      builder: (context, constraints) {
        assert(!constraints.hasInfiniteWidth, 'ResponsiveGrid does not support infinite width');
        final maxWidth = constraints.maxWidth;

        Device device = screenSize.whichDevice();
        return Wrap(
          spacing: 0,
          runSpacing: 0,
          crossAxisAlignment: WrapCrossAlignment.start,
          children: _buildCells(maxWidth, device),
        );
      },
    );
  }

  List<Widget> _buildCells(double maxWidth, Device device) {
    final widgets = <Widget>[];
    _ComputedSize computedSize = _ComputedSize(maxWidth, spacing);

    for (var i = 0; i < children.length; i++) {
      widgets.addAll(_buildCell(
        index: i,
        maxWidth: maxWidth,
        device: device,
        computedSize: computedSize,
      ));
    }

    return widgets;
  }

  List<Widget> _buildCell({
    required int index,
    required double maxWidth,
    required Device device,
    required _ComputedSize computedSize,
  }) {
    final columnSizes = _gridSizes._getSizeToColumn(index);
    final cellWidth = columnSizes.size.width(maxWidth, device);
    final padding = computedSize.getPadding(cellWidth);

    return [
      Container(
        constraints: BoxConstraints(
          minWidth: cellWidth,
          maxWidth: cellWidth,
        ),
        padding: padding,
        child: children[index],
      ),
      if (columnSizes.needCompleteRowWithEmptySpace(device))
        SizedBox(
          width: columnSizes.sizeToCompleteRow!.width(maxWidth, device),
        ),
    ];
  }
}

class GridSizes {
  final List<String> linhas;
  final bool recursiveAreas;

  final List<_ColumnSize> _sizes = [];
  final List<_ColumnSize> _lastRowSizes = [];

  int _firstComponentIndexOnLastRow = 0;

  GridSizes(this.linhas, this.recursiveAreas) {
    _processSizes();
  }

  _ColumnSize _getSizeToColumn(int index) {
    List<_ColumnSize> localSizes = _getLocalSizeToComponentIndex(index);
    return localSizes[index % localSizes.length];
  }

  List<_ColumnSize> _getLocalSizeToComponentIndex(int index) {
    bool isComponentAfterLastRow = index >= _firstComponentIndexOnLastRow;
    if (!recursiveAreas && isComponentAfterLastRow) {
      return _lastRowSizes;
    } else {
      return _sizes;
    }
  }

  void _processSizes() {
    _firstComponentIndexOnLastRow = 0;
    final rows = linhas;

    for (var rowIdx = 0; rowIdx < rows.length; rowIdx++) {
      String row = rows[rowIdx];
      List<String> columns = row.splitToList(",");

      int totalRowSizePhone = 0;
      int totalRowSizeTablet = 0;
      int totalRowSizeDesktop = 0;

      bool isLastRow = rowIdx == rows.length - 1;
      if (!isLastRow) _firstComponentIndexOnLastRow += columns.length;
      for (var colIdx = 0; colIdx < columns.length; colIdx++) {
        String query = columns[colIdx];
        final _ColumnSize columnSize;
        if (query == '*') {
          if (colIdx != columns.length - 1) throw Exception("The '*' character must be the last column of the row");
          columnSize = _ColumnSize(
            ResponsiveSize(
              phone: 12 - totalRowSizePhone <= 0 ? 12 : 12 - totalRowSizePhone,
              tablet: 12 - totalRowSizeTablet <= 0 ? 12 : 12 - totalRowSizeTablet,
              desktop: 12 - totalRowSizeDesktop <= 0 ? 12 : 12 - totalRowSizeDesktop,
            ),
            null,
          );
        } else {
          final size = ResponsiveSize.byAreas(query);

          totalRowSizePhone += size.phone;
          totalRowSizeTablet += size.tablet;
          totalRowSizeDesktop += size.desktop;
          ResponsiveSize? emptySizeToCompleteRow;
          if (colIdx == columns.length - 1) {
            final emptySizeToCompleteRowOnPhone = totalRowSizePhone % 12 == 0 ? 0 : 12;
            final emptySizeToCompleteRowOnTablet = totalRowSizeTablet % 12 == 0 ? 0 : 12;
            final emptySizeToCompleteRowOnDesktop = totalRowSizeDesktop % 12 == 0 ? 0 : 12;

            if ((emptySizeToCompleteRowOnPhone + emptySizeToCompleteRowOnTablet + emptySizeToCompleteRowOnDesktop) > 0) {
              emptySizeToCompleteRow = ResponsiveSize(
                phone: emptySizeToCompleteRowOnPhone,
                tablet: emptySizeToCompleteRowOnTablet,
                desktop: emptySizeToCompleteRowOnDesktop,
              );
            }
          }
          columnSize = _ColumnSize(size, emptySizeToCompleteRow);
        }

        _sizes.add(columnSize);

        if (isLastRow) _lastRowSizes.add(columnSize);
      }
    }
  }
}

class _ColumnSize {
  final ResponsiveSize size;
  final ResponsiveSize? sizeToCompleteRow;

  _ColumnSize(
    this.size,
    this.sizeToCompleteRow,
  );

  bool needCompleteRowWithEmptySpace(Device device) {
    return sizeToCompleteRow != null && sizeToCompleteRow!.columnSize(device) > 0;
  }

  @override
  String toString() => '_ColumnSize(size: $size, sizeToCompleteRow: $sizeToCompleteRow)';
}

class _ComputedSize {
  final double maxWidth;
  final double spacing;
  double width = 0;
  int row = 0;

  _ComputedSize(this.maxWidth, this.spacing);

  EdgeInsets getPadding(double cellWidth) {
    if (spacing == 0) return EdgeInsets.zero;

    _addWidth(cellWidth);
    bool needRightSpacing = _needRightSpacing();
    bool needLeftSpacing = _needLeftSpacing(cellWidth);
    bool needTopSpacing = this.needTopSpacing();

    return EdgeInsets.only(
      right: needRightSpacing ? spacing : 0,
      left: needLeftSpacing ? spacing : 0,
      top: needTopSpacing ? spacing * 2 : 0,
    );
  }

  void _addWidth(double cellWidth) {
    if (width + cellWidth > maxWidth) {
      width = 0;
      row++;
    }
    width += cellWidth;
  }

  bool _needRightSpacing() {
    return width + spacing < maxWidth;
  }

  bool _needLeftSpacing(double cellWidth) {
    return width - cellWidth > 0;
  }

  bool needTopSpacing() {
    return row > 0;
  }
}
