import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ACrudHeader extends AContainerHeader {
  ACrudHeader._({
    required String text,
    required Widget? headerOptions,
  }) : super.text(
          text,
          headerOptions: headerOptions,
        );

  factory ACrudHeader.text(
    String headerText, {
    VoidCallback? onAddTap,
    List<MoreOption>? moreOptions,
  }) {
    return ACrudHeader._(
      text: headerText,
      headerOptions: _createMoreOptions(onAddTap, moreOptions),
    );
  }

  static Widget _createMoreOptions(VoidCallback? onAddTap, List<MoreOption>? moreOptions) {
    return ASpacingRow(
      children: [
        ElevatedButton.icon(
          onPressed: onAddTap,
          icon: const Icon(Icons.add),
          label: const Text("incluir"),
          style: successButtonStyle,
        ),
        if (!moreOptions.isNullOrEmpty)
          AMoreOptionsButton(
            buttonBuilder: (onTap) {
              return OutlinedButton.icon(
                icon: const Icon(Icons.more_vert),
                label: const Text("mais opções"),
                onPressed: onTap,
              );
            },
            options: [
              MoreOption.icon(
                icon: const Icon(Icons.edit),
                label: const Text("editar"),
                onTap: () {},
              ),
              MoreOption.divider(),
              MoreOption.icon(
                icon: const Icon(Icons.delete),
                label: const Text("excluir"),
                onTap: () {},
              ),
            ],
          ),
      ],
    );
  }
}
