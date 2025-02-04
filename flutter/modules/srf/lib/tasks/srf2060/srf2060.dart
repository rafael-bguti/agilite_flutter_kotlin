import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'srf2060_controller.dart';

class Srf2060 extends StatefulWidget {
  const Srf2060({super.key});

  @override
  State<Srf2060> createState() => _Srf2060State();
}

class _Srf2060State extends State<Srf2060> {
  final controller = Srf2060Controller();

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
          header: const AContainerHeader.text("Enviar e-mail dos documentos"),
          child: ASpacingColumn(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AForm(
                controller.formFilterController,
                child: ACard(
                  child: AGrid.rows(
                    crossAxisAlignment: WrapCrossAlignment.end,
                    rows: [
                      AGridRow(
                        areas: '6-12, 4-6, 2-6',
                        children: [
                          ASpacingColumn(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Emissão dos documentos",
                                style: textTheme?.bodyLarge?.copyWith(fontFamily: 'HeaderFont'),
                              ),
                              const ADateRange(
                                nameIni: "emissIni",
                                nameEnd: "emissFim",
                              ),
                            ],
                          ),
                          const ABoolField(
                            "reenviar",
                            labelText: "Listar e-mails já enviados",
                          ),
                          ElevatedButton(
                            onPressed: controller.buscarEmails,
                            child: const Text("Listar"),
                          )
                        ],
                      ),
                    ],
                  ),
                ),
              ),
              switch (state) {
                Srf2060InitialState() => const SizedBox.shrink(),
                Srf2060EmptyState() => const AAlert.warning(message: "Nenhum e-mail foi localizado para enviar"),
                Srf2060FullState() => ACard(
                    header: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text("E-mails localizados"),
                        FilledButton.tonal(
                          style: successButtonStyle,
                          onPressed: () {
                            controller.enviarEmails();
                          },
                          child: const Text("Enviar"),
                        ),
                      ],
                    ),
                    child: SizedBox(
                      width: double.infinity,
                      height: 450,
                      child: ASpread(
                        controller: controller.spreadEnvioController,
                        columns: [
                          AColumnString("cgs18nome", "Natureza").widthChar(8),
                          AColumnString("cgs15nome", "Modelo").widthChar(20),
                          AColumnString("cgs80email", "Destinatário").widthChar(40),
                          AColumnString("srf01nome", "Nome").widthChar(50),
                          AColumnDate("srf01dtEmail", "Data envio").widthChar(12),
                          AColumnString(
                            "srf01integracaoGdf",
                            "Possui nota",
                            alignment: Alignment.center,
                            cellFormatter: _integracaoFormatter,
                          ).widthChar(11),
                          AColumnString(
                            "srf01integracaoScf",
                            "Possui boleto",
                            alignment: Alignment.center,
                            cellFormatter: _integracaoFormatter,
                          ).widthChar(13),
                        ],
                        readOnly: true,
                      ),
                    ),
                  ),
              },
              const SizedBox(height: 16),
            ],
          ),
        );
      },
    );
  }

  final CellFormatter _integracaoFormatter = (spreadController, row, name) => spreadController.value[row].getString(name) == "10" ? "Sim" : "Não";
}
