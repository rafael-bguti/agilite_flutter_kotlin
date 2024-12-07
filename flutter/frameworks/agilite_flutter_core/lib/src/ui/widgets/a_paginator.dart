import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class APaginator extends StatelessWidget {
  final int pageSize;
  final int currentPage;
  final int totalRecords;
  final void Function(int pageSize) onPageSizeChange;
  final void Function(int delta) onPageChange;
  const APaginator({
    required this.pageSize,
    required this.currentPage,
    required this.totalRecords,
    required this.onPageSizeChange,
    required this.onPageChange,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return ASpacingRow(
      children: [
        const Text("Exibir"),
        DropdownButton<int>(
          value: pageSize,
          items: const [
            DropdownMenuItem(value: 10, child: Text("   10 registros  ")),
            DropdownMenuItem(value: 20, child: Text("   20 registros  ")),
            DropdownMenuItem(value: 50, child: Text("   50 registros  ")),
            DropdownMenuItem(value: 100, child: Text(" 100 registros  ")),
          ],
          style: TextStyle(color: onBackgroundColor),
          onChanged: (value) => onPageSizeChange(value ?? 10),
        ),
        Text("${(pageSize * currentPage) + 1}-$endPage de $totalRecords"),
        IconButton(
          icon: const Icon(Icons.chevron_left),
          onPressed: currentPage == 0 ? null : () => onPageChange(-1),
        ),
        IconButton(
          icon: const Icon(Icons.chevron_right),
          onPressed: endPage == totalRecords ? null : () => onPageChange(1),
        ),
      ],
    );
  }

  int get endPage => min(pageSize * (currentPage + 1), totalRecords);
}
