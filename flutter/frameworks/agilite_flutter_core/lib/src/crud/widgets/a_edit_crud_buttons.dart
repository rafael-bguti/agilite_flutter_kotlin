import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AEditCrudButtons extends StatelessWidget {
  final void Function() onSave;
  final void Function()? onCancel;
  const AEditCrudButtons({required this.onSave, this.onCancel, super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingRow(
      spacing: 16,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        OutlinedButton(
          style: buildOutlinedButtonStyle(onBackgroundColor),
          onPressed: () => _onCancel(context),
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              'Cancelar',
              style: textTheme?.titleMedium?.copyWith(
                color: onBackgroundColor,
              ),
            ),
          ),
        ),
        FilledButton.icon(
          style: successButtonStyle,
          onPressed: onSave,
          label: Padding(
            padding: const EdgeInsets.fromLTRB(0, 8, 8, 8),
            child: Text(
              "Salvar",
              style: textTheme?.titleMedium?.copyWith(
                color: onSuccessColor,
              ),
            ),
          ),
          icon: Padding(
            padding: const EdgeInsets.fromLTRB(8, 0, 0, 0),
            child: Icon(Icons.save, color: onSuccessColor),
          ),
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
