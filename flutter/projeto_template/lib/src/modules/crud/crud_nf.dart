import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudNf extends StatefulWidget {
  const CrudNf({super.key});

  @override
  State<CrudNf> createState() => _CrudNfState();
}

class _CrudNfState extends State<CrudNf> {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        headerLabel: "Notas Fiscais",
        headerOptions: ElevatedButton.icon(
          onPressed: () {},
          icon: const Icon(Icons.add),
          label: const Text("Nova Nota Fiscal"),
          style: successButtonStyle,
        ),
        child: ASpacingColumn(
          children: [
            PainelDeFiltros(),
            const Placeholder(),
          ],
        ),
      ),
    );
  }
}

class PainelDeFiltros extends StatelessWidget {
  const PainelDeFiltros({super.key});

  @override
  Widget build(BuildContext context) {
    return AGrid.rows([
      AGridRow("6 ", [
        ASearchField(
          onSearchChanged: (value) {
            print('Buscar por $value');
          },
        )
      ]),
    ]);
  }
}
