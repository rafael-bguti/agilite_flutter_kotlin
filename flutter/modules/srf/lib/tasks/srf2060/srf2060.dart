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
        return SingleChildScrollView(
          child: AForm(
            controller.formController,
            child: AContainer(
              header: const AContainerHeader.text("Enviar e-mail dos documentos"),
              child: ASpacingColumn(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  ACard(
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
                  ACard(
                    header: const Text("E-mails localizados para enviar"),
                    child: SizedBox(
                      width: double.infinity,
                      height: 450,
                      child: ASpread.columns(
                        spreadEmailsControllerName,
                        [
                          AColumnString("cgs18nome", "Natureza").widthChar(8),
                          AColumnString("cgs15nome", "Modelo").widthChar(20),
                          AColumnString("cgs80email", "Destinatário").widthChar(40),
                          AColumnString("srf01nome", "Nome").widthChar(50),
                          AColumnDate("srf01dtEmail", "Data envio").widthChar(12),
                          AColumnString(
                            "srf01integracaoGdf",
                            "Possui nota",
                            alignment: Alignment.center,
                          ).widthChar(11),
                          AColumnString(
                            "srf01integracaoScf",
                            "Possui boleto",
                            alignment: Alignment.center,
                          ).widthChar(13),
                        ],
                        selectColumnName: "enviar",
                        selectPanelWidget: FilledButton.tonal(
                          style: successButtonStyle,
                          onPressed: () {
                            controller.enviarEmails();
                          },
                          child: const Text("Enviar e-mails"),
                        ),
                        readOnly: true,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}
