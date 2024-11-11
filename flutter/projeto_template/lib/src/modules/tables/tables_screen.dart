import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:projeto_estudo/src/modules/tables/simple_flutter_table.dart';

class TablesScreen extends StatelessWidget {
  const TablesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        taskHeader: "Tables / Spread",
        child: ASpacingColumn(
          spacing: 32,
          children: const [
            ASeparator(label: "Spread não editável"),
            SimpleFlutterTable(),
            ASeparator(label: "Simple Flutter Table"),
            SimpleFlutterTable(),
          ],
        ),
      ),
    );
  }
}
