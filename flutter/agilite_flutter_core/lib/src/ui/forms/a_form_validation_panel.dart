import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AFormValidationPanel extends StatefulWidget {
  const AFormValidationPanel({
    Key? key,
  }) : super(key: key);

  @override
  State<AFormValidationPanel> createState() => _AFormValidationPanelState();
}

class _AFormValidationPanelState extends State<AFormValidationPanel> {
  late AFormState? formState;
  final GlobalKey _key = GlobalKey();
  @override
  void initState() {
    formState = context.findAncestorStateOfType<AFormState>();
    if (formState == null) {
      throw 'AFormValidationPanel precisa estar dentro de um AForm';
    }
    runOnNextBuild(() => formState!.registerPanelValidationGlobalKey(_key));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    if (formState == null) {
      return const SizedBox.shrink();
    }

    return Container(
      key: _key,
      child: AConsumer(formState!.validationMessages$, builder: (_, validations$) {
        final msgs = validations$.value;
        if (msgs.isEmpty) {
          return const SizedBox.shrink();
        }

        return Container(
          color: errorContainerColor,
          padding: const EdgeInsets.all(8),
          constraints: const BoxConstraints(maxHeight: 145),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("Corrija as validações abaixo antes de continuar:", style: TextStyle(color: errorColor, fontWeight: FontWeight.bold)),
              Divider(color: errorColor),
              Expanded(
                child: ListView.builder(
                  itemBuilder: (context, index) => Text(' - ${msgs[index]}', style: TextStyle(color: errorColor)),
                  itemCount: msgs.length,
                ),
              ),
            ],
          ),
        );
      }),
    );
  }
}
