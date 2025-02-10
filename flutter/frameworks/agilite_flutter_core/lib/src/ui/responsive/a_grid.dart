// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const double _defaultSpacing = 16;
const WrapCrossAlignment _defaultCrossAxisAlignment = WrapCrossAlignment.start;

class AGrid extends StatelessWidget {
  final List<AGridRow> rows;

  final double? spacing;
  final WrapCrossAlignment? crossAxisAlignment;

  // final GridSizes _gridSizes;
  AGrid.oneRow({
    required String areas,
    required List<Widget> children,
    this.spacing,
    this.crossAxisAlignment,
    super.key,
  }) : rows = [AGridRow(areas: areas, children: children)];

  const AGrid(
    this.rows, {
    this.spacing,
    this.crossAxisAlignment,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        assert(!constraints.hasInfiniteWidth, 'ResponsiveGrid does not support infinite width');

        final device = ScreenSize(context).whichDevice();
        final maxWidth = constraints.maxWidth;

        return ASpacingColumn(
          crossAxisAlignment: CrossAxisAlignment.start,
          spacing: spacing ?? _defaultSpacing,
          children: rows.map((e) => _buildRow(e, maxWidth, device)).toList(),
        );
      },
    );
  }

  Widget _buildRow(AGridRow row, double maxWidth, Device device) {
    return Wrap(
      spacing: 0,
      runSpacing: spacing ?? _defaultSpacing,
      crossAxisAlignment: crossAxisAlignment ?? _defaultCrossAxisAlignment,
      children: row._buildCells(device, maxWidth, spacing ?? _defaultSpacing),
    );
  }
}

/// Areas:
/// Informar o tamanho por dispositivo separado por - e os dispositivos separados por ,
/// o Dispositivo segue o padrao desktop[-tablet[-phone]], o valor default para todos é 12
/// Exemplo: 2-6, 4-6, 1-2-6 -> nesse caso primeiro componente: 2 desktop, 6 tablet, 12 phone. segundo componente: 4 desktop, 6 tablet, 12 phone. terceiro componente: 1 desktop, 2 tablet, 6 phone
///          5, *    -- * indica resto do espaco
///          10-6-4 -> nesse caso primeiro componente: 10 desktop, 6 tablet, 4 phone
///
class AGridRow {
  final String areas;
  final List<Widget> children;
  final List<List<String>> _splitedAreas;

  AGridRow({
    required this.areas,
    required this.children,
  }) : _splitedAreas = _splitAreas(areas);

  List<Widget> _buildCells(Device device, double maxWidth, double spacing) {
    final widgets = <Widget>[];
    int totalArea = 0;
    for (int i = 0; i < children.length; i++) {
      double leftSpacing = totalArea % 12 == 0 ? 0 : spacing / 2;
      int area = _area(i, device);
      totalArea += area;
      double rightSpacing = totalArea % 12 == 0 ? 0 : spacing / 2;

      final cellWidth = area / 12 * maxWidth;
      widgets.add(
        Container(
          constraints: BoxConstraints(
            minWidth: cellWidth,
            maxWidth: cellWidth,
          ),
          child: Padding(
            padding: EdgeInsets.only(left: leftSpacing, right: rightSpacing),
            child: children[i],
          ),
        ),
      );
    }

    return widgets;
  }

  int _area(int index, Device device) {
    int areaIndex = index % _splitedAreas.length;
    int deviceIndex = device == Device.phone
        ? 2
        : device == Device.tablet
            ? 1
            : 0;

    return int.parse(_splitedAreas.getOrNull(areaIndex)?.getOrNull(deviceIndex) ?? '12');
  }

  static List<List<String>> _splitAreas(String areas) {
    return areas.split(",").map((e) => e.split("-")).toList();
  }
}
