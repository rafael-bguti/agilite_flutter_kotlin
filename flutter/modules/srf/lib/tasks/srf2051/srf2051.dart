import 'package:agilite_flutter_core/core.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';

import 'srf2051_controller.dart';

class Srf2051 extends StatefulWidget {
  const Srf2051({super.key});

  @override
  State<Srf2051> createState() => _Srf2051State();
}

class _Srf2051State extends State<Srf2051> {
  final controller = Srf2051Controller();

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
        return ATaskContainer(
          header: const AContainerHeader.text("Processar retorno NFe"),
          child: ASpacingColumn(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ACard(
                child: ASpacingColumn(
                  children: [
                    AFileField(
                      fileType: FileType.custom,
                      allowedExtensions: ['xml'],
                      controller: controller.fileController,
                      labelText: "Arquivo XML de retorno da prefeitura",
                    ),
                    Center(
                      child: FilledButton(
                        style: primaryButtonStyle,
                        onPressed: controller.processarRetorno,
                        child: const Text("Processar Retorno"),
                      ),
                    ),
                    Text("Quantidade de documentos para processar: ${state.qtdDocsPraProcessar}"),
                  ],
                ),
              ),
              ACard(
                header: const Text("Documentos processados"),
                child: SizedBox(
                  width: double.infinity,
                  height: 400,
                  child: ASpread(
                    name: "docsProcessados",
                    columns: [
                      AColumnString("srf01numero", "Número").widthFlex(2),
                      AColumnDate("srf01dtEmiss", "Data de emissão").widthFlex(3),
                      AColumnString("srf01nome", "Cliente").widthFlex(8),
                      AColumnDouble("srf01vlrTotal", "Valor total").widthFlex(3),
                    ],
                    value: state.docsProcessados,
                    readOnly: true,
                  ),
                ),
              ),
              const SizedBox(height: 16),
            ],
          ),
        );
      },
    );
  }
}
