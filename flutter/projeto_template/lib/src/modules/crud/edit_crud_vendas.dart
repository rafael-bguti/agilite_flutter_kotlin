import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudEditVendas extends StatefulWidget {
  final int? id;
  const CrudEditVendas({this.id, super.key});

  @override
  State<CrudEditVendas> createState() => _CrudEditVendasState();
}

class _CrudEditVendasState extends State<CrudEditVendas> {
  late final CrudEditController controller;

  @override
  void initState() {
    super.initState();
    controller = CrudEditController(id: widget.id);
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.only(top: 16.0),
      color: backgroundColor,
      child: SingleChildScrollView(
        child: AView(
          controller: controller,
          builder: (context, state) {
            return AContainer(
              header: AContainerHeader.text('${widget.id == null ? 'Nova' : 'Alterar'} venda'),
              child: Column(
                children: [
                  const Placeholder(),
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
              ),
            );
          },
        ),
      ),
    );
  }
}
