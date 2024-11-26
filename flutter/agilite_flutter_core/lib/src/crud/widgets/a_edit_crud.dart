import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AEditCrud extends StatefulWidget {
  final CrudDescr descr;
  final int? id;

  const AEditCrud({
    required this.descr,
    this.id,
    super.key,
  });

  @override
  State<AEditCrud> createState() => _AEditCrudState();
}

class _AEditCrudState extends State<AEditCrud> {
  late final CrudEditController controller = CrudEditController(id: widget.id);

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: AView(
          controller: controller,
          builder: (context, state) {
            return Column(
              children: [
                Expanded(
                  child: AContainer(
                    header: AContainerHeader.text('${widget.id == null ? "Incluindo" : "Editando"} - ${widget.descr.singular}'),
                    child: Center(child: Text("aa")),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 16.0),
                  child: Row(
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          ANavigator.pop("ao");
                        },
                        child: const Text('Voltar rota'),
                      ),
                      ElevatedButton(
                        onPressed: () {
                          closeAllDialogs();
                        },
                        child: const Text('CLose dialogs'),
                      ),
                    ],
                  ),
                ),
              ],
            );
          },
        ),
      ),
    );
  }
}
