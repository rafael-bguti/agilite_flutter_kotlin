import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'srf2050_controller.dart';

class Srf2050 extends StatefulWidget {
  const Srf2050({super.key});

  @override
  State<Srf2050> createState() => _Srf2050State();
}

class _Srf2050State extends State<Srf2050> {
  final controller = Srf2050Controller();

  @override
  void dispose() {
    super.dispose();
    controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AView(
      controller: controller,
      builder: (_, state) {
        return SingleChildScrollView(
          child: AContainer(
            header: const AContainerHeader.text("Emitir NFS-e"),
            child: ASpacingColumn(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                ACard(
                  header: const Text("Documentos a serem emitidos"),
                  child: SizedBox(
                    width: double.infinity,
                    height: 400,
                    child: ASpread(
                      controller: controller.spreadEmitirController,
                      columns: [
                        AColumnString("srf01numero", "Número").widthFlex(2),
                        AColumnDate("srf01dtEmiss", "Data de emissão").widthFlex(3),
                        AColumnString("srf01nome", "Cliente").widthFlex(8),
                        AColumnDouble("srf01vlrTotal", "Valor total").widthFlex(3),
                      ],
                      readOnly: true,
                    ),
                  ),
                ),
                ACard(
                  child: Center(
                    child: FilledButton(
                      style: primaryButtonStyle,
                      onPressed: controller.emitirDocumento,
                      child: const Text("Emitir NFSes"),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
              ],
            ),
          ),
        );
      },
    );
  }
}
