import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class EditCrudButtons extends StatelessWidget {
  final void Function() onSave;
  final void Function()? onCancel;
  const EditCrudButtons({required this.onSave, this.onCancel, super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingRow(
      spacing: 16,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        FilledButton.icon(
          style: buildButtonStyle(onSuccessColor, Colors.orange),
          onPressed: () => _onCancel(context),
          label: Text(
            'Cancelar',
            style: textTheme?.titleMedium?.copyWith(
              color: Colors.orange,
            ),
          ),
          icon: const Icon(Icons.arrow_back, color: Colors.orange),
        ),
        FilledButton.icon(
          style: successButtonStyle,
          onPressed: onSave,
          label: Text(
            "Salvar",
            style: textTheme?.titleMedium?.copyWith(
              color: onSuccessColor,
            ),
          ),
          icon: Icon(Icons.save, color: onSuccessColor),
        ),
      ],
    );
  }

  void _onCancel(BuildContext context) {
    if (onCancel == null) {
      Navigator.of(context).pop();
    } else {
      onCancel!.call();
    }
  }
}
