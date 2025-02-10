import 'package:agilite_flutter_core/core.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';

import 'srf2030_controller.dart';

class Srf2030 extends StatefulWidget {
  const Srf2030({super.key});

  @override
  State<Srf2030> createState() => _Srf2030State();
}

class _Srf2030State extends State<Srf2030> {
  final controller = Srf2030Controller();

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
          header: const AContainerHeader.text("Importar documentos via JSON"),
          child: ASpacingColumn(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AGrid.oneRow(
                areas: '4, 8',
                children: [
                  Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      const ADivider.text(text: "Importar documentos"),
                      AText(
                        "Esse processo importa novos dpcumentos a partir de um arquivo JSon.",
                        style: moreDetailTextStyle,
                      ),
                    ],
                  ),
                  Padding(
                    padding: EdgeInsets.only(top: 24.0),
                    child: ACard(
                      child: Column(
                        children: [
                          AFileField(
                            fileType: FileType.custom,
                            allowedExtensions: ['json'],
                            controller: controller.fileController,
                            labelText: "Arquivo JSon",
                          ),
                          const SizedBox(height: 16),
                          Center(
                            child: FilledButton(
                              style: primaryButtonStyle,
                              onPressed: controller.processarJson,
                              child: const Text("Processar arquivo"),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
            ],
          ),
        );
      },
    );
  }
}
