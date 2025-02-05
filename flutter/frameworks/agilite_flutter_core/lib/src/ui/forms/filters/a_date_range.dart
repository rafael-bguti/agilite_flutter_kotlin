import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ADateRange extends StatefulWidget with FieldControllerCreatorMixin {
  final String? label;

  final String nameIni;
  final String nameEnd;
  final bool fluid;

  final void Function(DateTime initialDate, DateTime finalDate)? onChanged;

  const ADateRange({
    required this.nameIni,
    required this.nameEnd,
    this.fluid = false,
    this.label,
    this.onChanged,
    super.key,
  });

  @override
  State<ADateRange> createState() => _ADateRangeState();
}

class _ADateRangeState extends State<ADateRange> {
  AFormState? formState;

  @override
  void initState() {
    super.initState();
    formState = context.findAncestorStateOfType<AFormState>();
  }

  final values = const [
    RangeValue(1, "Hoje"),
    RangeValue(2, "Ontem"),
    RangeValue(3, "Últimos 7 dias"),
    RangeValue(4, "Últimos 30 dias"),
    RangeValue(5, "Este mês"),
    RangeValue(6, "Mês passado"),
    RangeValue(7, "Este ano"),
    RangeValue(8, "Ano passado"),
  ];

  @override
  Widget build(BuildContext context) {
    BoxConstraints constraint = BoxConstraints(maxWidth: widget.fluid ? double.infinity : 370);
    final fields = [
      Expanded(child: ATextField.date(widget.nameIni)),
      const Text("até"),
      Expanded(child: ATextField.date(widget.nameEnd)),
    ];

    return Container(
      constraints: constraint,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (widget.label != null) Text(widget.label!, style: textTheme?.labelLarge),
          const SizedBox(height: 4),
          DropdownButtonFormField<RangeValue>(
            hint: const Text("Selecione um intervalo"),
            items: values
                .map((e) => DropdownMenuItem<RangeValue>(
                      value: e,
                      child: Text(e.label),
                    ))
                .toList(),
            onChanged: _onChanged,
          ),
          const SizedBox(height: 8),
          ASpacingRow(
            children: fields,
          ),
        ],
      ),
    );
  }

  void _onChanged(RangeValue? value) {
    if (value == null) return;

    final now = DateTime.now();
    var initialDate = DateTime(now.year, now.month, now.day);
    var finalDate = DateTime(now.year, now.month, now.day);

    switch (value.value) {
      case 1:
        break;
      case 2:
        initialDate = initialDate.subtract(const Duration(days: 1));
        finalDate = finalDate.subtract(const Duration(days: 1));
        break;
      case 3:
        initialDate = initialDate.subtract(const Duration(days: 7));
        break;
      case 4:
        initialDate = initialDate.subtract(const Duration(days: 30));
        break;
      case 5:
        initialDate = DateTime(now.year, now.month, 1);
        break;
      case 6:
        initialDate = DateTime(now.year, now.month - 1, 1);
        finalDate = DateTime(now.year, now.month, 0);
        break;
      case 7:
        initialDate = DateTime(now.year, 1, 1);
        break;
      case 8:
        initialDate = DateTime(now.year - 1, 1, 1);
        finalDate = DateTime(now.year - 1, 12, 31);

        break;
    }

    if (formState != null) {
      (formState?.controller?.getController(widget.nameIni) as DateController?)?.value = initialDate;
      (formState?.controller?.getController(widget.nameEnd) as DateController?)?.value = finalDate;
    }
    widget.onChanged?.call(initialDate, finalDate);
  }
}

class RangeValue {
  final int value;
  final String label;

  const RangeValue(this.value, this.label);
}
