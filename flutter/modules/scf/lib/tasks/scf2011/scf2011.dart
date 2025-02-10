import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:scf/tasks/scf2011/scf2011_controller.dart';

class Scf2011 extends StatefulWidget {
  const Scf2011({super.key});

  @override
  State<Scf2011> createState() => _Scf2011State();
}

class _Scf2011State extends State<Scf2011> {
  final controller = Scf2011Controller();

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
          header: const AContainerHeader.text("Baixar boletos"),
          child: ASpacingColumn(
            children: [
              AGrid.oneRow(
                areas: '4, 8',
                children: [
                  Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      const ADivider.text(text: "Baixa de boletos"),
                      AText(
                        "Esse processo irá baixar os boletos que foram pagos no banco.",
                        style: moreDetailTextStyle,
                      ),
                    ],
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 24.0),
                    child: AForm(
                      controller.formController,
                      child: ASpacingColumn(
                        spacing: 16.0,
                        children: [
                          const AAutocompleteField.api(
                            'cgs38RecBoletoComApi',
                            labelText: 'Bancos com integração bancária configurada',
                            req: true,
                          ),
                          Center(
                            child: FilledButton(
                              style: primaryButtonStyle,
                              onPressed: controller.processarBoletos,
                              child: const Text("Baixar boletos"),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
              if (state is Scf2011CompleteState) const ADivider.text(text: "Resultado do processamento"),
              if (state is Scf2011CompleteState) _ResultadoDoProcessamento(boletosProcessados: state.boletosProcessados),
            ],
          ),
        );
      },
    );
  }
}

class _ResultadoDoProcessamento extends StatelessWidget {
  List<BoletoProcessado> boletosProcessados;
  _ResultadoDoProcessamento({required this.boletosProcessados, super.key});

  @override
  Widget build(BuildContext context) {
    if (boletosProcessados.isEmpty) {
      return Center(
        child: AText(
          "Nenhum novo pagamento foi localizado.",
          style: textTheme?.headlineSmall?.copyWith(fontFamily: 'HeaderFont'),
        ),
      );
    }
    return ListView.builder(
      shrinkWrap: true,
      itemCount: boletosProcessados.length,
      itemBuilder: (context, index) {
        final boleto = boletosProcessados[index];
        return Padding(
          padding: const EdgeInsets.only(top: 8.0),
          child: _BoletoCard(
            boleto: boleto,
          ),
        );
      },
    );
  }
}

class _BoletoCard extends StatelessWidget {
  final BoletoProcessado boleto;
  const _BoletoCard({required this.boleto, super.key});

  @override
  Widget build(BuildContext context) {
    return ACard(
      leftBorderColor: boleto.baixadoComSucesso ? Colors.blueAccent : Colors.orangeAccent,
      child: Row(
        children: [
          Expanded(
            child: AGrid(
              [
                AGridRow(
                  areas: '2-4, 4-10, 4-12',
                  children: [
                    ASpacingColumn(
                      mainAxisSize: MainAxisSize.min,
                      spacing: 8,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        _cell("Número", boleto.numero ?? ''),
                        _cell("Vencimento", boleto.dataVencimento?.format() ?? ''),
                      ],
                    ),
                    ASpacingColumn(
                      mainAxisSize: MainAxisSize.min,
                      spacing: 8,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        _cell("Pagamento", boleto.recebimento.dataSituacao.format() ?? ''),
                        _cell("Cliente", boleto.recebimento.pagador?.nome ?? ''),
                      ],
                    ),
                    _cell("Situação", boleto.status),
                  ],
                )
              ],
            ),
          ),
          Text(
            boleto.recebimento?.valorTotalRecebido?.format() ?? '',
            style: textTheme?.titleLarge,
            textAlign: TextAlign.right,
          ),
        ],
      ),
    );
  }

  Widget _cell(String title, String value) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        AText(
          title,
          style: textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.normal, fontSize: (textTheme?.bodyMedium?.fontSize ?? 13) - 3),
        ),
        AText(
          value,
          style: textTheme?.bodyMedium?.copyWith(fontWeight: FontWeight.bold),
        ),
      ],
    );
  }
}
