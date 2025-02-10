//Renderizadores de colunas da spread
import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/ui/widgets/tags/a_tag.dart';
import 'package:flutter/material.dart';

class StatusPagamentoCustomCellRenderer {
  final String colunaVcto;
  final String colunaPgto;

  const StatusPagamentoCustomCellRenderer(this.colunaVcto, this.colunaPgto);
  CellRenderer cellRender() {
    return (context, spreadController, row, columnName, isSelected) {
      final vcto = spreadController.value[row].getDateTime(colunaVcto);
      final pgto = spreadController.value[row].getDateTime(colunaPgto);
      final Widget chip;
      if (pgto != null) {
        chip = ATag(text: 'Pago', color: successColor);
      } else {
        if (vcto == null) return const SizedBox.shrink();
        final hoje = DateTime.now();
        if (vcto.isBefore(hoje)) {
          return ATag(text: 'Vencido', color: errorColor);
        } else {
          return ATag(text: 'A vencer', color: warningColor);
        }
      }
      return chip;
    };
  }
}
