import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:scf/tasks/scf2010/scf2010_controller.dart';

class Scf2010 extends StatefulWidget {
  const Scf2010({super.key});

  @override
  State<Scf2010> createState() => _Scf2010State();
}

class _Scf2010State extends State<Scf2010> {
  final controller = Scf2010Controller();

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AView(
      controller: controller,
      builder: (_, state) {
        return ATaskContainer(
          header: const AContainerHeader.text("Enviar boletos para o banco"),
          child: ASpacingColumn(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              ACard(
                header: const Text("Boletos a serem enviados"),
                child: SizedBox(
                  width: double.infinity,
                  height: 400,
                  child: ASpread(
                    controller: controller.spreadEmitirController,
                    columns: [
                      AColumnDate("scf02dtVenc", "Data de vencimento").widthFlex(4),
                      AColumnString("cgs80nome", "Cliente").widthFlex(8),
                      AColumnString("cgs45nome", "Banco").widthFlex(4),
                      AColumnDouble("scf02valor", "Valor").widthFlex(3),
                    ],
                    readOnly: true,
                  ),
                ),
              ),
              ACard(
                child: AConsumer(
                  notifier: controller.spreadEmitirController,
                  builder: (_, spreadController, __) {
                    final linhasSelecionadas = spreadController.selectedRowCount;
                    return Center(
                      child: FilledButton(
                        style: primaryButtonStyle,
                        onPressed: linhasSelecionadas == 0 ? null : controller.emitirDocumento,
                        child: Text("Enviar $linhasSelecionadas boleto${linhasSelecionadas > 1 ? 's' : ''}"),
                      ),
                    );
                  },
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
