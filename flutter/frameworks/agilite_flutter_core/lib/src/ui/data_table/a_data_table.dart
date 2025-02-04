import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _MAX_WIDTH = 220.0;

class ADataTable extends StatefulWidget {
  final bool showVerticalScroll;
  final bool showHorizontalScroll;

  final List<ADataTableColumn> columns;
  final List<Map<String, dynamic>> rows;

  final void Function(Map<String, dynamic> row)? onRowTap;
  final void Function(List<int> selectedRows)? onSelectedRowsChanged;

  final List<int>? selectedRows;

  const ADataTable({
    required this.columns,
    required this.rows,
    this.showVerticalScroll = true,
    this.showHorizontalScroll = true,
    this.onRowTap,
    this.onSelectedRowsChanged,
    this.selectedRows,
    super.key,
  });

  @override
  State<ADataTable> createState() => _ADataTableState();
}

class _ADataTableState extends State<ADataTable> {
  late final List<int> _selectedRows = widget.selectedRows ?? [];

  @override
  void dispose() {
    _selectedRows.clear();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var result = _table();
    if (widget.showHorizontalScroll) {
      result = SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        child: result,
      );
    }
    if (widget.showVerticalScroll) {
      result = SingleChildScrollView(
        scrollDirection: Axis.vertical,
        child: result,
      );
    }
    return result;
  }

  Widget _table() {
    return DataTable(
      columns: widget.columns.map((c) => c.toDataColumn()).toList(),
      rows: widget.rows
          .mapIndexed(
            (index, row) => DataRow(
              cells: _cells(row),
              selected: _selectedRows.contains(index),
              onSelectChanged: widget.onSelectedRowsChanged == null
                  ? null
                  : (sel) {
                      _onSelectRow(index, sel);
                    },
            ),
          )
          .toList(),
    );
  }

  List<DataCell> _cells(Map<String, dynamic> row) {
    return widget.columns.map(
      (c) {
        final String? text = c.formatter?.call(row, c.name) ?? row[c.name]?.toString();
        return DataCell(
          Container(
            constraints: BoxConstraints(maxWidth: c.maxWidth ?? _MAX_WIDTH),
            child: Text(text ?? ""),
          ),
          onTap: widget.onRowTap == null
              ? null
              : () {
                  widget.onRowTap?.call(row);
                },
        );
      },
    ).toList();
  }

  void _onSelectRow(int index, bool? selected) {
    bool isSelected = selected ?? false;
    if (isSelected) {
      _selectedRows.add(index);
    } else {
      _selectedRows.remove(index);
    }
    widget.onSelectedRowsChanged?.call(_selectedRows);

    setState(() {});
  }
}

class ADataTableColumn {
  final String name;
  final String label;
  final bool numeric;
  final double? maxWidth;

  final ColumnFormatter? formatter;

  const ADataTableColumn(
    this.name,
    this.label, {
    this.maxWidth,
    this.numeric = false,
    this.formatter,
  });

  DataColumn toDataColumn() {
    return DataColumn(label: Text(label), numeric: numeric);
  }
}
